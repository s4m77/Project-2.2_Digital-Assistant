##file that contains class for facial recognition model

import cv2
import numpy as np
import os
import joblib
##highgui is used to show the image



class Model:
    MODEL_PATH = "model.joblib"
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    model=None
    def __init__(self):
        if os.path.exists(self.MODEL_PATH):
            self.model = joblib.load(self.MODEL_PATH)
        else:
            print("Model not found")
            exit(1)
    
    def process(self,image):

        faces=self.findFaces(image)
        cropped=[]
        for (x,y,w,h) in faces:
            cropped.append(image[y:y+h,x:x+w])
        ##resize the image to 64x64
        resized=[]
        for i in cropped:
            resized.append(cv2.resize(i,(64,64)))
        ##convert to grayscale
        gray=[]
        for i in resized:
            gray.append(cv2.cvtColor(i, cv2.COLOR_BGR2GRAY))
    
        return gray
    
    def predict(self,image):
        gray=self.process(image)
        predictions=[]
        for i in gray:
            predictions.append(self.model.predict(i.reshape(1,-1)))
        return predictions
    def findFaces(self,image):
        faces=self.face_cascade.detectMultiScale(image, 1.3, 5)
        return faces



##open the camera and feed the image to the model
##if the model predicts a face, draw a rectangle around it
if __name__=="__main__":
    model=Model()
    cap=cv2.VideoCapture(0)
    while True:
        ##use highgui to show the image
        ret,frame=cap.read()
        ##show the image
        cv2.imshow("frame",frame)
        
        
    cap.release()
    cv2.destroyAllWindows()
