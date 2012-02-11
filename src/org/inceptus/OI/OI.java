package org.inceptus.OI;

import edu.wpi.first.wpilibj.can.CANTimeoutException;
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
    private InJoystick otherJoy;
    
    public boolean init(){
        //Init the joysticks
        mainJoy = new InJoystick(1);
        otherJoy = new InJoystick(2);
        
        //Return Success
        return true;
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
    
    
    public void moveLowerConveyor(LowerConveyor lowerConveyor){
        
        //Try catch for errors
        try {
            //If the ramp button is pressed
            if(mainJoy.getRawButton(4)){
                lowerConveyor.moveDown();
            }else if(mainJoy.getRawButton(3)){
                lowerConveyor.moveUp();
            }else{
                lowerConveyor.stop();
            }
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in lowerConveyor");

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in lowerConveyor");

        }
    }
}
