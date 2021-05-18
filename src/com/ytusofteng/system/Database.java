package com.ytusofteng.system;

import com.ytusofteng.model.entities.*;
import com.ytusofteng.model.accounts.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class EntityAccountRelationship {
    public Entity entity;
    public Account account;

    public EntityAccountRelationship(Entity entity, Account account) {
        this.entity = entity;
        this.account = account;
    };
};

class LentEntity {
    public Entity entity;
    public Date lendDate;

    public LentEntity (Entity entity, Date date) {
        this.entity = entity;
        this.lendDate = date;
    }
}

public class Database {
    private final ArrayList<Entity> entities;
    private final ArrayList<Account> accounts;
    private final HashMap<Account, HashMap<Entity, LentEntity>> lentEntities;
    private final ArrayList<EntityAccountRelationship> reservedEntities;

    public Database() {
        this.entities = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.lentEntities = new HashMap<>();
        this.reservedEntities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public Entity getEntityById(int id, ArrayList<Entity> entities) {
        if (entities == null) {
            return null;
        }

        List<Entity> searchResult = entities.stream()
            .filter(e -> e.getId() == id)
            .collect(Collectors.toList());

        if (searchResult.size() == 0) {
            return null;
        }

        return searchResult.get(0);
    }

    public Entity getEntityById(int id) {
        return this.getEntityById(id, this.entities);
    }

    public Account getAccountById(int id, ArrayList<Account> accounts) {
        if (accounts == null) {
            return null;
        }

        List<Account> searchResult = accounts.stream()
            .filter(e -> e.getId() == id)
            .collect(Collectors.toList());

        if (searchResult.size() == 0) {
            return null;
        }

        return searchResult.get(0);
    }

    public Account getAccountById(int id) {
        return this.getAccountById(id, this.accounts);
    }

    private HashMap<Entity, LentEntity> getAccountEntityMap(Account account) {
        return this.lentEntities.computeIfAbsent(account, k -> new HashMap<>());
    }

    public ArrayList<Entity> getEntitiesOfAccount(Account account) {
        return new ArrayList<>(this.getAccountEntityMap(account).keySet());
    }

    public boolean hasAccountLentEntity(Account account, Entity entity) {
        ArrayList<Entity> accountEntities = this.getEntitiesOfAccount(account);
        return this.getEntityById(entity.getId(), accountEntities) != null;
    }

    public void lendEntity(Account account, Entity entity, Date lendDate) {
        this.getAccountEntityMap(account).put(entity, new LentEntity(entity, lendDate));
    }

    public void returnEntity(Account account, Entity entity) {
        if (!this.hasAccountLentEntity(account, entity)) {
            return;
        }

        this.getAccountEntityMap(account).remove(entity);
    }
}
