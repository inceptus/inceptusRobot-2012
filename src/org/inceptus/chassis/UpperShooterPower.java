package org.inceptus.chassis;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author innoying
 */
public class UpperShooterPower {
    
    private Jaguar conveyorMotor;
    private Jaguar upperShootingMotor;
    private Jaguar lowerShootingMotor;
    
    private double offset = 0;
    
    private double targetSpeed = 0;
    
    public UpperShooterPower(){
            
            //Setup the Jaguars
            conveyorMotor = new Jaguar(5);
            upperShootingMotor = new Jaguar(2);
            lowerShootingMotor = new Jaguar(3);
            
            //Coast motors
            //upperShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            //lowerShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            
    }
    
    public void prepareToShoot( double inches ){
        /*
        //Prebuilt values
        final double maxPower = .5;
        final int maxDistance = 16 * 12;
        final double minPower = .3;
        final int minDistance = (int) (9.35 * 12);
        
        //y2(maxDistance) = maxPower
        //y1(minDistance) = minPower
        //(y2 - y1)/(x2 - x1)
        
        //Assume linear
        double temp = (maxPower - minPower)/(maxDistance - minDistance) * inches;
        */
        
        double v = DriverStation.getInstance().getBatteryVoltage();
        double f = inches / 12;
        
        System.out.println("v:"+v);
        
        double temp = ((.44*(f)+16-v)/22);
        
        System.out.println(temp+"");
        
        //Catch bad (>.8) case where motors burn out
        if(temp > .8){
            temp = .8;
        }
        
        //Catch bad negative values
        if(temp < 0){
            temp = 0;
        }
        
        temp += offset;
        
        targetSpeed = temp;
        
        System.out.println("Test:" + temp);
        
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, ""+temp);
        
        DriverStationLCD.getInstance().updateLCD();

    }
    
    public void setTest( double speed ){
        targetSpeed = speed;
    }
    
    public void set(){
        
        //Go fast
        lowerShootingMotor.set(targetSpeed);
        //Slower to add backspin
        upperShootingMotor.set(targetSpeed * .9);
                
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
