package org.inceptus.drive;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
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
    public boolean Init(){
        //Try catch for errors
        try {

            //Setup the drive motors
            leftFront = new CANJaguar(12);
            rightFront = new CANJaguar(11);
            leftRear = new CANJaguar(10);
            rightRear = new CANJaguar(5);

            //Setup the Drive
            robotDrive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

        } catch (CANTimeoutException ex) { //Catch CANTimeout Error

            //Print Error
            Debug.fatal(ex, "CAN Timeout in Drive");
            //Return failure
            return false;

        } catch (Exception ex){ //Catch all for errors

            //Print Error
            Debug.fatal(ex, "Unknown error in Drive");
            //Return failure
            return false;

        }

        //Return Success
        return true;
    }

    public boolean DriveWithValues(double x, double y, double rotation, double throttle){
        //Try catch for errors
        try {

            //Setup min and max values for magnitude
            final double magnitudeMin = .3;
            final double magnitudeMax = .7;

            //Setup the twist multiplier
            final double twistMultiplier = .5;

            //Adjust throttle to 0-1 range
            throttle = ( ( throttle + 1 ) / 2 );

            //Scale the magnitude down to not overpower the motors
            throttle = magnitudeMin + ( throttle * (magnitudeMax - magnitudeMin) );

            //Scale the throttle with each of the values
            x = x * (1 - throttle);
            y = y * (1 - throttle);

            //Scale the rotation with the value
            rotation = rotation * twistMultiplier;

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
