package org.inceptus.chassis;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author innoying
 */
public class LowerConveyor {
    
    private Jaguar conveyorMotor;
    
    public LowerConveyor(){

        //Setup the Jaguar
        conveyorMotor = new Jaguar(6);
           
    }
    
    public void moveUp(){
        
        //Go up at not full power to break conveyor
        conveyorMotor.set(.5);
        
    }
    
    public void moveDown(){
        
        //May be jamed
        conveyorMotor.set(-1);
        
    }
    
    public void stop(){
        
        //Stop
        conveyorMotor.set(0);
        
    }
    
    public void moveValue( double speed ){
        
        //Move
        conveyorMotor.set(speed);
        
    }
}
