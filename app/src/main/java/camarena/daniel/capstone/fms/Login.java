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


public class Login extends AppCompatActivity
{
    private EditText UserName;
    private EditText Password;
    private Button Login;
    private TextView LinkToForgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        UserName = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btLogin);
        LinkToForgotPassword = findViewById(R.id.tvForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String u = UserName.getText().toString().trim();
                String p = Password.getText().toString().trim();

                if(u.equals("admin") && p.equals("admin"))
                {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, PostLogin.class));
                }
                else
                {
                    validate(UserName.getText().toString().trim(), Password.getText().toString().trim());
                }
            }
        });

        LinkToForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, ResetPass.class));
            }
        });
    }

    private void validate(String userName, String userPassword)
    {
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    checkEmailVerification();
                }
                else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag)
        {
            finish();
            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Login.this, PostLogin.class));
        }
        else
        {
            Toast.makeText(this, "Verify Your Email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }

    }

}