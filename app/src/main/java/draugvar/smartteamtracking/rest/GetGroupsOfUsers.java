package draugvar.smartteamtracking.rest;


import android.os.AsyncTask;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Beacon;
import draugvar.smartteamtracking.data.Group;

//Path /user/{userId}/groups. Returns list of groups user is part of.

public class GetGroupsOfUsers extends AsyncTask<Void,Void,List<Group>>{

    private Long userId;

    public GetGroupsOfUsers(Long userId) {
        this.userId = userId;
    }

    @Override
    protected List<Group> doInBackground(Void... params) {
        Log.d("Rest", "Get on user/{userId}/groups");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/groups";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<List<Group>> groupResponse =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {});
        List<Group> groups = groupResponse.getBody();
        Log.d("Rest", groups.toString());
        return groups;
    }
}
