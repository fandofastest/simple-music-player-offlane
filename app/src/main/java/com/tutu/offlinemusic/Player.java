package com.tutu.offlinemusic;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity  {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private MediaPlayer mPlayer;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    String judul,namafile,linkfoto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);




        judul=getIntent().getStringExtra("judul");

        namafile=getIntent().getStringExtra("namafile");
        linkfoto=getIntent().getStringExtra("foto");







        backwardbtn = (ImageButton)findViewById(R.id.btnBackward);
        forwardbtn = (ImageButton)findViewById(R.id.btnForward);
        playbtn = (ImageButton)findViewById(R.id.btnPlay);
        pausebtn = (ImageButton)findViewById(R.id.btnPause);
        songName = (TextView)findViewById(R.id.txtSname);
        startTime = (TextView)findViewById(R.id.txtStartTime);
        songTime = (TextView)findViewById(R.id.txtSongTime);
        songName.setText(judul);

        String banner=getIntent().getStringExtra("banner");


        mPlayer = new MediaPlayer();


        try {
            AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("music/"+namafile);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }











        songPrgs = (SeekBar)findViewById(R.id.sBar);
        songPrgs.setClickable(false);

        pausebtn.setEnabled(true);

        playmusik();

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playmusik();

            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                pausebtn.setEnabled(false);
                playbtn.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime + fTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime - bTime) > 0)
                {
                    sTime = sTime - bTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };



    public void onBackPressed() {
        super.onBackPressed();
       mPlayer.stop();
    }


    public void loadbanner(String banner){

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(banner);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
            mAdView.loadAd(adRequest);
        // else Log state of adsize/adunit
        ((LinearLayout)findViewById(R.id.adView)).addView(mAdView);
    }




    public void playmusik(){


        Toast.makeText(Player.this, "Playing Audio", Toast.LENGTH_SHORT).show();
        mPlayer.start();
        eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        if(oTime == 0){
            songPrgs.setMax(eTime);
            oTime =1;
        }
        songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
        startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        songPrgs.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);
        pausebtn.setEnabled(true);
        playbtn.setEnabled(false);
    }


}
