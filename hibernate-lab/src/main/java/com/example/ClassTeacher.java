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

    @OneToMany(mappedBy = "classTeacher", fetch = FetchType.LAZY)
    private List<Teacher> teachers;

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

    public static void addTeacherToGroup(Session session, Teacher teacher, ClassTeacher classTeacher) {
        // Przypisanie nauczyciela do grupy
        teacher.setClassTeacher(classTeacher);

        // Zapisanie zmiany w bazie
        session.saveOrUpdate(teacher);
        System.out.println("Teacher " + teacher.getName() + " added to group " + classTeacher.getTeacherGroupName());
    }

    public static void removeTeacherFromGroup(Session session, Teacher teacher) {

        teacher.setClassTeacher(null);


        session.saveOrUpdate(teacher);
        System.out.println("Teacher " + teacher.getName() + " removed from group");
    }
    public static Teacher search(Session session, String surname) {
        // Wykorzystanie HQL do wyszukiwania nauczyciela w bazie danych
        String hql = "FROM Teacher t WHERE t.surname = :surname";
        Teacher teacher = session.createQuery(hql, Teacher.class)
                .setParameter("surname", surname)
                .uniqueResult();
        if (teacher != null) {
            System.out.println("Found teacher: " + teacher.getName() + " " + teacher.getSurname());
        } else {
            System.out.println("Teacher with surname " + surname + " not found.");
        }
        return teacher;
    }

    public static List<Teacher> searchPartial(Session session, String stringFragment) {
        // Wykorzystanie HQL do wyszukiwania nauczycieli w bazie danych
        String hql = "FROM Teacher t WHERE t.surname LIKE :fragment";
        List<Teacher> teachers = session.createQuery(hql, Teacher.class)
                .setParameter("fragment", "%" + stringFragment + "%")
                .getResultList();
        if (!teachers.isEmpty()) {
            System.out.println("Found teachers with surname containing '" + stringFragment + "':");
            teachers.forEach(t -> System.out.println(t.getName() + " " + t.getSurname()));
        } else {
            System.out.println("No teachers found with surname containing '" + stringFragment + "'.");
        }
        return teachers;
    }


    public static List<Teacher> sortBySalary(Session session, ClassTeacher classTeacher) {
        // Wykorzystanie HQL do pobrania nauczycieli z sortowaniem po wynagrodzeniu
        String hql = "FROM Teacher t WHERE t.classTeacher.id = :classTeacherId ORDER BY t.salary DESC";
        List<Teacher> teachers = session.createQuery(hql, Teacher.class)
                .setParameter("classTeacherId", classTeacher.getId())
                .getResultList();
        System.out.println("Teachers in group " + classTeacher.getTeacherGroupName() + " sorted by salary:");
        teachers.forEach(t -> System.out.println(t.getName() + " " + t.getSurname() + " - Salary: " + t.getSalary()));
        return teachers;
    }

    public static void addSalary(Session session, Teacher teacher, double bonus) {
        // Pobranie nauczyciela z bazy danych
        Teacher managedTeacher = session.get(Teacher.class, teacher.getId());
        if (managedTeacher != null) {
            managedTeacher.setSalary(managedTeacher.getSalary() + bonus);

            // Aktualizacja nauczyciela w bazie danych
            session.saveOrUpdate(managedTeacher);
            System.out.println("CONGRATULATIONS! YOU'VE EARNED A BONUS TO YOUR SALARY! Current salary: " + managedTeacher.getSalary());
        } else {
            System.out.println("Teacher not found in the database.");
        }
    }






    // Inne metody (addSalary, removeTeacher, changeCondition itp.)...
}
