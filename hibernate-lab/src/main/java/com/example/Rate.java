package com.example;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private double value;  // Ocena w skali 0-6

    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private ClassTeacher classTeacher;  // Grupa, dla której wystawiona została ocena

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;  // Data wystawienia oceny

    @Column(name = "comment")
    private String comment;  // Opcjonalny komentarz

    // Konstruktor, Gettery i Settery

    public Rate() {}

    public Rate(double value, ClassTeacher classTeacher, Date date, String comment) {
        this.value = value;
        this.classTeacher = classTeacher;
        this.date = date;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
