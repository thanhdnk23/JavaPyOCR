package com.web.app.ocrweb.plugins;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class ImageConverter {
    /**
     * Chuyển đổi MultipartFile thành đối tượng BufferedImage.
     * 
     * @param multipartFile Tệp hình ảnh dưới dạng MultipartFile.
     * @return Đối tượng BufferedImage.
     * @throws IOException Nếu có lỗi khi đọc tệp hình ảnh.
     */
    public static BufferedImage multipartFileToBufferedImage(MultipartFile multipartFile) throws IOException {
        return ImageIO.read(multipartFile.getInputStream());
    }

    /**
     * Chuyển đổi tệp hình ảnh thành đối tượng BufferedImage.
     * 
     * @param file Hình ảnh dưới dạng đối tượng File.
     * @return Đối tượng BufferedImage.
     * @throws IOException Nếu có lỗi khi đọc tệp hình ảnh.
     */
    public static BufferedImage fileToBufferedImage(File file) throws IOException {
        return ImageIO.read(file);
    }

    /**
     * Chuyển đổi file ảnh thành chuỗi base64.
     * 
     * @param imageFile file hình ảnh cần chuyển đổi.
     * @param format    Định dạng của ảnh (ví dụ: "png", "jpg").
     * @return Chuỗi base64 của hình ảnh.
     * @throws IOException
     */
    public static String imageToBase64(File imageFile, String format) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Chuyển đổi chuỗi base64 thành đối tượng BufferedImage.
     * 
     * @param base64Image Chuỗi base64 của hình ảnh.
     * @return Đối tượng BufferedImage.
     * @throws IOException
     */
    public static BufferedImage base64ToBufferedImage(String base64Image) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(inputStream);
    }

    /**
     * Lưu BufferedImage thành file ảnh.
     * 
     * @param bufferedImage Đối tượng BufferedImage cần lưu.
     * @param format        Định dạng của ảnh (ví dụ: "png", "jpg").
     * @param outputFile    File output để lưu ảnh.
     * @throws IOException
     */
    public static void saveBufferedImageToFile(BufferedImage bufferedImage, String format, File outputFile)
            throws IOException {
        ImageIO.write(bufferedImage, format, outputFile);
    }

    /**
     * Chuyển đổi BufferedImage thành chuỗi base64.
     * 
     * @param bufferedImage Đối tượng BufferedImage cần chuyển đổi.
     * @param format        Định dạng của ảnh (ví dụ: "png", "jpg").
     * @return Chuỗi base64 của hình ảnh.
     * @throws IOException
     */
    public static String bufferedImageToBase64(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Chuyển chuỗi base64 thành mảng byte.
     * 
     * @param base64Image Chuỗi base64 của hình ảnh.
     * @return Mảng byte của hình ảnh.
     */
    public static byte[] base64ToByteArray(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }

    /**
     * Chuyển đổi mảng byte thành BufferedImage.
     * 
     * @param imageBytes Mảng byte của hình ảnh.
     * @return Đối tượng BufferedImage.
     * @throws IOException
     */
    public static BufferedImage byteArrayToBufferedImage(byte[] imageBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(inputStream);
    }

    /**
     * Chuyển đổi BufferedImage thành mảng byte.
     * 
     * @param bufferedImage Đối tượng BufferedImage cần chuyển đổi.
     * @param format        Định dạng của ảnh (ví dụ: "png", "jpg").
     * @return Mảng byte của hình ảnh.
     * @throws IOException
     */
    public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, outputStream);
        return outputStream.toByteArray();
    }

    public static void main(String[] args) {
        try {
            // Đọc file ảnh và chuyển đổi thành base64
            File imageFile = new File("path/to/your/image.png");
            String base64Image = imageToBase64(imageFile, "png");
            System.out.println("Base64 Image: " + base64Image);

            // Chuyển base64 thành BufferedImage
            BufferedImage bufferedImage = base64ToBufferedImage(base64Image);

            // Lưu BufferedImage thành file
            File outputFile = new File("path/to/output/image.png");
            saveBufferedImageToFile(bufferedImage, "png", outputFile);

            // Chuyển đổi BufferedImage lại thành base64
            String newBase64Image = bufferedImageToBase64(bufferedImage, "png");
            System.out.println("Converted Base64 Image: " + newBase64Image);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
