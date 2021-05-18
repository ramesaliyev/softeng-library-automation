package com.ytusofteng.system;

import com.ytusofteng.model.accounts.Account;
import com.ytusofteng.model.entities.Entity;

import java.util.ArrayList;
import java.util.Date;

public class Library {
    private final Database db;
    private final Config config;

    public Library() {
        this.db = new Database();
        this.config = new Config();
    }

    public void addEntity(Entity entity) {
        this.db.addEntity(entity);
    }

    public void addAccount(Account account) {
        this.db.addAccount(account);
    }

    public void setLendingCountLimit(Class<? extends Account> accountClass, int value) {
        this.config.setLendingCountLimit(accountClass, value);
    }

    public void setLendingCountDuration(Class<? extends Account> accountClass, int value) {
        this.config.setLendingCountDuration(accountClass, value);
    }

    public void checkoutEntity(Account account, Entity entity, Date lendDate) {
        if (this.hasAccountLentEntity(account, entity)) {
            System.out.println("Fail: Account#"+ account.getId() + " already have Entity#"+ entity.getId() +".");
            return;
        }

        if (!entity.hasStock()) {
            System.out.println("Fail: Entity has no issue left in stock!");
            return;
        }

        this.db.lendEntity(account, entity, lendDate);
        entity.decreaseStock();
        System.out.println("Success: Entity#"+ entity.getId() +" lent successfully to Account#"+ account.getId() + ".");
    }

    public void checkoutEntity(Account account, Entity entity) {
        this.checkoutEntity(account, entity, new Date());
    }

    public void returnEntity(Account account, Entity entity) {
        if (!this.hasAccountLentEntity(account, entity)) {
            System.out.println("Fail: Account#"+ account.getId() + " does not have given entity.");
            return;
        }

        this.db.returnEntity(account, entity);
        entity.increaseStock();
        System.out.println("Success: Entity#"+ entity.getId() +" successfully returned back by Account#"+ account.getId() + ".");
    }

    public void reserveEntity() {

    }

    public boolean hasAccountLentEntity(Account account, Entity entity) {
        return this.db.hasAccountLentEntity(account, entity);
    }

    public Entity getEntityById(int id) {
        return this.db.getEntityById(id);
    }

    public Account getAccountById(int id) {
        return this.db.getAccountById(id);
    }

    public ArrayList<Entity> getEntitiesOfAccount(Account account) {
        return this.db.getEntitiesOfAccount(account);
    }
}
