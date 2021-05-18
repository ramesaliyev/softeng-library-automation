package com.ytusofteng.test;

import com.ytusofteng.model.accounts.*;
import com.ytusofteng.model.entities.*;
import com.ytusofteng.system.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LendingTest {
    private Library library;
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
    private Magazine magazineSP;
    private Magazine magazineGJ;
    private Magazine magazineBOP;
    
    @BeforeEach
    public void setup() {
        // Create new library.
        library = new Library();

        // Fill library with test data.
        TestHelper.createInitialTestData(library);

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
        magazineSP = (Magazine) library.getEntityById(101);
        magazineGJ = (Magazine) library.getEntityById(102);
        magazineBOP = (Magazine) library.getEntityById(103);
    }

    @Test
    public void testBasicLending() {
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

        System.out.println();
        System.out.println("Step 9: Try to checkout a Textbook as Officer.");

        System.out.println();
        System.out.println("Step 10: Try to checkout a Textbook as Lecturer.");
    }
}
