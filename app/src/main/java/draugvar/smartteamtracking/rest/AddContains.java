package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Group;

// Path /user/{userId}/{groupId}/accept. Adds relationship between user and group, removes pending and returns group

public class AddContains extends AsyncTask<Void,Void,Group> {

    private Long userId;
    private Long groupId;

    public AddContains(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    protected Group doInBackground(Void... params) {
        Log.d("Rest", "Post on /user/{userId}/{groupId}/accept");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/"+groupId+"/accept";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Group result = restTemplate.postForObject(url, null, Group.class);
        Log.d("Rest", "Returning " + result);
        return result;
    }
}
