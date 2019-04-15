package com.mobile.Smf.database;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.Smf.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase extends AppCompatActivity {

    TextView txtData;
    Button btnFetch;

    private static final String DB = "Mobile";
    private static final String user = "thkh_mobile1";
    private static final String pass = "Thd14061985";
    private static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (TextView) this.findViewById(R.id.txtData);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // TODO Auto-generated method stub
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");
            }
        });
    }


    private class ConnectMySql extends AsyncTask<String, Void, String> {

        public static final String SMF_TABLE_NAME = "User";
        public static final String SMF_COLUMN_ID = "UserId";
        public static final String SMF_COLUMN_NAME = "Name";
        public static final String SMF_COLUMN_AGE = "Age";
        private String res = "";


        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DB_URL, user, pass);
                System.out.println("Databaseection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();

                st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", SMF_TABLE_NAME));

                st.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s (%s int NOT NULL AUTO_INCREMENT primary key, %s VARCHAR(100), %s int NOT NULL)",
                        SMF_TABLE_NAME, SMF_COLUMN_ID,SMF_COLUMN_NAME, SMF_COLUMN_AGE));


                ResultSet rs =  st.executeQuery(String.format("SELECT %s, %s, %s FROM %s WHERE %s = 1;",
                        SMF_COLUMN_ID, SMF_COLUMN_NAME, SMF_COLUMN_AGE,SMF_TABLE_NAME,SMF_COLUMN_ID));

                if(!rs.next())
                    if(!fillDB(st))
                        throw new RuntimeException("DB not filled");

                //ResultSetMetaData rsmd = rs.getMetaData();

                ResultSet rs2 = st.executeQuery(String.format("SELECT * FROM %s", SMF_TABLE_NAME));

                while (rs2.next()) {

                    result += "ID: "+ rs2.getInt(1) + " Name: " + rs2.getString(2) + " Age: " + rs2.getInt(3) + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            txtData.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(DataBase.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();
        }

        private boolean fillDB(Statement st) {
            try {
            st.executeUpdate(String.format("INSERT INTO %s (%s, %s) VALUES ('ZanderBOIII', 82);", SMF_TABLE_NAME, SMF_COLUMN_NAME, SMF_COLUMN_AGE));
            st.executeUpdate(String.format("INSERT INTO %s (%s, %s) VALUES ('TudorBOIII', 65);", SMF_TABLE_NAME, SMF_COLUMN_NAME, SMF_COLUMN_AGE));
            } catch (Exception e) {return false;}
            return true;
        }

    }

}
