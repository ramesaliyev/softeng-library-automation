package com.ytusofteng.system;

import com.ytusofteng.model.entities.Entity;

public class Library {
    private Database db;
    private Config config;

    public Library() {
        this.db = new Database();
        this.config = new Config();
    }

    public void addEntity(Entity entity) {
        this.db.addEntity(entity);
    }

    public void removeEntity() {

    }

    public void addAccount() {

    }

    public void removeAccount() {

    }

    public void setConfig() {

    }

    public void getConfig() {

    }
}
