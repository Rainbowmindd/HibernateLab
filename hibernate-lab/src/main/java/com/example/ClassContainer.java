package com.example;

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

    // Inne metody (summary, etc.)
}

