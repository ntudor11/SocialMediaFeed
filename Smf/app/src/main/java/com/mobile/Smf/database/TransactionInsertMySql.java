package com.mobile.Smf.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class TransactionInsertMySql implements Callable<Boolean> {

    private Connection con;
    private PreparedStatement[] input;
    private String[] params;
    private byte[] pic;

   /*
   * MAKE THIS NICE!!!!
   * */

    public TransactionInsertMySql(byte[] pic,String ... params) {
        this.pic = pic;
        this.params = params;

    }


    public Boolean call() {

        Savepoint save1 = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(params[0], params[1], params[2]);

            input = new PreparedStatement[1];
            input[0] = con.prepareStatement(params[3]);
            if(pic != null) {
                input[0].setBytes(1, pic);
            }


            con.setAutoCommit(false);

            save1 = con.setSavepoint("save1");

            for (int i = 0; i < input.length; i++) {
                input[i].execute();
            }

            con.commit();

            //Log.d("TransactionInsertMySql", "Transation committed");

            return true;

            } catch (ClassNotFoundException exce) { exce.printStackTrace(); }
              catch (SQLException ex) { ex.printStackTrace();
                    try {
                        con.rollback(save1);
                        return false;
                        } catch(SQLException e){
                            throw new RuntimeException("Failed to rollback changes " +
                                "in TransactionInsertMySql with arguments:"+ Arrays.toString(input)+ "\n");
                            }
                    }

        finally {
                try {
                    for (int i = 0; i < input.length; i++) {
                        if (input[i] != null)
                                input[i].close();
                    }

                    if(con != null)
                        con.close();

                } catch (SQLException e) {e.printStackTrace();}
        }
        return true;
    }
}
