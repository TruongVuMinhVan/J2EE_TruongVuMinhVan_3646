package Nhom6.TruongVuMinhVan_3646.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@lombok.extern.slf4j.Slf4j
public class HomeController {

    @GetMapping("/")
    public String home() {
        log.info("Entering home page");
        // Chuyển hướng trang chủ về danh sách sách
        return "home/index";
    }
}
