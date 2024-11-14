package com.web.app.ocrweb.plugins;

import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessor {

    /**
     * Giảm kích thước và nén hình ảnh mà vẫn giữ chất lượng đủ để xử lý mã QRCode.
     * @param fileImage: File hình ảnh gốc
     * @param maxWidth: Chiều rộng tối đa ảnh sau khi giảm kích thước
     * @param maxHeight: Chiều cao tối đa ảnh sau khi giảm kích thước
     * @param outputFile: File ảnh đầu ra sau khi nén
     * @throws IOException
     */
    public void resizeAndCompressImage(File fileImage, int maxWidth, int maxHeight, File outputFile) throws IOException {
        // Sử dụng thư viện Thumbnailator để giảm kích thước và nén ảnh
        Thumbnails.of(fileImage)
                .size(maxWidth, maxHeight) // Giảm kích thước ảnh, giữ tỷ lệ gốc
                .outputQuality(0.85) // Chỉ định chất lượng nén, 0.85 tương ứng với 85%
                .toFile(outputFile); // Xuất ra file ảnh đã xử lý
    }

    /**
     * Đọc ảnh từ file và chuyển sang BufferedImage.
     * @param file: File hình ảnh
     * @return BufferedImage
     * @throws IOException
     */
    public BufferedImage readImageFromFile(File file) throws IOException {
        return ImageIO.read(file);
    }
}
