/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.inceptus;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
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
        
        //Disable the watchdog
        Watchdog.getInstance().setEnabled(false);
        
        //Get the target finder class
        targetFinder = new TargetFinder();
                    
        //Get the drive class
        drive = new Drive();
        
        //Get the ramp class
        ramp = new Ramp();
            
        //Get the ramp class
        lowerConveyor = new LowerConveyor();
            
        //Get the upper shooter class
        upperShooter = new UpperShooter();

        //Get the OI class
        oi = new OI();
        
    }

    /**
     * This function is called once during autonomous
     */
    public void autonomousInit() {
        
        //Start the wheels
        upperShooter.prepareToShoot(176);
        
        //Run
        upperShooter.set();
        
        //Wait 1 second
        Timer.delay(1);
        
        //Bring in 1 ball
        lowerConveyor.moveUp();
        
        //Wait 3 seconds
        Timer.delay(3);
        
        //Stop the lower Conveyor
        lowerConveyor.stop();
        
        //Push ball into shooter
        upperShooter.moveConveyorWithValue(1);
        
        //Delay 4 seconds to shoot and then repeat
        Timer.delay(4);
        
        //Stop
        upperShooter.stopConveyor();
        
        //Bring in 1 ball
        lowerConveyor.moveUp();
        
        //Push ball into shooter
        upperShooter.moveConveyorWithValue(1);
        
        //Delay 6 seconds to shoot and then repeat
        Timer.delay(6);
        
        //Stop shooting and conveyors
        upperShooter.stopConveyor();
        upperShooter.stopShooting();
        lowerConveyor.stop();
        
    }
    
    public void disabledInit(){

        //Stop the drive
        drive.stop();

        //Stop shooting
        upperShooter.stopShooting();

        //Stop the shooter
        upperShooter.stopConveyor();

        //Stop the lower Conveyor
        lowerConveyor.stop();
        
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
        oi.runUpperConvey(upperShooter);
        
        oi.runUpperShooter(upperShooter, targetFinder);
        
        //test shoot
        oi.testShoot(upperShooter);
    }
}