from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
from flask_cors import CORS
import extractTags
import title_analyzer
app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
CORS(app)

@app.route('/api/opencv')
def home():
     return render_template('index.html')

@app.route('/api/opencv/distractinfo', methods = ['GET'])
def test():
    title = request.args.get('title')
    distracting = title_analyzer.TitleAnalyzer()
    print("*******************************************")
    print("들어온 문자열", title)
    print("태그들", distracting.get_nouns(title))
    print("카테고리", distracting.get_category(title))
    print("가격", distracting.get_price(title))
    result = []
    result.append(distracting.get_nouns(title))
    result.append(distracting.get_category(title))
    result.append(distracting.get_price(title))
    print("result", result)
    #return {'title': title }
    return {'tags': distracting.get_nouns(title), 'categories': distracting.get_category(title), 'price': distracting.get_price(title)}

@app.route('/api/opencv/imgUpload', methods = ['GET', 'PUT', 'POST'])
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
