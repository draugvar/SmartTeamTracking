package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.data.User;

public class AuthOrSignupUser extends AsyncTask<User, Void, User> {
    @Override
    protected User doInBackground(User... params) {
        try {

            User user = params[0];
            Log.d("LoginTask","Input user for rest call is: "+user.toString());
            final String url = "http://amaca.ga:8080/user?token=" + user.getAuthToken()+
                                "&facebookId="+user.getFacebookId()+"&name="+user.getName()+
                                "&surname="+user.getSurname()+"&email="+user.getEmail();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            User userFromServer = restTemplate.getForObject(url, User.class);
            Log.d("LoginTask", userFromServer.toString());
            return userFromServer;

        } catch (Exception e) {
            Log.e("LoginTask", e.getMessage(), e);
            return null;
        }
    }
}
