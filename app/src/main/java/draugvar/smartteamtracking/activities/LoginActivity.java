package draugvar.smartteamtracking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest_async_tasks.AuthOrSignupUser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Facebook Login button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

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
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {
                                    // doing something shish for server
                                    String token = loginResult.getAccessToken().getToken();
                                    String id = object.getString("id");
                                    String name = object.getString("first_name");
                                    String surname = object.getString("last_name");
                                    String mail = object.getString("email");
                                    Log.d("LoginActivity", token + " | " + id + " | " + name
                                            + " | " + surname + " | " + mail);


                                    Realm realm = Realm.getDefaultInstance();
                                    User user = new User();
                                    user.setFacebookId(id);
                                    user.setName(name);
                                    user.setSurname(surname);
                                    user.setEmail(mail);
                                    //new AuthOrSignupUser().execute(user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, picture, email");
                request.setParameters(parameters);
                request.executeAsync();

                /*GraphRequest friend_request = GraphRequest.newMyFriendsRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {

                                ObjectMapper objectMapper = new ObjectMapper();

                                RealmList<User> realm_friends = new RealmList<>();
                                realm.copyToRealm(realm_friends);

                                try {
                                    realm.beginTransaction();
                                    List<User> friends = objectMapper
                                            .readValue(objects.toString(),
                                                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
                                    realm_friends.addAll(friends);
                                    realm.commitTransaction();
                                } catch(IOException e) {
                                    e.printStackTrace();
                                }
                                realm.beginTransaction();
                                user.set(realm_friends);
                                realm.commitTransaction();
                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "id, name");
                friend_request.setParameters(param);
                friend_request.executeAsync();*/

                //new HttpRequestTask().execute();

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                Log.d("LoginActivity", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.d("LoginActivity", exception.getCause().toString());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
