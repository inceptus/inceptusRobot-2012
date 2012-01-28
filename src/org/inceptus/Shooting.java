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
    public static int calculateRPMs(int distance, boolean angle60, int hoop){
        //Calculate height
        int height;
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
        //If the angle is 60 degrees
        if(angle60){
            return distance*height;
        }else{
            return distance*height;
        }
    }
    //Shoot
    public static void shoot(int RPMs){
        //Ramp up to rpm
    }
}
