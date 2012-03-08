package org.inceptus.OI;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author innoying
 */
public class InJoystick extends Joystick{
    
    //Throttle min and max values
    private static final double limitMax = .8;
    private static final double limitMin = .2;
    
    //The scale for twisting values
    private static final double twistScale = .4;
    
    //Constructor
    public InJoystick(int port){
        //Call super
        super(port);
    }

    public double getScaledThrottle(){
        //Get raw throttle ( -1 - 1 )
        double T = this.getRawAxis(3) * -1;
        
        //Add 1 to make value 0-2 then scale back to 0-1
        T = ( ( T + 1 ) / 2 );
        
        //Scale value between high and low limits
        T = limitMin + ( T * (limitMax - limitMin) );
        
        //Return the scaled value
        return T;
    }
    
    public double getRawAxisThreshold(int axis){
        //Get the raw axis value
        double A = this.getRawAxis(axis);
        //If the absolute calue if greater than the threshold
        if(Math.abs(A) > .2){
            //Return the value
            return A;
        }else{
            //Return 0 since it is under the threshold
            return 0;
        }
    }
    
    public double getScaledX(){
        //Get the Raw X value
        double X = this.getRawAxisThreshold(1);
        //Get the throttle value
        double T = this.getScaledThrottle();
        //Return the modified throttle value * the raw X value
        return X * T;
    }
    
    public double getScaledY(){
        //Get the Raw Y value
        double Y = this.getRawAxisThreshold(2);
        //Get the throttle value
        double T = this.getScaledThrottle();
        //Return the modified throttle value * the raw Y value
        return Y * T;
    }
    
    public double getScaledTwist(){
        //Return the raw twist value * the twist scale
        return this.getRawAxis(4) * twistScale;
    }
}
