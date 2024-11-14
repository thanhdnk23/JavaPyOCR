package com.web.app.ocrweb.controller.home_ui;
import com.web.app.ocrweb.service.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DockerController {

    @Autowired
    private DockerService dockerService;

    // Pull Docker Image
    @GetMapping("/pullImage")
    public ResponseEntity<String> pullImage() {
        return dockerService.pullDockerImage("4sandbox/devhub-ocr");
    }

    // Liệt kê các container
    @GetMapping("/listContainers")
    public ResponseEntity<String> listContainers() {
        return dockerService.listDockerContainers();
    }

    // Xóa Docker container
    @GetMapping("/removeContainer")
    public ResponseEntity<String> removeContainer(@RequestParam("containerName") String containerName) {
        return dockerService.removeDockerContainer(containerName);
    }

    // Lấy logs container
    @GetMapping("/containerLogs")
    public ResponseEntity<String> getContainerLogs(@RequestParam("containerName") String containerName) {
        return dockerService.getContainerLogs(containerName);
    }
}
