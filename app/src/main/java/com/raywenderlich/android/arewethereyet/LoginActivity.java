package com.raywenderlich.android.arewethereyet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raywenderlich.android.arewethereyet.R;

/**
 * Created by Notkak on 25/5/2559.
 */
public class LoginActivity extends Activity {
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;
    String username1,password1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        //mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, com.raywenderlich.android.arewethereyet.RegisterActivity.class);
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
                        HttpPost httpPost = new HttpPost("http://10.255.13.193/projectNT/checkLogin.php");
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
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e("Error1", ex.toString());
                            return null;
                        }
                    }
                    protected void onPostExecute(JSONArray result) {
                        super.onPostExecute(result);
                        try {
                            Toast.makeText(v.getContext(), "เข้าสู่ระบบเรียนร้อย", Toast.LENGTH_LONG).show();
                            String strMemberID=result.getJSONObject(0).getString("username");

                            Intent intent = new Intent(v.getContext(), Hello.class);
                            intent.putExtra("MemberID",strMemberID);
                            startActivity(intent);
                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                }.execute();

            }
        });
    }
    private StringBuilder inputStreamToString(InputStream streammessage){
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


}