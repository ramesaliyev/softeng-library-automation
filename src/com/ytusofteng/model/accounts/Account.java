package com.ytusofteng.model.accounts;

public class Account {
    private int id;
    private long balance;
    public String name;
    public String surname;
    public String email;

    public Account(int id, int balance, String name, String surname, String email) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }
}
