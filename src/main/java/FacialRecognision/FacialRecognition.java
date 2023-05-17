package FacialRecognision;

import java.util.ArrayList;
//openCV imports
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.Video;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class FacialRecognition {
    
    public static boolean loaded=false;
    public static CascadeClassifier faceDetector;
    public static String classifierPath="src\\main\\resources\\Facial models\\haarcascade_frontalface_default.xml";
    public static VideoCapture camera;
    
    public static void main(String[] args) {
        //test if the camera is working 
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        if(!openCamera()){
            System.out.println("Error: Camera not found!");
            System.exit(0);
        }
    }


    public static boolean peopleInCamera(){
        //load the image from the camera
        Mat image;
        try {
            image = LoadImageFromCamera();
        } catch (InterruptedException e) {
            System.out.println("Error: Camera not found!");
            return false;
        }
        //check if there is a face in the image
        if(isFaceInImage(image)){
            return true;
        }
        else{
            return false;
        }


    }

    public static Mat LoadImageFromCamera() throws InterruptedException {
        if(!openCamera()){
            System.out.println("Error: Camera not found!");
            throw new InterruptedException();
        }
        Mat image = new Mat();
        camera.read(image);
        return image;
    }


    

    private static boolean openCamera() throws UnsatisfiedLinkError{
        if(camera==null){
            camera = new VideoCapture(0);
        }
        if(!camera.isOpened()){
            return false;
        }
        return true;
    }


    public static boolean isFaceInImage(Mat image){
        //use detectMultiScale to detect faces
        //the image needs to be greyscale
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        //load the classifier
        if(!loaded){
            faceDetector = new CascadeClassifier(classifierPath);
            loaded=true;
        }
        //detect faces
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        //if there is a face return true
        if(faceDetections.toArray().length>0){
            return true;
        }
        else{
            return false;
        }
    }
}
