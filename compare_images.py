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
from difflib import SequenceMatcher

def init_models(model_type, use_beamsearch, use_gpu, det_db_box_thresh, rec_model_dir):
    # Cấu hình VietOCR
    config = Cfg.load_config_from_name(model_type)
    config['cnn']['pretrained'] = True
    config['predictor']['beamsearch'] = use_beamsearch
    config['device'] = 'cuda' if use_gpu and torch.cuda.is_available() else 'cpu'
    recognitor = Predictor(config)

    # Cấu hình PaddleOCR
    detector = PaddleOCR(
        use_angle_cls=False,
        lang="vi",
        use_gpu=use_gpu,
        det_db_box_thresh=det_db_box_thresh,
        rec_model_dir=rec_model_dir
    )
    return recognitor, detector

def predict(recognitor, detector, img, padding=4):
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

def compare_texts(texts1, texts2):
    matched = []
    different = []
    
    for text1 in texts1:
        found_match = False
        for text2 in texts2:
            similarity = SequenceMatcher(None, text1, text2).ratio()
            if similarity > 0.8:  # Ngưỡng để xác định là giống nhau
                matched.append((text1, text2))
                found_match = True
                break
        if not found_match:
            different.append(text1)

    return matched, different

def compare_images(recognitor, detector, img1, img2):
    results1 = predict(recognitor, detector, img1)
    results2 = predict(recognitor, detector, img2)

    texts1 = [res["text"] for res in results1]
    texts2 = [res["text"] for res in results2]

    matched, different_img1 = compare_texts(texts1, texts2)
    _, different_img2 = compare_texts(texts2, texts1)

    return {
        "matched_texts": matched,
        "different_texts_img1": different_img1,
        "different_texts_img2": different_img2
    }

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--img1', required=True, help='Path to the first input image')
    parser.add_argument('--img2', required=True, help='Path to the second input image')
    parser.add_argument('--model_type', default='vgg_seq2seq', help='Type of VietOCR model to use')
    parser.add_argument('--use_beamsearch', action='store_true', help='Enable beamsearch for better accuracy')
    parser.add_argument('--use_gpu', action='store_true', help='Use GPU if available')
    parser.add_argument('--det_db_box_thresh', type=float, default=0.5, help='Detection box threshold for PaddleOCR')
    parser.add_argument('--rec_model_dir', default='./paddle_rec/', help='Directory for PaddleOCR recognition model')
    args = parser.parse_args()

    img_path1 = os.path.normpath(args.img1)
    img_path2 = os.path.normpath(args.img2)

    if not os.path.exists(img_path1) or not os.path.exists(img_path2):
        print("One or both image files do not exist.")
        return

    img1 = cv2.imread(img_path1, cv2.IMREAD_GRAYSCALE)
    img2 = cv2.imread(img_path2, cv2.IMREAD_GRAYSCALE)

    recognitor, detector = init_models(
        model_type=args.model_type,
        use_beamsearch=args.use_beamsearch,
        use_gpu=args.use_gpu,
        det_db_box_thresh=args.det_db_box_thresh,
        rec_model_dir=args.rec_model_dir
    )

    start_time = time.time()
    comparison_results = compare_images(recognitor, detector, img1, img2)
    processing_time = time.time() - start_time

    output = {
        "processing_time": processing_time,
        "comparison_results": comparison_results
    }
    print(json.dumps(output, ensure_ascii=False, indent=4))

if __name__ == "__main__":
    main()
