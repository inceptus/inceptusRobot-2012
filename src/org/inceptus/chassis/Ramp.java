package org.inceptus.chassis;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author innoying
 */
public class Ramp {
    
    //Setup the vars
    private Victor rampMotor;
    private DigitalInput lowerLimitSwitch;
    private DigitalInput upperLimitSwitch;
    
    
    public boolean init(){
        
        //Setup the ramp motor
        rampMotor = new Victor(6);
        
        //Setup the Limit Switches
        lowerLimitSwitch = new DigitalInput(1);
        upperLimitSwitch = new DigitalInput(2);
        
        //Return Success
        return true;
        
    }
    
    public boolean moveUp(){
        
        //If the limit switch is pressed or not
        if(upperLimitSwitch.get()){
            //Hold position
            rampMotor.set(0);
        }else{
            //Drive up
            rampMotor.set(.4);
        }
        
        //Return Success
        return true;
    }
    
    public boolean moveDown(){
        
        //If the limit switch is pressed or not
        if(lowerLimitSwitch.get()){
            //Hold position
            rampMotor.set(0);
        }else{
            //Drive down
            rampMotor.set(-.4);
        }
        
        //Return Success
        return true;
    }
    
    public boolean stop(){
        //Stop the motor
        rampMotor.set(0);
        //Return Success
        return true;
    }
}
