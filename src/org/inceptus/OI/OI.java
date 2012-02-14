package org.inceptus.OI;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.camera.TargetFinder;
import org.inceptus.chassis.Drive;
import org.inceptus.chassis.LowerConveyor;
import org.inceptus.chassis.Ramp;
import org.inceptus.debug.Debug;


/**
 *
 * @author innoying
 */
public class OI {
    //Joysticks
    private InJoystick mainJoy;
    private Joystick otherJoy;
    
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
        if(mainJoy.getRawButton(5)){
            ramp.moveDown();
        }else{
            ramp.moveUp();
        }
        
    }
    
    public void updateCamera(TargetFinder targetFinder){
        //If the process image button is pressed
        if(otherJoy.getRawButton(10)){
            //Get a new image
            targetFinder.processImage();
        }
        
    }
    
    
    public void moveLowerConveyor(LowerConveyor lowerConveyor){
        
        //Try catch for errors
        try {
            
            //If the conveyor button is pressed
            if(mainJoy.getRawButton(4)){
                lowerConveyor.moveDown();
            }else if(mainJoy.getRawButton(3)){
                lowerConveyor.moveUp();
            }else{
                lowerConveyor.stop();
            }
            
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in " + this.getClass().getName());

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in " + this.getClass().getName());

        }
        
    }
}
