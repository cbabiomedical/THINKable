package com.example.thinkableproject;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, emailAddress, userPassword, re_enterPassword;
    private DatePickerDialog datePickerDialog;
    TextView signIn;
    private EditText dateButton;
    Button signUp;
    ImageView occupation;
    private RadioButton male;
    private RadioButton female;
    private String gender = "";
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private GoogleSignInClient mGoogleSignInClient;
    AutoCompleteTextView act;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuthggl;
    FirebaseFirestore database;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuthggl.getCurrentUser();
        if (user != null) {
            Intent intentg = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intentg);
        }
    }

    ArrayAdapter<String> arrayAdapter_season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // connecting backend variables to front end components
        username = findViewById(R.id.username);
        emailAddress = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        re_enterPassword = findViewById(R.id.reEnter);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        dateButton = findViewById(R.id.dob);
        signIn = findViewById(R.id.signInReg);
        occupation = findViewById(R.id.occupation);

        database = FirebaseFirestore.getInstance();

        // Calling initDatePicker Function
        initDatePicker();
        //Setting date text
        dateButton.setText(getTodayDate());

        mAuthggl = FirebaseAuth.getInstance();

        createRequest();

        //suggestionbox
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

        findViewById(R.id.imageViewggl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInggl();
            }
        });

        // Accessing location to read or write data in firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //Getting an instance of firebase Auth class
        firebaseAuth = FirebaseAuth.getInstance();

        //onClick Function of Sign in TextView
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigates from Register Activity to Sign In Activity
                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        // onClick Function of Sign up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling registerUser function
                registerUser();
            }

        });


    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("417444273764-8063e4mavdbkqmlvhp1o10gb2snomk67.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuthggl.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuthggl.getCurrentUser();
                            Intent intentg = new Intent(getApplicationContext(), GgInformation.class);
                            startActivity(intentg);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Sorry auth failed", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }

    // getDate function that returns Date in String format
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
    private void registerUser() {
        // Storing input received from user in edit text inside String variables
        String userName = username.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String dob = dateButton.getText().toString().trim();
        String reEnter = re_enterPassword.getText().toString().trim();
        String occupation = act.getText().toString();
        Log.d("job", occupation);
        String preference = "";
        String suggestions = "";
        String favourites="";
        String location="";
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
        if (password.isEmpty()) {
            userPassword.setError("Password is Required");
            userPassword.requestFocus();
            return;
        }
        // Notifying user if password has less than 8 characters
        if (password.length() < 8) {
            userPassword.setError("Minimum Password length should be 8 characters!");
            userPassword.requestFocus();
            return;
        }
        // Notifying user if reEnter password is empty
        if (reEnter.isEmpty()) {
            re_enterPassword.setError("Re_ Enter Password");
            re_enterPassword.requestFocus();
            return;
        }
        // Notifying user if password entered and re entered password does not match
        if (!password.equals(reEnter)) {
            re_enterPassword.setError("Password does not match");
            re_enterPassword.requestFocus();
            return;
        }
        // setting progress bar as visible for authentication time
        progressBar.setVisibility(View.VISIBLE);
        // Creating a new User account in firebase with user's email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Creating user object and passing user input as parameters
                    User user = new User(
                            userName,
                            email,
                            occupation,
                            gender,
                            dob,
                            preference,
                            suggestions,
                            favourites,
                            location
                    );
                    // getting an instance of firebase database using getInstance()
                    // accessign the location in database to write data
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //If task complete navigating from Register Activity to Suggestions Activity
                                        Intent intentveri = new Intent(RegisterActivity.this, Suggestions.class);
                                        startActivity(intentveri);
                                        // Display Toast message "Registration successful"
                                        Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();

                                    } else {
                                        // Display Toast message "Registration failed" if error occurs
                                        Toast.makeText(RegisterActivity.this, "Registration Unsuccessful. Try Again!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    // Setting visibility of progress bar once the registration function is complete
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                    String uid = task.getResult().getUser().getUid();
                    database.collection("users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //If task complete navigating from Register Activity to Suggestions Activity
                                Intent intentveri = new Intent(RegisterActivity.this, Suggestions.class);
                                startActivity(intentveri);
                                // Display Toast message "Registration successful"
                                Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();

                            } else {
                                // Display Toast message "Registration failed" if error occurs
                                Toast.makeText(RegisterActivity.this, "Registration Unsuccessful. Try Again!", Toast.LENGTH_LONG)
                                        .show();
                            }
                            // Setting visibility of progress bar once the registration function is complete
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