package draugvar.smartteamtracking.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import org.parceler.Parcels;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.User;


public class UpdateGroup extends IntentService {

    public static volatile boolean shouldContinue = true;

    public UpdateGroup() {
        super("UpdateGroup");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (shouldContinue) {
            if (intent != null) {
                Log.d("Interactive", "Inside onHandleIntent. Back from sleep");
                Messenger messenger = intent.getParcelableExtra("messenger");
                Long groupId = intent.getLongExtra("gid", -1);
                Log.d("Interactive", "Received groupId =" + groupId);

                //Rest call
                final String url = SmartApplication.serverPath + "/group/" + groupId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Set<User>> groupResponse = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Set<User>>() {
                });
                Set<User> users = groupResponse.getBody();
                Log.d("Interactive", "Rest call returned group:" + users);

                //Send message to handler
                Message message = Message.obtain();
                Bundle data = new Bundle();
                data.putParcelable("userSet", Parcels.wrap(users));
                message.setData(data);
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Interactive", "Inside onHandleIntent. Going to sleep.");
            SystemClock.sleep(5000);
        }
        Log.d("Interactive", "UpdateGroup IntentService finished");
    }

}
