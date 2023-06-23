import spacy
import pandas as pd
from spacy.util import minibatch, compounding
from spacy.training import Example

import os
os.environ["KMP_DUPLICATE_LIB_OK"]="TRUE"


def load_csv(file_path):
    """
    Load a csv file into a dataframe.

    Args:
        file_path (str): Path to csv file.

    Returns:
        Pandas DataFrame: Dataframe with 'body' and 'score' columns.
    """
    df = pd.read_csv(file_path, usecols=['sentence', 'label'])
    return df

def create_spacy_dataset(df, labels, split_ratio=0.8):
    
    # Shuffle the dataframe
    df = df.sample(frac=1).reset_index(drop=True)
    # Split the dataframe into training and validation sets
    train_data = []
    val_data = []
    #split_ratio = 0.8  # 80% for training, 20% for validation

    # Iterate through the dataframe and create a list of tuples for each row
    for index, row in df.iterrows():
        text = row["sentence"]
        score = row["label"]
        label_dict = {}
        for label in labels:
            label_dict[label] = 1 if label == score else 0
        if index < len(df) * split_ratio:
            train_data.append((text, {"cats": label_dict}))
        else:
            val_data.append((text, {"cats": label_dict}))
    return train_data, val_data

def define_nlp_pipeline(labels):
    # Define the pipeline components
    nlp = spacy.blank('en')
    if "textcat_multilabel" not in nlp.pipe_names:
        textcat = nlp.add_pipe("textcat_multilabel")
        #nlp.add_pipe(textcat, last=True)
    else:
        textcat = nlp.get_pipe("textcat_multilabel")

    # Add the labels to the pipeline
    for label in labels:
        textcat.add_label(label)
    return nlp


def TrainSpacy(nlp, train_data, test_data, iterations):
    with nlp.select_pipes(enable="textcat_multilabel"):
        optimizer = nlp.begin_training()
        print("Training the model...")
        print('{}\t{}'.format("Iteration", "Loss"))
        k = 0
        for j in range(iterations):
            losses = {}
            batches = minibatch(train_data, size = compounding(4.,32.,1.0001))
            for batch in batches:
                text, annotations = zip(*batch)
                example = []
                for i in range(len(text)):
                    try:
                        doc = nlp.make_doc(text[i])
                    except:
                        print("error in Text with lenght : " + str(len(text)) + "***** for text[i] : " + str(text[i]) + " in annotation : " +str(annotations[i]))
                    example.append(Example.from_dict(doc, annotations[i]))
                nlp.update(example, sgd=optimizer, drop=0.2, losses = losses)
            print('{}\t{:.2f}'.format(k, losses['textcat_multilabel']))

            score = EvaluateSpacy(nlp, test_data)
            # remove keys values pairs from score dictionary
            score = {k: v for k, v in score.items() if k != 'token_p' and k != 'token_r'and k != 'token_f'}
            score['token_acc'] = k

            # assign dict to dataframe
            if k==0:
                df_score = pd.DataFrame(score)
            else:
                newrows = pd.DataFrame(score)
                df_score = pd.concat([df_score.loc[:],newrows]).reset_index(drop=True)
            k += 1      

        nlp.to_disk('model')
        # rename df_score column "token_acc" to "iteration"
        df_score.rename(columns={'token_acc': 'iteration'}, inplace=True)    
        df_score.to_excel("model/score.xlsx", index=False)
        
        print("Model trained and saved in model/multiclass and and scores are located in model/score.xlsx")

def EvaluateSpacy(nlp, test_data):
    test = []
    for texts, annotation in test_data:
        doc = nlp.make_doc(texts)
        test.append(Example.from_dict(doc, annotation))    
    score = nlp.evaluate(test)
    return score

def predict(text):
    nlp = spacy.load('model')
    doc = nlp(text)
    return nlp,doc.cats


def main():

    # # load the original dataset
    df = load_csv('data/training.csv')

    # shuffle the dataset rows
    df = df.sample(frac=1).reset_index(drop=True)
    
    # Define the labels
    labels=["1","2","3","4","5","6"]

    train_data, val_data = create_spacy_dataset(df, labels, split_ratio=0.8)

    nlp = define_nlp_pipeline(labels)

    TrainSpacy(nlp, train_data, val_data, 4)

    predict_text = "wiki tell me about john lennon"
    cats = predict(predict_text)
    
    # cats is tuple[Language, Dict[str, float]]. I want to format the output to be a bit more readable.

    print(cats)

main()

