/**
 * 
 */
package com.jplusplus.main;

import com.sun.javadoc.Type;

final class TypeMangleVisitor {
    
    static public String mapType(Type t) {
        final String[] ret = { "" };
        AbstractTypeVisitor vis = new AbstractTypeVisitor() {
            @Override
            protected void afterProcessArray() {
            }

            @Override
            protected void beforeProcessArray() {
                ret[0] += "[";
            }

            @Override
            protected void processBoolean() {
                ret[0] += "Z";
            }

            @Override
            protected void processByte() {
                ret[0] += "B";
            }

            @Override
            protected void processChar() {
                ret[0] += "C";
            }

            @Override
            protected void processInt() {
                ret[0] += "I";
            }

            @Override
            protected void processLong() {
                ret[0] += "J";
            }

            @Override
            protected void processShort() {
                ret[0] += "S";
            }

            @Override
            protected void processDouble() {
                ret[0] += "D";
            }

            @Override
            protected void processFloat() {
                ret[0] += "F";
            }

            @Override
            protected void processObject(String name) {
                ret[0] += "L" + name.replace(".", "/") + ";";
            }
        };
        vis.processType(t);
        return ret[0];
    }
        
}