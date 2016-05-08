package com.example.tanakorn.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.text.InputType;
import android.widget.TextView;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ContactListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String m_Text = "";
    private String session;
    List<User> dataset;
    private User u;
    int mSelectedItems;
    String search;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        Log.d("iii", intent.getStringExtra("session"));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);

        mRecyclerView.setHasFixedSize(true);
        dataset = new ArrayList<User>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initPlayer();

    }

    public void bindAdapter() {
        mAdapter = new CustomAdapter(this, dataset,session);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPlayer() {
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
                String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/getContact", params);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    contactResult(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            bindAdapter();
            }
        };
        loginTask.execute();
    }

    public void alert(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add contact");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                AsyncTask<Void, Void, String> findTask = new AsyncTask<Void, Void, String>() {
                    String searchText;

                    protected void onPreExecute() {
                        super.onPreExecute();
                        searchText = m_Text;
                    }

                    @Override
                    protected String doInBackground(Void... voids) {
                        HttpHelper helper = new HttpHelper();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("sessionid", session);
                        params.put("keyword", searchText);
                        String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/searchUser", params);
                        Log.d("show",result);
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        try {
                            showResult(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        initPlayer();
                        mAdapter.notifyDataSetChanged();
                    }
                };
                findTask.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
            if (oType.equals("userList")) {
                Log.d("hello", String.valueOf(oContent.length()));
                final String a[] = new String[oContent.length()];
                for (int i = 0; i < oContent.length(); i++) {
                    a[i] = oContent.get(i).toString();
                }
                  // Where we track the selected items
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the dialog title
                builder.setTitle("Add Contact")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(a, 0,null)
                                // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                                final int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                AsyncTask<Void, Void, String> findTask = new AsyncTask<Void, Void, String>() {
                                    String searchText;

                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        searchText = a[selectedPosition];
                                    }

                                    @Override
                                    protected String doInBackground(Void... voids) {
                                        HttpHelper helper = new HttpHelper();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("sessionid", session);
                                        params.put("username", searchText);
                                        String result = helper.POST("https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/addContact", params);
                                        Log.d("show",result);
                                        return result;
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        initPlayer();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                };
                                findTask.execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }
        }
    }

    public void contactResult(String string) throws JSONException {
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
        } else {
            if (oType.equals("contact")) {
                dataset.clear();
                Log.d("ooo", "ID : " + oContent);
                ArrayList<String> a = new ArrayList<String>();
                for (int i = 0; i < oContent.length(); i++) {
                    a.add(oContent.get(i).toString());
                    u = new User(a.get(i));
                    dataset.add(u);
                }

            }
        }
    }
}
