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
    
    private double targetRPM = 0;
    
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
                    lowerShootingMotor.setX( lowerShootingMotor.getX() + .01 );
                    upperShootingMotor.setX( upperShootingMotor.getX() + .01 );
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
    
    public double inchesToRPMs(double distance){
        int angle = 45;
        int hoop = 4; 
        double wheelDiameter = 6;
        //Calculate height
        int height = 0;
        //Switch for hoop
        switch(hoop){
            case 1:
                height = 34;
                break;
            case 2:
            case 3:
                height = 60;
                break;
            case 4:
            default:
                height = 91;
                break;
        }
        //Add adjustment for air resistance.
        distance *= 1.05;
        //Change angle from degrees to radians
        double angleRads = angle * Math.PI/180;
        //Calculate velocity needed and chnage to RPM required
        double velocity = (distance - 9)/(Math.sqrt((46 - height+(distance - 9)*Math.tan(angleRads))/16.087) * Math.cos(angleRads));
        double RPM = velocity * 60 / (wheelDiameter * Math.PI);
        return RPM;
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
