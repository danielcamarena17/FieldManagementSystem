package camarena.daniel.capstone.fms;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity
{
    private EditText resetEmail;
    private Button reset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        resetEmail = findViewById(R.id.etResetEmail);
        reset = findViewById(R.id.btResetButton);
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = resetEmail.getText().toString().trim();
                if(email.equals(""))
                {
                    Toast.makeText(ResetPass.this, "Please Enter Registered Email Address", Toast.LENGTH_LONG).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPass.this,"Password Reset Email Sent", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ResetPass.this, Login.class));
                            }
                            else
                            {
                                Toast.makeText(ResetPass.this,"Error: Email Address not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
