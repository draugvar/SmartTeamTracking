package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.User;

public class AuthOrSignupUser extends AsyncTask<User, Void, User> {
   //Either login or signup user. Returns user object. For login only needs token

    @Override
    protected User doInBackground(User... params) {
        try {

            User user = params[0];
            Log.d("Rest","GET on /user. Input user for rest call is: "+user.toString());
            final String url = SmartApplication.serverPath+"/user?token=" + user.getAuthToken()+
                                "&facebookId="+user.getFacebookId()+"&name="+user.getName()+
                                "&surname="+user.getSurname()+"&email="+user.getEmail();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            User userFromServer = restTemplate.getForObject(url, User.class);
            Log.d("Rest", userFromServer.toString());
            return userFromServer;

        } catch (Exception e) {
            Log.e("Rest", e.getMessage(), e);
            return null;
        }
    }
}
