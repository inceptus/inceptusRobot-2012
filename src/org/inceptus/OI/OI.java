package org.inceptus.OI;

import edu.wpi.first.wpilibj.Joystick;
import org.inceptus.camera.Target;
import org.inceptus.camera.TargetFinder;
import org.inceptus.chassis.Drive;
import org.inceptus.chassis.LowerConveyor;
import org.inceptus.chassis.Ramp;
import org.inceptus.chassis.UpperShooter;
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
    
    public void runUpperConvey(UpperShooter upperShooter){
        
        //If the shoot values are set
        upperShooter.moveConveyorWithValue(otherJoy.getRawAxis(5) * -1);
        
    }
   
    public void runUpperShooter(UpperShooter upperShooter, TargetFinder targetFinder){
        
        //Calc buttons from the POV hat
        boolean button1 = (otherJoy.getRawAxis(6) == 1);
        boolean button2 = (otherJoy.getRawAxis(6) == -1);
        
        //If changed
        if(button1 != lastButton1){
            
            //Adjust offset up
            upperShooter.adjustOffset(true);
            
            Debug.log("bumpDown");
            
        }
        
        //If changed
        if(button2 != lastButton2){
            
            //Adjust offset down
            upperShooter.adjustOffset(false);
            
            Debug.log("bumpUp");
            
        }
        
        //Reset "lastButton"
        lastButton1 = button1;
        lastButton2 = button2;
        
        /*
        //If the process image button is pressed
        if(otherJoy.getRawButton(3)){
            
            //Get a new image
            Target highTarget = targetFinder.processImage();
            
            //Prepare the wheels to shoot
            upperShooter.prepareToShoot(highTarget.distance);
            
            System.out.println("distance:" + highTarget.distance);
        }
        
        //If the ramp up button is pressed
        if(otherJoy.getRawButton(4)){
            
            Debug.log("running");
            
            //Set the motor powers
            upperShooter.set();
            
        }else{
            
            //Stop the motor
            upperShooter.stopShooting();
            
        }*/
        
        if(otherJoy.getRawButton(3)){
            
            //30
            upperShooter.setTest(.3);
            
        }

        if(otherJoy.getRawButton(4)){
            
            //40
            upperShooter.setTest(.4);
            
        }
        
        if(otherJoy.getRawButton(5)){
            
            //50
            upperShooter.setTest(.5);
            
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
    
    public void testShoot(UpperShooter upperShooter){
        
        if(otherJoy.getRawButton(2)){
            upperShooter.prepareToShoot(144);
        }else{
            upperShooter.stopShooting();
        }
        
        upperShooter.set();
    }
}
