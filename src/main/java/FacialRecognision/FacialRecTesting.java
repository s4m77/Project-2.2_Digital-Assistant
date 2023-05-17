package FacialRecognision;

import java.util.ArrayList;
import java.util.Random;

//openCV imports
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.highgui.HighGui;


public class FacialRecTesting {
    //file contains many methods of adding noise to an image to test the facial recognition

    
    public static Mat addSaltAndPepperNoise(Mat image, double noisePercentage) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        int numPixels = (int) (image.total() * noisePercentage);

        for (int i = 0; i < numPixels; i++) {
            int row = (int) (Math.random() * image.rows());
            int col = (int) (Math.random() * image.cols());

            if (Math.random() < 0.5) {
                noisyImage.put(row, col, 255, 255, 255);  // Set pixel to white (salt)
            } else {
                noisyImage.put(row, col, 0, 0, 0);        // Set pixel to black (pepper)
            }
        }

        return noisyImage;
    }
    
    public static Mat addGaussianNoise(Mat image, double mean, double stdDev) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        Random random = new Random();

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        byte[] noise = new byte[rows * cols * channels];
        random.nextBytes(noise);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < channels; k++) {
                    double pixelValue = noisyImage.get(i, j)[k] + noise[i * cols * channels + j * channels + k] * stdDev + mean;
                    noisyImage.put(i, j, k, pixelValue);
                }
            }
        }

        return noisyImage;
    }

    public static Mat addPeriodicNoise(Mat image, double amplitude, int frequencyX, int frequencyY) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = amplitude * Math.sin(2 * Math.PI * i / frequencyX);
                double y = amplitude * Math.sin(2 * Math.PI * j / frequencyY);

                for (int k = 0; k < channels; k++) {
                    double pixelValue = noisyImage.get(i, j)[k] + x + y;
                    noisyImage.put(i, j, k, pixelValue);
                }
            }
        }

        return noisyImage;
    }

    public static Mat EqualizeHistoMat(Mat image) {
        //assumes image is greyscale
        Mat equalizedImage = new Mat(image.size(), image.type());
        Imgproc.equalizeHist(image, equalizedImage);
        return equalizedImage;
    }


    
}
