/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inceptus.chassis;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author innoying
 */
public class UpperShooterEncoder {
    
    //Import motors
    private Jaguar conveyorMotor;
    private CANJaguar upperShootingMotor;
    private CANJaguar lowerShootingMotor;
    
    //Motor offset
    private double offset = 0;
    
    //RPMs to shoot at for each motor
    private double lowerTargetRPMs = 0;
    private double upperTargetRPMs = 0;
    
    public UpperShooterEncoder() throws CANTimeoutException {
        
        //Setup the Jaguars
        conveyorMotor = new Jaguar(5);
        upperShootingMotor = new CANJaguar(2);
        lowerShootingMotor = new CANJaguar(3);

        //Coast motors by default
        upperShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        lowerShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        
        //Setup encoders
        lowerShootingMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
        upperShootingMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
        
    }
    
    private double inchesToRPMs( double inches, int hoop ){
        
        //Shooting angle (convert to radians)
        final double shootingAngle = 60 * (180/Math.PI);
        
        //The hoop height
        double hoopHeight = 0;
        
        //The shooting wheels diameter
        final double wheelDiameter = 6;
        
        //Set height for each hoop
        switch (hoop){
            case 4:
                hoopHeight = 34;
            break;
            case 3:
            case 2:
                hoopHeight = 60;
            break;
            default:
                hoopHeight = 91;
            break;
        }
        
        //Adjust for camera height
        double actualHeight = hoopHeight - 34;
        
        //Adjust for height from shooter
        double differenceHeight = hoopHeight - 52;
        
        //Adjust for air
        inches = inches * 1.05;
        
        //Calculate actual distance
        inches = Math.sqrt(square(inches) - square(actualHeight));
        
        //Calculate the velocity
        double velocity = (-193.044 * square(inches)) / (differenceHeight * Math.tan(shootingAngle));
        
        //If we have a negative velocity
        if(velocity < 0){
            
            //Exit now
            return 0;
            
        }
        
        //Move velocity calcs
        velocity = Math.sqrt(velocity) / Math.cos(shootingAngle);
        
        //Return the calculated rpms
        return velocity * 60 / (wheelDiameter * Math.PI);
        
    }
    
    public void prepareToShoot( double inches, int hoop ){
        
        //Setup the target RPMs
        lowerTargetRPMs = inchesToRPMs(inches, hoop);    
        upperTargetRPMs = inchesToRPMs(inches, hoop) * .95;    
        
    }
    
    public boolean set() throws CANTimeoutException {
        
        //Max motor range
        final int maxDiff = 200;
        
        //The max rate of speed change
        final double changeRate = .01;
        
        int totalReady = 0;
        
        //The lower motor's speed 
        double lowerDiff = lowerShootingMotor.getSpeed() - lowerTargetRPMs;
        
        //If the motor RPMs are not within the range
        if( Math.abs(lowerDiff) > maxDiff ){
            
            //If positive
            if(Math.abs(lowerDiff) == lowerDiff){
                
                //Change by change rate
                lowerShootingMotor.setX(lowerShootingMotor.getX() + changeRate);
                
            }else{
                
                //Change by change rate
                lowerShootingMotor.setX(lowerShootingMotor.getX() - changeRate);
                
            }
            
        }else{
            
            //Add to ready total
            totalReady += 1;
            
        }
        
        //The upper motor's speed 
        double upperDiff = upperShootingMotor.getSpeed() - upperTargetRPMs;
        
        //If the motor RPMs are not within the range
        if( Math.abs(lowerDiff) > maxDiff ){
            
            //If positive
            if(Math.abs(lowerDiff) == lowerDiff){
                
                //Change by change rate
                upperShootingMotor.setX(upperShootingMotor.getX() + changeRate);
                
            }else{
                
                //Change by change rate
                upperShootingMotor.setX(upperShootingMotor.getX() - changeRate);
                
            }
            
        }else{
            
            //Add to ready total
            totalReady += 1;
            
        }
        
        //Return success if both wheels are ready
        return (totalReady == 2);
        
    }
    
    public void stopShooting() throws CANTimeoutException{
    
        //Set both target RPMs to 0
        upperTargetRPMs = 0;
        lowerTargetRPMs = 0;
        
        //Stop both motors
        lowerShootingMotor.setX(0);
        upperShootingMotor.setX(0);
        
    }
    
    public void stopConveyor(){
        
        //Set to 0
        conveyorMotor.set(0);
        
    }
    
    public void moveConveyorWithValue(double power){
        
        //Set to power
        conveyorMotor.set(power);
        
    }
    
    public void moveConveyorUp(){
        
        //Move up
        moveConveyorWithValue(-1);
        
    }
    
    public void moveConveyorDown(){
        
        //Move up
        moveConveyorWithValue(1);
        
    }
    
    public double square( double value ){
        
        //Square the value
        return value * value;
        
    }
}
