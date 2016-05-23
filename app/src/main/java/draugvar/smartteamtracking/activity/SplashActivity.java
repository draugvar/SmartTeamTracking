package draugvar.smartteamtracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.AuthOrSignupUser;
import draugvar.smartteamtracking.singleton.WorkflowManager;
import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
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
        */

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("LoginTask","Inside onCreate of SplashActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                            //Reference to usedId in the WorkflowManager, useful for rest calls
                            WorkflowManager.getWorkflowManager().setMyselfId(responseUser.getUid());

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
        });
        thread.start();
        while(!thread.isAlive()){
            finish();
        }
    }
}
