package FacialRecognision;

import java.util.ArrayList;
import java.util.Random;

//openCV imports
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.highgui.HighGui;

public class ImageProcessing {
    
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

    // film grain 
    public static Mat addFilmGrain(Mat image, double strength, double mean) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        Random random = new Random();

        byte[] noise = new byte[rows * cols * channels];
        random.nextBytes(noise);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < channels; k++) {
                    double pixelValue = noisyImage.get(i, j)[k] +
                            strength * noise[i * cols * channels + j * channels + k] + mean;
                    noisyImage.put(i, j, k, pixelValue);
                }
            }
        }

        return noisyImage;
    }
    //     anisotropic
    public static Mat addAnisotropicNoise(Mat image, double strength, double mean, int blockSize, int maxVariation) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double noise = random.nextGaussian() * strength + mean;

                for (int k = 0; k < channels; k++) {
                    double pixelValue = noisyImage.get(i, j)[k] + noise;
                    noisyImage.put(i, j, k, pixelValue);
                }

                int blockStartX = Math.max(j - blockSize / 2, 0);
                int blockStartY = Math.max(i - blockSize / 2, 0);
                int blockEndX = Math.min(j + blockSize / 2, cols - 1);
                int blockEndY = Math.min(i + blockSize / 2, rows - 1);

                double variation = random.nextDouble() * maxVariation;

                for (int y = blockStartY; y <= blockEndY; y++) {
                    for (int x = blockStartX; x <= blockEndX; x++) {
                        if (x == j && y == i) continue;

                        double diff = noisyImage.get(i, j)[0] - noisyImage.get(y, x)[0];
                        double distance = Math.sqrt((j - x) * (j - x) + (i - y) * (i - y));

                        for (int k = 0; k < channels; k++) {
                            double pixelValue = noisyImage.get(y, x)[k] + variation * diff / distance;
                            noisyImage.put(y, x, k, pixelValue);
                        }
                    }
                }
            }
        }

        return noisyImage;
    }
    //     read noise
    public static Mat simulateReadNoise(Mat image, double stdDev) {
        Mat noisyImage = new Mat(image.size(), image.type());
        image.copyTo(noisyImage);

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < channels; k++) {
                    double noise = random.nextGaussian() * stdDev;
                    double pixelValue = noisyImage.get(i, j)[k] + noise;
                    noisyImage.put(i, j, k, pixelValue);
                }
            }
        }

        return noisyImage;
    }
    //     sensor heat
    public static Mat simulateSensorHeat(Mat image, double intensity, double displacement) {
        Mat heatedImage = new Mat(image.size(), image.type());
        image.copyTo(heatedImage);

        int rows = image.rows();
        int cols = image.cols();
        int channels = image.channels();

        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double offsetX = displacement * Math.sin((double) i / intensity);
                double offsetY = displacement * Math.cos((double) j / intensity);

                int newX = (int) (j + offsetX);
                int newY = (int) (i + offsetY);

                if (newX >= 0 && newX < cols && newY >= 0 && newY < rows) {
                    for (int k = 0; k < channels; k++) {
                        double pixelValue = image.get(newY, newX)[k];
                        heatedImage.put(i, j, k, pixelValue);
                    }
                }
            }
        }

        return heatedImage;
    }


    public static Mat EqualizeVChannel(Mat image){
        //convert image to HSV
        Mat hsvImage = new Mat(image.size(), image.type());
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        //split the image into its channels
        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(hsvImage, channels);
        //equalize the V channel
        Imgproc.equalizeHist(channels.get(2), channels.get(2));
        //merge the channels back together
        Core.merge(channels, hsvImage);
        //convert back to BGR
        Mat equalizedImage = new Mat(image.size(), image.type());
        Imgproc.cvtColor(hsvImage, equalizedImage, Imgproc.COLOR_HSV2BGR);
        return equalizedImage;
        
    }

}
