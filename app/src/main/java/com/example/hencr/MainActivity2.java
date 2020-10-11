package com.example.hencr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {


    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "datbase";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;


    private static final String FILENAME =  "encryption key location";
    static pailier paillier;
    static aesfor aesen;
    static file fl;
    static BigInteger r;

    EditText e1,e2,e3,e4;
    Button button;
    ImageButton history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("Status", "SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Status", "ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Status", "FAILURE");
        }

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);

        button = findViewById(R.id.button);

        history = findViewById(R.id.history);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                insertdata(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString());
            }
        });

        fl = new file();

        paillier = new pailier();
        if(fl.check(FILENAME)){
            BigInteger[] rValue = fl.getKey(FILENAME);
            paillier.KeyGeneration(512, 62,rValue[0],rValue[1]);
            r = rValue[2];
        }
        else {
            r = new BigInteger(512, new Random());
            paillier.KeyGeneration(512, 62);
            BigInteger[] temp;
            temp = paillier.getPQ();
            temp[2] = new BigInteger(""+r);
            temp[3] = new BigInteger(""+100 +  (int)(Math.random()*(10000-100+1)));
            fl.fileWrite(temp,FILENAME);
        }
        aesen=new aesfor();

    }

    public void insertdata(String a, String b, String c, String d){
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+":1433/"+database,username,password);
            Statement st=con.createStatement();
            BigInteger a1=new BigInteger(a);
            BigInteger ena=paillier.EncrypStr(a,r);
            BigInteger enb=paillier.EncrypStr(b,r);
            BigInteger c1=new BigInteger(c);
            BigInteger enc=paillier.EncrypStr(c,r);
            String end=aesen.encrypt(d);

            st.executeUpdate("INSERT INTO "+ d +" VALUES('"+ ena +"','"+ enb +"','"+ enc +"')");
            Log.d("Message", ena+" "+enb+" "+enc );

            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(ena))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enb))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enc))));

/*
            st.executeQuery("SELECT * FROM "+"ena"+"");
*/

            e1.setText("");
            e2.setText("");
            e3.setText("");

            con.close();

        }catch(Exception e)
        {
            System.out.println("Error in connection" + e);
        }
    }

}