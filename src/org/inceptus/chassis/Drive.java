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
    public Drive(){
        //Try catch for errors
        try {

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

        } catch (CANTimeoutException ex) { //Catch CANTimeout Error
            
            //Print Error
            Debug.fatal(ex, "CAN Timeout in " + this.getClass().getName());

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in " + this.getClass().getName());
        }
    }

    public boolean driveWithValues(double x, double y, double rotation){
        //Try catch for errors
        try {
            //Drive using values
            robotDrive.mecanumDrive_Cartesian(x, y, rotation, 0);

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in DriveWithValues");
            //Return failure
            return false;

        }

        //Return Success
        return true;
    }
}
