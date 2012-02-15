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
    
    public double inchesToRPMs(double distance){
        int angle = 57;
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
        double velocity = -193.044*distance*distance/(height-52-distance*Math.tan(angleRads));
        if(velocity < 0)
            return 0;
        else
            velocity = Math.sqrt(velocity)/Math.cos(angleRads);
        double RPM = velocity * 60 / (wheelDiameter * Math.PI);
        return RPM;
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
