import numpy as np
import cv2
import cvlib as cv
from cvlib.object_detection import draw_bbox
#Load the Image
image_path = 'REPL-ATTRACT-MORE-MEN-780x520.jpg'
src = cv2.imread(image_path) # 이미지 읽기


# object detection (물체 검출)
bbox, label, conf = cv.detect_common_objects(src)

print(bbox, label, conf)
print("검출한 대상 : ", label[0])
im = draw_bbox(src, bbox, label, conf)
#dst는 src에서 디텍트한 이미지를 자른것
dst = src.copy()
imgo = src[bbox[0][1]:bbox[0][3], bbox[0][0]:bbox[0][2]]

height, width = imgo.shape[:2]

#Create a mask holder
mask = np.zeros(imgo.shape[:2],np.uint8)

#Grab Cut the object
bgdModel = np.zeros((1,65),np.float64)
fgdModel = np.zeros((1,65),np.float64)

#Hard Coding the Rect The object must lie within this rect.
rect = (10,10,width-30,height-30)
cv2.grabCut(imgo,mask,rect,bgdModel,fgdModel,5,cv2.GC_INIT_WITH_RECT)
mask = np.where((mask==2)|(mask==0),0,1).astype('uint8')
img1 = imgo*mask[:,:,np.newaxis]

#Get the background
background = imgo - img1

#Change all pixels in the background that are not black to white
background[np.where((background > [0,0,0]).all(axis = 2))] = [255,255,255]

#Add the background and the image
final = background + img1

#To be done - Smoothening the edges
height1, width1, channel = final.shape
b= g= r = count = 0
for y in range(0, height1):
    for x in range(0, width1):
        imgB = final.item(y, x, 0)
        imgG = final.item(y, x, 1)
        imgR = final.item(y, x, 2)

        if imgB > 50 and imgG > 50 and imgR > 50\
                and imgB < 255 and imgG < 255 and imgR < 255:
            b += imgB
            g += imgG
            r += imgR

            count += 1


print("검출한 b, g, r : ", (int)(b/count), (int)(g/count), (int)(r/count) )
cv2.imshow('image', final )

k = cv2.waitKey(0)

if k==27:
    cv2.destroyAllWindows()