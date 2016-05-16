package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;

public class RemoveUserFromGroup extends AsyncTask<Void,Void,Boolean> {

    private Long userId;
    private Long groupId;

    public RemoveUserFromGroup(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("Rest", "Delete on /user/{userId}/{groupId}");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/"+groupId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        restTemplate.delete(url);
        //Always returns true
        Log.d("Rest", "Deleted user from group");
        return true;
    }
}
