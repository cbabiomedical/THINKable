package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, emailAddress, userPassword, re_enterPassword;
    private DatePickerDialog datePickerDialog;
    private TextView signIn;
    private Button dateButton;
    private Button signUp;
    Spinner occupation;
    private String occupationSelected;
    private RadioButton male;
    private RadioButton female;
    private String gender = "";
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        re_enterPassword = findViewById(R.id.reEnter);
        occupation = findViewById(R.id.occupation);
        dateButton = findViewById(R.id.datePickerButton);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        signIn=findViewById(R.id.signInReg);
        initDatePicker();

        dateButton.setText(getTodayDate());

        String[] items = new String[]{"Teacher", "Manager", "Doctor", "Engineer", "MicroBiologist"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        occupation.setAdapter(adapter1);

        occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupationSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,PreferencesSecPage.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);
                Log.d("DOB", date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "OCT";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();

    }

    private void registerUser() {
        String userName = username.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String dob = dateButton.getText().toString().trim();
        String reEnter = re_enterPassword.getText().toString().trim();
        String occupation = occupationSelected;

        if (male.isChecked()) {
            gender = "Male";
        }
        if (female.isChecked()) {
            gender = "Female";
        }

        if (userName.isEmpty()) {
            username.setError("Full Name is Required");
            username.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailAddress.setError("Email is Required");
            emailAddress.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Email is Invalid");
            emailAddress.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            userPassword.setError("Password is Required");
            userPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            userPassword.setError("Minimum Password length should be 8 characters!");
            userPassword.requestFocus();
            return;
        }
        if (reEnter.isEmpty()) {
            re_enterPassword.setError("Re_ Enter Password");
            re_enterPassword.requestFocus();
            return;
        }
        if (!password.equals(reEnter)) {
            re_enterPassword.setError("Password does not match");
            re_enterPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(
                            userName,
                            email,
                            occupation,
                            gender,
                            dob
                    );

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration Unsuccessful. Try Again!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                }
            }
        });
    }

    public void gotoOTP(View view) {
        Intent intentotp = new Intent(RegisterActivity.this,EnterPhoneActivity.class);
        startActivity(intentotp);

    }
}