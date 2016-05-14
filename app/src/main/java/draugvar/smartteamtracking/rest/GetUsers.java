package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.Set;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.User;

//Return null if group doesn't exist. Else return list of user of group identified by id.

public class GetUsers extends AsyncTask<Long,Void,Set<User>> {

    @Override
    protected Set<User> doInBackground(Long... params) {
        Log.d("Rest","GET on /group/groupId");

        Long groupId = params[0];
        final String url = SmartApplication.serverPath+"/group/"+groupId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Set<User>> groupResponse =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Set<User>>() {});
        Set<User> users = groupResponse.getBody();
        Log.d("Rest", users.toString());
        return users;
    }
}
