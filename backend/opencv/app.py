from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
from flask_cors import CORS
import extractTags
from title_analyzer import TitleAnalyzer

app = Flask(__name__)
CORS(app)
title_analyzer = TitleAnalyzer()

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

@app.route('/category', methods = ['GET'])
def category_by_title():
    return str(title_analyzer.get_category(request.args.get('title')))
    
@app.route('/hashtag', methods = ['GET'])
def keyword_by_title():
    return str(title_analyzer.get_nouns(request.args.get('title')))

@app.route('/price', methods=['GET'])
def price_by_title():
    return str(title_analyzer.get_price(request.args.get('title')))

if __name__ == '__main__':
    app.debug = True
    app.run(port=5000, debug = True)
