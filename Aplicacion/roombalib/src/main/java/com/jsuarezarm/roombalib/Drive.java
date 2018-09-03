package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 11/1/18.
 */

public class Drive {

    /**
     *
     * @param velocity (-500 - 500 mm/s)
     * @param radius (-2000 - 2000 mm)
     * @return
     */
    public static byte[] withRadius(int velocity, int radius) {
        byte[] command = new byte[5];

        command[0] = Opcode.DRIVE;
        command[1] = Convert.toHighByte(velocity);
        command[2] = Convert.toLowByte(velocity);
        command[3] = Convert.toHighByte(radius);
        command[4] = Convert.toLowByte(radius);

        return command;
    }

    /**
     *
     * @param leftVelocity (-500 - 500 mm/s)
     * @param rightVelocity (-500 - 500 mm/s)
     * @return
     */
    public static byte[] wheels(int leftVelocity, int rightVelocity) {
        byte[] command = new byte[5];

        command[0] = Opcode.DRIVE_WHEELS;
        command[1] = Convert.toHighByte(rightVelocity);
        command[2] = Convert.toLowByte(rightVelocity);
        command[3] = Convert.toHighByte(leftVelocity);
        command[4] = Convert.toLowByte(leftVelocity);

        return command;
    }

}
