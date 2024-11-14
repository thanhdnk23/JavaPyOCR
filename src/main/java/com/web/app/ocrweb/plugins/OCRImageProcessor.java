package com.web.app.ocrweb.plugins;

// import java.awt.Color;
// import java.awt.Graphics2D;
// import java.awt.image.BufferedImage;
// import java.io.File;

// import javax.imageio.ImageIO;

// import org.json.JSONObject; 
// import net.sourceforge.tess4j.ITesseract;
// import net.sourceforge.tess4j.Tesseract;
// import net.sourceforge.tess4j.TesseractException;
// import vn.pipeline.Annotation;
// import vn.pipeline.VnCoreNLP;
// import vn.pipeline.Word;

// public class OCRImageProcessor {
//     private static ITesseract instance;
//     private static VnCoreNLP vnCoreNLP; // Khai báo VnCoreNLP là static để tránh việc load lại nhiều lần

//     static {
//         try {
//             // Khởi tạo VnCoreNLP chỉ một lần duy nhất
//             vnCoreNLP = new VnCoreNLP();
            
//             // Khởi tạo ITesseract chỉ một lần duy nhất
//             instance = new Tesseract();
//             instance.setDatapath("src/main/resources/language");
//             instance.setLanguage("vie");
//             instance.setVariable("user_defined_dpi", "300");

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         OCRImageProcessor ocrProcessor = new OCRImageProcessor();
//         try {
//             File imageFile = new File("/workspaces/OCRWeb/img/Test5.png");
//             BufferedImage image = ImageIO.read(imageFile);
//             BufferedImage processedImage = ocrProcessor.preprocessImage(image);
//             String jsonResult = ocrProcessor.OCR_Form_Image(processedImage);
//             System.out.println(jsonResult);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     // Phương thức tiền xử lý ảnh để cải thiện chất lượng OCR
//     private BufferedImage preprocessImage(BufferedImage image) {
//         BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//         Graphics2D graphics = grayImage.createGraphics();
//         graphics.drawImage(image, 0, 0, null);
//         graphics.dispose();

//         for (int y = 0; y < grayImage.getHeight(); y++) {
//             for (int x = 0; x < grayImage.getWidth(); x++) {
//                 int rgb = grayImage.getRGB(x, y);
//                 Color color = new Color(rgb);
//                 int r = Math.min(255, (int) (color.getRed() * 1.2));
//                 int g = Math.min(255, (int) (color.getGreen() * 1.2));
//                 int b = Math.min(255, (int) (color.getBlue() * 1.2));
//                 grayImage.setRGB(x, y, new Color(r, g, b).getRGB());
//             }
//         }

//         return grayImage;
//     }

//     // Hàm OCR_Form_Image để trả về kết quả dạng JSON
//     public String OCR_Form_Image(BufferedImage image) {
//         JSONObject jsonResponse = new JSONObject();
//         try {
//             // Thực hiện OCR và trả về chuỗi kết quả
//             String result = instance.doOCR(image);
//             jsonResponse.put("raw_text", result);

//             // Phân tích chuỗi văn bản bằng VnCoreNLP
//             String parsedInfo = extractMeaningfulInfo(result);
//             jsonResponse.put("parsed_info", new JSONObject(parsedInfo)); // Chuyển đổi parsedInfo thành JSONObject

//         } catch (TesseractException e) {
//             e.printStackTrace();
//         }
//         return jsonResponse.toString(); // Trả về JSON dưới dạng chuỗi
//     }

//     // Phân tích và trích xuất thông tin bằng VnCoreNLP
//     private String extractMeaningfulInfo(String text) {
//         JSONObject parsedInfo = new JSONObject();

//         try {
//             Annotation annotation = new Annotation(text);
//             vnCoreNLP.annotate(annotation);

//             // Duyệt qua các từ để lấy thông tin quan trọng
//             for (Word word : annotation.getWords()) {
//                 switch (word.getNerLabel()) {
//                     case "PERSON":
//                         parsedInfo.put("Tên", word.getForm());
//                         break;
//                     case "DATE":
//                         parsedInfo.put("Ngày sinh", word.getForm());
//                         break;
//                     case "ID":
//                         parsedInfo.put("Số CMND/ID", word.getForm());
//                         break;
//                     // Thêm các loại thông tin cần thiết khác
//                     default:
//                         break;
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return parsedInfo.toString(); // Trả về JSONObject dưới dạng chuỗi
//     }

//     // Phương thức giải phóng bộ nhớ
//     public static void releaseMemory() {
//         if (vnCoreNLP != null) {
//             vnCoreNLP = null; // Giải phóng bộ nhớ của VnCoreNLP
//         }
//         if (instance != null) {
//             instance = null; // Giải phóng bộ nhớ của ITesseract
//         }
//     }
// }
