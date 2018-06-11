package camarena.daniel.capstone.fms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class SingleEmployeeItem extends AppCompatActivity
{
    private TextView queryName;
    private TextView queryPosition;
    private TextView queryPhone;
    private TextView queryEmail;

    private Button clockIn;
    private Button clockOut;
    private Button startLunch;
    private Button endLunch;

    private FirebaseAuth firebaseAuth;
    private String number;

    private String employee;
    private String date;
    private String timeIn;
    private String timeOut;
    private String lunchStart;
    private String lunchEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_employee_item);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        queryName = findViewById(R.id.tvQueryName);
        queryPosition = findViewById(R.id.tvQueryPosition);
        queryPhone = findViewById(R.id.tvQueryPhone);
        queryEmail = findViewById(R.id.tvQueryEmail);

        clockIn = findViewById(R.id.btClockIn);
        clockOut = findViewById(R.id.btClockOut);
        startLunch = findViewById(R.id.btStartLunch);
        endLunch = findViewById(R.id.btEndLunch);

        firebaseAuth = FirebaseAuth.getInstance();
        final String key = getIntent().getExtras().getString("key");
        final DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference().child("Employees");
        final Query query = employeeRef.orderByChild("name").startAt(key).endAt(key + "\uf8ff");

        employee = key;
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        this.date = dateFormat.format(date);

        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Employees employee = snapshot.getValue(Employees.class);

                    queryName.setText(employee.getName());
                    queryPosition.setText("Position: " + employee.getPosition());
                    number = employee.getPhonenumber();
                    queryPhone.setText("Phone Number: " + employee.getPhonenumber());
                    queryEmail.setText(employee.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(SingleEmployeeItem.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });
        //getIntent().removeExtra("key");

        queryPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int REQUEST_PHONE_CALL = 1;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(SingleEmployeeItem.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(SingleEmployeeItem.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        startActivity(callIntent);
                    }
                }
            }
        });

        clockIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                timeIn = timeFormat.format(currentTime);
                new SendTimeInRequest().execute();
                Toast.makeText(SingleEmployeeItem.this, "Time Submitted Successfully", Toast.LENGTH_LONG).show();
            }
        });

        clockOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                timeOut = timeFormat.format(currentTime);
                new SendTimeInRequest().execute();
                Toast.makeText(SingleEmployeeItem.this, "Time Submitted Successfully", Toast.LENGTH_LONG).show();
            }
        });

        startLunch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                lunchStart = timeFormat.format(currentTime);
                new SendTimeInRequest().execute();
                Toast.makeText(SingleEmployeeItem.this, "Time Submitted Successfully", Toast.LENGTH_LONG).show();
            }
        });

        endLunch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                lunchEnd = timeFormat.format(currentTime);
                new SendTimeInRequest().execute();
                Toast.makeText(SingleEmployeeItem.this, "Time Submitted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class SendTimeInRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0)
        {
            try{
                URL url = new URL("https://script.google.com/macros/s/AKfycbwP7irNwYdvkPr0jPiI9jRgqh2llU1nc5vJPHC-D7ScN5hb4Ps/exec");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("date", date);
                postDataParams.put("employee", employee);
                postDataParams.put("timeIn", timeIn);
                postDataParams.put("timeOut", timeOut);
                postDataParams.put("lunchStart", lunchStart);
                postDataParams.put("lunchEnd", lunchEnd);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch (Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext())
        {
            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logout Methods
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SingleEmployeeItem.this, Login.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.logout:
            {
                Logout();
                break;
            }
            case R.id.settingsTimesheet:
            {
                Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/17JdbOsLMxBoKUDCkmlv8OYzs0maFJUerVWXyqHKCBiA/edit?usp=sharing");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
            case R.id.settingsMenu:
            {
                Uri uri = Uri.parse("https://youtu.be/w_0AKgt2M78");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
            case R.id.settingsNewUser:
            {
                startActivity(new Intent(SingleEmployeeItem.this, NewUser.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
