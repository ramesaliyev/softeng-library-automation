package com.ytusofteng.test;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestRames extends TestBase {
    @Test
    public void testCheckoutWithPastDueCheckoutsAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity when there is past due entities in account as student.");

        Date date2MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(2).toInstant());

        assertFalse(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));
        library.checkoutEntity(studentRA, magazineBOP, date2MonthsAgo); // Past due.
        library.checkoutEntity(studentRA, magazineGJ);
        assertTrue(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ)); // Cannot checkout.
    }

    @Test
    public void testCheckoutWithPastDueCheckoutsAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity when there is past due entities in account as lecturer.");

        Date date2MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(2).toInstant());
        Date date7MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(7).toInstant());

        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookLA));
        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ));
        library.checkoutEntity(lecturerYES, textbookLA, date2MonthsAgo);
        library.checkoutEntity(lecturerYES, textbookML, date7MonthsAgo); // Past due.
        library.checkoutEntity(lecturerYES, magazineGJ);
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookLA));
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ)); // Cannot checkout.
    }

    @Test
    public void testReturnPastDueEntityAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to return past due entity as student.");

        Date date2MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(2).toInstant());
        long studentInitialBalance = studentRA.getBalance(); // Student has 100 initial balance.

        library.checkoutEntity(studentRA, magazineBOP, date2MonthsAgo); // Past due.
        assertEquals(studentInitialBalance, studentRA.getBalance());
        assertTrue(library.hasAccountLentEntity(studentRA, magazineBOP));
        library.returnEntity(studentRA, magazineBOP);
        assertFalse(library.hasAccountLentEntity(studentRA, magazineBOP));
        assertTrue(studentRA.getBalance() < studentInitialBalance); // Fine applied.
    }

    @Test
    public void testReturnPastDueEntityAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to return past due entity as lecturer.");

        Date date7MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(7).toInstant());
        long lecturerInitialBalance = lecturerOK.getBalance(); // Lecturer has 100 initial balance.

        library.checkoutEntity(lecturerYES, textbookML, date7MonthsAgo); // Past due.
        assertEquals(lecturerInitialBalance, lecturerYES.getBalance());
        assertTrue(library.hasAccountLentEntity(lecturerYES, textbookML));
        library.returnEntity(lecturerYES, textbookML);
        assertFalse(library.hasAccountLentEntity(lecturerYES, textbookML));
        assertTrue(lecturerYES.getBalance() < lecturerInitialBalance); // Fine applied.
    }

    @Test
    public void testCheckoutWithNoBalanceAsStudent() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity as when has no balance as student.");

        Date date2MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(2).toInstant());

        library.checkoutEntity(studentRA, magazineBOP, date2MonthsAgo); // Past due.
        library.returnEntity(studentRA, magazineBOP);
        assertTrue(studentRA.getBalance() < 0); // Fine applied left no positive balance.

        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ));
        library.checkoutEntity(studentRA, magazineGJ);
        assertFalse(library.hasAccountLentEntity(studentRA, magazineGJ)); // Cannot checkout due to negative balance.
    }

    @Test
    public void testCheckoutWithNoBalanceAsLecturer() {
        System.out.println();
        System.out.println("TEST: Try to checkout an entity as when has no balance as lecturer.");

        Date date7MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(7).toInstant());

        library.checkoutEntity(lecturerYES, magazineBOP, date7MonthsAgo); // Past due.
        library.returnEntity(lecturerYES, magazineBOP);
        assertTrue(lecturerYES.getBalance() < 0); // Fines were applied left no positive balance.

        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ));
        library.checkoutEntity(lecturerYES, magazineGJ);
        assertFalse(library.hasAccountLentEntity(lecturerYES, magazineGJ)); // Cannot checkout due to negative balance.
    }

    @Test
    public void testCheckoutReservedOutOfStockEntityAccountThatReservedIt() {
        System.out.println();
        System.out.println("TEST: Try to checkout out of stock entity with the account that reserved it.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        library.reserveEntity(studentUAI, bookTM); // Ok

        assertFalse(library.hasAccountLentEntity(studentUAI, bookTM));
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.checkoutEntity(studentUAI, bookTM); // Should NOT be able to checkout.
        assertFalse(library.hasAccountLentEntity(studentUAI, bookTM)); // Did NOT checkout.
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
    }

    @Test
    public void testCheckoutReservedOutOfStockEntityAnotherAccount() {
        System.out.println();
        System.out.println("TEST: Try to checkout out of stock entity with another account.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        library.reserveEntity(studentUAI, bookTM); // Ok

        assertFalse(library.hasAccountLentEntity(studentMB, bookTM));
        assertFalse(library.hasAccountReservedEntity(studentMB, bookTM));
        library.checkoutEntity(studentMB, bookTM); // Should NOT be able to checkout.
        assertFalse(library.hasAccountLentEntity(studentMB, bookTM)); // Did NOT checkout.
        assertFalse(library.hasAccountReservedEntity(studentMB, bookTM));
    }
}
