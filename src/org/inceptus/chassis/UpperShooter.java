package org.inceptus.chassis;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.debug.Debug;

/**
 *
 * @author innoying
 */
public class UpperShooter {
    
    private CANJaguar conveyorMotor;
    private CANJaguar upperShootingMotor;
    private CANJaguar lowerShootingMotor;
    
    private int targetRPM = 0;
    
    public boolean init(){
        //Try catch for errors
        try {
            
            //Setup the Jaguars
            conveyorMotor = new CANJaguar(14);
            upperShootingMotor = new CANJaguar(15);
            lowerShootingMotor = new CANJaguar(16);
            
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in " + this.getClass().getName());
            //Return Failure
            return false;

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in " + this.getClass().getName());
            //Return Failure
            return false;
        }
        //Return Success
        return true;
        
    }
    
    public void prepareToShoot( int inches ){
        targetRPM = inchesToRPMs(inches);
    }
    
    public boolean perodic() {
        try {
            if(isAtSpeed()){
                return true;
            }else{
                //If at full power
                if( lowerShootingMotor.getX() == 100 ){
                    //Decrease target RPMs
                    targetRPM -= 100;
                    //Return not ready
                    return false;
                }else{
                    //Speed up the motors
                    lowerShootingMotor.setX( lowerShootingMotor.getX() + 1 );
                    upperShootingMotor.setX( upperShootingMotor.getX() + 1 );
                    //Return not ready
                    return false;
                }
            }
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in " + this.getClass().getName());
            //Return Failure
            return false;

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in " + this.getClass().getName());
            //Return Failure
            return false;
        }
                
    }
    
    public void stop(){
        try{
            targetRPM = 0;
            lowerShootingMotor.setX(0);
            upperShootingMotor.setX(0);
        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in " + this.getClass().getName());

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in " + this.getClass().getName());

        }
    }
    
    public void shoot() {
        //TODO: Callback after say 5 seconds to stop the motor
    }
    
    private int inchesToRPMs(double inches){
        //TODO calc
        return (int) inches;
    }
    
    private boolean isAtSpeed() throws CANTimeoutException{
        //If the upper motor is at speed
        if( upperShootingMotor.getSpeed() >= targetRPM ){
            //If the lower motor is at speed
            if( lowerShootingMotor.getSpeed() >= targetRPM ){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
