import pandas as pd
import numpy as np
import os
import cv2
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
from matplotlib.pyplot import figure
from PIL import Image
import difflib
import re
import math
import json
import sys
import argparse

import torch
import Levenshtein

from vietocr.vietocr.tool.predictor import Predictor
from vietocr.vietocr.tool.config import Cfg

from PaddleOCR import PaddleOCR, draw_ocr

import time

# Specifying output path and font path.
FONT = './PaddleOCR/doc/fonts/latin.ttf'

def calculate_score(predicted_text, ground_truth_text):
    return 1 - (Levenshtein.distance(predicted_text, ground_truth_text) / max(len(predicted_text), len(ground_truth_text)))

def predict(recognitor, detector, img_path, padding=4):
    # Load image
    img = cv2.imread(img_path)

    # Text detection
    result = detector.ocr(img_path, cls=False, det=True, rec=False)
    result = result[:][:][0]

    # Filter Boxes
    boxes = []
    for line in result:
        boxes.append([[int(line[0][0]), int(line[0][1])], [int(line[2][0]), int(line[2][1])]])
    boxes = boxes[::-1]

    # Add padding to boxes
    padding = 4
    for box in boxes:
        box[0][0] = box[0][0] - padding
        box[0][1] = box[0][1] - padding
        box[1][0] = box[1][0] + padding
        box[1][1] = box[1][1] + padding

    # Text recognition
    results = []
    for box in boxes:
        cropped_image = img[box[0][1]:box[1][1], box[0][0]:box[1][0]]
        try:
            cropped_image = Image.fromarray(cropped_image)
        except:
            continue

        rec_result = recognitor.predict(cropped_image)
        text = rec_result

        result_dict = {
            "box": box,
            "text": text,
            "score": 0.0  # Placeholder for score, update with actual score if available
        }
        results.append(result_dict)
        print(result_dict)

    return results

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--img', required=True, help='Path to the input image')
    args = parser.parse_args()

    # Configure VietOCR
    config = Cfg.load_config_from_name('vgg_transformer')
    config['cnn']['pretrained'] = True
    config['predictor']['beamsearch'] = True
    config['device'] = 'cpu'  # Change to 'cuda' if using GPU

    recognitor = Predictor(config)

    # Configure PaddleOCR
    detector = PaddleOCR(use_angle_cls=False, lang="vi", use_gpu=False)

    # Predict
    results = predict(recognitor, detector, args.img, padding=2)

    # Print results
    print(json.dumps(results, ensure_ascii=False, indent=4))

if __name__ == "__main__":
    main()
