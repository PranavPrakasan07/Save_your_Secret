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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                int flag = 0;

                flag = 0;

                if(TextUtils.isEmpty(email_text.getText().toString()))
                {
                    Toast.makeText(Login.this, "Fill email credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
                if(TextUtils.isEmpty(password_text.getText().toString()))
                {
                    Toast.makeText(Login.this, "Fill password credential", Toast.LENGTH_SHORT).show();
                    flag = 1;

                }
                if(!password_text.getText().toString().equals(retype_password.getText().toString()))
                {
                    Toast.makeText(Login.this, "Retyped password doesn't match", Toast.LENGTH_SHORT).show();
                    flag = 1;

                }

                if(flag == 0) {

                    if (connection!=null){
                        Statement statement = null;
                        try {
                            statement = connection.createStatement();

                            statement.executeUpdate("INSERT INTO credential_table VALUES('" + email_text.getText().toString() + "','" + password_text.getText().toString() + "');");

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Log.d("Status", "Connection is null");
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("email", email_text.getText().toString());
                    bundle.putString("password", password_text.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;

                flag = 0;

                if(TextUtils.isEmpty(email_text.getText().toString()))
                {
                    Toast.makeText(Login.this, "Fill email credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
                if(TextUtils.isEmpty(password_text.getText().toString()))
                {
                    Toast.makeText(Login.this, "Fill password credential", Toast.LENGTH_SHORT).show();
                    flag = 1;
                }

                if(flag == 0) {

                    if (connection!=null){
                        Statement statement = null;
                        try {
                            statement = connection.createStatement();

                            ResultSet resultSet = statement.executeQuery("SELECT * FROM credential_table WHERE email = '" + email_text.getText().toString() + "' AND password_hash = '" + password_text.getText().toString() + "';");
                            resultSet.next();

                            try {
                                if(resultSet.getString(1).equals(email_text.getText().toString()))
                                {
                                    if(resultSet.getString(2).equals(password_text.getText().toString()))
                                    {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", email_text.getText().toString());
                                        bundle.putString("password", password_text.getText().toString());
                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(Login.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                                }

                            } catch (SQLException e) {
                                Toast.makeText(Login.this, "Unable to login", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        } catch (SQLException e) {
                            e.printStackTrace();
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