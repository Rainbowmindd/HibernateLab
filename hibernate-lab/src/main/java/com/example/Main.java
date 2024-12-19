package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Date;


public class Main {
  public static void main(String[] args) {
    // Uzyskanie sesji Hibernate
    SessionFactory factory = HibernateUtil.getSessionFactory();
    Session session = factory.getCurrentSession();

    try {
      // Rozpoczęcie transakcji
      Transaction transaction = session.beginTransaction();

      // Tworzenie nowych nauczycieli
      Teacher teacher1 = new Teacher("Jan", "Kowalski", TeacherCondition.PRESENT, 1980, 4500.0);
      Teacher teacher2 = new Teacher("Anna", "Nowak", TeacherCondition.PRESENT, 1985, 5000.0);
      Teacher teacher3 = new Teacher("Piotr", "Wiśniewski", TeacherCondition.SICK, 1990, 4800.0);
      Teacher teacher4 = new Teacher("Katarzyna", "Lewandowska", TeacherCondition.DELEGATION, 1975, 5200.0);

      // Zapisanie obiektów do bazy danych
      session.save(teacher1);
      session.save(teacher2);
      session.save(teacher3);
      session.save(teacher4);

      // Dodanie ocen
      ClassTeacher classTeacher = new ClassTeacher("Math", 3);
      session.save(classTeacher);
      addRate(session, 5, classTeacher, "Good performance");

      // Zakończenie transakcji
      transaction.commit();
      System.out.println("Teachers and rates saved!");

    } finally {
      factory.close();  // Zamykanie sesji
    }
  }

  // Dodanie oceny do klasy
  public static void addRate(Session session, int value, ClassTeacher classTeacher, String comment) {
    Date rateDate = new Date(); // Pobranie aktualnej daty
    Rate rate = new Rate(value, classTeacher, rateDate, comment);
    session.save(rate);
    System.out.println("Rate added: " + rate);
  }
}
