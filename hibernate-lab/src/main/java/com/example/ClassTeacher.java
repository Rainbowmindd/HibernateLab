package com.example;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

@Entity
@Table(name = "class_teacher")
public class ClassTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_group_name")
    private String teacherGroupName;

    @OneToMany(mappedBy = "classTeacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Teacher> listTeachers;

    @Column(name = "max_teacher_number")
    private int maxTeacherNumber;

    public ClassTeacher() {
        this.listTeachers = new ArrayList<>();
    }

    public ClassTeacher(String teacherGroupName, int maxTeacherNumber) {
        this.teacherGroupName = teacherGroupName;
        this.maxTeacherNumber = maxTeacherNumber;
        this.listTeachers = new ArrayList<>();
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherGroupName() {
        return teacherGroupName;
    }

    public void setTeacherGroupName(String teacherGroupName) {
        this.teacherGroupName = teacherGroupName;
    }

    public List<Teacher> getListTeachers() {
        return listTeachers;
    }

    public void setListTeachers(List<Teacher> listTeachers) {
        this.listTeachers = listTeachers;
    }

    public int getMaxTeacherNumber() {
        return maxTeacherNumber;
    }

    public void setMaxTeacherNumber(int maxTeacherNumber) {
        this.maxTeacherNumber = maxTeacherNumber;
    }

    // Metody zarządzające nauczycielami w klasie
    public void addTeacher(Session session, Teacher teacher) {
        if (listTeachers.size() >= maxTeacherNumber) {
            System.out.println("GROUP IS FULL, cannot add new teacher");
            return;
        }

        Comparator<Teacher> teacherComparator = Comparator
                .comparing(Teacher::getSurname)
                .thenComparing(Teacher::getName);

        boolean exists = listTeachers.stream()
                .anyMatch(t -> teacherComparator.compare(t, teacher) == 0);

        if (exists) {
            System.out.println("This teacher ALREADY EXISTS in this group");
        } else {
            session.save(teacher);
            listTeachers.add(teacher);  // Dodajemy również do listy
            System.out.println("Teacher added successfully");
        }
    }


    // Inne metody (addSalary, removeTeacher, changeCondition itp.)...
}
