package com.example.screeningtest;

import static com.example.screeningtest.Adapter.ChildName;
import static com.example.screeningtest.Adapter.getChildName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewReport extends AppCompatActivity {
    TextView tv1;
    ImageView back;
    int correct,wrong;
    Button logout,share;

    private FirebaseAuth mAuth;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        tv1=findViewById(R.id.cAns);
        back=findViewById(R.id.back);
        logout=findViewById(R.id.logOut);
        share=findViewById(R.id.Share);
        mAuth = FirebaseAuth.getInstance();

        correct=getIntent().getIntExtra("Correct",0);
        correct=getIntent().getIntExtra("Correct",0);
        wrong=getIntent().getIntExtra("Wrong",0);


        //Toast.makeText(getApplicationContext(), name,Toast.LENGTH_LONG).show();



        tv1.setText(HomeActivity2.getScore());
        //Log.i("plz", getScore());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReport.this);

                builder.setMessage(R.string.wantTOLogout);

                builder.setTitle(R.string.alert);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        mAuth.signOut();
                        Intent intent = new Intent(ViewReport.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_SUBJECT,"Screening Test Application by TECH FUEGO");
                String shareMessage= "Hey! my kid "+getChildName()+" got score of "+HomeActivity2.getScore()+" in Screening Test application for SLD created by TECH FUEGO ";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ViewReport.this,HomeActivity2.class);
        startActivity(i);
    }
}