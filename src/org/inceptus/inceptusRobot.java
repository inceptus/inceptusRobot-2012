/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.inceptus;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.inceptus.OI.OI;
import org.inceptus.chassis.Drive;
import org.inceptus.chassis.LowerConveyor;
import org.inceptus.chassis.Ramp;
import org.inceptus.camera.TargetFinder;

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
   
    //Global camera class
    private TargetFinder targetfinder;
    
    //Global Operator Interface class
    private OI oi;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        //TODO: Catch false error returns and handle
        
        //Get the target finder class
        targetfinder = new TargetFinder();
        
        //Get the drive class
        drive = new Drive();
        //Try to init the drive
        drive.init();
        
        //Get the ramp class
        ramp = new Ramp();
        //Try to init the ramp
        ramp.init();
        
        //Get the ramp class
        lowerConveyor = new LowerConveyor();
        //Try to init the ramp
        lowerConveyor.init();

        //Get the oi class
        oi = new OI();
        //Init the OI
        oi.init();
        
        
    }

    /**
     * This function is called once during autonomous
     */
    public void autonomousInit() {
        //Process the camera image
        boolean processImage = targetfinder.processImage();
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
        
    }
}