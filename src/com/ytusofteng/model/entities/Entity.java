package com.ytusofteng.model.entities;

import java.util.Date;

public class Entity {
    private int id;
    private int issueCount;
    private String name;
    private Date createdAt;

    public Entity(int id, int issueCount, String name, Date createdAt) {
        this.id = id;
        this.issueCount = issueCount;
        this.name = name;
        this.createdAt = createdAt;
    }
}
