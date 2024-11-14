package com.web.app.ocrweb.plugins;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageCompressor {

    /**
     * Nén và lưu ảnh dưới dạng file mới với chất lượng tùy chỉnh (giảm dung lượng).
     * @param inputImageFile File ảnh gốc.
     * @param outputImageFile File ảnh đầu ra sau khi nén.
     * @param quality Mức chất lượng từ 0.0 đến 1.0 (1.0 là chất lượng cao nhất).
     * @throws IOException
     */
    public static void compressImage(File inputImageFile, File outputImageFile, float quality) throws IOException {
        // Đọc ảnh gốc vào BufferedImage
        BufferedImage image = ImageIO.read(inputImageFile);

        // Tạo ImageWriter cho định dạng JPEG
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // Thiết lập mức nén, chất lượng thấp hơn sẽ giảm dung lượng hơn
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);  // Quality: 0.0 (low) to 1.0 (high)

        // Tạo đầu ra file và ghi ảnh với nén
        try (FileOutputStream fos = new FileOutputStream(outputImageFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    /**
     * Nén ảnh và trả về mảng byte của ảnh đã nén.
     * @param image BufferedImage cần nén.
     * @param quality Mức chất lượng từ 0.0 đến 1.0 (1.0 là chất lượng cao nhất).
     * @return Mảng byte của ảnh đã nén.
     * @throws IOException
     */
    public static byte[] compressImageToByteArray(BufferedImage image, float quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Tạo ImageWriter cho định dạng JPEG
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        // Thiết lập mức nén
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }

        return baos.toByteArray();
    }

    public static void main(String[] args) {
        try {
            // Đọc file ảnh gốc
            File inputImageFile = new File("path/to/your/image.png");
            File outputImageFile = new File("path/to/output/compressed_image.jpg");

            // Nén và lưu ảnh với chất lượng 0.8 (80%)
            compressImage(inputImageFile, outputImageFile, 0.8f);
            System.out.println("Ảnh đã được nén và lưu tại: " + outputImageFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
