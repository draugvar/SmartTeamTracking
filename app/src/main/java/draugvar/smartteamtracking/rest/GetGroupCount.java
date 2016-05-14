package draugvar.smartteamtracking.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;

//Returns -1 if can't find group, else returns number of users of a Group.

public class GetGroupCount extends AsyncTask<Long,Void,Integer> {
    @Override
    protected Integer doInBackground(Long... params) {
        Log.d("Rest","GET on /group/groupId/count");

        Long groupId = params[0];
        final String url = SmartApplication.serverPath+"/group/"+groupId+"/count";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Integer result =  restTemplate.getForObject(url, Integer.class);
        Log.d("Rest","Returning "+result);
        return result;
    }
}
