
package com.example;
import javax.persistence.*;


@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TeacherCondition status;  //sdajhds

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "salary") //ssss
    private double salary;
    @ManyToOne
    @JoinColumn(name = "class_teacher_id") // Foreign key do ClassTeacher
    private ClassTeacher classTeacher;

    // Gettery i settery

    public Teacher() {}

    public Teacher(String name, String surname, TeacherCondition status, int birthYear, double salary) {
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    // Getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) { this.surname = surname; }

    public String getName() {
        return name;
    }
    public void setName(String name) {}

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }
    public ClassTeacher getClassTeacher() { return classTeacher; }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public TeacherCondition getStatus() {
        return status;
    }

    public void setStatus(TeacherCondition status) {
        this.status = status;
    }
}
