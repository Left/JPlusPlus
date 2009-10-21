package com.jplusplus.main;

import com.sun.javadoc.Type;

public abstract class AbstractTypeVisitor {
   
    public void processType(Type t) {
        if (t.qualifiedTypeName().equals("void")) {
            processVoid();
            return;
        }

        {
            String dim = t.dimension();
            for (;dim.length() > 0; dim = dim.substring(2)) {
                beforeProcessArray();
            }
        }

        if (t.qualifiedTypeName().equals("byte")) {
            processByte();
        } else if (t.qualifiedTypeName().equals("short")) {
            processShort();
        } else if (t.qualifiedTypeName().equals("int")) {
            processInt();
        } else if (t.qualifiedTypeName().equals("long")) {
            processLong();
        } else if (t.qualifiedTypeName().equals("char")) {
            processChar();
        } else if (t.qualifiedTypeName().equals("double")) {
            processDouble();
        } else if (t.qualifiedTypeName().equals("float")) {
            processFloat();
        } else if (t.qualifiedTypeName().equals("boolean")) {
            processBoolean();
        } else {
            processObject(t.qualifiedTypeName());
        }
        
        {
            String dim = t.dimension();
            for (;dim.length() > 0; dim = dim.substring(0, dim.length() - 2)) {
                afterProcessArray();
            }
        }
    }
    
    protected void processFloat() {
    }

    protected void processDouble() {
    }

    protected void processVoid() {
    }

    protected void beforeProcessArray() {
    }

    protected void afterProcessArray() {
    }

    protected void processByte() {        
    }

    protected void processShort() {
    }

    protected void processInt() {
    }
    
    protected void processLong() {
    }

    protected void processChar() {
    }

    protected void processBoolean() {
    }

    protected void processObject(String name) {        
    }
}
