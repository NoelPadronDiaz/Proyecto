package com.jsuarezarm.roombalib;

/**
 * Created by jonay on 12/1/18.
 */

public class PredefinedAction {

    public static byte[] moveStop() {
        return Drive.wheels(0, 0);
    }

    public static byte[] moveForward() {
        return Drive.wheels(Parameters.VELOCITY, Parameters.VELOCITY);
    }

    public static byte[] moveForwardLeft() {
        return Drive.wheels(Parameters.VELOCITY - 40, Parameters.VELOCITY);
    }

    public static byte[] moveForwardRight() {
        return Drive.wheels(Parameters.VELOCITY, Parameters.VELOCITY - 40);
    }

    public static byte[] moveBackward() {
        return Drive.wheels(-Parameters.VELOCITY, -Parameters.VELOCITY);
    }

    public static byte[] moveBackwardLeft() {
        return Drive.wheels(-Parameters.VELOCITY + 40, -Parameters.VELOCITY);
    }

    public static byte[] moveBackwardRight() {
        return Drive.wheels(-Parameters.VELOCITY, -Parameters.VELOCITY + 40);
    }

    public static byte[] rotateLeft() {
        return Drive.wheels(-Parameters.VELOCITY, Parameters.VELOCITY);
    }

    public static byte[] rotateRight() {
        return Drive.wheels(Parameters.VELOCITY, -Parameters.VELOCITY);
    }

}
