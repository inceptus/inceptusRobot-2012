package org.inceptus.camera;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCamera.ExposureT;
import edu.wpi.first.wpilibj.camera.AxisCamera.WhiteBalanceT;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import org.inceptus.debug.Debug;

/**
 *
 * @author innoying
 */
public class TargetFinder {
    
    //If we should use debug code
    final boolean debug = true;
    
    //Image thresholds for colors
    private final int redLow = 200;
    private final int redHigh = 256;
    private final int greenLow = 0;
    private final int greenHigh = 128;
    private final int blueLow = 0;
    private final int blueHigh = 128;
    
    //Setup the box min height and widths
    private final int bboxWidthMin = 400;
    private final int bboxHeightMin = 400;
    
    //Save the intertia settings 
    private final float inertiaXMin = .5f;
    private final float inertiaYMin = .25f;
    
    //Min and max ratios
    private final double ratioMin = 1;
    private final double ratioMax = 2;
    
    //Setup the cam values
    private final int camBrightness = 50;
    private final int camColor = 50;
    private final WhiteBalanceT camWhiteBalance = WhiteBalanceT.hold;
    private final ExposureT camExposure = ExposureT.hold;
    public static final int IMAGE_WIDTH = 320;
    public static final int IMAGE_HEIGHT = 240;
    
    //Setup the camera
    AxisCamera cam;
    
    //Top target
    private Target highTarget = Target.NullTarget;
    
    //Other 4 targets
    private Target target1 = Target.NullTarget;
    private Target target2 = Target.NullTarget;
    private Target target3 = Target.NullTarget;
    private Target target4 = Target.NullTarget;
    
    //Box and inertia criteria
    private CriteriaCollection boxCriteria;
    private CriteriaCollection inertiaCriteria;

    public TargetFinder() {
        
        //Log
        Debug.log("TargetFinder() begin");

        //Start to conncet to the camera
        cam = AxisCamera.getInstance();
        
        //Set the settings
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeBrightness(camBrightness);
        cam.writeColorLevel(camColor);
        cam.writeWhiteBalance(camWhiteBalance);
        cam.writeExposureControl(camExposure);
        cam.writeMaxFPS(15);
        cam.writeExposurePriority(AxisCamera.ExposurePriorityT.none);
        cam.writeCompression(50);
        
        //Setup the criteria values
        boxCriteria = new CriteriaCollection();
        inertiaCriteria = new CriteriaCollection();
        
        //Save the values
        boxCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH,
                30, bboxWidthMin, false);
        boxCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT,
                40, bboxHeightMin, false);
        inertiaCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_XX,
                0, inertiaXMin, true);
        inertiaCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_NORM_MOMENT_OF_INERTIA_YY,
                0, inertiaYMin, true);
        
        //Wait for the camera to start (seconds)
        Timer.delay(8);
        
    }

    private void addTarget(Target t) {
        
        //Fill the first empty target slot.
        if (target1.isNull()) {
            target1 = t;
        } else if (target2.isNull()) {
            target2 = t;
        } else if (target3.isNull()) {
            target3 = t;
        } else if (target4.isNull()) {
            target4 = t;
        }
        
    }

    public Target processImage() {
        
        //If the image is the latest
        boolean success = cam.freshImage();
        //If we got the latest image successfully
        if (success) {
            try {
                //Log
                Debug.log("Entered try loop");
                //Get a fresh image
                ColorImage im = cam.getImage();
                //Log 
                Debug.log("Get a fresh image");
                //If Debug is enabled
                if (debug) {
                    //Save a debug image
                    Debug.logImage(im);
                    //Log image save
                    Debug.log("Saved Color Image");
                }
                //Get a threshold'ed image
                BinaryImage thresholdIm = im.thresholdRGB(
                        redLow, redHigh,
                        greenLow, greenHigh,
                        blueLow, blueHigh
                );
                
                //Run a particle filter using the box criteria
                BinaryImage filteredBoxIm = thresholdIm.particleFilter(boxCriteria);
                //Order the particle analysis
                ParticleAnalysisReport[] xparticles = filteredBoxIm.getOrderedParticleAnalysisReports();
                //Log results
                Debug.log(xparticles.length + " particles at " + Timer.getFPGATimestamp());
                
                //Run another particle filer using intertia criteria
                BinaryImage filteredInertiaIm = filteredBoxIm.particleFilter(inertiaCriteria);
                //Order the particle analysis
                ParticleAnalysisReport[] particles = filteredInertiaIm.getOrderedParticleAnalysisReports();
                //Log results
                Debug.log(particles.length + " particles at " + Timer.getFPGATimestamp());

                
                //Targets aren't found yet so they are null.
                highTarget = Target.NullTarget;
                target1 = Target.NullTarget;
                target2 = Target.NullTarget;
                target3 = Target.NullTarget;
                target4 = Target.NullTarget;
                
                //Log
                Debug.log("Targets created");
                
                // Minimum y <-> higher in image.
                double minY = IMAGE_HEIGHT; 
                
                //For each target found
                for (int i = 0; i < particles.length; i++) {
                    //Create a new target
                    Target t = new Target(i, particles[i]);
                    //If the target is the correct ratio
                    if (t.ratio > ratioMin && t.ratio < ratioMax) {
                        //Add the target
                        addTarget(t);
                        //If it is largest Y value (highest)
                        if (t.boxCenterY <= minY) {
                            //Save at the hightarget
                            highTarget = t;
                        }
                    }
                    //Log
                    Debug.log("Target " + i + ": (" + t.boxCenterX + "," + t.boxCenterY + ") Distance: " + t.distance);
                }
                //Log
                Debug.log("Best target: " + highTarget.index);
                Debug.log("Distance to the target: " + highTarget.distance);
                
                //Free images from memory.
                im.free();
                thresholdIm.free();
                filteredBoxIm.free();
                filteredInertiaIm.free();
                
                return highTarget;
                
            } catch (AxisCameraException ex) {
                
                //Print Error
                Debug.fatal(ex, "Axis Camera Error " + this.getClass().getName());

            } catch (NIVisionException ex) {
                
                //Print Error
                Debug.fatal(ex, "NIVision Error " + this.getClass().getName());
                
            } catch (Exception ex){
                
                //Print Error
                Debug.fatal(ex, "Unknown Error " + this.getClass().getName());
                
            }
        }
        return Target.NullTarget;
    }
    
    public Target getHighestTarget() {
        return highTarget;
    }

    public Target getTarget1() {
        return target1;
    }

    public Target getTarget2() {
        return target2;
    }

    public Target getTarget3() {
        return target3;
    }

    public Target getTarget4() {
        return target4;
    }
}
