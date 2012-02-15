package org.inceptus.chassis;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author innoying
 */
public class UpperShooter {
    
    private CANJaguar conveyorMotor;
    private CANJaguar upperShootingMotor;
    private CANJaguar lowerShootingMotor;
    
    private double offset = 0;
    
    private double targetSpeed = 0;
    
    public UpperShooter() throws CANTimeoutException{
            
            //Setup the Jaguars
            conveyorMotor = new CANJaguar(14);
            upperShootingMotor = new CANJaguar(15);
            lowerShootingMotor = new CANJaguar(16);
            
            //Coast motors
            upperShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            lowerShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            
    }
    
    public void prepareToShoot( double inches){
        
        //Prebuilt values
        final double maxPower = 1;
        final int maxDistance = 30 * 12;
        final double minPower = .2;
        final int minDistance = 2 * 12;
        
        //y2(maxDistance) = maxPower
        //y1(minDistance) = minPower
        //(y2 - y1)/(x2 - x1)
        
        //Assume linear
        double temp = (maxPower - minPower)/(maxDistance - minDistance) * inches;
        
        //Catch bad (>1) case
        if(temp > 1){
            temp = 1;
        }
        
        //Catch bad negative values
        if(temp < 0){
            temp = 0;
        }
        
        temp += offset;
        
        targetSpeed = temp;

    }
    
    public void set() throws CANTimeoutException {
        
        //Go fast
        lowerShootingMotor.setX(targetSpeed);
        //Slower to add backspin
        upperShootingMotor.setX(targetSpeed * .95);
                
    }
    
    public void stopShooting() throws CANTimeoutException{
        
        targetSpeed = 0;
        lowerShootingMotor.setX(0);
        upperShootingMotor.setX(0);
        
    }
    
    public void moveConveyorWithValue(double power) throws CANTimeoutException {
        
        //Set to power
        conveyorMotor.setX(power);
        
    }
    
    public void stopConveyor() throws CANTimeoutException{
        
        //Set to 0
        conveyorMotor.setX(0);
        
    }
    
    public void adjustOffset(boolean direction){
        
        if(direction){
            offset -= .01;
        }else{
            offset += .01;
        };
        
    }
}
