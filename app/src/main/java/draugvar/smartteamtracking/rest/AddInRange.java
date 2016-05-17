package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Beacon;
import draugvar.smartteamtracking.data.User;

//Returns true if correctly executed, if can't find either group or beacon returns false
public class AddInRange extends AsyncTask<Void,Void,Boolean> {

    private Long userId;
    private Integer major;
    private Integer minor;

    public AddInRange(Long userId, Integer major, Integer minor) {
        this.userId = userId;
        this.major = major;
        this.minor = minor;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("Rest", "Post on user/{userId}/beacon");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/beacon";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Beacon beacon = new Beacon();
        beacon.setMajor(major);
        beacon.setMinor(minor);
        Log.d("addInRange", beacon.toString());

        Boolean result = restTemplate.postForObject(url, beacon, Boolean.class);
        Log.d("Rest", "Returning " + result);
        return result;
    }
}
