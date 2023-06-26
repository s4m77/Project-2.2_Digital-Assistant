import cv2
import numpy as np
import os
from sklearn.decomposition import PCA
from sklearn.neighbors import KNeighborsClassifier
from skimage.feature import local_binary_pattern
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt

name = input("Who is in the picture?")
path="src/main/java/FacialRecognition/Faces"


##get frontal face
face_cascade = cv2.CascadeClassifier('src/main/resources/Facial models/haarcascade_frontalface_default.xml')
if face_cascade.empty() or face_cascade is None:
    print("face cascade model not found")
    exit(1)
##open the camera   
cap = cv2.VideoCapture(0)


counter=0
while True:
    ##capture frame by frame
    ret, frame = cap.read()
    ##convert to grayscale
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    ##detect faces
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)
    ##save faces 
    for (x, y, w, h) in faces:
        ##get from gray
        roi_gray = gray[y:y+h, x:x+w]
        ##reshape to 64x64
        roi_gray = cv2.resize(roi_gray, (64, 64))
        ##save the face
        cv2.imwrite(os.path.join(path , name+str(counter)+'.jpg'), roi_gray)
        counter+=1
    ##draw rectangle around the faces
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 255, 0), 2)
    ##if q is pressed exit
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    ##display the resulting frame
    cv2.imshow('frame', frame)



##when everything is done release the capture
cap.release()
cv2.destroyAllWindows()

