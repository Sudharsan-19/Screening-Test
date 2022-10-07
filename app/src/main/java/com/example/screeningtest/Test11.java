package com.example.screeningtest;

import static android.content.ContentValues.TAG;
import static com.example.screeningtest.HomeActivity2.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Random;

public class Test11 extends AppCompatActivity {

    private TextView quesTV,quesNum;
    private Button op1,op2;
    Random random;
    ArrayList<ParentQuizModel> parentQuizModel;
    ParentQuizModel parentQuizModel1;
    int currentScore=0,questionAttempted=0,currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test11);

        quesTV=findViewById(R.id.Questions);
        quesNum=findViewById(R.id.NoAttended);
        op1=findViewById(R.id.option1);
        op2=findViewById(R.id.option2);
        parentQuizModel=new ArrayList<>();
        random=new Random();
        parentQuizModel=list;
        Collections.shuffle(parentQuizModel);
        currentPos=random.nextInt(parentQuizModel.size());
        setDataToViews(currentPos);


        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(parentQuizModel.get(currentPos).getAnswer().equals(parentQuizModel.get(currentPos).getOption1())){
                    currentScore++;
                    Log.i("solve", String.valueOf(currentScore));

                }
                questionAttempted++;
                currentPos= random.nextInt(parentQuizModel.size());
                setDataToViews(currentPos);
            }
        });

        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(parentQuizModel.get(currentPos).getAnswer().equals(parentQuizModel.get(currentPos).getOption2())){
                    currentScore++;
                }
                questionAttempted++;
                currentPos= random.nextInt(parentQuizModel.size());
                setDataToViews(currentPos);
            }
        });

    }
    private void showBottomsheet(){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Test11.this);
        View bottomSheetView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_sheet,(LinearLayout)findViewById(R.id.Lscore));
        TextView scoreTV=bottomSheetView.findViewById(R.id.score);
        Button restart=bottomSheetView.findViewById(R.id.btnRe);
        scoreTV.setText("Your score is \n"+currentScore+"/15");
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionAttempted=0;
                currentPos=random.nextInt(parentQuizModel.size());
                setDataToViews(currentPos);
                currentScore=0;
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setDataToViews(int currentPos) {
        if(questionAttempted==15){
            showBottomsheet();
        }else{
            quesNum.setText("Question Attempted : "+questionAttempted+"/15");
            quesTV.setText(parentQuizModel.get(currentPos).getQuestion());
            op1.setText(parentQuizModel.get(currentPos).getOption1());
            op2.setText(parentQuizModel.get(currentPos).getOption2());

        }

    }
}