package com.example.hencr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Random;

public class WriteToDB extends AppCompatActivity {

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

    public static BigInteger enc_username_user = Login.encrypted_email;
    public static BigInteger enc_password_user = Login.encrypted_pw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
            Log.d("Status", "ERROR");
        } catch (SQLException e) {
            Log.d("Status", "FAILURE");
        }

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);

        db.collection("Credential").document(String.valueOf(enc_username_user)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("ThisistheValue", String.valueOf(enc_username_user));
                Log.d("ThisistheValue", String.valueOf(Objects.requireNonNull(documentSnapshot.get("password"))));

                try {
                    Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(Objects.requireNonNull(documentSnapshot.get("password"))))));
                    Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(Objects.requireNonNull(documentSnapshot.get("password"))))));
                    Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(Objects.requireNonNull(documentSnapshot.get("password"))))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

        button = findViewById(R.id.button);

        history = findViewById(R.id.history);

        final String[] choice = new String[1];

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton :
                        Toast.makeText(WriteToDB.this, "Add your ID Details", Toast.LENGTH_SHORT).show();
                        e1.setHint("Aadhaar Number");
                        e2.setHint("Ration Number");
                        e3.setHint("Licence Number");
                        choice[0] = "iddetails";
                        break;

                    case R.id.radioButton2 :
                        Toast.makeText(WriteToDB.this, "Add your Bank Details", Toast.LENGTH_SHORT).show();
                        e1.setHint("Card Number");
                        e2.setHint("CVV Number");
                        e3.setHint("Pin Number");
                        choice[0] = "bank";
                        break;


                    case R.id.radioButton3 :
                        Toast.makeText(WriteToDB.this, "Add your Contact Details", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), ReadFromDB.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
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

    public void insertdata(String a, String b, String c, String d) {
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+":1433/"+database,username,password);


//            Statement st=con.createStatement();
//            BigInteger a1=new BigInteger(a);
//            BigInteger ena=paillier.EncrypStr(a,r);
//            BigInteger enb=paillier.EncrypStr(b,r);
//            BigInteger c1=new BigInteger(c);
//            BigInteger enc=paillier.EncrypStr(c,r);
//
//            String end=aesen.encrypt(d);
//
//            st.executeUpdate("INSERT INTO "+ d +" VALUES('" + enc_username_user + "', '" + enc_password_user + "', '"+ ena +"','"+ enb +"','"+ enc +"')");
//            Log.d("Message", ena+" "+enb+" "+enc );
//
//            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(ena))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enb))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger(String.valueOf(enc))));
//
//            e1.setText("");
//            e2.setText("");
//            e3.setText("");
//
//            con.close();
//
//        }catch(Exception e)
//        {
//            Log.d("Error in connection" , String.valueOf(e));
//        }

            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            BigInteger ena = paillier.EncrypStr(a, r);
            BigInteger enb = paillier.EncrypStr(b, r);
            BigInteger enc = paillier.EncrypStr(c, r);

            String choice = null;

            if(d.equals("iddetails"))
            {
                choice = "An/NI+HyK1ZtS3gcUVfqqw==";
            }
            else if (d.equals("contact"))
            {
                choice = "Og4Dcf6Sq0uEGL3bBxLRog==";
            }
            else if (d.equals("bank"))
            {
                choice = "sDbW5jImEJiWJfxb+LWMww==";
            }

            st.executeUpdate("INSERT INTO [" + choice + "] VALUES('" + enc_username_user + "', '" + enc_password_user + "', '" + ena + "','" + enb + "','" + enc + "')");

            Log.d("Decr", paillier.DecrpyStr(new BigInteger(String.valueOf(ena))));
            Log.d("Decr", paillier.DecrpyStr(new BigInteger(String.valueOf(enb))));
            Log.d("Decr", paillier.DecrpyStr(new BigInteger(String.valueOf(enc))));

            con.close();

        } catch (Exception e) {
            Log.d("Error in connection" , String.valueOf(e));
        }
    }

}