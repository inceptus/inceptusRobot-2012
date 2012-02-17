package org.inceptus.chassis;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import org.inceptus.debug.Debug;

/**
 *
 * @author innoying
 */
public class Drive {
    
    //Motors
    private Jaguar leftFront;
    private Jaguar rightFront;
    private Jaguar leftRear;
    private Jaguar rightRear;
    //Drive
    private RobotDrive robotDrive;
    //accelerometer
    //private ADXL345_I2C accelerometer;
    //Gyro
    private Gyro gyro;
    
    //Initilize
    public Drive(){

        //Setup the drive motors
        leftFront = new Jaguar(7);
        rightFront = new Jaguar(8);
        leftRear = new Jaguar(9);
        rightRear = new Jaguar(10);

        //Setup the Drive
        robotDrive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

        //Invert the motors
        robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
        robotDrive.setInvertedMotor(MotorType.kRearRight, false);
        robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
        robotDrive.setInvertedMotor(MotorType.kRearLeft, true);

    }

    public void driveWithValues(double x, double y, double rotation){

        //Drive using values
        robotDrive.mecanumDrive_Cartesian(x, -y, -rotation, 0);
            
    }
    
    public void stop(){
        
        //Stop the motors
        robotDrive.stopMotor();
    }
    
    public void balance(){
        
        final double ratio = .02;
        
        double angle = gyro.getAngle();
        
        double speed = (angle * ratio);
        
        Debug.log(angle + "\t" + speed);
        
        if (angle >= 5 &&  angle <= -5){
            
            stop();
            
        }else if(angle <= 35 && angle >= -35){
            
            robotDrive.mecanumDrive_Cartesian(0.0, -speed, 0.0, 0.00);
            
        }else{
            
            stop();
            
        }
        
    }
}
