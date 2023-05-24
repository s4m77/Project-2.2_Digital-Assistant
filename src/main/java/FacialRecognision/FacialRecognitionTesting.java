package FacialRecognision;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

//openCV imports
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.highgui.HighGui;


public class FacialRecognitionTesting {

    

    

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //test the facial recognitions effectiveness by adding different types of noise to the image
        //load preset images
        Mat[] images = getImages();
        //first test the images with no noise
        int correct=0;
        FacialRecognition fr=FacialRecognition.getInstance();
        for(int i=0;i<images.length;i++){
            
            if(fr.isFaceInImage(images[i])){
                correct++;
            }
        }
        double correctNormal=correct/images.length;


        //now test the images with salt and pepper noise with intervals of 1% to 25%
        double[] saltAndPepperNoise = new double[25];
        for(int i=1;i<25;i++){
            correct=0;
            for(int j=0;j<images.length;j++){
                if(fr.isFaceInImage(ImageProcessing.addSaltAndPepperNoise(images[j], i/100))){
                    correct++;
                }
            }
            saltAndPepperNoise[i]=correct/images.length;
        }

        //now test the images with gaussian noise with different standard deviations from 0.1 to 2.5 with intervals of 0.1
        double[] gaussianNoise = new double[25];
        for(int i=1;i<25;i++){
            correct=0;
            for(int j=0;j<images.length;j++){
                if(fr.isFaceInImage(ImageProcessing.addGaussianNoise(images[j], 0, i/10))){
                    correct++;
                }
            }
            gaussianNoise[i]=correct/images.length;
        }

        //now test the images with periodic noise with different amplitudes from 10 to 200 with intervals of 10
        double[] periodicNoise = new double[19];
        for(int i=0;i<19;i++){
            correct=0;
            for(int j=0;j<images.length;j++){
                if(fr.isFaceInImage(ImageProcessing.addPeriodicNoise(images[j], (i+10)*10, 1, 1))){
                    correct++;
                }
            }
            periodicNoise[i]=correct/images.length;
        }


        //output the results into a txt file
        try{
            FileWriter fw = new FileWriter("results.txt");
            fw.write("Normal: "+correctNormal+"\n");
            fw.write("Salt and Pepper Noise: \n");
            for(int i=0;i<25;i++){
                fw.write(i+"%: "+saltAndPepperNoise[i]+"\n");
            }
            fw.write("Gaussian Noise: \n");
            for(int i=0;i<25;i++){
                fw.write(i/10+": "+gaussianNoise[i]+"\n");
            }
            fw.write("Periodic Noise: \n");
            for(int i=0;i<19;i++){
                fw.write((i+10)*10+": "+periodicNoise[i]+"\n");
            }
            fw.close();
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }

    }

    public static Mat[] getImages(){
        FacialRecognition fr=FacialRecognition.getInstance();
        try{ 
            return new Mat[]{fr.LoadImageFromCamera()};
        }
        catch(Exception e){
            System.out.println("Error: "+e);
            return null;
        }
    }
    
}
