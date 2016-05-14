package draugvar.smartteamtracking.rest;

// Invite users in a group identified by groupId. List of users is sent as a list of facebookId in the body.
// Returns null if can't find group, else returns the list of users facebookId's successfully invited.

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.User;

public class InviteUsersToGroup extends AsyncTask<Void,Void,List<String>>{

    private Long groupId;
    private List<String> fbUsers;

    public InviteUsersToGroup(Long groupId, List<String> fbUsers) {
        this.groupId = groupId;
        this.fbUsers = fbUsers;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        Log.d("Rest","POST on /group/{groupId}/invite");

        final String url = SmartApplication.serverPath+"/group/"+this.groupId+"/invite";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray jsonList = new JSONArray(fbUsers);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonList.toString(),headers);

        ResponseEntity<List<String>> stringResponse =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<String>>() {});

        List<String> invitedList = stringResponse.getBody();
        Log.d("Rest", invitedList.toString());
        return invitedList;
    }
}
