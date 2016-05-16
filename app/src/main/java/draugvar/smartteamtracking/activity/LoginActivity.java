package draugvar.smartteamtracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.AuthOrSignupUser;
import draugvar.smartteamtracking.singleton.WorkflowManager;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Facebook Login button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        Log.d("LoginTask","Inside onCreate of LoginActivity");

        realm = Realm.getDefaultInstance();

        // Request Permissions to read data from profile
        assert loginButton != null;
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        // CallbackManager of the Login
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {
                                    // Data received from server
                                    String token = loginResult.getAccessToken().getToken();
                                    String id = object.getString("id");
                                    String name = object.getString("first_name");
                                    String surname = object.getString("last_name");
                                    String mail = object.getString("email");
                                    Log.d("LoginTask", token + " | " + id + " | " + name
                                            + " | " + surname + " | " + mail);

                                    User requestUser = new User();
                                    requestUser.setAuthToken(token);
                                    requestUser.setFacebookId(id);
                                    requestUser.setName(name);
                                    requestUser.setSurname(surname);
                                    requestUser.setEmail(mail);

                                    User responseUser = new  AuthOrSignupUser().execute(requestUser).get();
                                    Log.d("LoginTask","Returned from rest call: "+ responseUser.toString());
                                    realm.beginTransaction();
                                    Myself myself = realm.createObject(Myself.class);
                                    User realmResponseUser = realm.copyToRealm(responseUser);
                                    myself.setUser(realmResponseUser);
                                    realm.commitTransaction();

                                    //Reference to usedId in the WorkflowManager, useful for rest calls
                                    WorkflowManager.getWorkflowManager().setMyselfId(responseUser.getUid());

                                } catch (JSONException | InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, picture, email");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                System.out.println("onCancel");
                Log.d("LoginTask", "LoginActivity onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("LoginActivity onError");
                Log.d("LoginTask", exception.getCause().toString());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
