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
    //Calculate the RPMs from either angle 45 or 60.
    public static int calculateRPMs(int distance, double angle, int hoop){
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
        double angleRads = angle * Math.PI/180;
        double velocity = (distance - 9)/(Math.sqrt((46 - height+(distance - 9)*Math.tan(angleRads))/16.087) * cos(angleRads));
        return velocity;
    }
    //Shoot
    public static void shoot(int RPMs){
        //Ramp up to rpm
    }
}
