package com.web.app.ocrweb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.app.ocrweb.payload.OcrResult;

public class Test {
    public static void main(String[] args) {
        // Đường dẫn file ảnh cần xử lý
        File imagePath = new File("b.jpg");
        runOCRContainer(imagePath);
    }

    public static List<OcrResult> runOCRContainer(File imageFile) {
        List<OcrResult> ocrResults = new ArrayList<>();

        try {
            // Lệnh gọi Python để chạy appv2.py với đường dẫn file ảnh
            String pythonScript = "/app/appv2.py";
            String pythonInterpreter = "python3"; // Sử dụng Python 3
            String[] command = { pythonInterpreter, pythonScript, imageFile.getAbsolutePath() };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Đọc output từ appv2.py
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // Giả định appv2.py trả về JSON, phân tích JSON thành OcrResult
                ObjectMapper objectMapper = new ObjectMapper();
                OcrResult ocrResult = objectMapper.readValue(output.toString(), OcrResult.class);
                ocrResults.add(ocrResult);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("[ERROR] Python script kết thúc với mã thoát: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Lỗi khi chạy appv2.py từ Java: " + e.getMessage());
            e.printStackTrace();
        }

        return ocrResults;
    }
}
