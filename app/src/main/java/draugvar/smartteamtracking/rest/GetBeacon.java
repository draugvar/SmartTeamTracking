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

//Returns list of all beacons, to be stored on client

public class GetBeacon extends AsyncTask<Void,Void,List<Beacon>>{

    @Override
    protected List<Beacon> doInBackground(Void... params) {
        Log.d("Rest","GET on /beacon");

        final String url = SmartApplication.serverPath+"/beacon";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<List<Beacon>> beaconResponse =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Beacon>>() {});
        List<Beacon> beacons = beaconResponse.getBody();
        Log.d("Rest", beacons.toString());
        return beacons;
    }
}
