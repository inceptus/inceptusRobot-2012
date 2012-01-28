/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inceptus;

import java.lang.Math;

/**
 *
 * @author innoying
 */
public class Shooting {
    //Calculate the RPMs from any angle and any distance
    public static double calculateRPMs(double distance, double angle, int hoop, double wheelDiameter){
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
        double velocity = (distance - 9)/(Math.sqrt((46 - height+(distance - 9)*Math.tan(angleRads))/16.087) * cos(angleRads));
        double RPM = velocity * 60 / (wheelDiameter * Math.PI);;
        return RPM;
    }
    //Shoot
    public static void shoot(double RPMs, int maxRPMs){
        //Ramp up to rpm
        double power = RPMs * 11 / 3 / maxRPMs;
        shootingWheel.set(power);
    }
}
