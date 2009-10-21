/**
 * 
 */
package com.jplusplus.main;

import com.sun.javadoc.Type;

final class TypeMappingVisitor {
    static public String mapType(Type t) {
        final String[] ret = { "" };
        AbstractTypeVisitor vis = new AbstractTypeVisitor() {
            @Override
            protected void afterProcessArray() {
                ret[0] = "jobjectArray";
            }

            @Override
            protected void beforeProcessArray() {
            }

            @Override
            protected void processBoolean() {
                ret[0] = "jboolean";
            }

            @Override
            protected void processByte() {
                ret[0] = "jbyte";
            }

            @Override
            protected void processChar() {
                ret[0] = "jchar";   
            }

            @Override
            protected void processInt() {
                ret[0] = "jint";
            }

            @Override
            protected void processLong() {
                ret[0] = "jlong";
            }

            @Override
            protected void processShort() {
                ret[0] = "jshort";
            }
            
            @Override
            protected void processDouble() {
                ret[0] = "jdouble";
            }

            @Override
            protected void processFloat() {
                ret[0] = "jfloat";
            }

            @Override
            protected void processObject(String name) {
                if (name.equals("java.lang.String")) {
                    ret[0] = "jstring";
                } else if (name.equals("java.lang.Class")) {
                    ret[0] = "jclass";
                } else {
                    ret[0] = "jobject";
                }
            }

            @Override
            protected void processVoid() {
                ret[0] = "void";
            }
                        
        };

        vis.processType(t);
        
        return ret[0];
    }
    

}