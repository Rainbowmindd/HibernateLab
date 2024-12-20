package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Main {
  public static void main(String[] args) {

    SessionFactory factory = HibernateUtil.getSessionFactory();
    Session session = factory.getCurrentSession();

    try {

      Transaction transaction = session.beginTransaction();


      Teacher teacher1 = new Teacher("Jan", "Kowalski", TeacherCondition.PRESENT, 1980, 4500.0);
      Teacher teacher2 = new Teacher("Anna", "Nowak", TeacherCondition.PRESENT, 1985, 5000.0);
      Teacher teacher3 = new Teacher("Piotr", "Wiśniewski", TeacherCondition.SICK, 1990, 4800.0);
      Teacher teacher4 = new Teacher("Katarzyna", "Lewandowska", TeacherCondition.DELEGATION, 1975, 5200.0);
      Teacher teacher5 = new Teacher("Maryla", "Rodowicz", TeacherCondition.DELEGATION, 1800, 6000.0);


      session.save(teacher1);
      session.save(teacher2);
      session.save(teacher3);
      session.save(teacher4);
      session.save(teacher5);


      ClassTeacher classTeacher = new ClassTeacher("Math", 3);
      ClassTeacher classTeacher1 = new ClassTeacher("Polish", 2);
      ClassTeacher classTeacher2 = new ClassTeacher("IT", 1);

      session.save(classTeacher);
      session.save(classTeacher1);
      session.save(classTeacher2);

      ClassTeacher.addTeacherToGroup(session, teacher1, classTeacher);
      ClassTeacher.addTeacherToGroup(session, teacher2, classTeacher);
      ClassTeacher.addTeacherToGroup(session, teacher3, classTeacher1);
      ClassTeacher.addTeacherToGroup(session, teacher4, classTeacher2);
      ClassTeacher.addTeacherToGroup(session, teacher5, classTeacher);

      displayAllTeachers(session);


      ClassTeacher.addSalary(session, teacher1, 500.0);


      ClassTeacher.removeTeacherFromGroup(session, teacher2);


      Teacher foundTeacher = ClassTeacher.search(session, "Kowalski");
      if (foundTeacher != null) {
        System.out.println("Found teacher: " + foundTeacher.getName() + " " + foundTeacher.getSurname());
      }

      List<Teacher> sortedBySalary = ClassTeacher.sortBySalary(session, classTeacher);
      System.out.println("Teachers in group " + classTeacher.getTeacherGroupName() + " sorted by salary:");
      sortedBySalary.forEach(t -> System.out.println(t.getName() + " " + t.getSurname() + " - Salary: " + t.getSalary()));

      List<Teacher> partialSearch = ClassTeacher.searchPartial(session, "Wiś");
      System.out.println("Teachers matching partial search:");
      partialSearch.forEach(t -> System.out.println(t.getName() + " " + t.getSurname()));


      Rate rate1 = new Rate(4.5, classTeacher, new Date(), "Very good");
      Rate rate2 = new Rate(5.0, classTeacher, new Date(), "Excellent");
      Rate rate3 = new Rate(3.5, classTeacher1, new Date(), "Needs improvement");
      Rate rate4 = new Rate(4.0, classTeacher2, new Date(), "Satisfactory");

      session.save(rate1);
      session.save(rate2);
      session.save(rate3);
      session.save(rate4);

      displayAllRates(session);

      saveTeachersToCSV(session);
      saveClassTeachersToCSV(session);
      saveRatesToCSV(session);


      //zwraca:  liczbę ocen i średnią ocen dla każdej grupy nauczycieli
      String hql = "SELECT c.teacherGroupName, COUNT(r.id) AS liczbaOcen, AVG(r.value) AS sredniaOcena " +
              "FROM Rate r " +
              "JOIN r.classTeacher c " +
              "GROUP BY c.teacherGroupName";


      Query<Object[]> query = session.createQuery(hql, Object[].class);
      List<Object[]> results = query.getResultList();

      for (Object[] result : results) {
        String teacherGroupName = (String) result[0];
        Long liczbaOcen = (Long) result[1];
        Double sredniaOcena = (Double) result[2];


        System.out.println("Grupa: " + teacherGroupName + ", Liczba ocen: " + liczbaOcen + ", Średnia ocena: " + sredniaOcena);
      }


      transaction.commit();
      System.out.println("Teachers, class teachers, and rates saved!");

    } finally {
      factory.close();
    }
  }
  public static void displayAllTeachers(Session session) {
    System.out.println("All teachers in the database:");
    List<Teacher> teachers = session.createQuery("FROM Teacher", Teacher.class).getResultList();
    for (Teacher teacher : teachers) {
      System.out.println("ID: " + teacher.getId() +
              ", Name: " + teacher.getName() +
              ", Surname: " + teacher.getSurname() +
              ", Condition: " + teacher.getStatus() +
              ", Year of Birth: " + teacher.getBirthYear() +
              ", Salary: " + teacher.getSalary() +
              ", Group: " + (teacher.getClassTeacher() != null ? teacher.getClassTeacher().getTeacherGroupName() : "None"));
    }
  }

  public static void displayAllRates(Session session) {
    System.out.println("All rates in the database:");
    List<Rate> rates = session.createQuery("FROM Rate", Rate.class).getResultList();
    for (Rate rate : rates) {
      System.out.println("ID: " + rate.getId() +
              ", Value: " + rate.getValue() +
              ", Date: " + rate.getDate() +
              ", Comment: " + rate.getComment() +
              ", Group: " + (rate.getClassTeacher() != null ? rate.getClassTeacher().getTeacherGroupName() : "None"));
    }
  }

  // Zapisz nauczycieli do pliku CSV
  public static void saveTeachersToCSV(Session session) {
    List<Teacher> teachers = session.createQuery("FROM Teacher", Teacher.class).getResultList();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("teachers.csv"))) {
      writer.write("ID,Name,Surname,Condition,BirthYear,Salary,Group\n");

      for (Teacher teacher : teachers) {
        writer.write(teacher.getId() + "," +
                teacher.getName() + "," +
                teacher.getSurname() + "," +
                teacher.getStatus() + "," +
                teacher.getBirthYear() + "," +
                teacher.getSalary() + "," +
                (teacher.getClassTeacher() != null ? teacher.getClassTeacher().getTeacherGroupName() : "None") + "\n");
      }

      System.out.println("Teachers saved to teachers.csv");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveClassTeachersToCSV(Session session) {
    List<ClassTeacher> classTeachers = session.createQuery("FROM ClassTeacher", ClassTeacher.class).getResultList();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("class_teacher.csv"))) {
      writer.write("ID,TeacherGroupName,MaxTeacherNumber\n");

      for (ClassTeacher classTeacher : classTeachers) {
        writer.write(classTeacher.getId() + "," +
                classTeacher.getTeacherGroupName() + "," +
                classTeacher.getMaxTeacherNumber() + "\n");
      }

      System.out.println("Class teachers saved to class_teacher.csv");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveRatesToCSV(Session session) {
    List<Rate> rates = session.createQuery("FROM Rate", Rate.class).getResultList();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("rates.csv"))) {
      writer.write("ID,Value,Date,Comment,Group\n");

      for (Rate rate : rates) {
        writer.write(rate.getId() + "," +
                rate.getValue() + "," +
                rate.getDate() + "," +
                rate.getComment() + "," +
                (rate.getClassTeacher() != null ? rate.getClassTeacher().getTeacherGroupName() : "None") + "\n");
      }

      System.out.println("Rates saved to rates.csv");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
