package com.example.screeningtest;

import static com.example.screeningtest.Adapter.ChildName;
import static com.example.screeningtest.HomeActivity2.getScore;
import static com.example.screeningtest.HomeActivity2.getValue;
import static com.example.screeningtest.HomeActivity2.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Test1 extends AppCompatActivity {
    TextView ques,optn1,optn2,exit;
    CardView Cques,Coptn1,Coptn2;
    ImageView back;
    Button nextBtn,btn,btn2;
    List<ParentQuizModel> allQnsList;
    String Aurl,uid;
    MediaPlayer mediaPlayer;
    ParentQuizModel parentQuizModel;
    private static int CorrectAns=0,WrongAns=0,index=1;

    public static int getCorrectAns(){return CorrectAns;}
    public static int getWrongAns(){return WrongAns;}


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,ref1;




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        ques = findViewById(R.id.TextQues);
        optn1 = findViewById(R.id.TextOptn1);
        optn2 = findViewById(R.id.TextOptn2);
        nextBtn=findViewById(R.id.nextBtn);

        Cques=findViewById(R.id.CardQues);
        Coptn1=findViewById(R.id.CardOptn1);
        Coptn2=findViewById(R.id.CardOptn2);

        back=findViewById(R.id.back);
        exit=findViewById(R.id.exit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Test1.this);

                builder.setMessage(R.string.doYouWantToExit);

                builder.setTitle(R.string.alert);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent intent = new Intent(Test1.this, HomeActivity2.class);
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


        btn=(Button)findViewById(R.id.idBtnPlay);
        btn2=findViewById(R.id.idBtnPause);


        Coptn1.setBackgroundColor(getResources().getColor(R.color.white));
        Coptn2.setBackgroundColor(getResources().getColor(R.color.white));
        nextBtn.setClickable(false);




//        mAuth = FirebaseAuth.getInstance();

//        uid=mAuth.getUid();
//        Log.i("uid for Test1.java",uid);
//        firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");

        //Log.i("testResult1",test1);
        //Log.i("testResult2",getValue());
        if(getScore().equals("Null")){
            allQnsList=list;
            Collections.shuffle(allQnsList);
            parentQuizModel=list.get(index);
            if(parentQuizModel.equals(15)){
                success();
            }else{
                setAllData();}
        }else {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(Test1.this);
            builder.setMessage(R.string.AlreadyTookTest);
            builder.setTitle(R.string.alert);

            builder.setCancelable(false);

            builder.setPositiveButton("Retest", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.cancel();
                    mAuth = FirebaseAuth.getInstance();
                    uid=mAuth.getUid();
                    firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    ref1 = firebaseDatabase.getReference("Children1").child(uid).child(ChildName);
                    ref1.keepSynced(true);
                    ref1.child("testResult1").setValue("Null");
                    index=1;
                    CorrectAns=0;
                    WrongAns=0;
                    allQnsList=list;
                    Collections.shuffle(allQnsList);
                    parentQuizModel=list.get(index);
                    if(parentQuizModel.equals(15)){
                        success();
                    }else{
                        setAllData();}
                }
            });

            builder.setNegativeButton("View Report", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If user click no
                    // then dialog box is canceled.
                    Intent i = new Intent(Test1.this, ViewReport.class);
                    startActivity(i);
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to play audio.
                playAudio(Aurl);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking the media player
                // if the audio is playing or not.
                if (mediaPlayer.isPlaying()) {
                    // pausing the media player if
                    // media player is playing we are
                    // calling below line to stop our media player.
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();

                    // below line is to display a message when media player is paused.
                    Toast.makeText(Test1.this, R.string.audioPaused, Toast.LENGTH_SHORT).show();
                } else {
                    // this method is called when media player is not playing.
                    Toast.makeText(Test1.this, R.string.audioNotPlayed, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    public void Correct(CardView cardView){
        cardView.setBackgroundColor(getResources().getColor(R.color.teal_200));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index<list.size()-1){
                    index++;
                    parentQuizModel=list.get(index);
                    if(list.get(index).equals(15)){
                        success();
                    }else{
                        resetColor();
                        setAllData();
                        enableButton();
                    }

                }else{
                    success();
                }
            }
        });
//        CorrectAns++;
//        index++;
//        parentQuizModel=list.get(index);

    }
    public void Wrong(CardView cardView){
        cardView.setBackgroundColor(getResources().getColor(R.color.teal_200));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WrongAns++;
                if(index<list.size()-1){

                    index++;
                    parentQuizModel=list.get(index);
                    if(list.get(index).equals(15)){
                        success();
                    }else{
                        resetColor();
                        setAllData();
                        enableButton();
                    }
                }else{
                    success();
                }

            }
        });

    }
    private void success(){

        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        Log.i("Correct1", String.valueOf(CorrectAns));
        firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Children1").child(uid).child(ChildName);
        databaseReference.keepSynced(true);

        databaseReference.child("testResult1").setValue(getCorrectAns()+" /15");

        Intent i=new Intent(Test1.this,ViewReport.class);
        i.putExtra("Correct",CorrectAns);
        i.putExtra("Wrong",WrongAns);

        Log.i("Correct2", String.valueOf(WrongAns));
        startActivity(i);
    }

    private void setAllData() {
        if(LoginActivity.getLang()==0||LoginActivity.getLang()==1){
            Aurl="https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl=en&q="+parentQuizModel.getQuestion();
            ques.setText(parentQuizModel.getQuestion());
            optn1.setText(parentQuizModel.getOption1());
            optn2.setText(parentQuizModel.getOption2());
        }else if(LoginActivity.getLang()==2){
            Aurl="https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl=ta&q="+parentQuizModel.getQuestion();
            ques.setText(parentQuizModel.getQuestion());
            optn1.setText(parentQuizModel.getOption1());
            optn2.setText(parentQuizModel.getOption2());
        }else if(LoginActivity.getLang()==3){
            Aurl="https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl=hi&q="+parentQuizModel.getQuestion();
            ques.setText(parentQuizModel.getQuestion());
            optn1.setText(parentQuizModel.getOption1());
            optn2.setText(parentQuizModel.getOption2());
        }

    }


    public void enableButton(){
        Coptn1.setClickable(true);
        Coptn2.setClickable(true);

    }
    public void disableButton(){
        Coptn1.setClickable(false);
        Coptn2.setClickable(false);

    }
    public void resetColor(){
        Coptn1.setBackgroundColor(getResources().getColor(R.color.white));
        Coptn2.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void optionAClick(View view) {
        disableButton();
        nextBtn.setClickable(true);
        if(parentQuizModel.getOption1().equals(parentQuizModel.getAnswer())){
            //Coptn1.setCardBackgroundColor(getResources().getColor(R.color.purple_200));
            if(index<=list.size()){
                CorrectAns++;
                Correct(Coptn1);
            }

        }else{
            //Coptn1.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
            WrongAns++;
            Wrong(Coptn1);
        }

    }

    public void optionBClick(View view) {
        disableButton();
        nextBtn.setClickable(true);
        if(parentQuizModel.getOption2().equals(parentQuizModel.getAnswer())){
            Log.i("qaaaa1",parentQuizModel.getOption1());
            Log.i("qaaaa2",parentQuizModel.getAnswer());
            //Coptn2.setCardBackgroundColor(getResources().getColor(R.color.purple_200));
            if(index<=list.size()){
                CorrectAns++;
                Correct(Coptn2);
                //Log.i("SampleeeC", String.valueOf(CorrectAns));
            }
        }else{
            WrongAns++;
            //Coptn2.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
            Wrong(Coptn2);

            //Log.i("SampleeeW", String.valueOf(WrongAns));
        }
    }
    private void playAudio(String audioUrl) {
        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio stream type for our media player.
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        try {
            // below line is use to set our
            // url to our media player.
            mediaPlayer.setDataSource(Aurl);

            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

            // below line is use to display a toast message.
            Toast.makeText(this, R.string.audioStartedPlaying, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // this line of code is use to handle error while playing our audio file.
            Toast.makeText(this, getString(R.string.errorFoundIs) + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Test1.this);

        builder.setMessage(R.string.wantToExit);

        builder.setTitle(R.string.alert);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Intent intent = new Intent(Test1.this, HomeActivity2.class);
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
}