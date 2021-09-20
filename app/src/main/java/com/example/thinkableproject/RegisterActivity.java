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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, emailAddress, userPassword, re_enterPassword;
    private DatePickerDialog datePickerDialog;
    private TextView signIn;
    private EditText dateButton;
    ImageView calendar;
    private Button signUp;
    Spinner occupation;
    private String occupationSelected;
    private RadioButton male;
    private RadioButton female;
    private String gender = "";
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private GoogleSignInClient mGoogleSignInClient;
    AutoCompleteTextView suggestionbox;
    private final static int RC_SIGN_IN=123;
    private FirebaseAuth mAuthggl;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuthggl.getCurrentUser();
        if(user!=null){
            Intent intentg = new Intent (getApplicationContext(),SignInActivity.class);
            startActivity(intentg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        re_enterPassword = findViewById(R.id.reEnter);
        occupation = findViewById(R.id.occupation);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        dateButton=findViewById(R.id.dob);
        signIn=findViewById(R.id.signInReg);
        calendar=findViewById(R.id.calendar);

        initDatePicker();

        dateButton.setText(getTodayDate());

        mAuthggl = FirebaseAuth.getInstance();

        createRequest();

        //suggestionbox
        suggestionbox=(AutoCompleteTextView) findViewById(R.id.suggetion_box);


        findViewById(R.id.imageViewggl).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signInggl();
            }
        });

        String[] items = new String[]{"Teacher", "Manager", "Doctor", "Engineer", "MicroBiologist","Dentist","Actor","Pilot","Postman","Dentist","Data Scientist","Marketer","Therapist"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        suggestionbox.setAdapter(adapter1);
        occupation.setAdapter(adapter2);

        occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupationSelected = parent.getItemAtPosition(position).toString();
                suggestionbox.setText(occupationSelected);
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
                Intent intent=new Intent(RegisterActivity.this,SignInActivity.class);
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

    private void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    private void signInggl() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuthggl.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task){
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuthggl.getCurrentUser();
                            Intent intentg = new Intent (getApplicationContext(),EnterPhoneActivity.class);
                            startActivity(intentg);

                        } else{
                            Toast.makeText(RegisterActivity.this, "Sorry auth failed", Toast.LENGTH_SHORT).show();


                        }
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
        String preference="";
        String suggestions = "";

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
                            dob,
                            preference,
                            suggestions
                    );

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intentveri = new Intent(RegisterActivity.this, Suggestions.class);
                                        startActivity(intentveri);
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


    public void goOTP(View view) {
        Intent intentVerifyNum = new Intent(RegisterActivity.this, EnterPhoneActivity.class);
        startActivity(intentVerifyNum);
    }

//    public void gotoOTPthruRegister(View view) {
//        Intent intentVerifyNum2 = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
//        startActivity(intentVerifyNum2);
//        Log.d("war","Clicked");
//    }
}