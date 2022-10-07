package com.example.screeningtest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText email,PIN;
    Button logIn,langbtn;
    TextView register;

    private String email1;
    private String PIN1;
    private FirebaseAuth mAuth;
    final int[] checkedItem = {-1};
    private static int LangNum=0;
    public static int getLang(){return LangNum;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        langbtn=(Button) findViewById(R.id.langbtn);

        email=findViewById(R.id.TIETLemail);
        PIN=findViewById(R.id.TIETLpin);

        register=findViewById(R.id.RegTV);

        logIn=(Button)findViewById(R.id.LLogIN);

        langbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email1=email.getText().toString().trim();
                PIN1=PIN.getText().toString().trim();
                if(email1.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your email",Toast.LENGTH_LONG).show();
                    email.setError("Please fill this field");
                    email.requestFocus();
                    return;
                }
                if(PIN1.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your PIN",Toast.LENGTH_LONG).show();
                    PIN.setError("Please fill this field");
                    PIN.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email1,PIN1).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "Logged IN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else if(isNetworkConnected()){
                        Toast.makeText(LoginActivity.this,
                                "Please Check Your Internet Connection",
                                Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() == null;
    }
    private void showChangeLanguageDialog() {
        final String[] listItems={"English","தமிழ்","हिन्दी"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("Choose language...");
        mBuilder.setIcon(R.drawable.ic_baseline_language_24);
        mBuilder.setSingleChoiceItems(listItems,checkedItem[0],new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    setLocale("en");
                    recreate();
                    LangNum=1;
                }
                else if(i==1){
                    setLocale("ta");
                    recreate();
                    LangNum=2;
                }
                else if(i==2){
                    setLocale("hi");
                    recreate();
                    LangNum=3;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog= mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My languages",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My languages","");
        setLocale(language);

    }

}