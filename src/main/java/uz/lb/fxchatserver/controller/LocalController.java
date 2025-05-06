package uz.lb.fxchatserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/local")
public class LocalController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/simple")
    public String simpleTest(@RequestHeader(name = "Accept-Language", required = false) String lang) {
        try {
            return messageSource.getMessage("user.not.found", null, new Locale(lang));
        } catch (Exception e) {
            log.error("error -> {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}