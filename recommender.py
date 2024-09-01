import spacy
import os
from collections import Counter
from nltk.corpus import wordnet
import string
from pydub import AudioSegment
import whisper


def aacToWav(count):
    curdir = os.getcwd()
    input_file = os.path.join(curdir,'uploads','trial_'+ str(count)+'.aac')
    output_file = os.path.join(curdir,'wavFiles','trial_'+str(count)+'.wav')

# print(curdir)
  
# convert mp3 file to wav file 
    sound = AudioSegment.from_file(input_file,format="aac") 
    sound.export(output_file, format="wav")

def remove_punctuation(text):
    # Using string.punctuation to get all punctuation characters
    translator = str.maketrans('', '', string.punctuation)
    
    # Removing punctuation using translate method
    text_without_punctuation = text.translate(translator)
    text_without_punctuation = text_without_punctuation.lower()
    
    return text_without_punctuation


def wordCount(word_dict,count):
    model = whisper.load_model("small.en")
    path = os.path.join('C:',os.sep, 'Users', '91987', 'Desktop', 'EDI 3rd SEM','python','wavFiles','trial_'+str(count)+'.wav')
    result = model.transcribe(path)
    text = result["text"]
    text = remove_punctuation(text)
    print(text)
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(text)
    filtered_words = [token.text for token in doc if not token.is_stop]
    word_dict.update(filtered_words)  
    return word_dict  


def get_synonyms(word):
    synonyms = []
    for syn in wordnet.synsets(word):
        for lemma in syn.lemmas():
            if word != lemma.name() and len(synonyms) < 4 :
                synonyms.append(lemma.name())
    return list(set(synonyms))

def recommend(word_dict):
    print(word_dict)
    most_common_word, most_common_count = word_dict.most_common(1)[0]
    synonyms = get_synonyms(most_common_word)
    synonyms = ', '.join(synonyms)
    return most_common_word,synonyms