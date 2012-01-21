/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.inceptus;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import org.inceptus.Constants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class inceptusRobot extends IterativeRobot {
    //Init the main joystick
    private Joystick driveJoy;
    //Init the arm Joystick
    private Joystick armJoy;
    
    //Init the drive motors
    private Jaguar rearLeftDrive;
    private Jaguar frontLeftDrive;
    private Jaguar rearRightDrive;
    private Jaguar frontRightDrive;
    
    //Init the conveyor motors
    private Jaguar lowerConveyorDrive;
    private Jaguar upperConveyorDrive;
    
    //Init the shooting motor
    private Jaguar shootingWheelDrive;
    
    //Setup the mecanum drive
    private RobotDrive drive;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Setup the drive joystick
        driveJoy = new Joystick(Constants.driveJoyPort);
        //Setup the arm joystick
        armJoy = new Joystick(Constants.armJoyPort); 
        //Setup the drive motors
        rearLeftDrive = new Jaguar(Constants.leftRearDrivePort);
        rearRightDrive = new Jaguar(Constants.rightRearDrivePort);
        frontLeftDrive = new Jaguar(Constants.leftFrontDrivePort);
        frontRightDrive = new Jaguar(Constants.rightFrontDrivePort);
        //Setup the Conveyor motors
        lowerConveyorDrive = new Jaguar(Constants.lowerConveyorDrivePort);
        upperConveyorDrive = new Jaguar(Constants.upperConveyorDrivePort);
        //Setup the shooting motor
        shootingWheelDrive = new Jaguar(Constants.shootingWheelDrivePort);
        //Setup the drive
        drive = new RobotDrive(rearLeftDrive, frontLeftDrive, rearRightDrive, frontRightDrive);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //Init joystick values T=Throttle
        double T, Z;
        
        //Set the joystick values
        T = driveJoy.getRawAxis(3);
        Z = driveJoy.getRawAxis(4);
        
        //Init the values for each
        double magnitude, rotation, vector;
        //Use the distance formula to 
        magnitude = driveJoy.getMagnitude();
        //Get the rotation
        vector = driveJoy.getDirectionDegrees();
        //Set the vector to the twist from the Joystick
        rotation = Z;
        
        //Check and use the thresholds
        magnitude = (Math.abs(magnitude) < Constants.magnitudeThreshold) ? 0 : magnitude;
        
        //Adjust to 0-1 range
        T = ((T+1)/2);
     
        //Scale the magnitude down to not overpower the motors
        T = Constants.magnitudeMin + (T * (Constants.magnitudeMax - Constants.magnitudeMin));  
        
        //Use the throttle value to normalize the magnitude
        magnitude = magnitude * (1-T);
        
        
        //Drive mecanum using polar coordinates
        drive.mecanumDrive_Polar(magnitude, vector, rotation);
        
        //Init the arm values
    }
    
}
