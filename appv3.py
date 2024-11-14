import os
import cv2
import json
import argparse
import time
from PIL import Image
from vietocr.vietocr.tool.predictor import Predictor
from vietocr.vietocr.tool.config import Cfg
from PaddleOCR import PaddleOCR
import torch

def init_models():
    # Sử dụng cấu hình nhẹ hơn nếu có thể
    config = Cfg.load_config_from_name('vgg_seq2seq')  # Mô hình nhẹ hơn so với 'vgg_transformer'
    config['cnn']['pretrained'] = True
    config['predictor']['beamsearch'] = False  # Không sử dụng beamsearch để tăng tốc
    config['device'] = 'cpu'

    recognitor = Predictor(config)

    # Cấu hình PaddleOCR với tốc độ tối ưu hơn
    detector = PaddleOCR(use_angle_cls=False, lang="vi", use_gpu=torch.cuda.is_available(), det_db_box_thresh=0.5, rec_model_dir='./paddle_rec/')  
    return recognitor, detector

recognitor, detector = init_models()

def predict(recognitor, detector, img, padding=4):
    # Nhận dạng văn bản
    result = detector.ocr(img, cls=False, det=True, rec=False)
    result = result[:][:][0]

    boxes = [[
        [max(0, int(line[0][0]) - padding), max(0, int(line[0][1]) - padding)],
        [min(img.shape[1], int(line[2][0]) + padding), min(img.shape[0], int(line[2][1]) + padding)]
    ] for line in result]

    results = []
    for box in boxes:
        cropped_image = img[box[0][1]:box[1][1], box[0][0]:box[1][0]]
        cropped_image = Image.fromarray(cropped_image)
        text = recognitor.predict(cropped_image)

        results.append({
            "box": box,
            "text": text,
            "score":0
        })

    return results

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--img', required=True, help='Path to the input image')
    args = parser.parse_args()

    img_path = os.path.normpath(args.img)
    if not os.path.exists(img_path):
        print(f"Image file {img_path} does not exist.")
        return

    img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)  # Đọc ảnh ở dạng thang xám để tối ưu
    img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))  # Giảm kích thước ảnh để xử lý nhanh hơn

    start_time = time.time()
    results = predict(recognitor, detector, img, padding=2)
    processing_time = time.time() - start_time

    output = {
        "processing_time": processing_time,
        "results": results
    }
    print(json.dumps(output, ensure_ascii=False, indent=4))

if __name__ == "__main__":
    main()
