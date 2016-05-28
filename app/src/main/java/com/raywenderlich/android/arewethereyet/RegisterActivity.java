package com.raywenderlich.android.arewethereyet;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class RegisterActivity extends Activity {
    EditText username,password,repassword,fullname;
    String username1,password1,fullname1;
    private static final int RESULT_LOAD_IMAGE=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        repassword=(EditText)findViewById(R.id.confirm_password);
        findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                username1=username.getText().toString();

                if(password.getText().toString().equals(repassword.getText().toString())){
                    password1=password.getText().toString();
                    AsyncTask<Void,Void,JSONArray> async = new AsyncTask <Void, Void, JSONArray>() {
                        @Override
                        protected JSONArray doInBackground(Void... params) {
                            ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
                            nameValue.add(new BasicNameValuePair("username",username1));
                            nameValue.add(new BasicNameValuePair("password",password1));
                            HttpClient httpClient=new DefaultHttpClient();
                            HttpPost httpPost=new HttpPost("http://192.168.56.1:8181/projectNT/register.php");
                            try{
                                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValue);
                                httpPost.setEntity(formEntity);
                                HttpResponse httpResponse=httpClient.execute(httpPost);
                                String result=inputStreamToString(httpResponse.getEntity().getContent()).toString();
                                Log.e("jsonarr",result);
                                if(result.equals("null")){
                                    return null;
                                }else{
                                    JSONArray jsonArray=new JSONArray(result);
                                    return jsonArray;
                                }
                            } catch (Exception ex){
                                ex.printStackTrace();
                                Log.e("Error1",ex.toString());
                                return null;
                            }
                        }
                        protected void onPostExecute(JSONArray result){
                            super.onPostExecute(result);
                            try{
                                Toast.makeText(v.getContext(),"สมัครสมาชิคเรียนร้อย"+result.getJSONObject(0).getString("checkinsert"),Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                                startActivity(intent);
//                            for(int j=0;j<result.length();j++){
//                                id_desmap[j]=result.getJSONObject(j).getString("id_desmap");
//                                name[j]=result.getJSONObject(j).getString("name");
//                                lat5[j]=result.getJSONObject(j).getString("lat");
//                                lng5[j]=result.getJSONObject(j).getString("lng");
//                                des_desmap[j]=result.getJSONObject(j).getString("des_desmap");
//                                pic[j]=result.getJSONObject(j).getString("pic");
//                            }
                            }
                            catch(Exception ex){
                                Log.e("Error",ex.toString());
                            }
                        }
                    }.execute();
                }else{
                    password.setError("password ไม่ตรงกัน");
                    password.requestFocus();
                    password.setText("");
                    repassword.setText("");
                }
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