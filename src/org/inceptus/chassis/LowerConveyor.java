package org.inceptus.chassis;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.debug.Debug;

/**
 *
 * @author innoying
 */
public class LowerConveyor {
    
    private CANJaguar conveyorMotor;
    
    public boolean init(){
        //Try catch for errors
        try {
            
            //Setup the Jaguar
            conveyorMotor = new CANJaguar(14);
            
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in Drive");
            //Return Failure
            return false;

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in Drive");
            //Return Failure
            return false;
        }
        //Return Success
        return true;
        
    }
    
    public void moveUp() throws CANTimeoutException{
        conveyorMotor.setX(.5);
    }
    
    public void moveDown() throws CANTimeoutException{
        conveyorMotor.setX(-1);
    }
    
    public void stop() throws CANTimeoutException{
        conveyorMotor.setX(0);
    }
}