package org.inceptus.debug;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 *
 * @author innoying
 */
public class Debug {
    //Print an error
    public static void fatal(Exception ex, String msg){
        //Output to console
        System.out.println( "(inceptus) FATAL Exception:" );
        //Print exception
        ex.printStackTrace();
        //Print to console
        System.out.println( "(inceptus) FATAL Exception Message: " + msg );
        //Send message to driverstation
        DriverStationLCD.getInstance().println( DriverStationLCD.Line.kUser3, 1, 
                "FATAL:" + msg );
    }
    
    //Log a message
    public static void log(String msg){
        //Output to console
        System.out.println( "(inceptus) Log Message:" + msg );
        //Send message to driverstation
        DriverStationLCD.getInstance().println( DriverStationLCD.Line.kUser2, 1,
                "Log:" + msg );
    }
    
    public static void logImage(ColorImage im) throws NIVisionException{
        //Save to file
        im.write("inceptus-"+Timer.getFPGATimestamp()+".jpg");
    }

    public static void logImage(BinaryImage im) throws NIVisionException {
        //Save to file
        im.write("inceptus-"+Timer.getFPGATimestamp()+".jpg");
    }
}
