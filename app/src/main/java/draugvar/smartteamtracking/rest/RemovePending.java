package draugvar.smartteamtracking.rest;


// Path /user/{userId}/{groupId}/refuse. Removes pending relationship. Returns a boolean for expressing success.

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Group;

public class RemovePending extends AsyncTask<Void,Void,Boolean>{

    private Long userId;
    private Long groupId;

    public RemovePending(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("Rest", "Post on /user/{userId}/{groupId}/refuse");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/"+groupId+"/refuse";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Boolean result = restTemplate.postForObject(url, null, Boolean.class);
        Log.d("Rest", "Returning " + result);
        return result;
    }
}
