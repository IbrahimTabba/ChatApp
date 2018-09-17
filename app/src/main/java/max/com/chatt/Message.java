package max.com.chatt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CEC on 15-Jul-18.
 */

public class Message {
    public String title , date_sent , date_received , type , Path1 , Path2 , fileName , Thumbnail , sender , reciver ;
    public long ID1 , ID2 , Record ;
    public boolean Downloaded , sent , delivered , readed , Deleted ;
    Message(String title , String date_sent , String type , String sender , String receiver , long ID1 , long ID2)
    {
        this.sender = sender ; this.reciver = receiver;
        this.title = title ; this.date_sent = date_sent ; this.type = type ; this.ID1 = ID1 ; this.ID2 = ID2;
        this.Downloaded = this.sent = this.delivered = this.readed = false;
        this.date_received = this.Path1 = this.Path2 = this.fileName = this.Thumbnail =  "" ;
        this.Record = -1;
        this.Deleted = false;
    }
    Message(String title , String date_sent , String type , String sender , String receiver , long ID1 , long ID2 ,
            String Thumbnail , String fileName , String Path1 , String Path2 )
    {
        this.sender = sender ; this.reciver = receiver;
        this.title = title ; this.date_sent = date_sent ; this.type = type ; this.ID1 = ID1 ; this.ID2 = ID2;
        this.Downloaded = this.sent = this.delivered = this.readed = false;
        this.date_received = "";
        this.Path1 = Path1 ; this.Path2 = Path2 ; this.fileName = fileName ; this.Thumbnail = Thumbnail;
        this.Record = -1;
        this.Deleted = false;
    }
    Message(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            this.sender = data.getString("sender");
            this.reciver = data.getString("reciver");
            this.title = data.getString("title");
            this.date_sent = data.getString("date_sent");
            this.date_received = data.getString("date_received");
            this.type = data.getString("type");
            this.ID1 = data.getLong("ID1");
            this.ID2 = data.getLong("ID2");
            this.Record = data.getLong("Record");
            this.sent = data.getBoolean("sent");
            this.delivered = data.getBoolean("delivered");
            this.readed = data.getBoolean("readed");
            this.Deleted = data.getBoolean("Deleted");
            this.Path2 = this.Path1 = this.Thumbnail = this.fileName = "";
            this.Downloaded = false;
            if(this.type.equals("image_message") || this.type.equals("file_message"))
            {
                this.Path1 = data.getString("Path1");
                this.Path2 = data.getString("Path2");
                this.Thumbnail = data.getString("Thumbnail");
                this.fileName = data.getString("fileName");
                this.Downloaded = data.getBoolean("Downloaded");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Message(JSONObject data , int val)
    {
        try {
            this.sender = data.getString("sender");
            this.reciver = data.getString("reciver");
            this.title = data.getString("title");
            this.date_sent = data.getString("date_sent");
            this.date_received = data.getString("date_received");
            this.type = data.getString("type");
            this.ID1 = data.getLong("ID1");
            this.ID2 = data.getLong("ID2");
            this.Record = data.getLong("Record");
            this.sent = data.getBoolean("sent");
            this.delivered = data.getBoolean("delivered");
            this.readed = data.getBoolean("readed");
            this.Deleted = data.getBoolean("Deleted");
            this.Path2 = this.Path1 = this.Thumbnail = this.fileName = "";
            this.Downloaded = false;
            if(this.type.equals("image_message") || this.type.equals("file_message"))
            {
                this.Path1 = data.getString("Path1");
                this.Path2 = data.getString("Path2");
                this.Thumbnail = data.getString("Thumbnail");
                this.fileName = data.getString("fileName");
                this.Downloaded = data.getBoolean("Downloaded");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Message> fromJsonArray(JSONArray arr)
    {
        ArrayList<Message> Mes = new ArrayList<>();
        for(int i = 0 ; i < arr.length() ; i++)
        {
            try
            {
                Mes.add(new Message((JSONObject) arr.get(i),0));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return Mes;
    }
    public JSONObject toJson()
    {
        try
        {
            if(this.type.equals("text_message"))
            {
                /*return JSON.Create_Reques(this.type,"title",this.title,"sender",this.sender , "reciver" , this.reciver ,
                        "date_sent",this.date_sent,"date_received",this.date_received,
                        "type",this.type,"ID1",this.ID1,"ID2",this.ID2,"sent",this.sent,"delivered",this.delivered,
                        "readed",this.readed,"Record",this.Record);*/
                return JSON.Create_Reques(this.type,"title",this.title,"sender",this.sender , "reciver" , this.reciver ,
                        "date_sent",this.date_sent,"date_received",this.date_received,
                        "type",this.type,"ID1",this.ID1,"ID2",this.ID2,"sent",this.sent,"delivered",this.delivered,
                        "readed",this.readed,
                        "Path1",this.Path1 , "Path2" , this.Path2 ,"fileName" , this.fileName ,
                        "Thumbnail",this.Thumbnail , "Downloaded" , this.Downloaded , "Record" , this.Record , "Deleted" , false );
            }
            else if(this.type.equals("image_message") || this.type.equals("file_message") )
            {
                return JSON.Create_Reques(this.type,"title",this.title,"sender",this.sender , "reciver" , this.reciver ,
                        "date_sent",this.date_sent,"date_received",this.date_received,
                        "type",this.type,"ID1",this.ID1,"ID2",this.ID2,"sent",this.sent,"delivered",this.delivered,
                        "readed",this.readed,
                        "Path1",this.Path1 , "Path2" , this.Path2 ,"fileName" , this.fileName ,
                        "Thumbnail",this.Thumbnail , "Downloaded" , this.Downloaded , "Record" , this.Record , "Deleted" , false );
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        String Res = "";
        Res += "title  : " + this.title;
        Res += "\n sender : " + this.sender;
        Res += "\n type : " + this.type;
        Res += "\n receiver : " + this.reciver;
        Res += "\n date_sent : " + this.date_sent;
        Res += "\n date_received : " + this.date_received;
        Res += "\n ID1 : " + String.valueOf(this.ID1);
        Res += "\n ID2 : " + String.valueOf(this.ID2);
        Res += "\n sent : " + String.valueOf(this.sent);
        Res += "\n delivered : " + String.valueOf(this.delivered);
        Res += "\n readed : " + String.valueOf(this.readed);
        Res += "\n Downloaded : " + String.valueOf(this.Downloaded);
        Res += "\n Path1 : " + this.Path1;
        Res += "\n Path2 : " + this.Path2;
        Res += "\n fileName : " + this.fileName;
        Res += "\n Record : " + this.Record;
        Res += "\n Deleted : " + String.valueOf(this.Deleted);
        return Res;
        //return super.toString();
    }
    public static int Binary_Search(ArrayList<Message> Arr , long ID)
    {
        int l = 0, r = Arr.size() - 1, mid ;
        if(r<l)
            return -1;
        while (r - l > 1)
        {
            mid = (r + l) / 2;
            if(Arr.get(mid).ID1==ID)
                return mid;
            else if (Arr.get(mid).ID1 < ID)
                l = mid;
            else
                r = mid;
        }
        if (Arr.get(l).ID1 == ID)
            return l;
        else if (Arr.get(r).ID1 == ID)
            return r;
        return -1;
    }
}
