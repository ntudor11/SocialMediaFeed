package com.mobile.Smf.database;

import java.util.concurrent.Callable;

public class TransactionInsertMySql implements Callable<Boolean> {

    private String DB_URL, user, pass;
    private String[] instructions;

    public TransactionInsertMySql(String ... params) {
        this.DB_URL = params[0];
        this.user = params[1];
        this.pass = params[2];
        instructions = new String[params.length-3];
        for(int i = 3; i < params.length-3; i++ ) {
            instructions[i-3] = params[i];
        }
    }

    public Boolean call() {
        return true; //implement
    }
}
