package com.example.dell.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class player extends AppCompatActivity {
    MediaPlayer mp;
    ArrayList<File> mysongs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mysongs = (ArrayList)b.getParcelableArrayList("songlist");
        int position = b.getInt("pos",0);
        TextView t = (TextView) findViewById(R.id.textView2);

        t.setText(mysongs.get(position).getName().toString());

        Uri u = Uri.parse(mysongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();




    }

}
