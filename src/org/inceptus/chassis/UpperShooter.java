package org.inceptus.chassis;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author innoying
 */
public class UpperShooter {
    
    private Jaguar conveyorMotor;
    private Jaguar upperShootingMotor;
    private Jaguar lowerShootingMotor;
    
    private double offset = 0;
    
    private double targetSpeed = 0;
    
    public UpperShooter(){
            
            //Setup the Jaguars
            conveyorMotor = new Jaguar(5);
            upperShootingMotor = new Jaguar(2);
            lowerShootingMotor = new Jaguar(3);
            
            //Coast motors
            //upperShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            //lowerShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            
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
    
    public void set(){
        
        //Go fast
        lowerShootingMotor.set(targetSpeed);
        //Slower to add backspin
        upperShootingMotor.set(targetSpeed * .95);
                
    }
    
    public void stopShooting(){
        
        targetSpeed = 0;
        lowerShootingMotor.set(0);
        upperShootingMotor.set(0);
        
    }
    
    public void moveConveyorWithValue(double power){
        
        //Set to power
        conveyorMotor.set(power);
        
    }
    
    public void stopConveyor(){
        
        //Set to 0
        conveyorMotor.set(0);
        
    }
    
    public void adjustOffset(boolean direction){
        
        if(direction){
            offset -= .01;
        }else{
            offset += .01;
        };
        
    }
}
