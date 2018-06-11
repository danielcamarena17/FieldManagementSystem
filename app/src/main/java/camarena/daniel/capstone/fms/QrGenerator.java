package camarena.daniel.capstone.fms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class QrGenerator extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;

    EditText qrText;
    Button genButton;
    ImageView qrImage;
    String text2QR;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);

        firebaseAuth = FirebaseAuth.getInstance();

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        qrText = findViewById(R.id.etQRGeneratorInput);
        genButton = findViewById(R.id.btGenerate);
        qrImage = findViewById(R.id.ivQRImageOutput);

        genButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                qrText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if(validate())
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try
                    {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE, 200,200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrImage.setImageBitmap(bitmap);
                    }
                    catch (WriterException e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(QrGenerator.this, "Successfully generated QR Code", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(QrGenerator.this, "Failed generating QR Code", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private Boolean validate()
    {
        Boolean result = false;

        text2QR = qrText.getText().toString().trim();
        if(text2QR.isEmpty())
        {
            Toast.makeText(QrGenerator.this, "Please Enter Employee's Full Name", Toast.LENGTH_LONG).show();
        }
        else
        {
            result = true;
        }
        return result;
    }


    public void OnClickShare(View view)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try
        {
            String qrImageName = text2QR.replaceAll("\\s+", "");
            File file = new File(QrGenerator.this.getExternalCacheDir(),qrImageName + ".png");

            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share QR Code via"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logout Methods
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(QrGenerator.this, Login.class));
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
                startActivity(new Intent(QrGenerator.this, NewUser.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
