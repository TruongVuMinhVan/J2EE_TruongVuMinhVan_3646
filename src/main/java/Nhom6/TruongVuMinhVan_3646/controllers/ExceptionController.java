package Nhom6.TruongVuMinhVan_3646.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
@Slf4j
public class ExceptionController implements ErrorController {
        @GetMapping
        public String handleError(HttpServletRequest request) {
                Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
                if (status != null) {
                        Integer statusCode = Integer.valueOf(status.toString());
                        if (statusCode == 500) {
                                // Log detailed error information
                                Exception exception = (Exception) request
                                                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);
                                if (exception != null) {
                                        log.error("Internal Server Error: ", exception);
                                } else {
                                        log.error("Internal Server Error: check server console for details");
                                }
                        }
                        if (statusCode == 404 || statusCode == 500 || statusCode == 403) {
                                return "error/" + statusCode;
                        }
                }
                return "home/index";
        }
}