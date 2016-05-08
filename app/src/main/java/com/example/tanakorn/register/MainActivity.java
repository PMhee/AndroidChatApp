package com.example.tanakorn.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public EditText mUser;
    public EditText mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = (EditText) findViewById(R.id.user);
        mPass = (EditText) findViewById(R.id.pass);
    }

    public void login(View view) {
        String msg = "Try to login for " + mUser.getText();
//        Toast toast = Toast.makeText(this,msg,Toast.LENGTH_SHORT);
//        toast.show();
        AsyncTask<Void, Void, String> loginTask = new AsyncTask<Void, Void, String>() {
            String username;
            String password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                username = mUser.getText().toString();
                password = mPass.getText().toString();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHelper helper = new HttpHelper();
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/signIn", params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                loginResult(s);

            }
        };
        loginTask.execute();
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void loginResult(String s) {
        JSONObject obj = null;

        try {
            obj = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        String oType = "";
        String oContent = "";
        try {
            oType = obj.getString("type");
            oContent = obj.getString("" + "content");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (oType.equals("error")) {
            showToast(oContent);
        } else {
            if (oType.equals("sessionid")) {
                showToast("Session : " + oContent);
            }
        }
        Intent intent = new Intent(this,ContactListActivity.class);
        intent.putExtra("session",oContent);
        startActivity(intent);
    }
}
