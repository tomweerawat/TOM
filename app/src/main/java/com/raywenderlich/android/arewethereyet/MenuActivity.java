package com.raywenderlich.android.arewethereyet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raywenderlich.android.arewethereyet.Register.MyTTS;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private final static int REQUEST_VOICE_RECOGNITION = 10001;
    private Button btnSay;
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;
    String username1,password1;


    private void callVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, REQUEST_VOICE_RECOGNITION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ผลลัพธ์ที่ส่งกลับมา
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnSay) {
                    MyTTS.getInstance(this)
                            .setLocale(Locale.ENGLISH)
                            .speak("HI");
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mContext = this;
        //mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mLogin = (Button) findViewById(R.id.button_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                username1 = mUsername.getText().toString();
                password1 = mPassword.getText().toString();
                AsyncTask<Void, Void, JSONArray> async = new AsyncTask<Void, Void, JSONArray>() {
                    @Override
                    protected JSONArray doInBackground(Void... params) {
                        ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
                        nameValue.add(new BasicNameValuePair("username", username1));
                        nameValue.add(new BasicNameValuePair("password", password1));
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://192.168.56.1:8181/projectNT/checkLogin.php");
                        try {
                            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValue);
                            httpPost.setEntity(formEntity);
                            HttpResponse httpResponse = httpClient.execute(httpPost);
                            String result = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                            if (result.equals("null")) {
                                return null;
                            } else {
                                Log.e("jsonarr", result);
                                JSONArray jsonArray = new JSONArray(result);
                                return jsonArray;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e("Error1", ex.toString());
                            return null;
                        }
                    }

                    private StringBuilder inputStreamToString(InputStream streammessage) {
                        String stringMsg="";
                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(streammessage));
                        try {
                            while ((stringMsg = bufferedReader.readLine())!=null){
                                stringBuilder.append(stringMsg);
                            }
                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                        return stringBuilder;

                    }

                    protected void onPostExecute(JSONArray result) {
                        super.onPostExecute(result);
                        try {
                            Toast.makeText(v.getContext(), "เข้าสู่ระบบเรียนร้อย", Toast.LENGTH_LONG).show();
                            String strMemberID = result.getJSONObject(0).getString("username");

                            Intent intent = new Intent(v.getContext(), Hello.class);
                            intent.putExtra("MemberID", strMemberID);
                            startActivity(intent);
                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                }.execute();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(this, CommentActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

            Intent i = new Intent(this, AllGeofencesActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
