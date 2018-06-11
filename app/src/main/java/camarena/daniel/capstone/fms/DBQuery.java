package camarena.daniel.capstone.fms;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DBQuery extends AppCompatActivity
{
    private EditText searchText;
    private ImageButton searchBt;
    private Button newEmployeeBt;

    private FirebaseAuth firebaseAuth;
    private RecyclerView recycleList;

    private DatabaseReference employeeDatabaseRef;

    private Query query;
    private FirebaseRecyclerOptions<Employees> recyclerOptions;
    private FirebaseRecyclerAdapter<Employees, employeeViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbquery);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        searchText = findViewById(R.id.etSearchName);
        searchBt = findViewById(R.id.ibSearchButton);
        newEmployeeBt = findViewById(R.id.btAddNewEmployee);

        firebaseAuth = FirebaseAuth.getInstance();
        recycleList = findViewById(R.id.rvlist);
        recycleList.hasFixedSize();
        recycleList.setLayoutManager(new LinearLayoutManager(this));

        employeeDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Employees");
        employeeDatabaseRef.keepSynced(true);

        final String display = "";
        viewDatabase(display);

        searchBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String search = searchText.getText().toString();
                viewDatabase(search);
            }
        });

        newEmployeeBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DBQuery.this, AddDBEntry.class));
            }
        });
    }

    public void viewDatabase(String itemSearch)
    {
        final DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference().child("Employees");
        query = employeeRef.orderByChild("name").startAt(itemSearch).endAt(itemSearch + "\uf8ff");

        recyclerOptions = new FirebaseRecyclerOptions.Builder<Employees>()
                .setQuery(query, Employees.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Employees, employeeViewHolder>(recyclerOptions)
        {
            @Override
            protected void onBindViewHolder(employeeViewHolder holder, final int position, final Employees model)
            {
                final String key = model.getName();
                holder.setName(model.getName());
                holder.setPosition(model.getPosition());
                holder.setPhoneNumber(model.getPhonenumber());
                holder.setEmail(model.getEmail());

                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent j = new Intent(DBQuery.this, SingleEmployeeItem.class);
                        j.putExtra("key", key);
                        startActivity(j);
                    }
                });

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(DBQuery.this);
                        builder.setTitle("Deleting Employee");
                        builder.setMessage("Are you sure you want to delete " + model.getName() + "?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                employeeDatabaseRef.child(model.getName()).removeValue();
                                firebaseRecyclerAdapter.notifyDataSetChanged();
                                firebaseRecyclerAdapter.notifyItemRemoved(which);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                        return true;
                    }
                });
            }

            @Override
            public employeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                return new employeeViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycleList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public static class employeeViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public employeeViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setName(String employeeName)
        {
            TextView employee_name = mView.findViewById(R.id.tvListName);
            employee_name.setText(employeeName);
        }

        public void setPosition(String employeePosition)
        {
            TextView employee_position = mView.findViewById(R.id.tvListPosition);
            employee_position.setText(employeePosition);
        }

        public void setPhoneNumber(String employeeNumber)
        {
            TextView employee_number = mView.findViewById(R.id.tvListPhone);
            employee_number.setText(employeeNumber);
        }

        public void setEmail(String employeeEmail)
        {
            TextView employee_email = mView.findViewById(R.id.tvListEmail);
            employee_email.setText(employeeEmail);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Logout Methods
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(DBQuery.this, Login.class));
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
                startActivity(new Intent(DBQuery.this, NewUser.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
