package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 11/1/18.
 */

public class Opcode {

    public static byte START = (byte) 128;
    public static byte BAUD = (byte) 129;
    public static byte CONTROL = (byte) 130;
    public static byte SAFE_MODE = (byte) 131;
    public static byte FULL_MODE = (byte) 132;
    public static byte POWER = (byte) 133;
    public static byte SPOT = (byte) 134;
    public static byte CLEAN = (byte) 135;
    public static byte MAX_CLEAN = (byte) 136;
    public static byte DRIVE = (byte) 137;
    public static byte DRIVE_WHEELS = (byte) 145;
    public static byte MOTORS = (byte) 138;
    public static byte PWM_MOTORS = (byte) 144;
    public static byte DRIVE_PWM = (byte) 146;
    public static byte LEDS = (byte) 139;
    public static byte SONG = (byte) 140;
    public static byte PLAY_SONG = (byte) 141;
    public static byte STREAM = (byte) 148;
    public static byte QUERY_LIST = (byte) 149;
    public static byte DO_STREAM = (byte) 150;
    public static byte QUERY = (byte) 142;
    public static byte DOCK = (byte) 143;
    public static byte SCHEDULING_LEDS = (byte) 162;
    public static byte DIGIT_LEDS_RAW = (byte) 163;
    public static byte DIGIT_LEDS_ASCII = (byte) 164;
    public static byte BUTTONS = (byte) 165;
    public static byte SCHEDULE = (byte) 167;
    public static byte SET_DAY_TIME = (byte) 168;
    public static byte STOP_MODE = (byte) 173;

}
