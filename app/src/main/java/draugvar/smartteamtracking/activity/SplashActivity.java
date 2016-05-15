package draugvar.smartteamtracking.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.AuthOrSignupUser;
import draugvar.smartteamtracking.singleton.WorkflowManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Realm.setDefaultConfiguration( new RealmConfiguration.Builder(this).build());

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("LoginTask","Inside onCreate of SplashActivity");

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "draugvar.smartteamtracking",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.e("SplashScreen", e.getMessage(), e);
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Realm realm = Realm.getDefaultInstance();
                if(AccessToken.getCurrentAccessToken() == null) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Myself myself = realm.where(Myself.class).findFirst();
                    User user = myself.getUser();
                    try {
                        User responseUser = new AuthOrSignupUser().execute(realm.copyFromRealm(user)).get();
                        if(responseUser != null) {
                            realm.beginTransaction();
                            User userForRealm = realm.copyToRealm(responseUser);
                            myself.setUser(userForRealm);
                            realm.commitTransaction();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
