package draugvar.smartteamtracking.activity;

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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.AuthOrSignupUser;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Facebook Login button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        Log.d("LoginTask","Inside onCreate of LoginActivity");


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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
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