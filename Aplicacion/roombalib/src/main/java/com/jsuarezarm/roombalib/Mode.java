package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 11/1/18.
 */

public class Mode {

    public static byte[] start() {
        byte[] command = new byte[1];

        command[0] = Opcode.START;

        return command;
    }

    public static byte[] safe() {
        byte[] command = new byte[1];

        command[0] = Opcode.SAFE_MODE;

        return command;
    }

    public static byte[] stop() {
        byte[] command = new byte[1];

        command[0] = Opcode.STOP_MODE;

        return command;
    }

    public static byte[] dock() {
        byte[] command = new byte[1];

        command[0] = Opcode.DOCK;

        return command;
    }

}
