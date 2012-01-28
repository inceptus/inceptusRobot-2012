/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inceptus;

/**
 *
 * @author innoying
 */
public class Constants {
    //Joysticks
    public final static int driveJoyPort = 1;
    public final static int armJoyPort = 2;
    //Drive Motors
    public final static int leftFrontDrivePort = 4;
    public final static int rightFrontDrivePort = 3;
    public final static int leftRearDrivePort = 2;
    public final static int rightRearDrivePort = 1;
    //Conveyor motors
    public final static int lowerConveyorDrivePort = 5;
    public final static int upperConveyorDrivePort = 6;
    //Shooting motor
    public final static int shootingWheelDrivePort = 7;
    //Threshold values to eliminate accidental driving
    public final static double magnitudeThreshold = .1;
    //Scale the magnitude
    public final static double magnitudeMin = .2;
    public final static double magnitudeMax = .7;
}
