package camarena.daniel.capstone.fms;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddDBEntry extends AppCompatActivity
{
    private EditText newName;
    private EditText newPosition;
    private EditText newPhoneNumber;
    private EditText newEmail;
    private Button add;

    private String Name, Position, PhoneNumber, Email;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dbentry);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        newName = findViewById(R.id.etNewEmployeeName);
        newPosition = findViewById(R.id.etNewEmployeePosition);
        newPhoneNumber = findViewById(R.id.etNewEmployeePhoneNumber);
        newEmail = findViewById(R.id.etNewEmployeeEmail);
        add = findViewById(R.id.btNewEmployeeAdd);

        firebaseAuth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validate())
                {
                    sendEmployeeData();
                    firebaseAuth.signOut();
                    Toast.makeText(AddDBEntry.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(AddDBEntry.this, DBQuery.class));
                }
                else
                {
                    Toast.makeText(AddDBEntry.this, "Registration Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private Boolean validate()
    {
        Boolean result = false;

        Name = newName.getText().toString().trim();
        Position = newPosition.getText().toString().trim();
        PhoneNumber = newPhoneNumber.getText().toString().trim();
        Email = newEmail.getText().toString().trim();

        if(Name.isEmpty() || Position.isEmpty() || PhoneNumber.isEmpty() || Email.isEmpty())
        {
            Toast.makeText(AddDBEntry.this, "Please Enter all Details", Toast.LENGTH_LONG).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void sendEmployeeData()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Employees");
        Employees newEmployee =  new Employees(Position, Email, Name, PhoneNumber);
        myRef.child(Name).setValue(newEmployee);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logout Methods
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(AddDBEntry.this, Login.class));
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
                startActivity(new Intent(AddDBEntry.this, NewUser.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
