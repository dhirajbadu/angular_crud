#https://pypi.org/project/python-resize-image/
#pip install python-resize-image
__author__ = 'dbadu'

import os,sys
from PIL import Image
from resizeimage import resizeimage
import argparse

parser = argparse.ArgumentParser(description='Image Utls')
parser.add_argument('--inputpath', '-ip', help='provide the input image path', required=True)
parser.add_argument('--outputpath', '-op', help='provide the output image path', required=True)
parser.add_argument('--height' , type=int , help='provide height', required=True)
parser.add_argument('--width', '-w', type=int , help='provide width', required=True)
parser.add_argument('--operation', '-o', help='provide operation', required=True)

args = parser.parse_args()
inputPath = args.inputpath
outPutPath = args.outputpath
height = args.height
width = args.width
operation = args.operation

def getDirFromPath(path):
    return path.rsplit('/', 1)[0]

def createFolder(path):
    if not os.path.exists(path):
        os.makedirs(path)

def image_resize(inputpath , outputPath , height , width):
    with open(inputpath, 'rb') as fd_img:
        img = Image.open(fd_img)
        img = resizeimage.resize_height(img, height)
        img = resizeimage.resize_width(img, width)
        img.save(outputPath, img.format)
        fd_img.close()

def image_crop(inputpath , outputPath , height , width):
    with open(inputpath, 'rb') as fd_img:
        img = Image.open(fd_img)
        img = resizeimage.resize_crop(img, [width, height])
        img.save(outPutPath, img.format)
        fd_img.close()

def main():
    if  os.path.exists(inputPath):
        createFolder(getDirFromPath(outPutPath))
        if operation == "resize":
            image_resize(inputPath , outPutPath , height , width)
            print ("200")
        elif operation == "crop":
            image_crop(inputPath , outPutPath , height , width)
            print ("200")


if __name__ == "__main__":
    main()