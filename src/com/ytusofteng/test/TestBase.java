package com.ytusofteng.test;

import com.ytusofteng.model.accounts.Lecturer;
import com.ytusofteng.model.accounts.Officer;
import com.ytusofteng.model.accounts.Student;
import com.ytusofteng.model.entities.Book;
import com.ytusofteng.model.entities.Magazine;
import com.ytusofteng.model.enums.BookType;
import com.ytusofteng.system.Config;
import com.ytusofteng.system.Library;
import org.junit.jupiter.api.BeforeEach;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class TestBase {
    protected Library library;
    protected Config libraryConfig;
    protected Lecturer lecturerOK;
    protected Lecturer lecturerYES;
    protected Officer officerST;
    protected Student studentUY;
    protected Student studentMEG;
    protected Student studentMB;
    protected Student studentRA;
    protected Student studentUAI;
    protected Book bookLOTR;
    protected Book bookHP;
    protected Book bookTM;
    protected Book textbookDSP;
    protected Book textbookLA;
    protected Book textbookML;
    protected Magazine magazineSP;
    protected Magazine magazineGJ;
    protected Magazine magazineBOP;

    @BeforeEach
    public void beforeEach() {
        // Create new library.
        library = new Library();

        // Fill library with test data.
        this.createInitialTestData();

        // Get config.
        libraryConfig = library.getConfig();

        // Get some entities and accounts.
        lecturerOK = (Lecturer) library.getAccountById(1);
        lecturerYES = (Lecturer) library.getAccountById(2);
        officerST = (Officer) library.getAccountById(51);
        studentUY = (Student) library.getAccountById(101);
        studentMEG = (Student) library.getAccountById(102);
        studentMB = (Student) library.getAccountById(103);
        studentRA = (Student) library.getAccountById(104);
        studentUAI = (Student) library.getAccountById(105);
        bookLOTR = (Book) library.getEntityById(1);
        bookHP = (Book) library.getEntityById(2);
        bookTM = (Book) library.getEntityById(3);
        textbookDSP = (Book) library.getEntityById(4);
        textbookLA = (Book) library.getEntityById(5);
        textbookML = (Book) library.getEntityById(6);
        magazineSP = (Magazine) library.getEntityById(101);
        magazineGJ = (Magazine) library.getEntityById(102);
        magazineBOP = (Magazine) library.getEntityById(103);
    }

    public void createInitialTestData() {
        Date dateYesterday = Date.from(ZonedDateTime.now().minusDays(1).toInstant());

        // Add entities.
        library.addEntity(new Book(1, 3, "The Lord of the Rings, The Fellowship of the Ring", parseDate("24/07/1954"), "J. R. R. Tolkien", BookType.FICTION));
        library.addEntity(new Book(2, 2, "Harry Potter and the Philosopher's Stone", parseDate("26/06/1997"), "J. K. Rowling", BookType.FICTION));
        library.addEntity(new Book(3, 1, "The Martian", parseDate("01/01/2011"), "Andy Weir", BookType.FICTION));
        library.addEntity(new Book(4, 10, "Digital Signal Processing", parseDate("01/01/2011"), "Friedrich Nietzsche", BookType.TEXTBOOK));
        library.addEntity(new Book(5, 10, "Linear Algebra Done Right", parseDate("01/01/2011"), "George Washington", BookType.TEXTBOOK));
        library.addEntity(new Book(6, 1, "Introduction to Machine Learning", parseDate("01/01/2011"), "Bugs Bunny", BookType.TEXTBOOK));
        library.addEntity(new Magazine(101, 300, "Science Point", parseDate("17/11/1989"), dateYesterday));
        library.addEntity(new Magazine(102, 300, "Games Journal", parseDate("03/13/2015"), dateYesterday));
        library.addEntity(new Magazine(103, 300, "Beauty of People", parseDate("21/03/2013"), dateYesterday));

        // Add accounts
        library.addAccount(new Lecturer(1, 100, "Oya", "Kalipsiz", "Prof", "oya.kalipsiz@yildiz.edu.tr"));
        library.addAccount(new Lecturer(2, 100, "Yunus Emre", "Selcuk", "Dr", "dr.yunus.emre.selcuk@yildiz.edu.tr"));
        library.addAccount(new Officer(51, 50, "Suna", "Tacalan", "Sef", "sef.suna.tacalan@yildiz.edu.tr"));
        library.addAccount(new Student(101,10,"Ugurcan", "Yilmaz", "ugurcan.yilmaz@gmail.com"));
        library.addAccount(new Student(102,10,"Muhammed Enes", "Gulsoy", "muhammed.enes.gulsoy@gmail.com"));
        library.addAccount(new Student(103,10,"Mehmet", "Bingol", "mehmet.bingol@gmail.com"));
        library.addAccount(new Student(104,10,"Rames", "Aliyev", "rames.aliyev@gmail.com"));
        library.addAccount(new Student(105,10,"Umut Arda", "Ince", "umur.arda.ince@gmail.com"));

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
