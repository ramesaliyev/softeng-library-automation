package com.ytusofteng.model.accounts;

public class Lecturer extends Account {
    private String title;
    public Lecturer(int id, int balance, String name, String surname, String title) {
        super(id, balance, name, surname);
        this.title = title;
    }
}
