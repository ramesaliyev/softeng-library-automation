package com.ytusofteng.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMehmet extends TestBase {
    @Test
    public void testCheckout() {
        System.out.println();
        System.out.println("TEST: Try to checkout entity.");

        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        library.checkoutEntity(studentRA, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        assertEquals(bookTM.getInStockCount(), bookTM.getIssueCount() - 1);
    }

    @Test
    public void testCheckoutOutOfStockEntity() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity which has no issue left in stock.");

        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM));
        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        library.checkoutEntity(studentMB, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM));
    }

    @Test
    public void testCheckoutSameEntityAgainToSameAccount() {
        System.out.println();
        System.out.println("TEST: Try to checkout same entity for same account more than once.");

        assertFalse(library.hasAccountLentEntity(studentUY, bookHP));
        library.checkoutEntity(studentUY, bookHP);
        assertTrue(library.hasAccountLentEntity(studentUY, bookHP));
        assertEquals(bookHP.getInStockCount(), bookHP.getIssueCount() - 1);
        library.checkoutEntity(studentUY, bookHP);
        assertEquals(bookHP.getInStockCount(), bookHP.getIssueCount() - 1);
        assertEquals(1, library.getEntitiesOfAccount(studentUY).size());
    }

    @Test
    public void testCheckoutTextbookAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to checkout a Textbook as Student.");

        assertEquals(0, library.getEntitiesOfAccount(studentUAI).size());
        library.checkoutEntity(studentUAI, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount());
        assertFalse(library.hasAccountLentEntity(studentUAI, textbookDSP));
        assertEquals(0, library.getEntitiesOfAccount(studentUAI).size());
    }

    @Test
    public void testCheckoutTextbookAsOfficier() {
        System.out.println();
        System.out.println("TEST: Try to checkout a Textbook as Officer.");

        assertEquals(0, library.getEntitiesOfAccount(officerST).size());
        library.checkoutEntity(officerST, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount());
        assertFalse(library.hasAccountLentEntity(officerST, textbookDSP));
        assertEquals(0, library.getEntitiesOfAccount(officerST).size());
    }

    @Test
    public void testCheckoutTextbookAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to checkout a Textbook as Lecturer.");

        assertEquals(0, library.getEntitiesOfAccount(lecturerOK).size());
        library.checkoutEntity(lecturerOK, textbookDSP);
        assertEquals(textbookDSP.getInStockCount(), textbookDSP.getIssueCount() - 1);
        assertTrue(library.hasAccountLentEntity(lecturerOK, textbookDSP));
        assertEquals(1, library.getEntitiesOfAccount(lecturerOK).size());
    }
}
