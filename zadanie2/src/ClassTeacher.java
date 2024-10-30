import java.util.ArrayList;
import java.util.List;

public class ClassTeacher{
    private String teacherGroupName;
    //lista nauczycieli
    private List<Teacher> teachers;
    private int maxTeacherNumber;

    ClassTeacher(String teacherGroupName, int maxTeacherNumber){
        this.teacherGroupName = teacherGroupName;
        this.maxTeacherNumber = maxTeacherNumber;
        this.teachers = new ArrayList<>();
    }
    public String getTeacherGroupName(){return teacherGroupName;}
    public int getMaxTeacherNumber(){return maxTeacherNumber;}

    public void addTeacher(Teacher teacher){

        if(teachers.size()>maxTeacherNumber){
            System.out.print("Przekroczona pojemnosc grupy.");
        }

        if(teachers.contains(teacher)){
            System.out.println("Ten nauczyciel należy juz do grupy");
        }

        teachers.add(teacher);
        System.out.println("Nauczyciel "+teacher.getName() + " " + teacher.getSurname()+" dodany do grupy");
    }
}
