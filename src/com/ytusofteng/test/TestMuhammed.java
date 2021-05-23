package com.ytusofteng.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMuhammed extends TestBase {
    @Test
    public void testReturnEntity() {
        System.out.println();
        System.out.println("TEST: Try to return entity.");

        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        library.checkoutEntity(studentRA, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        library.returnEntity(studentRA, bookTM);
        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        assertEquals(bookTM.getInStockCount(), bookTM.getIssueCount());
    }

    @Test
    public void testReturnEntityAccountDoesNotHave() {
        System.out.println();
        System.out.println("TEST: Try to return an entity that account does not have.");

        assertFalse(library.hasAccountLentEntity(studentRA, bookLOTR));
        assertEquals(bookLOTR.getInStockCount(), bookLOTR.getIssueCount());
        library.returnEntity(studentRA, bookLOTR);
        assertEquals(bookLOTR.getInStockCount(), bookLOTR.getIssueCount());
    }

    @Test
    public void testCheckoutReturnedPreviouslyOutOfStockEntity() {
        System.out.println();
        System.out.println("TEST: Try to checkout an returned entity.");

        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
        library.checkoutEntity(studentRA, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        assertEquals(0, bookTM.getInStockCount()); // bookTM has only 1 stock.

        library.checkoutEntity(studentMB, bookTM);
        assertTrue(library.hasAccountLentEntity(studentRA, bookTM));
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM)); // Could not checkout.

        library.returnEntity(studentRA, bookTM); // Return
        assertEquals(1, bookTM.getInStockCount());
        library.checkoutEntity(studentMB, bookTM);
        assertTrue(library.hasAccountLentEntity(studentMB, bookTM)); // Could checkout now.
        assertEquals(0, bookTM.getInStockCount());
        assertFalse(library.hasAccountLentEntity(studentRA, bookTM));
    }

    @Test
    public void testCheckoutMoreThanLimitAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to checkout entity count more than account limit as student.");

        assertEquals(0, library.getEntitiesOfAccount(studentMEG).size());
        // 1
        library.checkoutEntity(studentMEG, magazineSP);
        assertEquals(1, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineSP));
        // 2
        library.checkoutEntity(studentMEG, magazineGJ);
        assertEquals(2, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineGJ));
        // 3
        library.checkoutEntity(studentMEG, magazineBOP);
        assertEquals(3, library.getEntitiesOfAccount(studentMEG).size());
        assertTrue(library.hasAccountLentEntity(studentMEG, magazineBOP));
        // 4
        library.checkoutEntity(studentMEG, bookLOTR);
        assertEquals(3, library.getEntitiesOfAccount(studentMEG).size()); // Students limited to 3.
        assertFalse(library.hasAccountLentEntity(studentMEG, bookLOTR));
    }

    @Test
    public void testCheckoutMoreThanLimitAsOfficer() {
        System.out.println();
        System.out.println("TEST: Try to checkout entity count more than account limit as officer.");

        assertEquals(0, library.getEntitiesOfAccount(officerST).size());
        // 1
        library.checkoutEntity(officerST, magazineSP);
        assertEquals(1, library.getEntitiesOfAccount(officerST).size());
        assertTrue(library.hasAccountLentEntity(officerST, magazineSP));
        // 2
        library.checkoutEntity(officerST, magazineGJ);
        assertEquals(2, library.getEntitiesOfAccount(officerST).size());
        assertTrue(library.hasAccountLentEntity(officerST, magazineGJ));
        // 3
        library.checkoutEntity(officerST, magazineBOP);
        assertEquals(3, library.getEntitiesOfAccount(officerST).size());
        assertTrue(library.hasAccountLentEntity(officerST, magazineBOP));
        // 4
        library.checkoutEntity(officerST, bookLOTR);
        assertEquals(3, library.getEntitiesOfAccount(officerST).size()); // Officer limited to 3.
        assertFalse(library.hasAccountLentEntity(officerST, bookLOTR));
    }

    @Test
    public void testCheckoutMoreThanLimitAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to checkout entity count more than account limit as lecturer.");

        assertEquals(0, library.getEntitiesOfAccount(lecturerOK).size());
        // 1
        library.checkoutEntity(lecturerOK, magazineSP);
        assertEquals(1, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, magazineSP));
        // 2
        library.checkoutEntity(lecturerOK, magazineGJ);
        assertEquals(2, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, magazineGJ));
        // 3
        library.checkoutEntity(lecturerOK, magazineBOP);
        assertEquals(3, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, magazineBOP));
        // 4
        library.checkoutEntity(lecturerOK, bookLOTR);
        assertEquals(4, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, bookLOTR));
        // 5
        library.checkoutEntity(lecturerOK, bookHP);
        assertEquals(5, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, bookHP));
        // 6
        library.checkoutEntity(lecturerOK, bookTM);
        assertEquals(6, library.getEntitiesOfAccount(lecturerOK).size());
        assertTrue(library.hasAccountLentEntity(lecturerOK, bookTM));
        // 7
        library.checkoutEntity(lecturerOK, textbookDSP);
        assertEquals(6, library.getEntitiesOfAccount(lecturerOK).size()); // Lecturers limited to 6.
        assertFalse(library.hasAccountLentEntity(lecturerOK, textbookDSP));
    }
}

