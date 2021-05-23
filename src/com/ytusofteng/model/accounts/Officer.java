package com.ytusofteng.model.accounts;

public class Officer extends Account {
    private String title;

    public Officer(int id, int balance, String name, String surname, String title, String email) {
        super(id, balance, name, surname, email);
        this.title = title;
    }
}
