package com.ytusofteng.model.entities;

import java.util.Date;

public class Entity {
    private int id;
    private int issueCount;
    private String name;
    private Date createdAt;
    private int inStockCount;
    private int inReservationCount;

    public Entity(int id, int issueCount, String name, Date createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.issueCount = issueCount;
        this.inStockCount = issueCount;
        this.inReservationCount = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public int getInStockCount() {
        return this.inStockCount;
    }

    public boolean hasStock() {
        return (this.inStockCount - this.inReservationCount) > 0;
    }

    public void increaseStock() {
        this.inStockCount++;
    }

    public void decreaseStock() {
        this.inStockCount--;
    }

    public void increaseReservationCount() {
        this.inReservationCount++;
    }

    public void decreaseReservationCount() {
        this.inReservationCount--;
    }
}
