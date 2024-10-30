import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassTeacher classTeacher=new ClassTeacher("Grupa 1",4);

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher ("Jakub", "Pachut"));
        teachers.add(new Teacher ("Martyna", "Kindrat"));
        teachers.add(new Teacher ("Martyna", "Kindrat"));
        teachers.add(new Teacher ("Aneta", "Kaczmarczyk"));
        teachers.add(new Teacher ("Anna", "Jasielec"));
        teachers.add(new Teacher ("Anna", "Hellmann"));


       for(Teacher teacher : teachers) {
           classTeacher.addTeacher(teacher);
       }
    }
}