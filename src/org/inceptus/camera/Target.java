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
    
    //Calculated values.
    public final double centerX;
    public final double centerY;
    public final double ratio;
    
    //To check whether a target is null, we check whether bboxCornerX >= 0.
    public static final Target NullTarget = new Target(-1,-1,0,0,0);

    //Constructor
    public Target(int index, double bboxCornerX, double bboxCornerY,
                  double bboxWidth, double bboxHeight) {
        this.index = index;
        rawBboxCornerX = bboxCornerX;
        rawBboxCornerY = bboxCornerY;
        rawBboxWidth = bboxWidth;
        rawBboxHeight = bboxHeight;
        centerX = -TargetFinder.IMAGE_WIDTH/2.0 + bboxCornerX + bboxWidth/2.0;
        centerY = -TargetFinder.IMAGE_HEIGHT/2.0 + bboxCornerY + bboxHeight/2.0;
        ratio = bboxWidth / bboxHeight;
    }

    //Ease of use constuctor
    public Target(int index, ParticleAnalysisReport p) {
        this(index, p.boundingRectLeft, p.boundingRectTop, p.boundingRectWidth,
             p.boundingRectHeight);
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
