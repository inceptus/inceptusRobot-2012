/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.inceptus;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.OI.OI;
import org.inceptus.camera.Target;
import org.inceptus.camera.TargetFinder;
import org.inceptus.chassis.Drive;
import org.inceptus.chassis.LowerConveyor;
import org.inceptus.chassis.Ramp;
import org.inceptus.chassis.UpperShooter;
import org.inceptus.debug.Debug;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class inceptusRobot extends IterativeRobot {
    //Global drive class
    private Drive drive;
    
    //Global ramp class
    private Ramp ramp;
    
    //Global lowerConveyor class
    private LowerConveyor lowerConveyor;
    
    //Global lowerConveyor class
    private UpperShooter upperShooter;
   
    //Global camera class
    private TargetFinder targetFinder;
    
    //Global Operator Interface class
    private OI oi;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        //Get the target finder class
        targetFinder = new TargetFinder();
        
        try {
            
            //Get the drive class
            drive = new Drive();
            
        } catch (CANTimeoutException ex) { Debug.fatal(ex); }
        
        //Get the ramp class
        ramp = new Ramp();
        
        try {
            
            //Get the ramp class
            lowerConveyor = new LowerConveyor();
            
        } catch (CANTimeoutException ex) { Debug.fatal(ex); }

        try {
            
            //Get the upper shooter class
            upperShooter = new UpperShooter();
            
        } catch (CANTimeoutException ex) { Debug.fatal(ex); }
            
        //Get the OI class
        oi = new OI();
        
    }

    /**
     * This function is called once during autonomous
     */
    public void autonomousInit() {
        //Process the camera image
        Target bestTarget = targetFinder.processImage();
        
        Debug.log("Distance:" + (bestTarget.distance/12));
        
        Debug.log("Distance From Center:" + ((bestTarget.boxCenterX + bestTarget.rawBboxCornerX) - 160));
    }

    public void disabledInit(){
        //Auto disable the motors for everything
        
        //Stop the drive
        drive.stop();
        
        try {
            
            //Stop shooting
            upperShooter.stopShooting();
            
        } catch (CANTimeoutException ex) {
            
            //Error
            Debug.fatal(ex);
            
        }
        
        try {
            
            //Stop the shooter
            upperShooter.stopConveyor();
            
        } catch (CANTimeoutException ex) {
            
            //Error
            Debug.fatal(ex);
            
        }
        
        try {
            
            //Stop the lower Conveyor
            lowerConveyor.stop();
            
        } catch (Exception ex) {
            
            //Error
            Debug.fatal(ex);
            
        }
        
        //Stop the ramp motor
        ramp.stop();
        
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        //Drive with the latest Joystick values
        oi.driveWithJoy(drive);
        
        //Move the LowerConveyor
        oi.moveLowerConveyor(lowerConveyor);
        
        //Move the ramp using the button values
        oi.moveRamp(ramp);
        
        //Run the shooter (has camera code also)
        oi.runUpperShooter(upperShooter, targetFinder);
        
    }
}