package com.ytusofteng.tests;

import com.ytusofteng.model.entities.*;
import com.ytusofteng.system.Library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class TestHelper {
    public static void createInitialTestData(Library library) {
        Date aMonthAgo = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());

        library.addEntity(new Book(1, 3, "The Lord of the Rings, The Fellowship of the Ring\n", TestHelper.parseDate("24/07/1954"), "J. R. R. Tolkien"));
        library.addEntity(new Book(2, 3, "Harry Potter and the Philosopher's Stone", TestHelper.parseDate("26/06/1997"), "J. K. Rowling"));
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
