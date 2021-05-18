package com.ytusofteng.model.entities;

import java.util.Date;

public class Magazine extends Entity {
    private Date issueDate;

    public Magazine(int id, int issueCount, String name, Date createdAt, Date issueDate) {
        super(id, issueCount, name, createdAt);
        this.issueDate = issueDate;
    }
}
