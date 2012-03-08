/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inceptus.chassis;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author innoying
 */
public class UpperShooterEncoder {

    //Import motors
    private Jaguar conveyorMotor;
    private Jaguar upperShootingMotor;
    private Jaguar lowerShootingMotor;
    //Motor offset
    private double offset = 0;
    //RPMs to shoot at for each motor
    private double lowerTargetRPMs = 0;
    private double upperTargetRPMs = 0;
    //Encoder
    Encoder lowerEncoder;

    public UpperShooterEncoder(){

        //Setup the Jaguars
        conveyorMotor = new Jaguar(5);
        upperShootingMotor = new Jaguar(2);
        lowerShootingMotor = new Jaguar(3);

        //Setup the encoders
        lowerEncoder = new Encoder( new DigitalInput(5), new DigitalInput(6));
        lowerEncoder.start();
        lowerEncoder.setDistancePerPulse(360);
        
        //Coast motors by default
        //upperShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        //lowerShootingMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);

    }

    private double inchesToRPMs(double inches, int hoop) {

        //Shooting angle (convert to radians)
        final double shootingAngle = 56.5 * ( Math.PI / 180 );

        //The hoop height
        double hoopHeight = 0;

        //The shooting wheels diameter
        final double wheelDiameter = 6;

        //Set height for each hoop
        switch (hoop) {
            case 4: 
                hoopHeight = 91;
                break;
            case 3:
            case 2: 
                hoopHeight = 60;
                break;
            case 1:
            default:
                hoopHeight = 34;
                break;
        }

        //Adjust for camera height
        double actualHeight = hoopHeight - 34;

        //Adjust for height from shooter
        double differenceHeight = hoopHeight - 52;

        //Adjust for air
        inches = inches * 1.05;

        //Calculate actual distance
        inches = Math.sqrt(square(inches) - square(actualHeight)) - 9;

        //Calculate the velocity
        //Solve 1/2at^2+vt+y0=y
        double velocity = (-193.044 * square(inches)) / (differenceHeight - inches * Math.tan(shootingAngle));

        //If we have a negative velocity(impossible velocity)
        if (velocity < 0) {

            //Exit now
            return 0;

        }

        //Move velocity calcs
        velocity = Math.sqrt(velocity) / Math.cos(shootingAngle);

        //Return the calculated rpms
        return velocity * 60 / wheelDiameter;

    }

    public void prepareToShoot(double inches, int hoop) {

        //Setup the target RPMs
        lowerTargetRPMs = inchesToRPMs(inches, hoop);
        upperTargetRPMs = inchesToRPMs(inches, hoop) * .95;

        lowerTargetRPMs = 500;
        upperTargetRPMs = 500;
        
        System.out.println( "TargetRPMs:" + lowerTargetRPMs );
    }

    public boolean set(){
                
        //Max motor range
        final int maxDiff = 100;

        //The max rate of speed change
        final double changeRate = .01;

        int totalReady = 0;

        System.out.println("Speed:"+lowerEncoder.getRate());
        System.out.println("Target:"+lowerTargetRPMs);
        
        //The lower motor's speed 
        double lowerDiff = lowerEncoder.getRate() - lowerTargetRPMs;

        //If the motor RPMs are not within the range
        if (Math.abs(lowerDiff) > maxDiff) {

            //If positive
            if (Math.abs(lowerDiff) == lowerDiff) {

                //Change by change rate
                lowerShootingMotor.set(lowerShootingMotor.get() + changeRate);

            } else {

                //Change by change rate
                lowerShootingMotor.set(lowerShootingMotor.get() - changeRate);

            }

        } else {

            System.out.println("READY");
            
            //Add to ready total
            totalReady += 1;

        }
        /*
        //The upper motor's speed 
        double upperDiff = lowerEncoder.getRate() - upperTargetRPMs;

        //If the motor RPMs are not within the range
        if (Math.abs(lowerDiff) > maxDiff) {

            //If positive
            if (Math.abs(lowerDiff) == lowerDiff) {

                //Change by change rate
                upperShootingMotor.set(upperShootingMotor.get() + changeRate);

            } else {

                //Change by change rate
                upperShootingMotor.set(upperShootingMotor.get() - changeRate);

            }

        } else {

            //Add to ready total
            totalReady += 1;

        }
*/
        //Return success if both wheels are ready
        return (totalReady == 2);

    }

    public void stopShooting() {

        //Set both target RPMs to 0
        upperTargetRPMs = 0;
        lowerTargetRPMs = 0;

        //Stop both motors
        lowerShootingMotor.set(0);
        upperShootingMotor.set(0);

    }

    public void stopConveyor() {

        //Set to 0
        conveyorMotor.set(0);

    }

    public void moveConveyorWithValue(double power) {

        //Set to power
        conveyorMotor.set(power);

    }

    public void moveConveyorUp() {

        //Move up
        moveConveyorWithValue(-1);

    }

    public void moveConveyorDown() {

        //Move up
        moveConveyorWithValue(1);

    }

    public double square(double value) {

        //Square the value
        return value * value;

    }
}
