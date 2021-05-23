package com.ytusofteng.test;

import com.ytusofteng.model.accounts.*;
import com.ytusofteng.model.entities.*;
import com.ytusofteng.system.Config;
import com.ytusofteng.system.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LendingTest {
    private Library library;
    private Config libraryConfig;
    private Lecturer lecturerOK;
    private Lecturer lecturerYES;
    private Officer officerST;
    private Student studentUY;
    private Student studentMEG;
    private Student studentMB;
    private Student studentRA;
    private Student studentUAI;
    private Book bookLOTR;
    private Book bookHP;
    private Book bookTM;
    private Book textbookDSP;
    private Book textbookLA;
    private Book textbookML;
    private Magazine magazineSP;
    private Magazine magazineGJ;
    private Magazine magazineBOP;
    
    @BeforeEach
    public void setup() {
        // Create new library.
        library = new Library();

        // Fill library with test data.
        TestHelper.createInitialTestData(library);

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

    @Test
    public void testBasicLending() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime zdate2MonthsAgo = ZonedDateTime.now().minusMonths(2);
        ZonedDateTime zdate7MonthsAgo = ZonedDateTime.now().minusMonths(7);
        Date date2MonthsAgo = Date.from(zdate2MonthsAgo.toInstant());
        Date date7MonthsAgo = Date.from(zdate7MonthsAgo.toInstant());
        long dayDiff2Months = Duration.between(date2MonthsAgo.toInstant(), now).toDays();
        long dayDiff7Months = Duration.between(date7MonthsAgo.toInstant(), now).toDays();

        long studentInitialBalance = studentRA.getBalance();
        long lecturerInitialBalance = lecturerOK.getBalance();

        int studentLendingDurationLimit = libraryConfig.getLendingDurationLimit(studentRA.getClass());
        int lecturerLendingDurationLimit = libraryConfig.getLendingDurationLimit(lecturerOK.getClass());

        System.out.println("Step 1: Try to lend entity.");
        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        library.checkoutEntity(studentRA, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        assertEquals(bookTM.getInStockCount(), bookTM.getIssueCount() - 1);

        System.out.println();
        System.out.println("Step 2: Try to lend a entity which has no issue left in stock.");
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM));
        library.checkoutEntity(studentMB, bookTM);
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM));

        System.out.println();
        System.out.println("Step 3: Try to return an entity that account does not have.");
        assertEquals(bookLOTR.getInStockCount(), bookLOTR.getIssueCount());
        library.returnEntity(studentRA, bookLOTR);
        assertEquals(bookLOTR.getInStockCount(), bookLOTR.getIssueCount());

        System.out.println();
        System.out.println("Step 4: Try to lend same entity for same account more than once.");
        assertFalse(library.hasAccountLentEntity(studentUY, bookHP));
        library.checkoutEntity(studentUY, bookHP);
        assertTrue(library.hasAccountLentEntity(studentUY, bookHP));
        assertEquals(bookHP.getInStockCount(), bookHP.getIssueCount() - 1);
        library.checkoutEntity(studentUY, bookHP);
        assertEquals(bookHP.getInStockCount(), bookHP.getIssueCount() - 1);
        assertEquals(1, library.getEntitiesOfAccount(studentUY).size());

        System.out.println();
        System.out.println("Step 5: Try to return entity.");
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        library.returnEntity(studentRA, bookTM);
        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        assertEquals(bookTM.getInStockCount(), bookTM.getIssueCount());

        System.out.println();
        System.out.println("Step 6: Try to checkout an returned entity.");
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM)); 
        library.checkoutEntity(studentMB, bookTM);
        assertTrue(library.hasAccountLentEntity(studentMB, bookTM));
        assertEquals(bookTM.getInStockCount(), bookTM.getIssueCount() - 1);

        System.out.println();
        System.out.println("Step 7: Try to checkout entity count more than account limit.");
        assertEquals(0, library.getEntitiesOfAccount(studentMEG).size());
        library.checkoutEntity(studentMEG, magazineSP);
        assertEquals(1, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineSP));
        library.checkoutEntity(studentMEG, magazineGJ);
        assertEquals(2, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineGJ));
        library.checkoutEntity(studentMEG, magazineBOP);
        assertEquals(3, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineBOP));
        library.checkoutEntity(studentMEG, bookLOTR);
        assertEquals(3, library.getEntitiesOfAccount(studentMEG).size());
        assertFalse(library.hasAccountLentEntity(studentMEG, bookLOTR));

        System.out.println();
        System.out.println("Step 8: Try to checkout a Textbook as Student.");
        assertEquals(0, library.getEntitiesOfAccount(studentUAI).size());
        library.checkoutEntity(studentUAI, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount());
        assertFalse(library.hasAccountLentEntity(studentUAI, textbookDSP));

        System.out.println();
        System.out.println("Step 9: Try to checkout a Textbook as Officer.");
        assertEquals(0, library.getEntitiesOfAccount(officerST).size());
        library.checkoutEntity(officerST, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount());
        assertFalse(library.hasAccountLentEntity(officerST, textbookDSP));

        System.out.println();
        System.out.println("Step 10: Try to checkout a Textbook as Lecturer.");
        assertEquals(0, library.getEntitiesOfAccount(lecturerOK).size());
        library.checkoutEntity(lecturerOK, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount() - 1);
        assertTrue(library.hasAccountLentEntity(lecturerOK, textbookDSP));

        System.out.println();
        System.out.println("Step 11: Try to checkout an entity when there is past due entities in account as student.");
        assertFalse(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));
        library.checkoutEntity(studentRA, magazineBOP, date2MonthsAgo);
        library.checkoutEntity(studentRA, magazineGJ);
        assertTrue(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));

        System.out.println();
        System.out.println("Step 12: Try to checkout an entity when there is past due entities in account as lecturer.");
        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookLA));
        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ));
        library.checkoutEntity(lecturerYES, textbookLA, date2MonthsAgo);
        library.checkoutEntity(lecturerYES, textbookML, date7MonthsAgo);
        library.checkoutEntity(lecturerYES, magazineGJ);
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookLA));
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ));

        System.out.println();
        System.out.println("Step 13: Try to return past due entity as student.");
        assertEquals(studentInitialBalance, studentRA.getBalance());
        assertTrue(library.hasAccountLentEntity(studentRA, magazineBOP));
        library.returnEntity(studentRA, magazineBOP);
        assertFalse(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertEquals(studentInitialBalance - (dayDiff2Months - studentLendingDurationLimit), studentRA.getBalance());

        System.out.println();
        System.out.println("Step 14: Try to return past due entity as lecturer.");
        assertEquals(lecturerInitialBalance, lecturerYES.getBalance());
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookML));
        library.returnEntity(lecturerYES, textbookML);
        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertTrue(lecturerYES.getBalance() < 0);
        assertEquals(lecturerInitialBalance - (dayDiff7Months - lecturerLendingDurationLimit), lecturerYES.getBalance());

        System.out.println();
        System.out.println("Step 15: Try to checkout an entity as when has no balance as student.");
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));
        library.checkoutEntity(studentRA, magazineGJ);
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));
    }
}
