package com.pao.laboratory03.service;

import com.pao.laboratory03.exceptions.StudentNotFoundException;
import com.pao.laboratory03.model.Student;
import com.pao.laboratory03.model.Subject;

import java.util.*;

public class StudentService {
    private final List<Student> students = new ArrayList<>();

    //patternul de singleton
    private static StudentService instance;

    private StudentService() {} // Constructor privat

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    // Metodele:

    // *      a) void addStudent(String name, int age)
    // *         → creează Student și adaugă în listă
    // *         → dacă există deja un student cu același nume, aruncă RuntimeException
    // * 
    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul cu numele '" + name + "' exista deja!");
            }
        }
        students.add(new Student(name, age));
    }

    // b) Cautare după nume
    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit!");
    }

    // c) Adauga nota
    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName); 
        student.addGrade(subject, grade);          
    }

    // d) Afisare studenti cu note
    public void printAllStudents() {
        System.out.println("Studenti:   \n");
        students.forEach(System.out::println);
    }

    // e) Sortare descrescatoare dupa medie
    public void printTopStudents() {
        System.out.println("\n Top Studenti dupa medie: \n");
        
        List<Student> sortedList = new ArrayList<>(students);
        
        sortedList.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.getAverage(), s1.getAverage());
            }
        });
    
        for (Student s : sortedList) {
            System.out.println(s);
        }
    }

    // f) Medie pe materie 
    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sumMap = new HashMap<>();
        Map<Subject, Integer> countMap = new HashMap<>();
    
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            Map<Subject, Double> studentGrades = s.getGrades();
    
            for (Subject sub : studentGrades.keySet()) {
                Double grade = studentGrades.get(sub);
    
                if (!sumMap.containsKey(sub)) {
                    sumMap.put(sub, 0.0);
                    countMap.put(sub, 0);
                }
    
                Double currentSum = sumMap.get(sub);
                Integer currentCount = countMap.get(sub);
    
                sumMap.put(sub, currentSum + grade);
                countMap.put(sub, currentCount + 1);
            }
        }
    
        Map<Subject, Double> averages = new HashMap<>();
        for (Subject sub : sumMap.keySet()) {
            Double totalSum = sumMap.get(sub);
            Integer totalCount = countMap.get(sub);
            averages.put(sub, totalSum / totalCount);
        }
    
        return averages;
    }
}