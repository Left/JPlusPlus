package com.jplusplus.test;

/**
 * 
 */
public class TestClass {
    /**
     * Just a zero
     */
	public static final int ZERO = 0;
	/**
	 * One
	 */
	public static final int ONE = 1;
	/**
	 * Zero as an object
	 */
	public static final Integer ZERO_OBJ = 0;
	
	/**
	 * Function just for test
	 */
    public native void test1();
    
    /**
     * Yet another just for test function
     * @param str parameter
     */
    public native void test2(String str);
    
    /**
     * And one anoter test function
     * @param own
     */
    public native void test3(TestClass2 own);
    
    /**
     * Function just for test
     * 
     * @param i integer parameter comment
     * @param j short parameter comment 
     * @param k char parameter comment
     * @param l long parameter comment
     * @param b byte parameter comment
     * @param bb boolean parameter comment
     */
    public native void test4(int i, short j, char k, long l, byte b, boolean bb);
    
    /**
     * @return integer value
     */
    public native int returnInt();
    
    public native int[] returnArr();
    public native int[][] returnArr2();
    public native int[][][] returnArr3();
    
    public native String[] returnStrArr();
    public native String[][] returnStrArr2();
    public native String[][] returnStrArr3();
    
    private native void privateMethod();
    
    private native double doubleMethod();
    private native float floatMethod();
}
