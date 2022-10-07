package com.example.screeningtest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements PaymentResultListener {

    TextView name;
    FloatingActionButton addBtn;
    Button logout;


    private ProgressBar loadingPB;

    private ArrayList<AModel> list;

    private RecyclerView cardRV;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, ref1;
    Adapter adapter;

    private static String pname, uid, email, phno, ChildName;

    public static String getValue() {
        return pname;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPhno() {
        return phno;
    }

    public static String getChildName() {return ChildName;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cardRV = findViewById(R.id.idRVCard);

        addBtn = findViewById(R.id.addChild);

        logout=findViewById(R.id.logOut);

        name = findViewById(R.id.TVname);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        Log.i("UID", uid);
        Checkout.preload(getApplicationContext());

        firebaseDatabase = FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Children1").child(uid);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //String ac=snapshot.getValue().toString();
                    //System.out.println(ac);
                    //String sample=(String) snapshot1.child("childName").getValue().toString();
                    ChildName = (String) snapshot1.child("childName").getValue().toString();
                    String ChildAge = (String) snapshot1.child("childAge").getValue().toString();
                    String ChildGender=(String) snapshot1.child("childGender").getValue().toString();
                    String Childdob = (String) snapshot1.child("childdob").getValue().toString();
                    String ChildClass = (String) snapshot1.child("childClass").getValue().toString();
                    String TestResult1 = (String) snapshot1.child("testResult1").getValue();
                    String TestResult2 = (String) snapshot1.child("testResult2").getValue();

                    Log.i("ChildAge",ChildAge);

                    AModel aModel = new AModel(ChildName, ChildAge,ChildGender, Childdob, ChildClass, TestResult1, TestResult2);
                    list.add(aModel);


                    //Log.i("Name", String.valueOf(EContact));

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ref1 = firebaseDatabase.getReference("Parents").child(uid);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //String ac=snapshot.getValue().toString();
                    //System.out.println(ac);
                    pname = (String) snapshot1.child("pname1").getValue();
                    email = (String) snapshot1.child("parentEmail1").getValue();
                    phno = (String) snapshot1.child("parentPhno1").getValue().toString();

                    name.setText(pname);

                    //Log.i("Parent Email",email1);
                    //Log.i("tag1", ChildName);

                    //Log.i("Name", String.valueOf(EContact));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Log.i("Parent Email",getEmail());
        //Log.i("tag", getChildName());


        cardRV.setHasFixedSize(true);
        cardRV.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Adapter(this, list);
        cardRV.setAdapter(adapter);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size()==0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                    builder.setMessage(R.string.free_trail);

                    builder.setTitle(R.string.alert);
                    builder.setCancelable(false);

                    builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
//                            mAuth.signOut();
                            Intent intent = new Intent(HomeActivity.this, ChildActivity.class);
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                    builder.setMessage(R.string.freeTrialEnded);

                    builder.setTitle(R.string.alert);
                    builder.setCancelable(false);

                    builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            startPayment();
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
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                builder.setMessage(R.string.wantTOLogout);

                builder.setTitle(R.string.alert);
                builder.setCancelable(false);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        mAuth.signOut();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_3MBPwdaJTQ36Fm");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_baseline_home_24);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Team Fuego");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", getEmail());
            options.put("prefill.contact",getPhno());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Log.i("ONSUCCESS", s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i("ONERROR", s);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        builder.setMessage(R.string.wantTOLogout);

        builder.setTitle(R.string.alert);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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