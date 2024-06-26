package org.epam.gymservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReadmeController {

    @Value("${HTML.indexPage}")
    String index;

    @GetMapping("/")
    public String index() {
        return index;
    }

}
