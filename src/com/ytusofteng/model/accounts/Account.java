package com.ytusofteng.model.accounts;

public class Account {
    private int id;
    private int balance;
    public String name;
    public String surname;

    public Account(int id, int balance, String name, String surname) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }
}
