package com.example.thinkableproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

public class StartActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 100;

    Animation topAnim, bottomAnim;
    ImageView image, mainlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

//        AppUpdateManager mAppUpdateManager = AppUpdateManagerFactory.create(this);
//        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>()
//        {
//            @Override
//            public void onSuccess(AppUpdateInfo result)
//            {
//                if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
//                {
//                    try
//                    {
//                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, StartActivity.this
//                                ,RC_APP_UPDATE);
//
//                    } catch (IntentSender.SendIntentException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        topAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = findViewById(R.id.imageiew);

        image.setAnimation(topAnim);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        mainlogo = findViewById(R.id.mainlogo);

        mainlogo.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentStart = new Intent(StartActivity.this, SignInActivity.class);
                startActivity(intentStart);
                finish();

            }
        }, SPLASH_SCREEN);


    }

    public void startSignup(View view) {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private InstallStateUpdatedListener installStateUpdatedListener =new InstallStateUpdatedListener()
    {
        @Override
        public void onStateUpdate(InstallState state)
        {
            if(state.installStatus() == InstallStatus.DOWNLOADED)
            {
                showCompletedUpdate();
            }
        }
    };

    @Override
    protected void onStop()
    {
        //if(mAppUpdateManager!=null) mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    private void showCompletedUpdate()
    {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"New app is ready!",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();

    }

    @Override

    protected void onResume()
    {
        super.onResume();
//        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>()
//        {
//            @Override
//            public void onSuccess(AppUpdateInfo result)
//            {
//                if(result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
//                {
//                    try
//                    {
//                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, StartActivity.this
//                                ,RC_APP_UPDATE);
//                    } catch (IntentSender.SendIntentException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
    /* we can check without requestCode == RC_APP_UPDATE because
    we known exactly there is only requestCode from  startUpdateFlowForResult() */
        if(requestCode == RC_APP_UPDATE && resultCode != RESULT_OK)
        {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}