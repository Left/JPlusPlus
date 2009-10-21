package com.jplusplus.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

public class StubsGenerator {

    RootDoc root;
    String cmdLine;
   
    
    public StubsGenerator(RootDoc root, String cmdLine) {
        super();
        this.root = root;
        this.cmdLine = cmdLine;
    }


    void generateCPlusPlusStubs() {
        Set<Type> usedTypes = new HashSet<Type>();
        // First, pre-process all native methods to get referenced classes
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc doc = classes[i];
            collectUsedTypes(doc, usedTypes);
        }
        
        for (Iterator iterator = usedTypes.iterator(); iterator.hasNext();) {
            Type type = (Type) iterator.next();
            System.out.println(type);
        }
        
        for (int i = 0; i < classes.length; ++i) {
        	ClassDoc doc = classes[i];
        	
        	PrintStream preliminaryOutStream = System.out;
        	String outFileName = JavaUtils.fileNameFromClassName(doc.qualifiedName()) + ".h";
        	System.out.println(outFileName);
    		File outFile = new File(outFileName);
    		try {
    			outFile.createNewFile();
    		} catch (IOException e) {
    		    throw new RuntimeException("Can not create file " + outFileName, e);
    		}
    		
    		try {
    		    preliminaryOutStream = new PrintStream(outFile);
    		} catch (FileNotFoundException e) {
    		    throw new RuntimeException("Can not create file " + outFileName, e);
    		}

    		final TabbedPrintStream outStream = new TabbedPrintStream(preliminaryOutStream);    		
    		
    		String ifdefName = JavaUtils.classNameToTypedef(doc.qualifiedName());
    		outStream.println("/* DO NOT EDIT THIS FILE - it is machine generated */");
    		outStream.println("/*");
    		outStream.println(" Command line: " + cmdLine);
    		outStream.println("*/");
    		outStream.println("#include <jni.h>");
    		outStream.println("/* Header for class " + doc.qualifiedName() + " */");   		
    		
    		outStream.println("#ifndef " + ifdefName);
    		outStream.println("#define " + ifdefName);
    		    		
    		TabbedPrintStream currStream;
  		
/*    		
        	FieldDoc[] fields = doc.fields();
        	currStream = outStream.substream();
        	for (int j = 0; j < fields.length; j++) {
    			final FieldDoc fieldDoc = fields[j];

    			if (fieldDoc.isStatic() && fieldDoc.isFinal()) {
    				Type type = fieldDoc.type();
    				currStream.println("// " + fieldDoc.commentText());
    				currStream.println(vis.processType(type) + " " + fieldDoc.name()
                            + " = " + fieldDoc.constantValue()
                            + ";");
    			}
    		}
*/
    		    		
        	// currStream = outStream.substream();
        	currStream = outStream;
        	MethodDoc[] methods = doc.methods(false);
        	for (int j = 0; j < methods.length; j++) {
        		MethodDoc method = methods[j];
        		
        		if (method.isNative()) {
        		    Parameter[] parameters = method.parameters();
        		    
        		    currStream.println("/*");
        		    currStream.println(" * Class:     " + doc.qualifiedName());
        		    currStream.println(" * Method:    " + method.name() + " - " + method.commentText());
        		    currStream.println(" * Signature: " + JavaUtils.createMethodSignature(method));
        		    if (parameters.length > 0) {
        		        Tag[] params = method.tags("@param");
        		        
        		        currStream.println(" * Parameters:");
                        for (int k = 0; k < parameters.length; k++) {
                            Parameter parameter = parameters[k];
                            currStream.println(" *            " + parameter.type() + " " + 
                                    parameter.name() + " " + JavaUtils.tagStartFrom(params, parameter.name()));
                        }
        		    }
        		    
        		    if (!JavaUtils.isVoid(method.returnType())) {
        		        Tag[] tags = method.tags("@return");
        		        String returnTag = JavaUtils.tagStartFrom(tags, "");
                        currStream.println(" * Return:  " + method.returnType() + " " + returnTag); 
        		    }

        		    currStream.println(" */");
        		    

        		    String paramDesc = "";
                    for (int k = 0; k < parameters.length; k++) {
                        Parameter parameter = parameters[k];
                        paramDesc += TypeMappingVisitor.mapType(parameter.type()) + " " + parameter.name();
                        if (k != (parameters.length - 1)) {
                            paramDesc += ", ";
                        }
                    }
        		    
                    currStream.println("extern \"C\"");
                    currStream.println(
                            "JNIEXPORT " +
                            TypeMappingVisitor.mapType(method.returnType()) + " " +
        		            "JNICALL " +
        		            JavaUtils.mangleMethodName(method));
                    
                    currStream.substream().println("(JNIEnv* env, " +
                            (method.isStatic() ? "jclass thisClass" : "jobject thisObject") +
                            (paramDesc.equals("") ? "" : ", ") + paramDesc + ");");
           		
            		currStream.println();
        		}
    		}
        	        	
        	outStream.println("#endif // " + ifdefName);
        }
    }


    private void collectUsedTypes(ClassDoc doc, Set<Type> usedTypes) {

        MethodDoc[] methods = doc.methods(false);
        for (int j = 0; j < methods.length; j++) {
            MethodDoc method = methods[j];
            
            if (method.isNative()) {
                Type returnType = method.returnType();
                addType(usedTypes, returnType);
                Parameter[] parameters = method.parameters();
                for (int k = 0; k < parameters.length; k++) {
                    Parameter parameter = parameters[k];
                    addType(usedTypes, parameter.type());
                }
            }
        }
    }


    private void addType(Set<Type> usedTypes, Type type) {
        if (!type.isPrimitive()) {
            usedTypes.add(type);
        }
    }

}
