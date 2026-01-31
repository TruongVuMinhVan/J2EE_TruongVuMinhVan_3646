package Nhom6.TruongVuMinhVan_3646.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Chuyển hướng trang chủ về danh sách sách
        return "home/index";
    }
}

