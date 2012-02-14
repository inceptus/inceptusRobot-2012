package org.inceptus.chassis;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.inceptus.debug.Debug;

/**
 *
 * @author innoying
 */
public class Drive {
    //Motors
    private CANJaguar leftFront;
    private CANJaguar rightFront;
    private CANJaguar leftRear;
    private CANJaguar rightRear;
    //Drive
    private RobotDrive robotDrive;

    //Initilize
    public Drive() throws CANTimeoutException{

        //Setup the drive motors
        leftFront = new CANJaguar(12);
        rightFront = new CANJaguar(11);
        leftRear = new CANJaguar(10);
        rightRear = new CANJaguar(5);

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
        robotDrive.mecanumDrive_Cartesian(x, y, rotation, 0);
            
    }
}
