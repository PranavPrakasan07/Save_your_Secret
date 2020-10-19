package com.example.hencr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Login extends AppCompatActivity {

    EditText password_text, email_text, retype_password;
    Button login, signup, signup2;
    TextView message;
    ProgressBar progressBar;

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

    String email_field = "email";
    String pw_field = "password";

    BigInteger encrypted_email;
    BigInteger encrypted_pw;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            temp[2] = new BigInteger(""+ r);
            temp[3] = new BigInteger(""+100 +  (int)(Math.random()*(10000-100+1)));
            fl.fileWrite(temp,FILENAME);
        }
        aesen=new aesfor();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            Log.d("status", "SUCCESS");
        } catch (ClassNotFoundException e) {
            Log.d("Status", "ERROR");
        } catch (SQLException e) {
            Log.d("Status", "FAILURE");
        }

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        password_text = findViewById(R.id.password);
        email_text = findViewById(R.id.email);
        retype_password = findViewById(R.id.retype);

        message = findViewById(R.id.textView);

        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.sign_up_button);
        signup2 = findViewById(R.id.sign_up_button2);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retype_password.setVisibility(View.VISIBLE);
                signup2.setVisibility(View.VISIBLE);
                message.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                signup.setVisibility(View.GONE);

            }
        });

        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;

                email_field = email_text.getText().toString().trim();
                pw_field = password_text.getText().toString().trim();

                Log.d("EMailLogin", email_field);
                Log.d("PwordLogin", pw_field);

                encrypted_email = paillier.EncrypStr(email_field,r);
                encrypted_pw = paillier.EncrypStr(pw_field,r);

                Log.d("EMailLogin", String.valueOf(encrypted_email));
                Log.d("PwordLogin", String.valueOf(encrypted_pw));

                if(TextUtils.isEmpty(email_field))
                {
                    Toast.makeText(Login.this, "Fill email credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
                if(TextUtils.isEmpty(pw_field))
                {
                    Toast.makeText(Login.this, "Fill password credential", Toast.LENGTH_SHORT).show();
                    flag = 1;

                }
                if(!pw_field.equals(retype_password.getText().toString()))
                {
                    Toast.makeText(Login.this, "Retyped password doesn't match", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }

                if(flag == 0) {

                    if (connection!=null){
                        Statement statement = null;
                        try {
                            statement = connection.createStatement();

                            Log.d("Key", String.valueOf(r));
                            statement.executeUpdate("INSERT INTO credential_table VALUES('" + encrypted_email + "','" + encrypted_pw + "', '" + r + "');");

                        } catch (SQLException e) {
                            Log.d("Error message", "Error in inserting");
                        }
                    }
                    else {
                        Log.d("Status", "Connection is null");
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("email", String.valueOf(encrypted_email));
                    bundle.putString("password", String.valueOf(encrypted_pw));
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;

                email_field = email_text.getText().toString().trim();
                pw_field = password_text.getText().toString().trim();

                Log.d("EMailLogin", email_field);
                Log.d("PwordLogin", pw_field);

                if(TextUtils.isEmpty(email_field))
                {
                    Toast.makeText(Login.this, "Fill email credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
                if(TextUtils.isEmpty(pw_field))
                {
                    Toast.makeText(Login.this, "Fill password credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }

                if(flag == 0) {
                    if (connection!=null){
                        Statement statement = null;

                        try {
                            statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM credential_table;");

                            while(resultSet.next()) {

                                String key_from_db = resultSet.getString(3).trim().replaceAll("\"","");
                                BigInteger key = new BigInteger(key_from_db);
                                Log.d("Key", String.valueOf(key));
                                Log.d("Key from db", key_from_db);

                                encrypted_email = paillier.EncrypStr(email_field, key);
                                encrypted_pw = paillier.EncrypStr(pw_field, key);

                                Log.d("EmailEncLogin", String.valueOf(encrypted_email));
                                Log.d("PwEncLogin", String.valueOf(encrypted_pw));

                                if (String.valueOf(encrypted_email).equals(resultSet.getString(1))) {
                                    if (String.valueOf(encrypted_pw).equals(resultSet.getString(2))) {

                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", String.valueOf(encrypted_email));
                                        bundle.putString("password", String.valueOf(encrypted_pw));
                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                } else {
                                    Toast.makeText(Login.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                                }

                                } else {
                                    Toast.makeText(Login.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (SQLException e) {
                            Log.d("Error message", "String.valueOf(e)");
                        }
                    }
                    else {
                        Log.d("Status", "Connection is null");
                    }
                }
            }
        });

    }
}