package com.example;

import org.hibernate.Session;
import java.util.Date;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "class_container")
public class ClassContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_container_id")
    private Map<String, ClassTeacher> teacherGroups;

    public ClassContainer() {
        this.teacherGroups = new HashMap<>();
    }

    // Metody operujące na grupach
    public void addClass(String groupName, int maxTeacher) {
        if (!teacherGroups.containsKey(groupName)) {
            teacherGroups.put(groupName, new ClassTeacher(groupName, maxTeacher));
        } else {
            System.out.println("Group already exists!");
        }
    }

    public void removeClass(String groupName) {
        teacherGroups.remove(groupName);
        System.out.println("Class removed successfully!");
    }

    public List<String> findEmpty() {
        List<String> emptyGroups = new ArrayList<>();
        for (Map.Entry<String, ClassTeacher> entry : teacherGroups.entrySet()) {
            if (entry.getValue().getListTeachers().size() == 0) {
                emptyGroups.add(entry.getKey());
            }
        }
        return emptyGroups;
    }
    public void addClass(Session session, String groupName, int maxTeacher) {
        ClassTeacher existingGroup = session.createQuery("FROM ClassTeacher WHERE teacherGroupName = :groupName", ClassTeacher.class)
                .setParameter("groupName", groupName)
                .uniqueResult();

        if (existingGroup != null) {
            System.out.println("Group already exists!");
        } else {
            ClassTeacher newGroup = new ClassTeacher(groupName, maxTeacher);
            session.save(newGroup);
            System.out.println("Class added successfully!");
        }
    }

    public void removeClass(Session session, String groupName) {
        ClassTeacher groupToRemove = session.createQuery("FROM ClassTeacher WHERE teacherGroupName = :groupName", ClassTeacher.class)
                .setParameter("groupName", groupName)
                .uniqueResult();

        if (groupToRemove != null) {
            session.delete(groupToRemove);
            System.out.println("Class removed successfully!");
        } else {
            System.out.println("Group not found!");
        }
    }
    public List<String> findEmpty(Session session) {
        List<String> emptyGroups = session.createQuery("SELECT ct.teacherGroupName FROM ClassTeacher ct WHERE ct.listTeachers.size = 0", String.class)
                .getResultList();

        return emptyGroups;
    }
    public void addRate(Session session, double value, ClassTeacher classTeacher, Date date, String comment) {
        Rate rate = new Rate(value, classTeacher, date, comment);
        session.save(rate);
        System.out.println("Rate added successfully!");
    }





    // Inne metody (summary, etc.)
}

