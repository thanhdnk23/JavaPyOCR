package com.web.app.ocrweb.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.app.ocrweb.payload.OcrResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRImageProcessor_v1 {

    public static List<OcrResult> runOCRContainer(File imageFile) {
        List<OcrResult> ocrResults = new ArrayList<>();

        try {
            // Lệnh gọi Python để chạy appv2.py với đường dẫn file ảnh
            String pythonScript = "/app/appv3.py";
            String pythonInterpreter = "python3"; // Sử dụng Python 3
            String[] command = { pythonInterpreter, pythonScript,"--img \""+ imageFile.getAbsolutePath()+"\"" };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(false);
            Process process = processBuilder.start();

            // Đọc output từ appv2.py
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Đọc luồng lỗi từ appv2.py nếu có lỗi
            StringBuilder errorOutput = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("[ERROR] Python script kết thúc với mã thoát: " + exitCode);
                if (errorOutput.length() > 0) {
                    System.err.println("[ERROR OUTPUT] " + errorOutput.toString());
                }
                return ocrResults; // Trả về danh sách rỗng nếu có lỗi
            }

            // Giả định output từ appv2.py là JSON và phân tích nó
            String regex = "\\{.*?\\}";  // Regex để tìm đối tượng JSON
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(output.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            // Lọc ra tất cả các đối tượng JSON hợp lệ và chuyển chúng thành đối tượng Java
            while (matcher.find()) {
                String jsonString = matcher.group();
                try {
                    // Chuyển chuỗi JSON thành đối tượng OcrResult
                    OcrResult ocrResult = objectMapper.readValue(jsonString, OcrResult.class);
                    ocrResults.add(ocrResult);
                } catch (IOException e) {
                    System.err.println("[ERROR] Error parsing JSON: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Lỗi khi chạy appv2.py từ Java: " + e.getMessage());
            e.printStackTrace();
        }

        return ocrResults;
    }
}
