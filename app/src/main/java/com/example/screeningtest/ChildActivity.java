package com.example.screeningtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ChildActivity extends AppCompatActivity {
    TextInputEditText name,age,dob;
    Button btn1;
    AutoCompleteTextView clss,gender;
    ArrayAdapter<String> adapterItems,adapterItems1;

//    private static String ChildName,ChildAge,Childdob,ChildClass,TestResult1,TestResult2;
//
//    public static String getChildName1

    String[] items={"1","2","3","4","5","6","7","8","9","10","11","12"};

    String[] items1={"Male","Female","Others"};


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        name=findViewById(R.id.TIETKname);
        age=findViewById(R.id.TIETKage);
        clss=findViewById(R.id.TIETKclass);

        dob=findViewById(R.id.TIETsdate);
        gender=findViewById(R.id.TIETKgender);


        btn1=findViewById(R.id.Regbtn);
        mAuth=FirebaseAuth.getInstance();

        adapterItems = new ArrayAdapter<String>(this, R.layout.select_items, items);
        clss.setAdapter(adapterItems);


        adapterItems1 = new ArrayAdapter<String>(this, R.layout.select_items, items1);
        gender.setAdapter(adapterItems1);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChildName,ChildAge,ChildGender,Childdob,ChildClass,TestResult1,TestResult2;
                ChildName=name.getText().toString().trim();
                ChildAge=age.getText().toString().trim();
                ChildGender=gender.getText().toString().trim();
                Childdob=dob.getText().toString().trim();
                ChildClass=clss.getText().toString().trim();
                TestResult1="Null";
                TestResult2="Null";
                Children children=new Children(ChildName,ChildAge,ChildGender,Childdob,ChildClass,TestResult1,TestResult2);

                uid = mAuth.getUid();

                Log.i("uid for home2",uid);

                firebaseDatabase= FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
                databaseReference=firebaseDatabase.getReference("Children1").child(uid).child(ChildName);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.setValue(children);
                        Toast.makeText(ChildActivity.this, "Added to db successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ChildActivity.this,
                                HomeActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChildActivity.this,"Error in "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePD = new DatePickerDialog(
                        ChildActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dob.setText(date);
                    }
                }, year, month, day);
                datePD.show();

            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChildActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}