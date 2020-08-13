from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
from flask_cors import CORS
import extractTags

app = Flask(__name__)
CORS(app)

@app.route('/')
def home():
     return render_template('index.html')

@app.route('/imgUpload', methods = ['GET', 'PUT', 'POST'])
def upload_file():
   if request.method == 'PUT':
      #저장할 경로 + 파일명
      uploaded_files =request.files.getlist("file")
      resultTags = []
      for uploadedImg in uploaded_files:
          uploadedImg.save('./uploadImgs/' + secure_filename(uploadedImg.filename))
          print ("업로드한 이미지 ㅣ ",  uploadedImg.filename)
          resultTags.append(','.join(extractTags.extract_tags('./uploadImgs/' + secure_filename(uploadedImg.filename))))

      print("진짜 최종 태그들...",resultTags)
      return ','.join(resultTags)

if __name__ == '__main__':
    app.debug = True
    app.run(port=5000, debug = True)
