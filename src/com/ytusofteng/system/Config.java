package com.ytusofteng.system;

import com.ytusofteng.model.accounts.Account;

import java.util.HashMap;

public class Config {
    private final HashMap<Class<? extends Account>, Integer> lendingCountLimit;
    private final HashMap<Class<? extends Account>, Integer> lendingDurationLimit;

    public Config() {
        this.lendingCountLimit = new HashMap<>();
        this.lendingDurationLimit = new HashMap<>();
    }

    public void setLendingCountLimit(Class<? extends Account> accountClass, int limit) {
        this.lendingCountLimit.put(accountClass, limit);
    }

    public void setLendingCountDuration(Class<? extends Account> accountClass, int limit) {
        this.lendingDurationLimit.put(accountClass, limit);
    }

    public int getLendingCountLimit(Class<? extends Account> accountClass) {
        return lendingCountLimit.get(accountClass);
    }

    public int getLendingDurationLimit(Class<? extends Account> accountClass) {
        return lendingDurationLimit.get(accountClass);
    }
}
