package com.jplusplus.main;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

public class JavaUtils {

    public static String classNameToTypedef(String name) {
    	return "_Included_" + dotToSpace(name);
    }

    public static String[] getPackageName(String qualifiedName) {
    		String[] split = splitToComponents(qualifiedName);
    		String[] res = new String[split.length - 1];
    		for (int i = 0; i < res.length; i++) {
    			res[i] = split[i];
    		}
    		return res;
    	}

    private static String[] splitToComponents(String qualifiedName) {
        return qualifiedName.split("\\.");
    }

    public static String fileNameFromClassName(String name) {
        return dotToSpace(name);
    }

    private static String dotToSpace(String name) {
        return name.replace(".", "_");
    }

    public static String createMethodSignature(MethodDoc method) {
        String paramsStr = "";
        Parameter[] parameters = method.parameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            paramsStr += TypeMangleVisitor.mapType(parameter.type());
        }
        String retStr = "" + TypeMangleVisitor.mapType(method.returnType());
        if (retStr.equals("")) {
            retStr = "V";
        }
        return "(" + paramsStr + ")" + retStr;
    }

    public static boolean isVoid(Type type) {
        return type.isPrimitive() && type.simpleTypeName().equals("void");
    }

    public static String tagStartFrom(Tag[] tags, String string) {
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tags[i];
            if (tag.text().startsWith(string + " ")) {
                return tag.text().substring(string.length()).trim(); 
            }        
        }
        return "";
    }

    public static String mangleMethodName(MethodDoc method) {
        String[] splittedName = splitToComponents(method.containingClass().qualifiedName());
        String name = "Java_";
        for (int i = 0; i < splittedName.length; i++) {
            String string = splittedName[i];
            name += string + "_"; 
        }
        return name + method.name();
    }
}
