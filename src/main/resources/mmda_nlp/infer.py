import spacy
import os
from fastapi import FastAPI, HTTPException
from typing import Optional
import uvicorn

def prediction(text):
    # loadmodel
    nlp = spacy.load(os.getcwd()+'/src/main/resources/mmda_nlp/model')

    predict_text = text

    doc = nlp(predict_text)
    cats = doc.cats

    #label mapping 
    label_mapping = {
        "1": "<SCHEDULE>",
        "2": "<LOCATION>",
        "3": "<WIKI>",
        "4": "<MATHQUESTION>",
        "5": "<WEATHER>",
        "6": "<MATHEQUAL>"
    }
    highest_score_label = max(cats, key=cats.get)
    highest_score = cats[highest_score_label]

    return label_mapping[highest_score_label], "{:.4f}".format(highest_score)

#use to init app model
app = FastAPI()

@app.get("/predict")
def predict(text: Optional[str] = None):
    if text is None:
        raise HTTPException(status_code=400, detail="Missing 'text' query parameter")

    try:
        label, score = prediction(text) 
        

        response = {"label": label, "score": score}
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8000)

