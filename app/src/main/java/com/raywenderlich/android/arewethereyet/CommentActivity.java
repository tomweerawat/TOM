package com.raywenderlich.android.arewethereyet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by Notkak on 26/5/2559.
 */
public class CommentActivity extends Activity {
    ListView list;
    EditText eComment;
    Button btnCom;
    String[] comment,name,dates;
    String id_customer,comm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        list=(ListView) findViewById(R.id.custom_list);
        eComment=(EditText) findViewById(R.id.et_comment);
        btnCom=(Button)findViewById(R.id.bt_comment);
                btnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comm = eComment.getText().toString();
                AsyncTask<Void,Void,JSONArray> async = new AsyncTask <Void, Void, JSONArray>() {
                    @Override
                    protected JSONArray doInBackground(Void... params) {
                        ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
                        nameValue.add(new BasicNameValuePair("id_customer",id_customer));
                        nameValue.add(new BasicNameValuePair("comm",comm));
                        HttpClient httpClient=new DefaultHttpClient();
                        HttpPost httpPost=new HttpPost("http://10.255.13.193/projectNT/savecomment.php");
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
                            eComment.setText("");
                            eComment.clearFocus();
                            for(int j=0;j<result.length();j++){
                                if(result.getJSONObject(j).getString("checkcomment").equals("yes")){
                                   new AsyncTask<Void, Void, JSONArray>() {
                                        @Override
                                        protected JSONArray doInBackground(Void... params) {

                                            HttpClient httpClient = new DefaultHttpClient();
                                            HttpPost httpPost = new HttpPost("http://10.255.13.193/projectNT/loadcomment.php");
                                            try {
                                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                                String result = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                                                if (result.equals("null")) {
                                                    return null;
                                                } else {
                                                    return new JSONArray(result);
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                Log.e("Error1", ex.toString());
                                                return null;
                                            }
                                        }

                                        protected void onPostExecute(JSONArray result) {
                                            super.onPostExecute(result);
                                            try {
                                                //String[] comment,name,dates;
                                                comment = new String[result.length()];
                                                name = new String[result.length()];
                                                dates = new String[result.length()];
                                                for (int j = 0; j < result.length(); j++) {
                                                    comment[j] = result.getJSONObject(j).getString("comm");
                                                    name[j] = result.getJSONObject(j).getString("username");
                                                    dates[j] = result.getJSONObject(j).getString("datetime");
                                                }
                                                ArrayList image_details = getListData();
                                                list.setAdapter(new CustomListAdapter(CommentActivity.this, image_details));
                                            } catch (Exception ex) {
                                                Log.e("Error", ex.toString());

                                            }
                                        }
                                    }.execute();
                                }
                            }
                        }
                        catch(Exception ex){
                            Log.e("Error",ex.toString());
                        }
                    }
                }.execute();
            }
        });
        id_customer = getIntent().getStringExtra("MemberID");
       new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... params) {
               // ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
                //nameValue.add(new BasicNameValuePair("username", username1));
                //nameValue.add(new BasicNameValuePair("password", password1));
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.255.13.193/projectNT/loadcomment.php");
                try {
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
                    comment=new String[result.length()];
                    name=new String[result.length()];
                    dates=new String[result.length()];
                    for(int j=0;j<result.length();j++){
                        comment[j]=result.getJSONObject(j).getString("comm");
                        name[j]=result.getJSONObject(j).getString("username");
                        dates[j]=result.getJSONObject(j).getString("datetime");
                    }
                    ArrayList image_details= getListData();
                    list.setAdapter(new CustomListAdapter(CommentActivity.this, image_details));
                } catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }.execute();

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
    private ArrayList getListData() {
        ArrayList<NewsItem> results = new ArrayList<NewsItem>();
        // Add some more dummy data for testing
        Log.e("asdasdadsa",""+comment.length);
        for(int i=0;i<comment.length;i++) {
            NewsItem newsData = new NewsItem();
            newsData.setHeadline(comment[i]);
            newsData.setReporterName(name[i]);
            newsData.setDate(dates[i]);
            results.add(newsData);
        }
        return results;
    }

    }


