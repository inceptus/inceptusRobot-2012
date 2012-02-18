package org.inceptus.OI;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.camera.Target;
import org.inceptus.camera.TargetFinder;
import org.inceptus.chassis.Drive;
import org.inceptus.chassis.LowerConveyor;
import org.inceptus.chassis.Ramp;
import org.inceptus.chassis.UpperShooterEncoder;
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
    
    public void runUpperConvey(UpperShooterEncoder upperShooter){
        
        //If the shoot values are set
        upperShooter.moveConveyorWithValue(otherJoy.getRawAxis(5) * -1);
        
    }
   
    public void runUpperShooter(UpperShooterEncoder upperShooter, TargetFinder targetFinder) throws CANTimeoutException{
        /*
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
        */
        
        //If the process image button is pressed
        if(otherJoy.getRawButton(3)){
            
            //Get a new image
            Target highTarget = targetFinder.processImage();
            
            //Prepare the wheels to shoot at camera distance
            upperShooter.prepareToShoot( highTarget.distance, 4 );
            
            //Log
            System.out.println( "Distance:" + highTarget.distance );
        }
        
        //If the ramp up button is pressed
        if(otherJoy.getRawButton(4)){
            
            //Set the motor powers
            upperShooter.set();
            
        }else{
            
            //Stop the motor
            upperShooter.stopShooting();
            
        }
        
        if(otherJoy.getRawButton(3)){
            
            //30
            upperShooter.prepareToShoot( 170, 4 );
            
            System.out.println("45%");
            
        }else if(otherJoy.getRawButton(4)){
            
            //40
            upperShooter.prepareToShoot( 135, 4 );
            
            System.out.println("60%");
            
        }else if(otherJoy.getRawButton(2)){
            
            //50
            upperShooter.prepareToShoot( 100, 4 );
            
            System.out.println("55%");
            
        }else if(otherJoy.getRawButton(5)){
            
            //50
            upperShooter.prepareToShoot( 75, 4 );
            
            System.out.println("55%");
            
        }else if(otherJoy.getRawButton(6)){
            
            //50
            upperShooter.prepareToShoot( 50, 4 );
            
            System.out.println("55%");
            
        }else{
            
            //Stop
            upperShooter.stopShooting();
            
        }
        
        upperShooter.set();
        
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
