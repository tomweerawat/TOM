package com.raywenderlich.android.arewethereyet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Win81 User on 25/5/2559.
 */
public class Hello extends Activity {
   /* @Bind(R.id.btncomment)
    Button btncomment;
    @Bind(R.id.btnservice)
    Button btnservice;*/
    String MID;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        ButterKnife.bind(this);
        MID = getIntent().getStringExtra("MemberID");


    }





    @OnClick({R.id.btncomment, R.id.btnservice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btncomment:
                 intent = new Intent(this,CommentActivity.class);
                intent.putExtra("MemberID",MID);
                startActivity(intent);
                break;
            case R.id.btnservice:
                 intent = new Intent(this,MapsActivity.class);
                startActivity(intent);
                break;
        }
    }
}