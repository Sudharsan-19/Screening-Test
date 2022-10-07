package com.example.screeningtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ApiActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;
    String Aurl;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,ref1;
    Button btn,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        //btn=(Button)findViewById(R.id.btn21);
        btn=(Button)findViewById(R.id.idBtnPlay);
        btn2=findViewById(R.id.idBtnPause);
        //String url="https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl=ta&q=%E0%AE%85%E0%AE%A9%E0%AF%88%E0%AE%B5%E0%AE%B0%E0%AF%81%E0%AE%95%E0%AF%8D%E0%AE%95%E0%AF%81%E0%AE%AE%E0%AF%8D%20%E0%AE%B5%E0%AE%A3%E0%AE%95%E0%AF%8D%E0%AE%95%E0%AE%AE%E0%AF%8D";


        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance("https://screeningtest-75cd1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("sample");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //String ac=snapshot.getValue().toString();
                    //System.out.println(ac)
                    Aurl=snapshot1.child("url").getValue().toString();

                    Log.i("Parent Email",Aurl);

                    //Log.i("Name", String.valueOf(EContact));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApiActivity.this, "Fail to get audio url.", Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toast.makeText(ApiActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
                } else {
                    // this method is called when media player is not playing.
                    Toast.makeText(ApiActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                }
            }
        });





//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String getUrl=url.toString();
//
//                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(getUrl));
//                String title= URLUtil.guessFileName(getUrl,null,null);
//                request.setTitle(title);
//                request.setDescription("Downloading File");
//                String cookie = CookieManager.getInstance().getCookie(getUrl);
//                request.addRequestHeader("cookie",cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
//                DownloadManager downloadManager=(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//                Toast.makeText(ApiActivity.this,"Downloading Started",Toast.LENGTH_SHORT).show();
//
//            }
//        });
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
            Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // this line of code is use to handle error while playing our audio file.
            Toast.makeText(this, "Error found is " + e, Toast.LENGTH_SHORT).show();
        }
    }


}