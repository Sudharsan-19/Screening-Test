package com.example.screeningtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity2 extends AppCompatActivity {
  TextView tvN,tvA,tvD,tvC,exit,tvG;
  ImageView back;
    Button logout;
  public String ques1,ques2,ques3,ques4,ques5,ques6,ques7,ques8,ques9,ques10,ques11,ques12,ques13,ques14,ques15;

  Button btn1,btn2,vr;
  private static int correct1,wrong1;
  String uid;

  private static String score;
  public static String getScore(){return score;}

  public static ArrayList<ParentQuizModel> list;
  private FirebaseAuth mAuth;
  private FirebaseDatabase firebaseDatabase;
  private DatabaseReference databaseReference,ref1,ref2,ref3,databaseReference1;

  private static String test1;
  public static String getValue() {
        return test1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        tvN=findViewById(R.id.TV1);
        tvA=findViewById(R.id.TV2);
        tvD=findViewById(R.id.TV3);
        tvC=findViewById(R.id.TV4);
        btn1=findViewById(R.id.TV61);
        btn2=findViewById(R.id.TV51);
        vr=findViewById(R.id.TV62);
        tvG=findViewById(R.id.TV21);

        logout=findViewById(R.id.logOut);




        correct1=Test1.getCorrectAns();
        wrong1=Test1.getWrongAns();

        list=new ArrayList<>();


        back=findViewById(R.id.back);
        exit=findViewById(R.id.exit);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity2.this,Test2.class));

            }
        });
        Log.i("help", String.valueOf(correct1));

        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        //databaseReference1 = firebaseDatabase.getReference("Parents").child(uid);

//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    score=(String) snapshot1.child("testResult1").getValue().toString();
//                    Log.i("plz", score);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(getScore(), "Null")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity2.this);
                    builder.setMessage(R.string.takeTheTes);
                    builder.setTitle(R.string.alert);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.test1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            Intent i=new Intent(HomeActivity2.this,Test1.class);
                            startActivity(i);
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
                }else{
                    Intent i=new Intent(HomeActivity2.this,ViewReport.class);
                    startActivity(i);
                }
            }
        });


        tvN.setText(Adapter.getChildName());
        tvA.setText(Adapter.getChildAge());
        tvD.setText(Adapter.getChilddob());
        tvC.setText(Adapter.getChildClass());
        tvG.setText(Adapter.getChildGender());


        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("parentQuestions");
        databaseReference.keepSynced(true);


        ref1 = firebaseDatabase.getReference("Children1").child(uid);
        ref1.keepSynced(true);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //String ac=snapshot.getValue().toString();
                    //System.out.println(ac);
                    //String sample=(String) snapshot1.child("childName").getValue().toString();
                    //ques1=(String)snapshot1.getValue();
                    //test1=(String) snapshot1.child("testResult1").getValue().toString();
                    score=(String) snapshot1.child("testResult1").getValue().toString();
                    Log.i("plz", score);

                    //Log.i("testResult",test1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref2 = firebaseDatabase.getReference("parentQuestionsTa");
        ref2.keepSynced(true);

        ref3 = firebaseDatabase.getReference("parentQuestionHi");
        ref3.keepSynced(true);
        if(LoginActivity.getLang()==0 || LoginActivity.getLang()==1){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        //String ac=snapshot.getValue().toString();
                        //System.out.println(ac);
                        //String sample=(String) snapshot1.child("childName").getValue().toString();
                        //ques1=(String)snapshot1.getValue();
                        ParentQuizModel parentQuizModel1 = snapshot1.getValue(ParentQuizModel.class);
                        list.add(parentQuizModel1);

                        Log.i("questions", parentQuizModel1.getQuestion().toString());
//
//                    Log.i("ChildAge",ChildAge);

//                    QuesModel quesModel=new QuesModel(ques1,ques2,ques3,ques4,ques5,ques6,ques7,ques8,ques9,ques10,ques11,ques12,ques13,ques14,ques15);
//                    list.add(quesModel);


//                    Log.i("Name", String.valueOf(EContact));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(LoginActivity.getLang()==2){
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        //String ac=snapshot.getValue().toString();
                        //System.out.println(ac);
                        //String sample=(String) snapshot1.child("childName").getValue().toString();
                        //ques1=(String)snapshot1.getValue();
                        ParentQuizModel parentQuizModel1 = snapshot1.getValue(ParentQuizModel.class);
                        list.add(parentQuizModel1);

                        Log.i("questions", parentQuizModel1.getQuestion().toString());
//
//                    Log.i("ChildAge",ChildAge);

//                    QuesModel quesModel=new QuesModel(ques1,ques2,ques3,ques4,ques5,ques6,ques7,ques8,ques9,ques10,ques11,ques12,ques13,ques14,ques15);
//                    list.add(quesModel);


//                    Log.i("Name", String.valueOf(EContact));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(LoginActivity.getLang()==3){
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        //String ac=snapshot.getValue().toString();
                        //System.out.println(ac);
                        //String sample=(String) snapshot1.child("childName").getValue().toString();
                        //ques1=(String)snapshot1.getValue();
                        ParentQuizModel parentQuizModel1 = snapshot1.getValue(ParentQuizModel.class);
                        list.add(parentQuizModel1);

                        Log.i("questions", parentQuizModel1.getQuestion().toString());
//
//                    Log.i("ChildAge",ChildAge);

//                    QuesModel quesModel=new QuesModel(ques1,ques2,ques3,ques4,ques5,ques6,ques7,ques8,ques9,ques10,ques11,ques12,ques13,ques14,ques15);
//                    list.add(quesModel);


//                    Log.i("Name", String.valueOf(EContact));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1:snapshot.getChildren()){
//                    //String ac=snapshot.getValue().toString();
//                    //System.out.println(ac);
//                    //String sample=(String) snapshot1.child("childName").getValue().toString();
//                    //ques1=(String)snapshot1.getValue();
//                    ParentQuizModel parentQuizModel=snapshot1.getValue(ParentQuizModel.class);
//
//                    //Log.i("question1",ques14);
//
//                    //Log.i("ChildAge",ChildAge);
//
//                    //QuesModel quesModel=new QuesModel(ques1,ques2,ques3,ques4,ques5,ques6,ques7,ques8,ques9,ques10,ques11,ques12,ques13,ques14,ques15);
//                    //list.add(quesModel);
//
//
//
//                    //Log.i("Name", String.valueOf(EContact));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity2.this);
                builder.setMessage(R.string.ThisMustbeParent);
                builder.setTitle(R.string.alert);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent i=new Intent(HomeActivity2.this,Test1.class);
                        startActivity(i);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity2.this);

                builder.setMessage(R.string.wantTOLogout);

                builder.setTitle(R.string.alert);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        mAuth.signOut();
                        Intent intent = new Intent(HomeActivity2.this, LoginActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HomeActivity2.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}