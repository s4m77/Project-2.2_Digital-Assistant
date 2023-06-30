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
import numpy as np
import tensorflow as tf
from tensorflow.keras import layers
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from tensorflow.keras.models import load_model
##import train test split
from sklearn.model_selection import train_test_split

##highgui is used to show the image


class Model:
    CNN_MODEL_PATH = "src/main/java/FacialRecognition/my_model.h5"
    face_cascade = cv2.CascadeClassifier('src/main/resources/Facial models/haarcascade_frontalface_default.xml')
    CNN_MODEL=None

    picturePath="src/main/resources/Faces"

    def __init__(self):

        if os.path.exists(self.CNN_MODEL_PATH):
            self.CNN_MODEL = load_model(self.CNN_MODEL_PATH)
        else:
            print("facial recognition model not found")
            exit(1)

        if self.CNN_MODEL is None:
            print("CNN model not found")
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
            image=np.reshape(i,(1,64,64,1))
            number= self.CNN_MODEL.predict(image)
            number=np.argmax(number)
            print(number)
            predictions.append(self.intToName(number))
        return predictions




    def findFaces(self,image):
        faces=self.face_cascade.detectMultiScale(image, 1.3, 5)
        return faces


    def nameToInt(self,name):
        if name=="tom":
            return 0
        elif name=="Jesse":
            return 1
        elif name=="Elza":
            return 2
        elif name=="sam":
            return 3
        elif name=="Reda":
            return 4
        elif name=="kumar":
            return 5
        else:
            print("unknown name")
            return 6

    def intToName(self,num):
        if num==0:
            return "tom"
        elif num==1:
            return "Jesse"
        elif num==2:
            return "Elza"
        elif num==3:
            return "sam"
        elif num==4:
            return "Reda"
        elif num==5:
            return "kumar"
        else:
            return "unknown"












#open the camera and feed the image to the model
#if the model predicts a face, draw a rectangle around it
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



        # predictions=model.predict(frame)
        # if len(predictions)>0:
        #     break



        # predictions=model.predict(frame)
        # if len(predictions)>0:
        #     break


    ##print(predictions)
    cap.release()
    cv2.destroyAllWindows()


