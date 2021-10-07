package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class GgInformation extends AppCompatActivity {
    private EditText username, emailAddress;
    private DatePickerDialog datePickerDialog;
    private EditText dateButton;
    AppCompatButton save;
    ImageView occupation;
    private RadioButton male;
    private RadioButton female;
    private String gender = "";
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser mUser;
    AutoCompleteTextView act;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gg_information);
        username = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        progressBar = findViewById(R.id.progressBar);
        dateButton = findViewById(R.id.dob);
        occupation = findViewById(R.id.occupation);
        save = findViewById(R.id.save);

        ArrayAdapter<String> arrayAdapter_season;
        // Calling initDatePicker Function
        initDatePicker();
        //Setting date text
        dateButton.setText(getTodayDate());

        act = (AutoCompleteTextView) findViewById(R.id.suggetion_box);
        // Array list Adapter to display occupation in dropdown screen
        arrayAdapter_season = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, Job_items.items);
        act.setAdapter(arrayAdapter_season);

        //onClick function of occupation dropdown
        occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling show dropdown method which displays the elements in array adapter
                act.showDropDown();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    private String getTodayDate() {
        //getting instance of calendar class returning a calendar object
        Calendar calendar = Calendar.getInstance();
        // Storing Calendar year, month, day inside variables
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // Creating initDate Picker method
    private void initDatePicker() {


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                //Setting date to date Textview
                dateButton.setText(date);
                Log.d("DOB", date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Setting light theme for calendar
        int style = AlertDialog.THEME_HOLO_LIGHT;
        //Creating a dialog containing date picker
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

    // function to call datePickerDialog
    public void openDatePicker(View view) {
        datePickerDialog.show();

    }

    // Creating register user method
    private void saveUserData() {
        // Storing input received from user in edit text inside String variables
        String userName = username.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String dob = dateButton.getText().toString().trim();
        String occupation = act.getText().toString();
        Log.d("job", occupation);
        String preference = "";
        String suggestions = "";
        // Assigning male to gender variable if male radio button is checked
        if (male.isChecked()) {
            gender = "Male";
        }

        // Assigning female to gender variable if female radio button is checked
        if (female.isChecked()) {
            gender = "Female";
        }
        // notifying user if username field is empty
        if (userName.isEmpty()) {
            username.setError("Full Name is Required");
            username.requestFocus();
            return;
        }
        // notifying user if email field is empty
        if (email.isEmpty()) {
            emailAddress.setError("Email is Required");
            emailAddress.requestFocus();
            return;
        }
        // notifying user if email entered is not in email address format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Email is Invalid");
            emailAddress.requestFocus();
            return;
        }
        // Notifying user if password field is empty

        // Notifying user if password entered and re entered password does not match

        // setting progress bar as visible for authentication time
        progressBar.setVisibility(View.VISIBLE);
        // Creating a new User account in firebase with user's email and password

        //Creating user object and passing user input as parameters
        User user = new User(
                userName,
                email,
                occupation,
                gender,
                dob,
                preference,
                suggestions
        );
        // getting an instance of firebase database using getInstance()
        // accessign the location in database to write data
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        Log.d("User", mUser.getUid());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //If task complete navigating from Register Activity to Suggestions Activity
                            Intent intentveri = new Intent(GgInformation.this, Suggestions.class);
                            startActivity(intentveri);
                            // Display Toast message "Registration successful"
                            Toast.makeText(GgInformation.this, "Saved Successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            // Display Toast message "Registration failed" if error occurs
                            Toast.makeText(GgInformation.this, "Couldn't Save. Try Again!", Toast.LENGTH_LONG)
                                    .show();
                        }
                        // Setting visibility of progress bar once the registration function is complete
                        progressBar.setVisibility(View.GONE);

                    }
                });


    }
}