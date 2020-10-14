package com.example.hencr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
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

    RadioGroup radioGroup;

    String username_user;
    String password_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        username_user = bundle.getString("email");
        password_user = bundle.getString("password");

        assert username != null;
        Log.d("Username", username);
        assert password != null;
        Log.d("Password", password);


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

        radioGroup = findViewById(R.id.radioGroup);

        button = findViewById(R.id.button);

        history = findViewById(R.id.history);

        final String[] choice = new String[1];

        final String[] e1text = new String[1];
        final String[] e2text = new String[1];
        final String[] e3text = new String[1];

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton :
                        Toast.makeText(MainActivity2.this, "Add your ID details", Toast.LENGTH_SHORT).show();
                        e1.setHint("Aadhaar Number");
                        e2.setHint("Ratio Number");
                        e3.setHint("Licence Number");
                        choice[0] = "IDDetails";
                        break;

                    case R.id.radioButton2 :
                        Toast.makeText(MainActivity2.this, "Add your Bank Details", Toast.LENGTH_SHORT).show();
                        e1.setHint("Card Number");
                        e2.setHint("CVV Number");
                        e3.setHint("Pin Number");
                        choice[0] = "bank";
                        break;


                    case R.id.radioButton3 :
                        Toast.makeText(MainActivity2.this, "Add your Contact number", Toast.LENGTH_SHORT).show();
                        e1.setHint("Contact Number");
                        e2.setHint("WhatsApp Contact");
                        e3.setHint("Pin Code");
                        choice[0] = "contact";
                        break;

                }
            }
        });

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

//                if(e1.getText().toString().isEmpty())
//                {
//                    e1text[0] = "0";
//                }
//                if(e2.getText().toString().isEmpty())
//                {
//                    e2text[0] = "0";
//                }
//                if(e3.getText().toString().isEmpty())
//                {
//                    e3text[0] = "0";
//                }

                Log.d("Message", choice[0]);
                insertdata(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), choice[0]);
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
            BigInteger ene=paillier.EncrypStr(username_user,r);
            BigInteger enf=paillier.EncrypStr(password_user,r);

            String end=aesen.encrypt(d);

            st.executeUpdate("UPDATE credential_table SET email = '" + ene + "', password_hash = '" + enf + "' WHERE email = '"+ username_user +"' AND password_hash = '"+ password_user +"';");
            st.executeUpdate("INSERT INTO "+ d +" VALUES('" + ene + "', '" + enf + "', '"+ ena +"','"+ enb +"','"+ enc +"')");
            Log.d("Message", ena+" "+enb+" "+enc );

            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(ena))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enb))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enc))));

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