package com.ytusofteng.tests;

import com.ytusofteng.system.Library;

public class LendingTest {
    public LendingTest() {
        // Create new library.
        Library library = new Library();

        // Fill library with test data.
        TestHelper.createInitialTestData(library);
    }
}
