package com.web.app.ocrweb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DockerService {

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // Check if Docker image exists
    private boolean dockerImageExists(String imageName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "images", "-q", imageName);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.readLine() != null; // If there's output, image exists
            }
        } catch (IOException e) {
            return false; // Assume image doesn't exist on error
        }
    }

    // Pull Docker Image if not exists
    public ResponseEntity<String> pullDockerImage(String imageName) {
        StringBuilder logBuilder = new StringBuilder();
        if (dockerImageExists(imageName)) {
            logBuilder.append(getCurrentTimestamp()).append(" - Docker image ").append(imageName)
                    .append(" already exists.\n");
            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.OK);
        }
        try {
            logBuilder.append(getCurrentTimestamp()).append(" - Pulling Docker image ").append(imageName)
                    .append("...\n");
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "pull", imageName);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logBuilder.append(getCurrentTimestamp()).append(" - ").append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logBuilder.append(getCurrentTimestamp()).append(" - Docker image ").append(imageName)
                        .append(" pulled successfully.\n");
                return new ResponseEntity<>(logBuilder.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(logBuilder.toString() + "\nError occurred while pulling Docker image.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logBuilder.append(getCurrentTimestamp()).append(" - Error: ").append(e.getMessage()).append("\n");
            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Run Docker container
    public ResponseEntity<String> runDockerContainer(String imageName, String containerName, String imagePath) {
        StringBuilder logBuilder = new StringBuilder();
        try {
            String uniqueFileName = UUID.randomUUID().toString() + ".jpg";
            String containerFilePath = "/app/" + uniqueFileName;

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "run", "--name", containerName, "-v", imagePath + ":" + containerFilePath,
                    imageName, "python", "appv2.py", containerFilePath, "--lang", "vi");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logBuilder.append(getCurrentTimestamp()).append(" - ").append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logBuilder.append(getCurrentTimestamp()).append(" - Docker container ").append(containerName)
                        .append(" ran successfully.\n");
                return new ResponseEntity<>(logBuilder.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(logBuilder.toString() + "\nError occurred while running Docker container.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logBuilder.append(getCurrentTimestamp()).append(" - Error: ").append(e.getMessage()).append("\n");
            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // List running Docker containers
    public ResponseEntity<String> listDockerContainers() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "ps", "--format", "{{.Names}}");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logBuilder.append(getCurrentTimestamp()).append(" - Running container: ").append(line).append("\n");
                }
            }

            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.OK);
        } catch (IOException e) {
            logBuilder.append(getCurrentTimestamp()).append(" - Error listing Docker containers: ")
                    .append(e.getMessage()).append("\n");
            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove Docker container by name
    public ResponseEntity<String> removeDockerContainer(String containerName) {
        StringBuilder logBuilder = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "rm", "-f", containerName);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logBuilder.append(getCurrentTimestamp()).append(" - Container ").append(containerName)
                        .append(" removed successfully.\n");
                return new ResponseEntity<>(logBuilder.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(logBuilder.toString() + "\nError occurred while removing Docker container.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logBuilder.append(getCurrentTimestamp()).append(" - Error: ").append(e.getMessage()).append("\n");
            return new ResponseEntity<>(logBuilder.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get logs from Docker container
    public ResponseEntity<String> getContainerLogs(String containerName) {
        StringBuilder logs = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "logs", containerName);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.append(getCurrentTimestamp()).append(" - ").append(line).append("\n");
                }
            }

            return new ResponseEntity<>(logs.toString(), HttpStatus.OK);
        } catch (IOException e) {
            logs.append(getCurrentTimestamp()).append(" - Error retrieving logs for container ").append(containerName)
                    .append(": ").append(e.getMessage()).append("\n");
            return new ResponseEntity<>(logs.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
