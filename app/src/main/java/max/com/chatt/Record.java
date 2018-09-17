package max.com.chatt;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CEC on 05-Jul-18.
 */

public class Record {
    public ArrayList <Message> Messages ;
    public int Size ;
    long First_id , Last_id;
    public Record(JSONArray arr , int Size)
    {
        this.Messages = Message.fromJsonArray(arr);
        this.Size = Size;
        this.First_id = this.Last_id = -1;
        if(Messages.size()>0)
        {
            this.First_id = Messages.get(0).ID1;
            this.Last_id = Messages.get(Messages.size()-1).ID1;
        }
    }
    public Record(ArrayList<Message> Mes , int Size)
    {
        this.Messages = Mes;
        this.Size = Size;
        this.First_id = this.Last_id = -1;
        if(Messages.size()>0)
        {
            this.First_id = Messages.get(0).ID1;
            this.Last_id = Messages.get(Messages.size()-1).ID1;
        }
    }
    public void Add_Message(JSONObject obj , int Size)
    {
        Message Temp = new Message(obj);
        Messages.add(Temp);
        this.Size+=Size;
        this.Last_id = Temp.ID1;
    }
    public void Add_Message(Message Mes , int Size)
    {
        Messages.add(Mes);
        this.Size+=Size;
        this.Last_id = Mes.ID1;
    }
    public static int Binary_Search(Long ID , ArrayList<Record> Arr)
    {
        int l = 0 , r = Arr.size()-1 , mid ;
        if(r<l)
            return -1;
        while (r-l>1)
        {
            mid = (r+l)/2;
            if(Arr.get(mid).First_id <= ID)
                r = mid ;
            else
                l = mid;
        }
        if(Arr.get(r).First_id<=ID)
            return r;
        else if(Arr.get(l).First_id <= ID)
            return l;
        return -1;
    }
}
