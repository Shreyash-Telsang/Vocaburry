from flask import Flask, request, jsonify

import os
import shutil
import listener2
from collections import Counter
import recommender

app = Flask(__name__)

UPLOAD_FOLDER = os.path.join('C:', os.sep, 'Users', '91987', 'Desktop', 'EDI 3rd SEM', 'python', 'uploads')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

global count
count = 1
global word
global message
word_dict = Counter()

choice = 0
isList = True

global occr
occr = 0

message = ""

def increment_count():
    global count
    count += 1

def delete(folder):
    for filename in os.listdir(folder):
        file_path = os.path.join(folder, filename)
        try:
            if os.path.isfile(file_path) or os.path.islink(file_path):
                os.unlink(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)
        except Exception as e:
            print(f'Failed to delete {file_path}. Reason: {e}')

@app.route('/receive_message', methods=['POST'])
def receive_message():
    print("receive Message Started")
    data = request.json
    global message
    global occr
    global word
    global isList
    message = data.get('message')
    if message == "rec23":
        isList = False
    if message != "done11" and message != "rec23" and message != "done22":
        word = message
        
    print(message)
    if message == "done11":
        wavFiles = "C:\\Users\\91987\\Desktop\\EDI 3rd SEM\\python\\wavFiles"
        uploads = "C:\\Users\\91987\\Desktop\\EDI 3rd SEM\\python\\uploads"
        delete(wavFiles)
        delete(uploads)
        return jsonify({'response': str(occr)})
        
    if message == "done22":
        most_word, synons = recommender.recommend(word_dict)
        return jsonify({"word": most_word, "synonyms": synons})
    return jsonify({'response': 'Message received successfully'})

@app.route('/upload_file_listener', methods=['POST'])
def upload_file_listener():
    print("listener started")
    file = request.files['uploadedfile']
    global count
    global occr
    global word_dict
    global isList
    if file.filename == '':
        return "No selected file"

    if file:
        if not os.path.exists(app.config['UPLOAD_FOLDER']):
            os.makedirs(app.config['UPLOAD_FOLDER'])
        
        filename = "trial_" + str(count) + ".aac"
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        if isList:
            listener2.aacToWav(count)
            global word
            occr += listener2.countOccurrences(word, count)
            count += 1    
            return "YOOOOOOOOOOOOOOOOY"
        if not isList:
            recommender.aacToWav(count)
            word_dict = recommender.wordCount(word_dict, count)
            count += 1
            return "YOOOOOOOOOOOOOOOOY"

@app.route('/receive_message_recommender', methods=['POST'])
def receive_message_recommender():
    data = request.json
    global message
    global word_dict
    message = data.get('message')
    print(message)
    if message == "done22":
        most_word, synons = recommender.recommend(word_dict)
        return jsonify({"word": most_word, "synonyms": synons})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
