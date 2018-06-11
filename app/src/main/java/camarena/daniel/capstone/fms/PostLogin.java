package camarena.daniel.capstone.fms;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PostLogin extends AppCompatActivity
{
    private TextView newUser;
    private Button scanQR;
    private Button search;
    private Button generator;
    private Button newemployee;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        firebaseAuth = FirebaseAuth.getInstance();
        scanQR = findViewById(R.id.btQR);
        search = findViewById(R.id.btSearch);
        generator = findViewById(R.id.btGenerateQR);
        newemployee = findViewById(R.id.btPLNewEmployee);

        newemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostLogin.this, AddDBEntry.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(PostLogin.this, DBQuery.class));
            }
        });

        scanQR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PostLogin.this, QR.class));
            }
        });

        generator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(PostLogin.this, QrGenerator.class));
            }
        });
    }

    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(PostLogin.this, Login.class));
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
                    startActivity(new Intent(PostLogin.this, NewUser.class));
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}