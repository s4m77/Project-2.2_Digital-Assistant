## file that contains class for facial recognition model

import cv2
import numpy as np
import os
import joblib
import matplotlib.pyplot as plt
##highgui is used to show the image

from sklearn.decomposition import PCA
from sklearn.neighbors import KNeighborsClassifier
from skimage.feature import local_binary_pattern
from sklearn.model_selection import train_test_split
from sklearn.metrics.pairwise import cosine_similarity

##highgui is used to show the image


class Model:
    KNN_MODEL_PATH = "src\main\java\FacialRecognition\KNNMODELSELF"
    PCA_MODEL_PATH = "src\main\java\FacialRecognition\PCAMODELSELF"
    face_cascade = cv2.CascadeClassifier('src\\main\\resources\\Facial models\\haarcascade_frontalface_default.xml')
    KNN_MODEL=None
    PCA_MODEL=None

    picturePath="src\\main\\resources\\Faces"
  
    def __init__(self):
        
        if os.path.exists(self.KNN_MODEL_PATH):
            self.KNN_MODEL = joblib.load(self.KNN_MODEL_PATH)
        else:
            print("facial recognition model not found")
            exit(1)
        if os.path.exists(self.PCA_MODEL_PATH):
            self.PCA_MODEL = joblib.load(self.PCA_MODEL_PATH)
        else:
            print("PCA model not found")
            exit(1)
        if self.face_cascade.empty() or self.face_cascade is None:
            print("face cascade model not found")
            exit(1)
    def process(self, image):

        faces = self.findFaces(image)
        cropped = []
        for (x, y, w, h) in faces:
            cropped.append(image[y:y + h, x:x + w])
        ## resize the image to 64x64
        resized = []
        for i in cropped:
            resized.append(cv2.resize(i, (64, 64)))
        ## convert to grayscale
        gray = []
        for i in resized:
            gray.append(cv2.cvtColor(i, cv2.COLOR_BGR2GRAY))
            ##set to numpy array
        gray = np.array(gray)


        return gray

    def predict(self, image):
        gray = self.process(image)
        predictions = []
        for i in gray:
            feature_vector = self.extract_features(i)

            feature_vector = (feature_vector - np.mean(feature_vector)) / np.std(feature_vector)
            transformed = self.PCA_MODEL.transform(feature_vector.reshape(1, -1))

            value=self.KNN_MODEL.predict(transformed)
            predictions.append(value[0])
        return predictions
    

    def predictAll(self,image):
        gray= self.process(image)
        predictions=[]
        for i in gray:
            ##get the feature vector
            feature_vector=self.extract_features(i)
            ##normalize the feature vector
            feature_vector=(feature_vector-np.mean(feature_vector))/np.std(feature_vector)
            transformed=self.PCA_MODEL.transform(feature_vector.reshape(1,-1))

            ##predict the class
            value=self.KNN_MODEL.predict(transformed)
            predictions.append(value[0])

        return predictions

    

    def findFaces(self,image):
        faces=self.face_cascade.detectMultiScale(image, 1.3, 5)
        return faces
    

    def extract_features(self,image):
        ##method assumes that the image is a grayscale image
        
        # Apply LBP
        radius = 1
        num_points = 8 * radius
        lbp = local_binary_pattern(image, num_points, radius, method='uniform')
        
        # Flatten the LBP image into a feature vector
        feature_vector = lbp.flatten()
        
        return feature_vector
    

    
    
    





##open the camera and feed the image to the model
##if the model predicts a face, draw a rectangle around it
if __name__=="__main__":
    model=Model()
    
    cap=cv2.VideoCapture(0)

    while True:
        ret, frame = cap.read()
        faces=model.findFaces(frame)
        ##put a rectangle around the face
        if(len(faces)>0):
            predictions=model.predict(frame)
            print(predictions)
        for (x,y,w,h) in faces:
            cv2.rectangle(frame,(x,y),(x+w,y+h),(255,0,0),2)
            ##put the prediction on the image
            cv2.putText(frame,str(predictions[0]),(x,y),cv2.FONT_HERSHEY_SIMPLEX,1,(255,0,0),2)
        cv2.imshow("frame",frame)


        ##repeat until the user presses q
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
            
        

        
        
            

<<<<<<< HEAD

        # predictions=model.predict(frame)
        # if len(predictions)>0:
        #     break

=======

        # predictions=model.predict(frame)
        # if len(predictions)>0:
        #     break

>>>>>>> a39462a041355a4608605aef1ca891658691895f
    ##print(predictions)  
    cap.release()
    cv2.destroyAllWindows()

##if the model predicts a face, draw a rectangle around it
# if __name__=="__main__":
#     model=Model()
    
#     cap=cv2.VideoCapture(0)

#     while True:
#         ret, frame = cap.read()
#         predictions=model.predict(frame)
#         if len(predictions)>0:
#             break

#     print(predictions)
#     ##list are 1 to 1 convert them to tuples   
#     cap.release()
#     cv2.destroyAllWindows()

    
