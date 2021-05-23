package com.ytusofteng.system;

import com.ytusofteng.model.accounts.*;
import com.ytusofteng.model.entities.*;
import com.ytusofteng.model.enums.BookType;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private final Database db = new Database();
    private final Config config = new Config();

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

        if (!this.hasAccountReservedEntity(account, entity) && !entity.hasStock()) {
            System.out.println("Fail: Entity has no issue left in stock (all lent or reserved)!");
            return;
        }

        if (account.getBalance() <= 0) {
            System.out.println("Fail: Account has insufficient balance!");
            return;
        }

        if (this.getEntitiesOfAccount(account).size() >= this.config.getLendingCountLimit(account.getClass())) {
            System.out.println("Fail: Account reached to maximum lend limit!");
            return;
        }

        if (this.hasAccountPastDueEntities(account)) {
            System.out.println("Fail: Account has past due entities!");
            return;
        }

        if (entity.getClass() == Book.class) {
            Book book = (Book) entity;
            if (book.getType() == BookType.TEXTBOOK && account.getClass() != Lecturer.class) {
                System.out.println("Fail: Only lecturers can checkout books in Textbook type!");
                return;
            }
        }

        this.db.lendEntity(account, entity, lendDate);
        entity.decreaseStock();

        if (this.hasAccountReservedEntity(account, entity)) {
            entity.decreaseReservationCount();
            this.db.removeEntityReservation(account, entity);
        }

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

        LentEntity lentEntity = this.db.getAccountLentEntity(account, entity);
        long lendDurationInDays = Duration.between(lentEntity.lendDate.toInstant(), ZonedDateTime.now()).toDays();
        int lendingDurationLimitInDays = this.config.getLendingDurationLimit(account.getClass());

        if (lendDurationInDays > lendingDurationLimitInDays) {
            long pastDueFine = (lendDurationInDays - lendingDurationLimitInDays);
            account.setBalance(account.getBalance() - pastDueFine);
        }

        if (this.hasAccountReservedEntity(account, entity)) {
            this.db.removeEntityReservation(account, entity);
        }

        this.db.returnEntity(account, entity);
        entity.increaseStock();

        System.out.println("Success: Entity#"+ entity.getId() +" successfully returned back by Account#"+ account.getId() + ".");
    }

    public void reserveEntity(Account account, Entity entity, Date reservationDate) {
        if (entity.hasStock()) {
            System.out.println("Fail: Entity has issue in stock!");
            return;
        }

        if (this.hasAccountReservedEntity(account, entity)) {
            System.out.println("Fail: This account has already reserved this entity!");
            return;
        }

        if (this.db.getReservationsByEntity(entity).size() >= entity.getIssueCount()) {
            System.out.println("Fail: Reservations reached to issue count. There is no more issue to reserve!");
            return;
        }

        this.db.reserveEntity(account, entity, reservationDate);
        entity.increaseReservationCount();
    }

    public void reserveEntity(Account account, Entity entity) {
        this.reserveEntity(account, entity, new Date());
    }

    public boolean hasAccountPastDueEntities(Account account) {
        ArrayList<LentEntity> accountLentEntities = this.db.getLentEntitiesOfAccount(account);
        int lendingDurationLimitInDays = this.config.getLendingDurationLimit(account.getClass());
        Instant oldestValidCheckoutDate = ZonedDateTime.now().minusDays(lendingDurationLimitInDays).toInstant();

        List<LentEntity> searchResult = accountLentEntities.stream()
            .filter(le -> le.lendDate.toInstant().isBefore(oldestValidCheckoutDate))
            .collect(Collectors.toList());

        return searchResult.size() > 0;
    }

    public boolean hasAccountLentEntity(Account account, Entity entity) {
        return this.db.hasAccountLentEntity(account, entity);
    }

    public boolean hasAccountReservedEntity(Account account, Entity entity) {
        return this.db.hasAccountReservedEntity(account, entity);
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

    public ArrayList<ReservedEntity> getReservedEntitiesOfAccount(Account account) {
        return this.db.getReservedEntitiesOfAccount(account);
    }

    public Config getConfig() {
        return config;
    }
}
