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

def init_models(model_type, use_beamsearch, use_gpu, det_db_box_thresh, rec_model_dir):
    # Sử dụng cấu hình nhẹ hoặc tùy chọn mô hình khác từ đầu vào
    config = Cfg.load_config_from_name(model_type)
    config['cnn']['pretrained'] = True
    config['predictor']['beamsearch'] = use_beamsearch  # Cấu hình sử dụng beamsearch từ tham số
    config['device'] = 'cuda' if use_gpu and torch.cuda.is_available() else 'cpu'

    recognitor = Predictor(config)

    # Cấu hình PaddleOCR với tốc độ tối ưu hơn từ tham số
    detector = PaddleOCR(
        use_angle_cls=False,
        lang="vi",
        use_gpu=use_gpu,
        det_db_box_thresh=det_db_box_thresh,
        rec_model_dir=rec_model_dir
    )
    return recognitor, detector

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
        })

    return results

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--img', required=True, help='Path to the input image')
    parser.add_argument('--model_type', default='vgg_seq2seq', help='Type of VietOCR model to use')
    parser.add_argument('--use_beamsearch', action='store_true', help='Enable beamsearch for better accuracy')
    parser.add_argument('--use_gpu', action='store_true', help='Use GPU if available')
    parser.add_argument('--det_db_box_thresh', type=float, default=0.5, help='Detection box threshold for PaddleOCR')
    parser.add_argument('--rec_model_dir', default='./paddle_rec/', help='Directory for PaddleOCR recognition model')
    args = parser.parse_args()

    img_path = os.path.normpath(args.img)
    if not os.path.exists(img_path):
        print(f"Image file {img_path} does not exist.")
        return

    img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)
    img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))

    recognitor, detector = init_models(
        model_type=args.model_type,
        use_beamsearch=args.use_beamsearch,
        use_gpu=args.use_gpu,
        det_db_box_thresh=args.det_db_box_thresh,
        rec_model_dir=args.rec_model_dir
    )

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
