package com.sajal.another;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;

import java.util.Map;

@Controller(path = "/another")
public class AnotherController {
    @GetMapping("/hello")
    public Map<String,Object> hello() {
        System.out.println("Hello, GET! from AnotherController");
        return Map.of("message", "Hello, GET! from AnotherController");
    }
}
