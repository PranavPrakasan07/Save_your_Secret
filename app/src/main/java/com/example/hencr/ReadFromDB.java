package com.example.hencr;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ReadFromDB extends AppCompatActivity {

    private TextView textView;
    Button button;

    TextView e1,e2,e3;
    TextView e11,e22,e33;

    RadioGroup radioGroup;

    private static final String FILENAME =  "encryption key location";
    static pailier paillier;
    static aesfor aesen;
    static file fl;
    static BigInteger r;


    private static String ip = "192.168.43.205";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "datbase";
    private static String username = "test";
    private static String password = "test";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;

    BigInteger enc_username = Login.encrypted_email;
    BigInteger enc_password = Login.encrypted_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.backButton);

        assert enc_username != null;
        Log.d("username", String.valueOf(enc_username));
        assert enc_password != null;
        Log.d("password", String.valueOf(enc_password));

        radioGroup = findViewById(R.id.radioGroup);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("status", "SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Status", "ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("Status", "FAILURE");
        }

        e1 = findViewById(R.id.textView3);
        e2 = findViewById(R.id.textView4);
        e3 = findViewById(R.id.textView5);

        e11 = findViewById(R.id.textView33);
        e22 = findViewById(R.id.textView44);
        e33 = findViewById(R.id.textView55);

        final String[] choice = new String[1];

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton :
                        Toast.makeText(ReadFromDB.this, "ID Details", Toast.LENGTH_SHORT).show();
                        e1.setText("Aadhaar Number");
                        e2.setText("Ratio Number");
                        e3.setText("Licence Number");
                        choice[0] = "iddetails";
                        displayData(choice[0]);
                        break;

                    case R.id.radioButton2 :
                        Toast.makeText(ReadFromDB.this, "Bank Details", Toast.LENGTH_SHORT).show();
                        e1.setText("Card Number");
                        e2.setText("CVV Number");
                        e3.setText("Pin Number");
                        choice[0] = "bank";
                        displayData(choice[0]);
                        break;


                    case R.id.radioButton3 :
                        Toast.makeText(ReadFromDB.this, "Contact Details", Toast.LENGTH_SHORT).show();
                        e1.setText("Contact Number");
                        e2.setText("WhatsApp Contact");
                        e3.setText("Pin Code");
                        choice[0] = "contact";
                        displayData(choice[0]);
                        break;
                }
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
//            }
//        });
        
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

    public void displayData(String tableName){
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+":1433/"+database,username,password);

//            String end=aesen.encrypt(tableName);
//
//            ResultSet resultSet = st.executeQuery("SELECT * FROM " + tableName + " WHERE username = '"+ enc_username +"' AND password_hash = '"+ enc_password +"';");
//            resultSet.next();
//
//            Log.d("Message",(resultSet.getString(1).trim().replaceAll("\"", "")));
//            Log.d("Message", (resultSet.getString(2)));
//            Log.d("Message", (resultSet.getString(3)));
//            Log.d("Message", (resultSet.getString(4)));
//            Log.d("Message",(resultSet.getString(5)));
//
//            String value1 = paillier.DecrpyStr(new BigInteger((resultSet.getString(3)).trim().replaceAll("\"", "")));
//            String value2 = paillier.DecrpyStr(new BigInteger((resultSet.getString(4))));
//            String value3 = paillier.DecrpyStr(new BigInteger((resultSet.getString(5))));
//
//            Log.d("Message", paillier.DecrpyStr(new BigInteger((resultSet.getString(1)))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger((resultSet.getString(2)))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger((resultSet.getString(3)))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger((resultSet.getString(4)))));
//            Log.d("Message", paillier.DecrpyStr(new BigInteger((resultSet.getString(5)))));
//
//            e11.setText(value1);
//            e22.setText(value2);
//            e33.setText(value3);
//
//            con.close();
//
//        }catch(Exception e)
//        {
//            Log.d("Error in connection" , String.valueOf(e));
//        }


            Statement st=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String enh=aesen.encrypt(tableName);
            st.executeQuery("SELECT * FROM ["+enh+"]");
            Log.d("Tablename", enh);

            ResultSet rs = st.getResultSet();
            Log.d("Message", paillier.DecrpyStr(new BigInteger((rs.getString(1)))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger((rs.getString(2)))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger((rs.getString(3)))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger((rs.getString(4)))));
            Log.d("Message", paillier.DecrpyStr(new BigInteger((rs.getString(5)))));

            con.close();

        }catch(Exception e)
        {
            System.out.println("Error in connection" + e);
        }

    }

}
