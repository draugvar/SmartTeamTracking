package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;

//Path /user/{userId}/. Simply updates coordinates for user.


public class UpdateUserGPSCoordinates extends AsyncTask<Void,Void,Boolean> {

    private Long userId;
    private Double lat;
    private Double lon;

    public UpdateUserGPSCoordinates(Long userId, Double lat, Double lon) {
        this.userId = userId;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("Rest", "Post on /user/{userId}");
        final String url = SmartApplication.serverPath + "/user/" + userId + "?lat="+lat+"&lon="+lon;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Boolean result = restTemplate.postForObject(url, null, Boolean.class);
        Log.d("Rest", "Returning " + result);
        return result;
    }
}
