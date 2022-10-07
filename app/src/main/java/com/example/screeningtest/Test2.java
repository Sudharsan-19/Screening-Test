package com.example.screeningtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test2 extends AppCompatActivity {

    private WebView mywebView;
    String uid;
    final JSONObject object = new JSONObject();


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,ref1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();

        ObjectId parentID=new ObjectId();

        ObjectId kidID=new ObjectId();


        Log.i("mongoID",parentID.toString());

        mywebView=(WebView) findViewById(R.id.webview);
        WebSettings webSettings=mywebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);

        String[] permissions = {Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(Test2.this, permissions, 1010);



        try {
            // Create a new instance of a JSONObject

            JSONArray arrayy = new JSONArray();
            JSONArray arrayyy = new JSONArray();
            // With put you can add a name/value pair to the JSONObject
            object.put("MedicalHistory",arrayy);
            object.put("FamilyHistory", arrayyy);
            object.put("_id", kidID);
            object.put("ChildName", Adapter.getChildName());
            object.put("DateOfBirth", Adapter.getChilddob());
            object.put("Gender", Adapter.getChildGender());
            object.put("Grade", Adapter.getChildClass());

            final JSONObject object1 = new JSONObject();

            object1.put("_id",parentID);
            object1.put("FatherName","Father Name");
            object1.put("MotherName","Mother Name");
            object1.put("Email","EmailID");
            object1.put("MobileNumber","Number");
            object1.put("MotherTongue","Mother Tongue");
            // Calling toString() on the JSONObject returns the JSON in string format.
            JSONArray array1 = new JSONArray();
            array1.put("English");
            array1.put("Hindi");
            array1.put("Malayalam");
            array1.put("Bengali");
            object1.put("OtherLanguage",array1);

            JSONArray array2 = new JSONArray();
            array2.put("English");
            array2.put("Hindi");
            array2.put("Bengali");

            object.put("ParentDetails",object1);

            object.put("OtherLanguage",array2);

            object.put("__v",0);



            final String json = object.toString();
            Log.i("Json",json.toString());

        } catch (JSONException e) {
            Log.i( "JSONObject Failed", String.valueOf(e));
        }

        if (ContextCompat.checkSelfPermission(Test2.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Test2.this,new String[]{
                    Manifest.permission.RECORD_AUDIO
            },100);
        }


        mywebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {

                request.grant(request.getResources());


                //String[] resources = request.getResources();




            }
        });


        mywebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                mywebView.setVisibility(View.VISIBLE);
                String script="javascript:init('" + object + "')";
                mywebView.evaluateJavascript(script,null);

                //mywebView.loadUrl("javascript:init('" + object + "')");

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mywebView.setVisibility(view.INVISIBLE);
            }
        });
        mywebView.addJavascriptInterface(new JS(this),"JS");
        //String url = UniWebViewHelper.StreamingAssetURLForPath("local_www/index.html");
        //

        mywebView.loadUrl("file:///android_asset/index - Copy.html");


        //mywebView.loadUrl("file:///android_asset/sample22.html");


//        mywebView.loadUrl("file:///android_asset/sample22.html");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                cls cl1=new cls();
//
//                try {
//                    Document doc=Jsoup.connect("file:///android_asset/sample22.html").get();
//                    String title=doc.select(".ah").text();
//                    cl1.setTitle(title);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("title"+cl1.getTitle());
//                        Toast.makeText(getApplicationContext(), cl1.getTitle(),Toast.LENGTH_LONG).show();
//
//                    }
//                });
//            }
//        }).start();


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            mywebView.evaluateJavascript("haha();", new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    Log.i("exampleeee", s);
                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            if(reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if(msg != null) {
                                    Log.i("exampleeee", msg);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e("T5AG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            });
        }


    }
    public class mywebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public void onBackPressed(){
        if(mywebView.canGoBack()) {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }




}