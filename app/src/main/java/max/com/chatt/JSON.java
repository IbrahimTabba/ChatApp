package max.com.chatt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CEC on 05-Jun-18.
 */

public class JSON {
    public static JSONObject Create_Reques(String Type , Object... Args) throws JSONException {
        JSONObject Res = new JSONObject() , data = new JSONObject();
        Res.put("type",Type);
        int n = Args.length;
        for(int i = 0 ; i < n ; i++)
        {
            String key = Args[i].toString();
            i++;
            Object val = Args[i];
            if(val instanceof String)
            {
                data.put(key,val.toString());
            }
            else if (val instanceof Integer )
            {
                data.put(key,Integer.parseInt(val.toString()));
            }
            else if ( val instanceof Long)
            {
                data.put(key,Long.parseLong(val.toString()));
            }
            else if(val instanceof Boolean)
            {
                data.put(key,Boolean.parseBoolean(val.toString()));
            }
        }
        Res.put("data",data);
        return Res;
    }
    public static int Binary_Seaech(long ID ,  JSONArray arr )
    {
        try {
            int l = 0, r = arr.length() - 1, mid = (r + l) / 2;
            if(r<l)
                return -1;
            while (r - l > 1) {
                mid = (r + l) / 2;
                if (Long.parseLong(((JSONObject) arr.get(mid)).getString("ID")) <= ID)
                    l = mid;
                else
                    r = mid;
            }
            if (Long.parseLong(((JSONObject) arr.get(l)).getString("ID")) == ID)
                return l;
            else if (Long.parseLong(((JSONObject) arr.get(r)).getString("ID")) == ID)
                return r;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return -1;
    }
}
