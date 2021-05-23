package com.ytusofteng.system;

import com.ytusofteng.model.entities.*;
import com.ytusofteng.model.accounts.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class ReservedEntity {
    public Entity entity;
    public Account account;
    public Date reservationDate;

    public ReservedEntity(Account account, Entity entity, Date reservationDate) {
        this.account = account;
        this.entity = entity;
        this.reservationDate = reservationDate;
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
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final HashMap<Account, HashMap<Entity, LentEntity>> lentEntities = new HashMap<>();
    private final ArrayList<ReservedEntity> reservedEntities = new ArrayList<>();

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

    public ArrayList<LentEntity> getLentEntitiesOfAccount(Account account) {
        return new ArrayList<>(this.getAccountEntityMap(account).values());
    }

    public ArrayList<ReservedEntity> getReservedEntitiesOfAccount(Account account) {
        return (ArrayList<ReservedEntity>) this.reservedEntities.stream()
            .filter(e -> e.account == account)
            .collect(Collectors.toList());
    }

    public LentEntity getAccountLentEntity(Account account, Entity entity) {
        return this.getAccountEntityMap(account).get(entity);
    }

    public boolean hasAccountLentEntity(Account account, Entity entity) {
        ArrayList<Entity> accountEntities = this.getEntitiesOfAccount(account);
        return this.getEntityById(entity.getId(), accountEntities) != null;
    }

    public ReservedEntity getAccountReservedEntity(Account account, Entity entity) {
        ArrayList<ReservedEntity> reservedEntities = this.reservedEntities.stream()
            .filter(e -> e.account == account && e.entity == entity)
            .collect(Collectors.toCollection(ArrayList::new));

        return reservedEntities.size() > 0 ? reservedEntities.get(0) : null;
    }

    public boolean hasAccountReservedEntity(Account account, Entity entity) {
        return this.getAccountReservedEntity(account, entity) != null;
    }

    public ArrayList<ReservedEntity> getReservationsByEntity(Entity entity) {
        return this.reservedEntities.stream()
            .filter(e -> e.entity == entity)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void lendEntity(Account account, Entity entity, Date lendDate) {
        if (!this.hasAccountLentEntity(account, entity)) {
            this.getAccountEntityMap(account).put(entity, new LentEntity(entity, lendDate));
        }
    }

    public void returnEntity(Account account, Entity entity) {
        if (this.hasAccountLentEntity(account, entity)) {
            this.getAccountEntityMap(account).remove(entity);
        }
    }

    public void reserveEntity(Account account, Entity entity, Date reservationDate) {
        if (!this.hasAccountReservedEntity(account, entity)) {
            this.reservedEntities.add(new ReservedEntity(account, entity, reservationDate));
        }
    }

    public void removeEntityReservation(Account account, Entity entity) {
        this.reservedEntities.remove(this.getAccountReservedEntity(account, entity));
    }
}
