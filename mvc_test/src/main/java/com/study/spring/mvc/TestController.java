package com.study.spring.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public ModelAndView test() {
        log.info("test");
        return null;
    }

    @GetMapping("/test2")
    public ModelAndView test(@Token String token) {
        log.info("test2:{}",token);
        return null;
    }
}
