package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 11/1/18.
 */

public class Convert {

    public static byte toHighByte(int value) {
        return (byte) (value >> 8);
    }

    public static byte toLowByte(int value) {
        return (byte) (value & 0xFF);
    }

}
