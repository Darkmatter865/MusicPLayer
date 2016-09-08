package com.example.dell.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class player extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer mp;
    ArrayList<File> mysongs;
    int position;
    SeekBar sb;
    Button btPlay, btFB,btFF, btPv, btNxt;
    Uri u;
    TextView t;
    Thread UpdateSeekBar;
    String string;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Player");

        }

        btPlay= (Button) findViewById(R.id.btPLay);
        btFB= (Button) findViewById(R.id.btFB);
        btFF= (Button) findViewById(R.id.btFF);
        btNxt= (Button) findViewById(R.id.btNxt);
        btPv= (Button) findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        sb=(SeekBar) findViewById(R.id.sb);
        UpdateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalduration=mp.getDuration();
                int currentposition=0;
                while (currentposition<totalduration){
                    try {
                        sleep(500);
                        currentposition=mp.getCurrentPosition();
                        sb.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };


        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mysongs = (ArrayList)b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);
        t = (TextView) findViewById(R.id.textView2);

        t.setText(mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav",""));
        string = mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
        u = Uri.parse(mysongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        notificate();
        sb.setMax(mp.getDuration());
        UpdateSeekBar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btPLay:
                if(mp.isPlaying()){
                    mp.pause();
                    btPlay.setText(">");
                }
                else{
                    mp.start();
                    btPlay.setText("||");
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position= (position+1)%mysongs.size();
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                string = mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                notificate();
                t.setText(mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav",""));
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position= (position-1<0?mysongs.size()-1:position-1);
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                string = mysongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                notificate();
                t.setText(mysongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
                sb.setMax(mp.getDuration());
                break;

        }
    }
                private void notificate(){
                    NotificationManager notificationmgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    /*Intent intent = new Intent(this, player.class);
                    PendingIntent pintent = PendingIntent.getActivities(this, (int) System.currentTimeMillis(), new Intent[]{intent}, 0);
                    */

                    Notification notif = new Notification.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(string)
                            .setContentText("Your Current Song")
                            .build();

                    notificationmgr.notify(0, notif);


                }
   }
