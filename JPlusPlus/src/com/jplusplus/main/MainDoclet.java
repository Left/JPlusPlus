package com.jplusplus.main;

import java.io.FileInputStream;
import java.util.Arrays;

import com.sun.javadoc.*;

/**
 * 
 */
public class MainDoclet {
	public static boolean start(RootDoc root) {
    	/*
    	String[][] options = root.options();
    	for (int i = 0; i < options.length; i++) {
			String[] strings = options[i];
			if (strings != null && strings.length > 0 &&
					strings[0] == "-o") {
				outFileName = strings[1];
			}
		}
		*/

		String[][] options = root.options();
		String cmdLine = "javadoc";
    	for (int i = 0; i < options.length; i++) {
			String[] strings = options[i];
			for (int j = 0; j < strings.length; j++) {
				String string = strings[j];
				cmdLine += " " + string;
			}
		}

    	StubsGenerator generator = new StubsGenerator(root, cmdLine); 
    	
    	try {
    	    generator.generateCPlusPlusStubs();
    	} catch (Throwable e) {
    	    e.printStackTrace(System.err);
    	    return false;
    	}
    	
        return true; 
    }
    
    /*
    public static int optionLength(String option) {
        if (option.equals("-o")) {
            return 2;
        }
        return 0;
    }

    public static boolean validOptions(String options[][],
            DocErrorReporter reporter) {
        boolean foundOutOption = false;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals("-o")) {
                if (foundOutOption) {
                    reporter.printError("Only one -o option allowed.");
                    return false;
                } else {
                    foundOutOption = true;
                }
            }
        }
        if (!foundOutOption) {
            reporter
                    .printError("Usage: javadoc -o <out folder> -doclet " + 
                            MainDoclet.class.getCanonicalName() + " ...");
        }
        return foundOutOption;
    }
*/

}

