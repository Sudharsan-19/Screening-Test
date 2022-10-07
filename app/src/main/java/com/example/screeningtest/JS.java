package com.example.screeningtest;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JS {
    private Context context;
    public  JS(Context ctx){this.context=ctx;}

    @JavascriptInterface
    public void Getlink(String link){
        Toast.makeText(context ,""+link,Toast.LENGTH_SHORT).show();
    }
}
