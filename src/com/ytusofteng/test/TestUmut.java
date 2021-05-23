package com.ytusofteng.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestUmut extends TestBase {
    @Test
    public void testReserveEntityMoreThanIssueCount() {
        System.out.println();
        System.out.println("TEST: Try to reserved entity which already reserved issue count times.");

        library.checkoutEntity(studentUAI, bookHP); // bookHP has only 2 stock.
        library.checkoutEntity(studentRA, bookHP); // Out of stock.

        assertFalse(library.hasAccountReservedEntity(officerST, bookHP));
        assertFalse(library.hasAccountReservedEntity(studentMB, bookHP));
        assertFalse(library.hasAccountReservedEntity(studentUY, bookHP));

        library.reserveEntity(officerST, bookHP); // Ok
        library.reserveEntity(studentMB, bookHP); // Ok
        library.reserveEntity(studentUY, bookHP); // Wont reserve.

        assertTrue(library.hasAccountReservedEntity(officerST, bookHP));
        assertTrue(library.hasAccountReservedEntity(studentMB, bookHP));
        assertFalse(library.hasAccountReservedEntity(studentUY, bookHP)); // Did not reserve.
    }

    @Test
    public void testCheckoutReservedEntityAccountThatReservedIt() {
        System.out.println();
        System.out.println("TEST: Try to checkout returned entity that is out of stock with the account that reserved it.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        library.reserveEntity(studentUAI, bookTM); // Ok

        assertFalse(library.hasAccountLentEntity(studentUAI, bookTM));
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.checkoutEntity(studentUAI, bookTM); // Should be able to checkout.
        assertTrue(library.hasAccountLentEntity(studentUAI, bookTM)); // Did checkout.
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
    }

    @Test
    public void testCheckoutReservedEntityWithAnotherAccount() {
        System.out.println();
        System.out.println("TEST: Try to checkout returned entity that is out of stock with an account other than the account that reserved it.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        library.reserveEntity(studentUAI, bookTM); // Ok

        assertFalse(library.hasAccountLentEntity(studentUAI, bookTM));
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.checkoutEntity(studentMEG, bookTM); // Should NOT be able to checkout.
        assertFalse(library.hasAccountLentEntity(studentMEG, bookTM)); // Could not checkout.
    }

    @Test
    public void testCheckoutEntityThatHaveReservationsButAlsoStock() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity that has reservations but also in stock.");

        library.checkoutEntity(studentUAI, bookLOTR); // bookLOT has 3 stock.
        library.checkoutEntity(studentMEG, bookLOTR);
        library.checkoutEntity(studentUY, bookLOTR); // No stock left.

        // Get reservations.
        library.reserveEntity(studentRA, bookLOTR);
        library.reserveEntity(studentMB, bookLOTR);

        // Accounts that did not reserve cannot checkout yet.
        assertFalse(library.hasAccountLentEntity(lecturerOK, bookLOTR));
        library.checkoutEntity(lecturerOK, bookLOTR);
        assertFalse(library.hasAccountLentEntity(lecturerOK, bookLOTR));

        // Return entities.
        library.returnEntity(studentUAI, bookLOTR);
        library.returnEntity(studentMEG, bookLOTR);

        // Still cant checkout because 2 issues were reserved.
        assertFalse(library.hasAccountLentEntity(lecturerOK, bookLOTR));
        library.checkoutEntity(lecturerOK, bookLOTR);
        assertFalse(library.hasAccountLentEntity(lecturerOK, bookLOTR));

        // Return last copy.
        library.returnEntity(studentUY, bookLOTR);

        /// Now can reserve since there is non-reserved issue in stock.
        assertFalse(library.hasAccountLentEntity(lecturerOK, bookLOTR));
        library.checkoutEntity(lecturerOK, bookLOTR);
        assertTrue(library.hasAccountLentEntity(lecturerOK, bookLOTR));
    }

    @Test
    public void testReserveTextbookAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to reserve a textbook as lecturer.");

        library.checkoutEntity(lecturerOK, textbookML); // No stock left.

        assertFalse(library.hasAccountReservedEntity(lecturerOK, textbookML));
        library.reserveEntity(lecturerOK, textbookML);
        assertTrue(library.hasAccountReservedEntity(lecturerOK, textbookML)); // Lecturer can reserve textbooks.
    }

    @Test
    public void testReserveTextbookAsOfficer() {
        System.out.println();
        System.out.println("TEST: Try to reserve a textbook as lecturer.");

        library.checkoutEntity(lecturerOK, textbookML); // No stock left.

        assertFalse(library.hasAccountReservedEntity(officerST, textbookML));
        library.reserveEntity(officerST, textbookML);
        assertFalse(library.hasAccountReservedEntity(officerST, textbookML)); // Officer cannot reserve textbooks.
    }

    @Test
    public void testReserveTextbookAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to reserve a textbook as lecturer.");

        library.checkoutEntity(lecturerOK, textbookML); // No stock left.

        assertFalse(library.hasAccountReservedEntity(studentUY, textbookML));
        library.reserveEntity(studentUY, textbookML);
        assertFalse(library.hasAccountReservedEntity(studentUY, textbookML)); // Student cannot reserve textbooks.
    }
}
