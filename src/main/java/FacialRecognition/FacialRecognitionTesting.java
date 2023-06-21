package FacialRecognition;

import java.io.FileWriter;

//openCV imports
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class FacialRecognitionTesting {

    

    

    public static void main(String[] args) {
        
        //test the facial recognitions effectiveness by adding different types of noise to the image
        //load preset images
    
        //first test the images with no noise
        int correct=0;
        FacialRecognition fr=FacialRecognition.getInstance();
        Mat[] images = getImages();
        for(int i=0;i<images.length;i++){
            
            if(fr.isFaceInImage(images[i])){
                correct++;
            }
            System.out.println("base: "+i);
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
            System.out.println("salt and pepper: "+i);
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
            System.out.println("gaussian: "+i);
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
            System.out.println("periodic: "+i);
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
        //src\main\resources\Facedataset\Humans this path contains 6991 images of humans with names 1(number)
        //open them all
        Mat[] images = new Mat[100];
        for(int i=0;i<100;i++){
            String link="src\\main\\resources\\Facedataset\\Humans\\1 ("+(i+1)+").jpg";
            Mat temp=Imgcodecs.imread(link);
            if(temp.empty()){
                //make it an empty image
                temp=new Mat(100,100, CvType.CV_8UC3);
                System.out.println("Error: Image not found on value "+i);        
            }
            //convert to grayscale
            Imgproc.cvtColor(temp, temp, Imgproc.COLOR_BGR2GRAY);
            images[i]=temp;
        }
        return images;
    }
    
}
