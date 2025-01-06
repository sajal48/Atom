package com.sajal.another.repository;

import com.sajal.another.domain.Student;
import com.sajal.atom.annotations.atom.Atom;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Atom
public class StudentRepository {
    Map<Integer, Student> studentStore = new HashMap<>();

    public boolean addStudent(Student student) {
        if (studentStore.containsKey(student.id())) {
            return false;
        }
        studentStore.put(student.id(), student);
        return true;
    }

    public List<Student> getAllStudents() {
        return studentStore.values().stream().toList();
    }
    public Student getStudentById(int id) {
        return studentStore.get(id);
    }
    public boolean deleteStudent(int id) {
        if (studentStore.containsKey(id)) {
            studentStore.remove(id);
            return true;
        }
        return false;
    }
    public Student findByName(String name) {
        return studentStore.values().stream().filter(student -> student.name().equals(name)).findFirst().orElse(null);
    }

    public List<Student> findAllByName(String name) {
        return studentStore.values().stream().filter(student -> student.name().equals(name)).toList();
    }
    public List<Student> findAllByAge(int age) {
        return studentStore.values().stream().filter(student -> student.age() == age).toList();
    }
    public List<Student> findAllByAgeGreaterThan(int age) {
        return studentStore.values().stream().filter(student -> student.age() > age).toList();
    }

    @PostConstruct
    public void init() {
        studentStore.put(1, new Student(1, "Alice", 22));
        studentStore.put(2, new Student(2, "Bob", 23));
        studentStore.put(3, new Student(3, "Charlie", 24));
        studentStore.put(4, new Student(4, "David", 25));
        studentStore.put(5, new Student(5, "Eve", 26));
        studentStore.put(6, new Student(6, "Frank", 27));
        studentStore.put(7, new Student(7, "Grace", 28));
        studentStore.put(8, new Student(8, "Hank", 29));
        studentStore.put(9, new Student(9, "Ivy", 30));
        studentStore.put(10, new Student(10, "Jack", 31));
    }
}
