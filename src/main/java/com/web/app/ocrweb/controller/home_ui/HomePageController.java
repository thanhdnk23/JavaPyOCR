package com.web.app.ocrweb.controller.home_ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.web.app.ocrweb.payload.CardType;

@Controller
public class HomePageController {
    @GetMapping("/")
    public String home(Model model) {
        // Trả về tên của file HTML (hoặc template) cho trang chủ
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("showApiDocs", false);
        return "index"; // Tên của file HTML
    }
    @GetMapping("/docker")
    public String docker(Model model) {
        // Trả về tên của file HTML (hoặc template) cho trang chủ
        model.addAttribute("cardTypes", CardType.values());
        return "docker_index.html"; // index.html sẽ được tìm trong thư mục resources/templates
    }
    @GetMapping("/card-form")
    public String showCardForm(Model model) {
        // Thêm enum CardType vào model để Thymeleaf có thể sử dụng
        model.addAttribute("cardTypes", CardType.values());
        return "card-form"; // Đây là tên của view (file .html)
    }
    @GetMapping("/home")
    public String showHome(Model model) {
       model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("showApiDocs", false);
        return "index"; // Tên của file HTML
    }

    @GetMapping("/api-docs")
    public String showApiDocs(Model model) {
        model.addAttribute("showApiDocs", true);
        return "index"; // Tên của file HTML
    }
}
