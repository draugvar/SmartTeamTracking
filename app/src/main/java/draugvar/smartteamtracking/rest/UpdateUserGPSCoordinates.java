package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.User;

//Path /user/{userId}/. Simply updates coordinates for user.


public class UpdateUserGPSCoordinates extends AsyncTask<Void,Void,Boolean> {

    private User user;

    public UpdateUserGPSCoordinates(User user) {
        this.user = user;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("Rest", "Post on /user");
        final String url = SmartApplication.serverPath + "/user";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Boolean result = restTemplate.postForObject(url, user, Boolean.class);
        Log.d("Rest", "Returning " + result);
        return result;
    }
}
