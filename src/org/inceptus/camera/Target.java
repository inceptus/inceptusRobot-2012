package org.inceptus.camera;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author innoying
 */
public class Target {
    
    //The target index
    public final int index;
    
    //Measured values.
    public final double rawBboxCornerX;
    public final double rawBboxCornerY;
    public final double rawBboxWidth;
    public final double rawBboxHeight;
    
    public double distance = 0;
    
    //Calculated values.
    public final double boxCenterX;
    public final double boxCenterY;
    public final double ratio;
    public double actualHeight;
    
    private final double centerMassX;
    private final double centerMassY;
    
    //To check whether a target is null, we check whether bboxCornerX >= 0.
    public static final Target NullTarget = new Target(-1,-1,0,0,0,0,0);

    //Constructor
    public Target(int index, double bboxCornerX, double bboxCornerY,
                  double bboxWidth, double bboxHeight, 
                  double centerX, double centerY) {
        
        this.index = index;
        rawBboxCornerX = bboxCornerX;
        rawBboxCornerY = bboxCornerY;
        rawBboxWidth = bboxWidth;
        rawBboxHeight = bboxHeight;
        boxCenterX = -TargetFinder.IMAGE_WIDTH/2.0 + bboxCornerX + bboxWidth/2.0;
        boxCenterY = -TargetFinder.IMAGE_HEIGHT/2.0 + bboxCornerY + bboxHeight/2.0;
        ratio = bboxWidth / bboxHeight;
        centerMassX = centerX;
        centerMassY = centerY;
        
        //FUN MATH TIME. This will calculate the actual distance to be used for skew.
        double w = rawBboxWidth;
        double h = rawBboxHeight;
        //insert unmotivated constants to make this system of equations less ugly
        double r_1 = centerMassX * centerMassX / w - 2 * centerMassX + w / 2.0;
        double r_2 = centerMassX * h - (w - centerMassX) * h;
        double r_3 = (w - centerMassX)*(w - centerMassX) / w - 3*w/2.0 + 2 * centerMassX;
        double r_4 = 2*(h - 2*centerMassX);
        //the system of equations I am actually solving is r_1 b + r_2 = r_3 a and b + r_4 = a.
        double a = (r_2 - r_1 * r_4) / (r_3 - r_1);
        double b = (r_2 - r_3 * r_4) / (r_3 - r_1);
        actualHeight = (b - a) * centerMassX / w + h - b;
        
        distance = 3185.6 / (rawBboxHeight * Math.tan(0.4101));
        
    }
    
    //Ease of use constuctor
    public Target(int index, ParticleAnalysisReport p) {
        this(index, p.boundingRectLeft, p.boundingRectTop, p.boundingRectWidth,
             p.boundingRectHeight, p.center_mass_x, p.center_mass_y);
    }

    //If the target isn't null/non-existant
    public boolean isNotNull() {
        return (rawBboxCornerX >= 0);
    }
    
    //If the target is null/non-existant
    public boolean isNull() {
        return (rawBboxCornerX < 0);
    }
}
