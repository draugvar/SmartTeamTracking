package draugvar.smartteamtracking.rest;

//Creates a new group, adds group creator and returns group identifier. In case of error returns -1.

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import draugvar.smartteamtracking.application.SmartApplication;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.User;

public class CreateGroup extends AsyncTask<Void,Void,Long>{

    private Long userId;
    private Group group;

    public CreateGroup(Long userId, Group group) {
        this.userId = userId;
        this.group = group;
    }


    @Override
    protected Long doInBackground(Void... params) {
        Log.d("Rest","POST on /group");

        final String url = SmartApplication.serverPath+"/group?userId="+this.userId+"&name="+group.getName()+
                            "&lat="+group.getLatCenter()+"&lon="+group.getLonCenter()+"&radius="+group.getRadius();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Long result = restTemplate.postForObject(url,null,Long.class);
        Log.d("Rest","Returning "+result);
        return result;
    }
}
