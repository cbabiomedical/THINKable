package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.thinkableproject.databinding.ActivityUserDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails extends AppCompatActivity {
    FirebaseUser mUser;
    CircleImageView profilePicture;
    AutoCompleteTextView occupation;
    EditText username, emailAddress, dateOfBirth;
    AppCompatButton update;
    RadioButton male, female;
    String gender = "";
    AppCompatImageView camera;
    private DatePickerDialog datePickerDialog;
    private EditText dateButton;
    ActivityUserDetailsBinding binding;
    DatabaseReference reference;
    ImageView occupationSelected;
    FirebaseAuth auth;
    private Uri imageUri;
    private String myUri = "";
    private StorageReference storageProfilePicRif;
    private StorageTask uploadTask;
    boolean isMale;
    boolean isFemale;
    ArrayAdapter<String> arrayAdapter_season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_user_details);
        profilePicture = findViewById(R.id.profilePic);
        username = findViewById(R.id.userName);
        emailAddress = findViewById(R.id.email);
        occupation = findViewById(R.id.suggetion_box);
        dateOfBirth = findViewById(R.id.dob);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        update = findViewById(R.id.update);
        isMale = male.isChecked();
        camera=findViewById(R.id.iv_camera);
        isFemale = female.isChecked();
        occupationSelected = findViewById(R.id.occupation);

        initDatePicker();
        //Setting date text

        arrayAdapter_season = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, Job_items.items);
        occupation.setAdapter(arrayAdapter_season);

        //onClick function of occupation dropdown
        occupationSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling show dropdown method which displays the elements in array adapter
                occupation.showDropDown();
            }
        });
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        readData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                if(imageUri!=null) {
                    uploadProfileImage(imageUri);
                }
            }
        });
        auth=FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicRif= FirebaseStorage.getInstance().getReference().child(mUser.getUid());
        StorageReference profileRef=storageProfilePicRif.child("profilePic.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri=data.getData();
                Log.d("Uri", String.valueOf(imageUri));
                profilePicture.setImageURI(imageUri);

            }
        }
    }

    private void uploadProfileImage( Uri imageUri){
        //Upload Image to Firebase Storage
        StorageReference fileRef=storageProfilePicRif.child("profilePic.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      Picasso.get().load(uri).into(profilePicture);
                  }
              });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDetails.this,"Failed Uploading Image...",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void readData() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Toast.makeText(UserDetails.this, "Successfully Read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        username.setHint(String.valueOf(dataSnapshot.child("userName").getValue()));
                        emailAddress.setHint(String.valueOf(dataSnapshot.child("email").getValue()));
                        gender = String.valueOf(dataSnapshot.child("gender").getValue());
                        if (gender.equals("Male")) {
                            male.setChecked(true);
                        } else {
                            female.setChecked(true);
                        }
                        occupation.setHint(String.valueOf(dataSnapshot.child("occupation").getValue()));
                        dateOfBirth.setHint(String.valueOf(dataSnapshot.child("dob").getValue()));

                    } else {
                        Toast.makeText(UserDetails.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserDetails.this, "Read Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void updateData() {
        String userName = username.getText().toString();
        String email = emailAddress.getText().toString();
        String gender = "";
        String occupationUp = occupation.getText().toString();
        String dob = dateOfBirth.getText().toString();
        if (userName.isEmpty()) {
            userName = username.getHint().toString();

        }
        if (email.isEmpty()) {
            email = emailAddress.getHint().toString();

        }

        if (occupationUp.isEmpty()) {
            occupationUp = occupation.getHint().toString();

        }
        if (dob.isEmpty()) {
            dob = dateOfBirth.getHint().toString();

        }
        if (male.isChecked()) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        HashMap<String, Object> User = new HashMap();
        User.put("userName", userName);
        User.put("email", email);
        User.put("gender", gender);
        User.put("occupation", occupationUp);
        User.put("dob", dob);

        reference.child(mUser.getUid()).updateChildren(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserDetails.this, "User Details Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetails.this, "Failed Updating User Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                dateOfBirth.setText(date);
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
}