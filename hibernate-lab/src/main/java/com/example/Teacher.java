
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
    private TeacherCondition status;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "salary")
    private double salary;

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
    // Pozostałe gettery i settery...
}
