package com.jplusplus.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TabbedPrintStream extends PrintStream {
    private String prefix = "";

    public TabbedPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    public TabbedPrintStream(File file, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public TabbedPrintStream(OutputStream out, boolean autoFlush,
            String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public TabbedPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public TabbedPrintStream(OutputStream out) {
        super(out);
    }

    public TabbedPrintStream(String fileName, String csn)
            throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public TabbedPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);

    }
    
    public TabbedPrintStream substream() {
        TabbedPrintStream tabbedPrintStream = new TabbedPrintStream(this.out);
        tabbedPrintStream.prefix = "\t" + this.prefix; 
        return tabbedPrintStream;
    }

    @Override
    public void println(boolean x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(char x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(char[] x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(double x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(float x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(int x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(long x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(Object x) {
        print(prefix);
        super.println(x);
    }

    @Override
    public void println(String x) {
        print(prefix);
        super.println(x);
    }


    
        
}
