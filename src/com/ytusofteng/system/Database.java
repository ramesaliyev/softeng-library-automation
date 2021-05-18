package com.ytusofteng.system;

import com.ytusofteng.model.entities.*;
import com.ytusofteng.model.accounts.*;

import java.util.ArrayList;

public class Database {
    private ArrayList<Entity> entities;
    private ArrayList<Account> accounts;

    public Database() {
        this.entities = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
}
