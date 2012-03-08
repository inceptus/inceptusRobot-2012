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
    public double actualDistance = 0;
    //Calculated values.
    public final double boxCenterX;
    public final double boxCenterY;
    public final double ratio;
    public double actualHeight;
    public final double centerMassX;
    public final double centerMassY;
    //To check whether a target is null, we check whether bboxCornerX >= 0.
    public static final Target NullTarget = new Target(-1, -1, 0, 0, 0, 0, 0);

    //Constructor
    public Target(int index, double bboxCornerX, double bboxCornerY,
            double bboxWidth, double bboxHeight,
            double centerX, double centerY) {

        this.index = index;
        rawBboxCornerX = bboxCornerX;
        rawBboxCornerY = bboxCornerY;
        rawBboxWidth = bboxWidth;
        rawBboxHeight = bboxHeight;
        boxCenterX = -( TargetFinder.IMAGE_WIDTH / 2.0)  + bboxCornerX + (bboxWidth / 2.0);
        boxCenterY = -( TargetFinder.IMAGE_HEIGHT / 2.0) + bboxCornerY + (bboxHeight / 2.0);
        ratio = bboxWidth / bboxHeight;
        centerMassX = centerX - rawBboxCornerX;
        centerMassY = centerY - rawBboxCornerY;

        //FUN MATH TIME. This will calculate the actual distance to be used for skew.
        double w = rawBboxWidth;
        if (boxCenterX < 0){
            w = w - (boxCenterX / 25.0);
        }else{
            w = w + (boxCenterX / 25.0);
        }
        
        double h = rawBboxHeight;
        if (boxCenterY < 0){
            h = h - (boxCenterY / 25.0);
        }else{
            h = h + (boxCenterY / 25.0);
        }
        
        //insert unmotivated constants to make this system of equations less ugly
        /*double r_1 = (centerMassX * centerMassX / w) - (w / 2.0);
        double r_2 = (h * w) - (2 * centerMassX * h);
        double r_3 = ((w - centerMassX) * (w - centerMassX) / w) - (w / 2.0);
        double r_4 = (2 * h) - (4 * centerMassY);
        //the system of equations I am actually solving is r_1 b + r_2 = r_3 a and b + r_4 = a.*/
        distance = 3186 / (h * Math.tan(0.4101));
        //if(r_3 - r_1 < 2){
            //System.out.println("Distance with skew cannot be calculated from the given information.");
        //}
        /*else{
            System.out.println(r_1 + " " + r_2 + " " + r_3 + " " + r_4);
            double a = (r_2 - (r_1 * r_4)) / (r_3 - r_1);
            double b = (r_2 - (r_3 * r_4)) / (r_3 - r_1);
            actualHeight = ((b - a) * centerMassX / w) + h - b;
            System.out.println("Center of Mass (" + centerMassX + "," + centerMassY + ")" + "Box Dim. (" + w + "," + h + ")");
            System.out.println("(a, b) was calculated to be (" + a + ", " + b + ")");
            actualDistance = 3186 / (actualHeight * Math.tan(0.4101));
        }*/
        System.out.println(distance);

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
