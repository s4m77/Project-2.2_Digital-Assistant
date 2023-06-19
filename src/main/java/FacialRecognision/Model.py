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
    MODEL_PATH = "src\\main\\java\\FacialRecognision\\model.joblib"
    face_cascade = cv2.CascadeClassifier('src\\main\\resources\\Facial models\\haarcascade_frontalface_default.xml')
    model=None
    picturePath="src\\main\\resources\\Faces"
    Certainity=0.6

    def __init__(self):
        self.model = joblib.load(self.MODEL_PATH)
        if os.path.exists(self.MODEL_PATH):
            self.model = joblib.load(self.MODEL_PATH)
        else:
            print("facial recognition model not found")
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

        return gray

    def predict(self, image):
        gray = self.process(image)
        predictions = []
        for i in gray:
            predictions.append(self.model.predict(i.reshape(1, -1)))
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
    
    def train(self,components=100):
        dir=self.picturePath
        Labels=os.listdir(dir)


        temp2=[dir+"/"+i for i in Labels]
        images=np.array([plt.imread(image) for image in temp2])
        Labels=np.array(Labels)
        ##convert to grayscale
        proccessed= [self.process(i) for i in images]
        
        Vectors=np.array([self.extract_features(i) for i in proccessed])
        normalized=(Vectors-np.mean(Vectors))/np.std(Vectors)
        pca=PCA(n_components=components)
        reduced=pca.fit_transform(normalized)
        classifier=KNeighborsClassifier(n_neighbors=1)
        classifier.fit(reduced,Labels)
        joblib.dump(classifier,self.MODEL_PATH)
        print("Model trained")
        return classifier
    
    def getCertainity(self,image,label):
        ##use cosine similarity to get the certainity of the prediction
        gray=self.process(image)
        feature_vector=self.extract_features(gray)
        normalized=(feature_vector-np.mean(feature_vector))/np.std(feature_vector)
        pca=PCA(n_components=100)
        reduced=pca.fit_transform(normalized)
        prediction=self.model.predict(reduced)
        certainity=cosine_similarity(reduced,self.model.embedding_)
        return certainity



    def isFace(self,image,label):
        certainity=self.getCertainity(image,label)
        return certainity>self.Certainity

    def facesAndCertainity(self,image):
        ##find all the faces in the image
        predictions=self.predict(image)
        faces=self.findFaces(image)
        certainity=[]
        for i in range(len(predictions)):
            certainity.append(self.getCertainity(image, predictions[i]))
        return faces,certainity



##open the camera and feed the image to the model
##if the model predicts a face, draw a rectangle around it
if __name__=="__main__":
    model=Model()
    
    cap=cv2.VideoCapture(0)

    ret, frame = cap.read()
    ##show the image
    list1,list2=model.facesAndCertainity(frame)
    ##convert both to just lists
    list1=list(list1)
    list2=list(list2)
    ##list are 1 to 1 convert them to tuples 
    tuples=[]
    for i in range(len(list1)):
        tuples.append([list1[i],list2[i]])
    print(tuples)
    cap.release()
    cv2.destroyAllWindows()
    
