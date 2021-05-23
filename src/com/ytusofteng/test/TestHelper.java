package com.ytusofteng.test;

import com.ytusofteng.model.accounts.*;
import com.ytusofteng.model.entities.*;
import com.ytusofteng.model.enums.*;
import com.ytusofteng.system.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class TestHelper {
    public static void createInitialTestData(Library library) {
        Date dateMonthAgo = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
        Date dateYesterday = Date.from(ZonedDateTime.now().minusDays(1).toInstant());

        // Add entities.
        library.addEntity(new Book(1, 3, "The Lord of the Rings, The Fellowship of the Ring", TestHelper.parseDate("24/07/1954"), "J. R. R. Tolkien", BookType.FICTION));
        library.addEntity(new Book(2, 2, "Harry Potter and the Philosopher's Stone", TestHelper.parseDate("26/06/1997"), "J. K. Rowling", BookType.FICTION));
        library.addEntity(new Book(3, 1, "The Martian", TestHelper.parseDate("01/01/2011"), "Andy Weir", BookType.FICTION));
        library.addEntity(new Book(4, 10, "Digital Signal Processing", TestHelper.parseDate("01/01/2011"), "Friedrich Nietzsche", BookType.TEXTBOOK));
        library.addEntity(new Book(5, 10, "Linear Algebra Done Right", TestHelper.parseDate("01/01/2011"), "George Washington", BookType.TEXTBOOK));
        library.addEntity(new Book(6, 10, "Introduction to Machine Learning", TestHelper.parseDate("01/01/2011"), "Bugs Bunny", BookType.TEXTBOOK));
        library.addEntity(new Magazine(101, 300, "Science Point", TestHelper.parseDate("17/11/1989"), dateYesterday));
        library.addEntity(new Magazine(102, 300, "Games Journal", TestHelper.parseDate("03/13/2015"), dateYesterday));
        library.addEntity(new Magazine(103, 300, "Beauty of People", TestHelper.parseDate("21/03/2013"), dateYesterday));

        // Add accounts
        library.addAccount(new Lecturer(1, 100, "Oya", "Kalipsiz", "Prof"));
        library.addAccount(new Lecturer(2, 100, "Yunus Emre", "Selcuk", "Dr"));
        library.addAccount(new Officer(51, 50, "Suna", "Tacalan", "Sef"));
        library.addAccount(new Student(101,10,"Ugurcan", "Yilmaz"));
        library.addAccount(new Student(102,10,"Muhammed Enes", "Gulsoy"));
        library.addAccount(new Student(103,10,"Mehmet", "Bingol"));
        library.addAccount(new Student(104,10,"Rames", "Aliyev"));
        library.addAccount(new Student(105,10,"Umut Arda", "Ince"));

        // Configure library.
        // Max number of entity can be lent by account type.
        library.setLendingCountLimit(Student.class, 3);
        library.setLendingCountLimit(Officer.class, 3);
        library.setLendingCountLimit(Lecturer.class, 6);
        // Max durations (as days) for lent entities by account type.
        library.setLendingCountDuration(Student.class, 31);
        library.setLendingCountDuration(Officer.class, 31);
        library.setLendingCountDuration(Lecturer.class, 93);
    }

    public static Date parseDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return simpleDateFormat.parse(date);
        } catch(ParseException e) {
            e.printStackTrace();
            System.out.print("Cannot parse date " + date);
        }

        return null;
    }
}
