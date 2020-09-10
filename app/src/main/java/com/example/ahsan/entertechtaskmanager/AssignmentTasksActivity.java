package com.example.ahsan.entertechtaskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AssignmentTasksActivity extends AppCompatActivity {

    String address= "http://103.108.144.246/faceapi/index.php";

    String URL= "http://103.108.144.246/faceapi/index.php";

    JSONParser jsonParser=new JSONParser();
    ListView listView;
    Button button;
    TextView textView;
    int userIdIntent;

    InputStream is = null;

    ArrayAdapter<String> arrayAdapter;
    String line = null;
    String result = null;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_tasks);

        //listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        userIdIntent = 2;

//        getData();
//        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readData();

            }
        });

    }

    private void readData() {
        AssignmentTasksActivity.AttemptLogin attemptLogin= new AssignmentTasksActivity.AttemptLogin();
        attemptLogin.execute("2","","");
    }


    private void getData() {
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line=br.readLine())!=null){
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];
            for (int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                data[i]=jo.getString("task_id");

                Toast.makeText(getApplicationContext(),data[i],Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {



            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id", name));


            JSONObject json = jsonParser.makeHttpRequest(URL, "GET", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    if (result.getInt("success")==1){
                        Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(AssignmentTasksActivity.this,AssignmentTasksActivity.class);
//                        startActivity(intent);
//                        finish();
                    }

                    textView.setText(result.getString("task_date"));
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
