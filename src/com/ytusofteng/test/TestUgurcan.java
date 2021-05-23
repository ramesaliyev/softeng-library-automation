package com.ytusofteng.test;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestUgurcan extends TestBase {
    @Test
    public void testReserveOutOfStockEntity() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
    }

    @Test
    public void testReserveEntityThatHasStock() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity that has stock.");

        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
    }

    @Test
    public void testReserveEntityTwiceWithSameAccount() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity that already has been reserved by same account.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.

        // Reserve
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertTrue(library.hasAccountReservedEntity(studentUAI, bookTM));
        assertEquals(1, library.getReservedEntitiesOfAccount(studentUAI).size());

        // Reserve again?
        library.reserveEntity(studentUAI, bookTM);
        assertEquals(1, library.getReservedEntitiesOfAccount(studentUAI).size()); // Wont reserves twice.
    }

    @Test
    public void testReserveEntityWithNegativeBalanceAccount() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity with account that has negative balance.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.
        studentUAI.setBalance(-10); // Account that has negative balance.

        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM)); // Cannot reserve.
    }

    @Test
    public void testReserveEntityWithAccountThatHasPastDueEntities() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity with account that has past due entities.");


        Date date2MonthsAgo = Date.from(ZonedDateTime.now().minusMonths(2).toInstant());
        library.checkoutEntity(studentUAI, magazineBOP, date2MonthsAgo); // Past due.

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.

        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM)); // Cannot reserve.
    }

    @Test
    public void testReserveEntityWithAccountThatHasReachedMaxEntityLentLimit() {
        System.out.println();
        System.out.println("TEST: Try to reserve an entity with account that has reached entity checkout limit.");

        library.checkoutEntity(studentRA, bookTM); // bookTM has only 1 stock.

        library.checkoutEntity(studentUAI, magazineGJ);
        library.checkoutEntity(studentUAI, magazineBOP);
        library.checkoutEntity(studentUAI, magazineSP); // Limit reached (3 for students).

        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM));
        library.reserveEntity(studentUAI, bookTM);
        assertFalse(library.hasAccountReservedEntity(studentUAI, bookTM)); // Cannot reserve.
    }
}
