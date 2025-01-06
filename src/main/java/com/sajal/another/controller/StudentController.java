package com.sajal.another.controller;

import com.sajal.another.domain.Student;
import com.sajal.another.service.StudentService;
import com.sajal.atom.annotations.atom.Atom;
import com.sajal.atom.annotations.atom.Inject;
import com.sajal.atom.annotations.web.Controller;
import com.sajal.atom.annotations.web.GetMapping;

import java.util.List;

@Controller(path = "/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @GetMapping("/init")
    public void init() {
        studentService.init();
    }
}
