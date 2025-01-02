package com.sajal.another;

import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;
import com.sajal.atom.annotations.web.PostMapping;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, GET!";
    }

    @PostMapping("/hello")
    public String helloPost() {
        return "Hello, POST!";
    }
}