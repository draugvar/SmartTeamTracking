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
import draugvar.smartteamtracking.data.Group;

//Path /user/{userId}/pending. Returns list of groups user is pending on.

public class GetPendingGroupsOfUsers extends AsyncTask<Void,Void,List<Group>> {

    private Long userId;

    public GetPendingGroupsOfUsers(Long userId) {
        this.userId = userId;
    }

    @Override
    protected List<Group> doInBackground(Void... params) {
        Log.d("Rest", "Get on user/{userId}/pending");
        final String url = SmartApplication.serverPath + "/user/" + userId + "/pending";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<List<Group>> groupResponse =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {});
        List<Group> groups = groupResponse.getBody();
        Log.d("Rest", groups.toString());
        return groups;
    }
}