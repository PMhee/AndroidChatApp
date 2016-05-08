package com.example.tanakorn.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class chat extends AppCompatActivity {
    private String session;
    private String user;
    private chatAdapter mAdapter;
    private List<Message> dataset;
    RecyclerView chat;
    private LinearLayoutManager mLayoutManager;
    public String mm;
    private EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        chat = (RecyclerView) findViewById(R.id.chat);
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        user = intent.getStringExtra("user");
        e = (EditText) findViewById(R.id.mm);

        dataset = new ArrayList<Message>();
        mLayoutManager = new LinearLayoutManager(this);
        chat.setLayoutManager(mLayoutManager);
        initMessage();
    }
    public void sendM(View v){
        AsyncTask<Void, Void, String> loginTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mm = e.getText().toString();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHelper helper = new HttpHelper();
                HashMap<String, String> params = new HashMap<>();
                params.put("sessionid", session);
                params.put("targetname",user);
                params.put("message",mm);
                String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/postMessage", params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                bindAdapter();
                initMessage();
                mAdapter.notifyDataSetChanged();
            }
        };
        loginTask.execute();
    }
    public void initMessage(){
        AsyncTask<Void, Void, String> loginTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHelper helper = new HttpHelper();
                HashMap<String, String> params = new HashMap<>();
                params.put("sessionid", session);
                String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/getMessage", params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    showResult(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bindAdapter();
                mAdapter.notifyDataSetChanged();
            }
        };
        loginTask.execute();
    }
    public void bindAdapter() {
        mAdapter = new chatAdapter(this, dataset);
        chat.setAdapter(mAdapter);
    }
    public void showResult(String string) throws JSONException {
        JSONObject obj = null;

        try {
            obj = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        String oType = "";
        JSONArray oContent;
        try {
            oType = obj.getString("type");
            oContent = obj.getJSONArray("" + "content");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (oType.equals("error")) {
            Log.d("error", String.valueOf(oContent.length()));
        } else {
            if(oType.equals("getMessage")){
                Log.d("message", String.valueOf(oContent.length()));
                dataset.clear();
                for(int i=0;i<oContent.length();i++){
                    JSONObject o = (JSONObject) oContent.get(i);
                    Message message = new Message(o.getString("from"),o.getString("to"),o.getString("message"));
                    if(message.getTo().equals(user) || message.getForm().equals(user)) {
                        dataset.add(message);
                    }
                }

            }
        }
    }
}
