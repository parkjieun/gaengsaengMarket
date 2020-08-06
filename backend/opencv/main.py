import numpy as np
import cv2
import cvlib as cv
from cvlib.object_detection import draw_bbox

detectionName = {
    'bicycle': '자전거',
    'car': '차',
    'motorcycle': '오토바이',
    'airplane': '비행기',
    'bus': '버스',
    'train': '열차',
    'truck': '트럭',
    'boat': '보트',
    'trafficlight': '신호등 ',
    'bench': ' 벤치',
    'backpack': ' 백팩',
    'umbrella': ' 우산',
    'handbag': ' 핸드백',
    'tie': '넥타이 ',
    'suitcase': '수트케이스 ',
    'frisbee': '프리스비(원반) ',
    'skis': '스키 ',
    'snowboard': '스노우보드 ',
    'sportsball': '스포츠볼 ',
    'kite': '연 ',
    'baseballbat': '야구 배트',
    'baseballglov': '야구 글러브 ',
    'skateboard': '스케이트보드 ',
    'surfboard': '서핑보드 ',
    'tennisracket': '테니스 라켓 ',
    'bottle': '병 ',
    'wineglass': ' 와인잔',
    'cup': '컵 ',
    'fork': '포크 ',
    'knife': '나이크 ',
    'spoon': '수저 ',
    'bowl': '그릇(bowl) ',
    'banana': '바나나 ',
    'apple': '사과 ',
    'sandwich': '샌드위치 ',
    'orange': '오렌지 ',
    'broccoli': '브로콜리 ',
    'carrot': '당근 ',
    'hotdog': '핫도그 ',
    'pizza': '피자 ',
    'donut': '도넛 ',
    'cake': '케이크 ',
    'chair': '의자 ',
    'couch': '카우치  / 쇼파 ',
    'pottedplant': '화분 ',
    'bed': '침대',
    'diningtable': '식탁 ',
    'tv': '티비 ',
    'laptop': '노트북 ',
    'mouse': '마우스 ',
    'remote': '리모콘 ',
    'keyboard': '키보드 ',
    'cellphone': '핸드폰 ',
    'microwave': '전자렌지 ',
    'oven': '오븐 ',
    'toaster': '토스터기 ',
    'sink': '싱크대 ',
    'refrigerator': '냉장고 ',
    'book': '책 ',
    'clock': '시계 ',
    'vase': '꽃병 ',
    'scissors': '가위 ',
    'teddybear': '곰인형 ',
    'hairdrier': '헤어드라이기 ',
    'toothbrush': '칫솔 '
}
# Load the Image
image_path = 'person.jpg'
src = cv2.imread(image_path)  # 이미지 읽기

# object detection (물체 검출)
bbox, label, conf = cv.detect_common_objects(src)

print(bbox, label, conf)
resultTags=[]

#전달할 결과 이름, 태그들...
if label[0] in detectionName:
    print("검출한 대상 : ", detectionName[label[0]])
    resultTags.append(label[0])
im = draw_bbox(src, bbox, label, conf)
# dst는 src에서 디텍트한 이미지를 자른것
dst = src.copy()
imgo = src[bbox[0][1]:bbox[0][3], bbox[0][0]:bbox[0][2]]
#cv2.imshow('im', im)
height, width = imgo.shape[:2]

# Create a mask holder
mask = np.zeros(imgo.shape[:2], np.uint8)

# Grab Cut the object
bgdModel = np.zeros((1, 65), np.float64)
fgdModel = np.zeros((1, 65), np.float64)

# Hard Coding the Rect The object must lie within this rect.
rect = (10, 10, width - 13, height - 13)
# cv2.imshow('1111111', dst[10:width-30, 10:height-30] )
cv2.grabCut(imgo, mask, rect, bgdModel, fgdModel, 5, cv2.GC_INIT_WITH_RECT)
mask = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
img1 = imgo * mask[:, :, np.newaxis]

# Get the background
background = imgo - img1

# Change all pixels in the background that are not black to white
background[np.where((background > [0, 0, 0]).all(axis=2))] = [255, 255, 255]

# Add the background and the image
final = background + img1

# To be done - Smoothening the edges
height1, width1, channel = final.shape
b = g = r = count = 0
for y in range(0, height1):
    for x in range(0, width1):
        imgB = final.item(y, x, 0)
        imgG = final.item(y, x, 1)
        imgR = final.item(y, x, 2)

        if imgB < 255 and imgG < 255 and imgR < 255:
            b += imgB
            g += imgG
            r += imgR

            count += 1

print("검출한 b, g, r : ", (int)(b / count), (int)(g / count), (int)(r / count))
cv2.imshow('image', final)

k = cv2.waitKey(0)

if k == 27:
    cv2.destroyAllWindows()
