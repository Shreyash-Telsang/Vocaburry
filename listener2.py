import os
from pydub import AudioSegment 
import whisper 
import spacy




def aacToWav(count):
    curdir = os.getcwd()
    input_file = os.path.join(curdir,'uploads','trial_'+ str(count)+'.aac')
    output_file = os.path.join(curdir,'wavFiles','trial_'+str(count)+'.wav')

# print(curdir)
  
# convert mp3 file to wav file 
    sound = AudioSegment.from_file(input_file,format="aac") 
    sound.export(output_file, format="wav")


def countOccurrences(word,count):
    model = whisper.load_model("small.en")
    path = os.path.join('C:',os.sep, 'Users', '91987', 'Desktop', 'EDI 3rd SEM','python','wavFiles','trial_'+str(count)+'.wav')
    result = model.transcribe(path)
    text = result["text"]
    print(text)
    nlp = spacy.load("en_core_web_sm")
    doc = nlp(text)
    # print(doc)
    word1 = nlp(word)
    # print(word1[0])
    counting =0
    for token in doc:
        token = token.lemma_
        if token==word1[0].lemma_:
            counting = counting + 1
    return counting