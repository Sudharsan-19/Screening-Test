package com.example.screeningtest;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText Pname,UserPIN,UserCPIN,ParentPhno,ParentEmail,parentMT;
    Button SendOTP,VerifyOTP,RegBtn,Rec;
    TextView ResendOTP,LogIn;
    String Pname1,ParentEmail1,ParentMotherTongue,Password,CP1,ParentPhno1;
    EditText inputno1,inputno2,inputno3,inputno4,inputno5,inputno6;
    ProgressBar progressBar;
    Parents parent;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFilename = null;

    private MediaRecorder recorder = null;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String Username1;
    private String backOT;
    private int flag=0;
    public static final int RECORD_AUDIO = 0;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_register);

        SendOTP=(Button)findViewById(R.id.SOTPbtn);
        VerifyOTP=(Button)findViewById(R.id.VOTPbtn);
        RegBtn=(Button)findViewById(R.id.Regbtn);



        //Fname=findViewById(R.id.TIETUFname);
        Pname=findViewById(R.id.TIETMname);
        UserPIN=findViewById(R.id.TIETPpin);
        UserCPIN=findViewById(R.id.TIETUCpin);
        ParentPhno=findViewById(R.id.TIETPphno);
        ParentEmail=findViewById(R.id.TIETPemail);
        parentMT=findViewById(R.id.TIETPMT);

        inputno1=findViewById(R.id.input1);
        inputno2=findViewById(R.id.input2);
        inputno3=findViewById(R.id.input3);
        inputno4=findViewById(R.id.input4);
        inputno5=findViewById(R.id.input5);
        inputno6=findViewById(R.id.input6);

        progressBar=findViewById(R.id.VerifyProgressBar);


        //Rec=findViewById(R.id.Record);
//
//        mFilename=Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFilename+="/recorded_audio.3gp";


//        Rec.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
//                    startRecording();
//                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    stopRecording();
//                }
//
//                return false;
//            }
//        });




        ResendOTP=findViewById(R.id.resendOTP);
        LogIn=findViewById(R.id.LogIn);


        mAuth=FirebaseAuth.getInstance();

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Fname1=Fname.getText().toString().trim();
                Pname1=Pname.getText().toString().trim();
                ParentEmail1=ParentEmail.getText().toString().trim();
                ParentMotherTongue=parentMT.getText().toString().trim();
                Password=UserPIN.getText().toString().trim();
                CP1=UserCPIN.getText().toString().trim();
                ParentPhno1=ParentPhno.getText().toString().trim();

                if(TextUtils.isEmpty(Pname1)){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Mother Name!",Toast.LENGTH_LONG).show();
                    Pname.setError("Please fill this field");
                    Pname.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(ParentEmail1)){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your EMAIL!",Toast.LENGTH_LONG).show();
                    ParentEmail.setError("Please fill this field");
                    ParentEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your Password!",Toast.LENGTH_LONG).show();
                    UserPIN.setError("Please fill this field");
                    UserPIN.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(CP1)){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your Password!",Toast.LENGTH_LONG).show();
                    UserCPIN.setError("Please fill this field");
                    UserCPIN.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(ParentPhno1)){
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your Phone Number!",Toast.LENGTH_LONG).show();
                    ParentPhno.setError("Please fill this field");
                    ParentPhno.requestFocus();
                    return;

                }

                if(!TextUtils.isDigitsOnly(Password)&& !TextUtils.isDigitsOnly(CP1)){
                    Toast.makeText(getApplicationContext(),
                            "Please set the pin in digits!",Toast.LENGTH_LONG).show();
                    UserPIN.setError("Please set the PIN in digits");
                    UserPIN.requestFocus();
                    UserCPIN.setError("Please set the PIN in digits");
                    UserCPIN.requestFocus();
                    return;
                }

                if(!TextUtils.equals(Password,CP1)){
                    Toast.makeText(getApplicationContext(),
                            "Your passwords should be same",Toast.LENGTH_LONG).show();
                    UserCPIN.setError("Passwords should be matched");
                    UserCPIN.requestFocus();
                    return;
                }

                if(flag==1){
                    parent=new Parents(Pname1,ParentEmail1,ParentMotherTongue,Password,CP1,ParentPhno1);
                    registerNewUser(ParentEmail1,Password);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please do OTP verification",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        SendOTP.setOnClickListener(view -> {

            if(!ParentPhno.getText().toString().trim().isEmpty()) {
                if((ParentPhno.getText().toString().trim()).length() == 10) {

                    progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + ParentPhno.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            RegisterActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCodeSent(@NonNull String backOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    backOT=backOTP;

                                }
                            }
                    );

                }else{
                    Toast.makeText(RegisterActivity.this, "Check your number", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Please verify your number", Toast.LENGTH_SHORT).show();
            }
        });

        VerifyOTP.setOnClickListener(view -> {
            if(!inputno1.getText().toString().trim().isEmpty() && !inputno2.getText().toString().trim().isEmpty() && !inputno3.getText().toString().trim().isEmpty() && !inputno4.getText().toString().trim().isEmpty() && !inputno5.getText().toString().trim().isEmpty() && !inputno6.getText().toString().trim().isEmpty()){
                String enterOTP=inputno1.getText().toString()+
                        inputno2.getText().toString()+
                        inputno3.getText().toString()+
                        inputno4.getText().toString()+
                        inputno5.getText().toString()+
                        inputno6.getText().toString();

                if(backOT!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneauthcredential= PhoneAuthProvider.getCredential(
                            backOT, enterOTP
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneauthcredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "OTP Verified",Toast.LENGTH_SHORT).show();
                                        flag=flag+1;

                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Enter correct OTP",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else{
                    Toast.makeText(RegisterActivity.this, "Check your internet connection",Toast.LENGTH_SHORT).show();
                }




                //Toast.makeText(Verify.this, "OTP verified",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterActivity.this, "Check your OTP",Toast.LENGTH_SHORT).show();
            }
        });
        setupOTPInputs();
        ResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + ParentPhno.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        RegisterActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(RegisterActivity.this, "onverificationCompleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                backOT=newbackOTP;
                                Toast.makeText(RegisterActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });


    }

    private void setupOTPInputs(){
        inputno1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputno2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputno2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputno3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputno3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputno4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputno4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputno5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputno5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(! charSequence.toString().trim().isEmpty()){
                    inputno6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void registerNewUser(String DmailID, String Password){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(DmailID,Password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),
                                "REGISTRATION SUCCESSFUL!",
                                Toast.LENGTH_LONG).show();

                        FirebaseUser user=mAuth.getCurrentUser();
                        updateUI(user);

                        progressBar.setVisibility(View.GONE);

                    } else{
                        Toast.makeText(getApplicationContext(),
                                "REGISTRATION FAILED!"+"Please Try later",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });



    }

    private void updateUI(FirebaseUser currentUser) {
//        Username1=Pname1;
//        Log.i("Check12",Pname1);

        firebaseDatabase=FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("Parents").child(currentUser.getUid()).child(Pname1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(parent);
                Toast.makeText(RegisterActivity.this, "Added to db successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this,"Error in "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }


//    private void startRecording() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(mFilename);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            recorder.prepare();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//
//        recorder.start();
//    }
//
//    private void stopRecording() {
//        recorder.stop();
//        recorder.release();
//        recorder = null;
//    }
//



}