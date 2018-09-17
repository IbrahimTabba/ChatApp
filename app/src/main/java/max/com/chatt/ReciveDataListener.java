package max.com.chatt;

import org.json.JSONObject;

/**
 * Created by CEC on 02-Jul-18.
 */

public interface ReciveDataListener {
    public void ReciveData();
    public void LastMessage(Message Mes , String user);
    public void LastMessage(String Mes , String user);
}
