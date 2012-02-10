package org.inceptus.debug;

import edu.wpi.first.wpilibj.DriverStationLCD;

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
        DriverStationLCD.getInstance().println( DriverStationLCD.Line.kUser3, 1, "FATAL:" + msg );
    }
}
