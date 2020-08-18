import numpy as np
import cv2
#import cvlib as cv
import colorName as color
#from cvlib.object_detection import draw_bbox

detectionName = {
    'bicycle': '자전거',
    'car': '차',
    'motorcycle': '오토바이',
    'airplane': '비행기',
    'bus': '버스',
    'train': '열차',
    'truck': '트럭',
    'boat': '보트',
    'trafficlight': '신호등',
    'bench': '벤치',
    'backpack': '백팩',
    'umbrella': '우산',
    'handbag': '핸드백',
    'tie': '넥타이',
    'suitcase': '수트케이스',
    'frisbee': '프리스비(원반)',
    'skis': '스키',
    'snowboard': '스노우보드',
    'sportsball': '스포츠볼',
    'kite': '연',
    'baseballbat': '야구 배트',
    'baseballglov': '야구 글러브',
    'skateboard': '스케이트보드',
    'surfboard': '서핑보드',
    'tennisracket': '테니스 라켓',
    'bottle': '병',
    'wineglass': '와인잔',
    'cup': '컵',
    'fork': '포크',
    'knife': '나이프',
    'spoon': '수저',
    'bowl': '그릇(bowl)',
    'banana': '바나나',
    'apple': '사과',
    'sandwich': '샌드위치',
    'orange': '오렌지',
    'broccoli': '브로콜리',
    'carrot': '당근',
    'hotdog': '핫도그',
    'pizza': '피자',
    'donut': '도넛',
    'cake': '케이크',
    'chair': '의자',
    'couch': '카우치  / 쇼파',
    'pottedplant': '화분',
    'bed': '침대',
    'diningtable': '식탁 ',
    'tv': '티비',
    'laptop': '노트북',
    'mouse': '마우스',
    'remote': '리모콘',
    'keyboard': '키보드',
    'cellphone': '핸드폰',
    'microwave': '전자렌지',
    'oven': '오븐',
    'toaster': '토스터기',
    'sink': '싱크대',
    'refrigerator': '냉장고',
    'book': '책',
    'clock': '시계',
    'vase': '꽃병',
    'scissors': '가위',
    'teddybear': '곰인형',
    'hairdrier': '헤어드라이기',
    'toothbrush': '칫솔'
}

# 색 추출하는 함수
def detect_color(image):
    height1, width1, channel = image.shape
    b = g = r = count = 0
    for y in range(0, height1):
        for x in range(0, width1):
            imgB = image.item(y, x, 0)
            imgG = image.item(y, x, 1)
            imgR = image.item(y, x, 2)

            if imgB < 255 and imgG < 255 and imgR < 255:
                b += imgB
                g += imgG
                r += imgR

                count += 1

    # print("검출된 색상 이름 : ", color.whatIsColorName((int)(r / count), (int)(g / count), (int)(b / count)))
    return color.whatIsColorName(int(r / count), int(g / count), int(b / count))

# 결과 태그 추출하는 함수
def extract_tags(image_path):
    print(image_path.strip())
    # Load the Image
    #image_path = 'http://i3a504.p.ssafy.io/static/image/post/971b97bb25161bf402493e975f3ab378.PNG'
    src = cv2.imread(image_path.strip())  # 이미지 읽기

    #yolo로 물체검출

    net = cv2.dnn.readNet("./yolo/yolov3.weights", "./yolo/yolov3.cfg")
    classes = []
    with open("./yolo/coco.names", "r") as f:
        classes = [line.strip() for line in f.readlines()]
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    #colors = np.random.uniform(0, 255, size=(len(classes), 3))

    height1, width1, channels = src.shape

    blob = cv2.dnn.blobFromImage(src, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)

    label = []
    conf = []
    bbox = []
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.5:
                # Object detected

                center_x = int(detection[0] * width1)
                center_y = int(detection[1] * height1)
                w = int(detection[2] * width1)
                h = int(detection[3] * height1)
                # 좌표
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)
                bbox.append([abs(x), abs(y), abs(w+x), abs(h+y)]) #기존에서는 w, h에 x,y값 안붙여도 되던데 여기는 왜이러는지.. 아마 기본 검출 메소드는 리사이즈를 자동으로해주나보다
                conf.append(float(confidence))
                label.append(classes[class_id])

    # object detection (물체 검출) //gpu 이용한 간단했던 기본코드
    #bbox, label, conf = cv.detect_common_objects(src)
    print("물체 검출 결과 : ", bbox, label, conf)

    # 최종적으로 보낼 태그들
    resultTags = []

    bigN = bigArea = 0
    # 목록에 있는 물건이 검출되지 않음
    if len(label) == 0:
        resultTags.append(detect_color(src))
        print("최종적으로 서버에 보내질 태그들 : ", resultTags)
        return resultTags
    else:
        if len(label) == 1:  # 목록에 있는 물건이 한개 이상 검출됨
            bigN = 0
        elif len(label) > 1:  # 목록에 있는 물건이 한개 이상 검출됨
            for i in range(len(bbox)):
                print("bbox[i] : ", bbox[i], i)
                area = (bbox[i][2] - bbox[i][0]) * (bbox[i][3] - bbox[i][1])
                if (bigArea <= area):
                    bigArea = area
                    bigN = i

        print("bigN : ",bigN)
        #print("src : ", src)
        imgo = src[bbox[bigN][1]:bbox[bigN][3], bbox[bigN][0]:bbox[bigN][2]]
        height, width = imgo.shape[:2]

        # Create a mask holder
        mask = np.zeros(imgo.shape[:2], np.uint8)

        # Grab Cut the object
        bgdModel = np.zeros((1, 65), np.float64)
        fgdModel = np.zeros((1, 65), np.float64)

        # rect Hard Coding 배경 더 섬세하게 쳐내기 위해서.
        rect = (10, 10, width - 13, height - 13)
        cv2.grabCut(imgo, mask, rect, bgdModel, fgdModel, 5, cv2.GC_INIT_WITH_RECT)
        mask = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
        img1 = imgo * mask[:, :, np.newaxis]

        # Get the background
        background = imgo - img1

        # Change all pixels in the background that are not black to white
        background[np.where((background > [0, 0, 0]).all(axis=2))] = [255, 255, 255]

        # Add the background and the image
        final = background + img1

        resultTags.append(detect_color(final))
        # To be done - Smoothening the edges
        if label[bigN] in detectionName:
            resultTags.append(detectionName[label[bigN]])

        #print("검출된것이 0 이상일때 최종적으로 서버에 보내질 태그들 : ", resultTags)

        return resultTags