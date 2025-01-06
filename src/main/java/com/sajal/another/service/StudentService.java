package com.sajal.another.service;

import com.sajal.another.domain.Student;
import com.sajal.another.repository.StudentRepository;
import com.sajal.atom.annotations.atom.Atom;

import java.util.List;

@Atom
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void init() {
        studentRepository.init();
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentRepository.getStudentById(id);
    }

    public List<Student> getStudentsByName(String name) {
        return studentRepository.findAllByName(name);
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findAllByAge(age);
    }
}
