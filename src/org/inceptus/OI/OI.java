package org.inceptus.OI;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.inceptus.camera.Target;
import org.inceptus.camera.TargetFinder;
import org.inceptus.chassis.*;
import org.inceptus.debug.Debug;


/**
 *
 * @author innoying
 */
public class OI {
    
    //Joysticks
    private InJoystick mainJoy;
    private Joystick otherJoy;
    
    boolean lastButton1 = false;
    boolean lastButton2 = false;
    
    public OI(){
        
        //Init the joysticks
        mainJoy = new InJoystick(1);
        otherJoy = new Joystick(2);
        
    }
    
    public boolean driveWithJoy(Drive drive){
        
        //Drive
        drive.driveWithValues(
                mainJoy.getScaledX(), 
                mainJoy.getScaledY(), 
                mainJoy.getScaledTwist()
        );
        
        //Return Success
        return true;
        
    }

    public void moveRamp(Ramp ramp) {
        
        //If the ramp button is pressed
        if(otherJoy.getRawButton(1)){
            //Call the down routine
            ramp.moveDown();
        }else{
            //Call the up routine
            ramp.moveUp();
        }
        
    }
    
    public void runUpperConvey(UpperShooterPower upperShooter){
        
        //If the shoot values are set
        upperShooter.moveConveyorWithValue(otherJoy.getRawAxis(5) * -1);
        
    }
   
    public void runUpperShooter(UpperShooterPower upperShooter, TargetFinder targetFinder){
        
        //Calc buttons from the POV hat
        boolean button1 = (otherJoy.getRawAxis(6) == 1);
        boolean button2 = (otherJoy.getRawAxis(6) == -1);
        
        //If changed
        if(button1 != lastButton1){
            if(button1 == true){
                //Adjust offset up
                upperShooter.adjustOffset(true);

                Debug.log("bumpDown:"+upperShooter.targetDistance);
            }   
        }
        
        //If changed
        if(button2 != lastButton2){
            if(button2 == true){
                //Adjust offset down
                upperShooter.adjustOffset(false);

                Debug.log("bumpUp:"+upperShooter.targetDistance);
            }
        }
        
        //Reset "lastButton"
        lastButton1 = button1;
        lastButton2 = button2;
        
        if(otherJoy.getRawButton(3)){
            
            //50
            upperShooter.prepareToShoot();
            
            System.out.println("55%");
            
        }else{
            
            //Stop
            upperShooter.stopShooting();
            
        }
        
        upperShooter.set();
        
    }
    
    public void alignTowardTarget(Drive drive, TargetFinder targetFinder) {
        
        if(otherJoy.getRawButton(6)){

             Target highTarget = targetFinder.processImage();
            
            //Center the robot onto the target
            if (highTarget.boxCenterX < 160) {

                drive.driveWithValues(0, 0, .2);

                Timer.delay(.1);

                drive.stop();

            } else {

                drive.driveWithValues(0, 0, -.2);

                Timer.delay(.1);

                drive.stop();

            }
            
        }else{
            
            drive.stop();
            
        }
    }
    
    public void moveLowerConveyor(LowerConveyor lowerConveyor){
        
        //Try catch for errors
        try {
            
            lowerConveyor.moveValue(otherJoy.getRawAxis(2) * .7);
            
        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex);

        }
        
    }

}
