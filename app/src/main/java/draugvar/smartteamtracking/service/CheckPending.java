package draugvar.smartteamtracking.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.singleton.WorkflowManager;
import io.realm.Realm;
import io.realm.RealmList;

public class CheckPending extends IntentService {

    private Realm realm;
    private static final int checkFrequency = 2000;

    public CheckPending(String name) {
        super(name);
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "Started CheckPending service");
        Long userId = realm.where(Myself.class).findFirst().getUser().getUid();

        while(true){        //Dovremmo fare finchè siamo nella activity, registrata nel singleton.
            SystemClock.sleep(checkFrequency);
            checkPendingOnServer(userId);
        }
    }

    //Cannot use async task, must perform a rest call directly
    public void checkPendingOnServer(Long userId){
        Log.d("Service", "Get on user/{userId}/groups");

        String url = SmartApplication.serverPath + "/user/" + userId + "/groups";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<List<Group>> groupResponse =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {});
        List<Group> groups = groupResponse.getBody();
        Log.d("Service", groups.toString());


    }
}
