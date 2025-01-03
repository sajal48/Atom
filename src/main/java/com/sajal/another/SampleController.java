package com.sajal.another;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;

import java.util.Map;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public Map<String,Object> hello() {
        System.out.println("Hello, GET!");
      return Map.of("message", "Hello, GET!");
    }
}