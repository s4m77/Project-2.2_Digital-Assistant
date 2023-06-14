package FacialRecognision;

//openCV imports
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;


public class FacialRecognition {
    

    private CascadeClassifier faceDetector;
    private String classifierPath=System.getProperty("user.dir")+"/src/main/resources/Facial models/haarcascade_frontalface_default.xml";
    private String eyeClassifierPath=System.getProperty("user.dir")+"/src/main/resources/Facial models/haarcascade_eye_tree_eyeglasses.xml";
    private VideoCapture camera;
    private static FacialRecognition instance;

    public FacialModel currentModel;

    public static void main(String[] args) {
     


    }
    public enum FacialModel{
        FACE, EYE
    }

    private FacialRecognition(){
        //load the classifier
        nu.pattern.OpenCV.loadLocally();
        faceDetector = new CascadeClassifier(classifierPath);
        this.currentModel=FacialModel.FACE; // DEFAULT ATM
        //open the camera
        camera=new VideoCapture(0);

    }

    public static FacialRecognition getInstance(){
        if(instance==null){
            instance=new FacialRecognition();
        }
        return instance;
    }

    public void setFacialModel(FacialModel model){
        switch (model) {
            case FACE -> faceDetector = new CascadeClassifier(classifierPath);
            case EYE -> faceDetector = new CascadeClassifier(eyeClassifierPath);
        }
        currentModel=model;
    }


    public boolean peopleInCamera(){
        //load the image from the camera
        Mat image;
        try {
            image = LoadImageFromCamera();
        } catch (InterruptedException e) {
            System.out.println("Error: Camera not found!");
            return false;
        }
        //check if there is a face in the image
        return isFaceInImage(image);


    }

    public Mat LoadImageFromCamera() throws InterruptedException {
        Mat image = new Mat();
        camera.read(image);
        return image;
    }

    public boolean isFaceInImage(Mat image){
        //use detectMultiScale to detect faces
        
       
        //detect faces
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        //if there is a face return true
        return faceDetections.toArray().length > 0;
    }
}
