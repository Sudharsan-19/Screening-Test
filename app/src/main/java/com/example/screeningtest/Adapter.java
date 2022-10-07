package com.example.screeningtest;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    Context context;
    ArrayList<AModel> list;
    public static String ChildName;
    public static String ChildAge;
    public static String ChildGender;
    public static String Childdob;
    public static String ChildClass;
    public static String TestResult1;
    public static String TestResult2;

    public static String getChildGender() {
        return ChildGender;
    }

    public static void setChildGender(String childGender) {
        ChildGender = childGender;
    }

    public static String getChilddob() {
        return Childdob;
    }

    public static void setChilddob(String childdob) {
        Childdob = childdob;
    }

    public static String getChildName() {
        return ChildName;
    }

    public static void setChildName(String childName) {
        ChildName = childName;
    }

    public static String getChildAge() {
        return ChildAge;
    }

    public static void setChildAge(String childAge) {
        ChildAge = childAge;
    }

    public static String getChildClass() {
        return ChildClass;
    }

    public static void setChildClass(String childClass) {
        ChildClass = childClass;
    }

    public static String getTestResult1() {
        return TestResult1;
    }

    public static void setTestResult1(String testResult1) {
        TestResult1 = testResult1;
    }

    public static String getTestResult2() {
        return TestResult2;
    }

    public static void setTestResult2(String testResult2) {
        TestResult2 = testResult2;
    }

    public Adapter(Context context, ArrayList<AModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AModel aModel=list.get(position);
        ChildName=aModel.getChildName();
        ChildAge=aModel.getChildAge();
        ChildGender=aModel.getChildGender();
        Childdob=aModel.getChilddob();
        ChildClass=aModel.getChildClass();
        TestResult1=aModel.getTestResult1();
        TestResult2= aModel.getTestResult2();

        holder.Cname.setText(ChildName);
        holder.Cage.setText(ChildAge);


        // TAKE A LOOOOOOOOOK AT THISSSSSSSSSSSSSS CHAR SEQ ERR
        //holder.Econtact.setText(aModel.getVictimEcontact().toString());
        holder.Cclass.setText(ChildClass);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        Adapter adapt;

        TextView Cname,Cage,Cclass,Tr1,Tr2;

        @SuppressLint("CutPasteId")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Cname=itemView.findViewById(R.id.idTVCourseName);
            Cage=itemView.findViewById(R.id.idTVCousePrice);
            Cclass=itemView.findViewById(R.id.idTVneed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(itemView.getContext(),HomeActivity2.class);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
