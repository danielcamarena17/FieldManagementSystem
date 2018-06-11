package camarena.daniel.capstone.fms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewUser extends AppCompatActivity
{
    private EditText newName;
    private EditText newEmail;
    private EditText newPassword;
    private Button   newRegisterButton;
    private TextView returnMainMenu;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        newRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(validate())
                {
                    String user_email = newEmail.getText().toString().trim();
                    String user_password = newPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(NewUser.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        returnMainMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(NewUser.this, Login.class));
            }
        });
    }

    private void setupUIViews()
    {
        newName = findViewById(R.id.etNewName);
        newPassword = findViewById(R.id.etNewPassword);
        newEmail = findViewById(R.id.etNewEmail);
        newRegisterButton = findViewById((R.id.btNewRegister));
        returnMainMenu = findViewById(R.id.tvReturnMainMenu);
    }

    private Boolean validate()
    {
        Boolean result = false;
        String name = newName.getText().toString();
        String password = newPassword.getText().toString();
        String email = newEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(NewUser.this, "Please Enter All The Details", Toast.LENGTH_LONG).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(NewUser.this, "Registration Successful, Verification Email has been sent!", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(NewUser.this, Login.class));
                    }
                    else
                    {
                        Toast.makeText(NewUser.this, "Verification Email has not been sent!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}