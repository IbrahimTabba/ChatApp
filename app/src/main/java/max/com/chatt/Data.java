package max.com.chatt;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static max.com.chatt.Message_List.num_recored;

/**
 * Created by CEC on 30-Jun-18.
 */

public class Data {
    public static long ID = 0 ;
    public static String first_name , last_name;
    public static String Player;
    public static Boolean LogedIn = false;
    private  static SimpleDateFormat mdformat = new SimpleDateFormat("h:mm a",Locale.US);
    private static Calendar calendar = Calendar.getInstance();
    public static ReciveDataListener RDL ;
    public static ReciveMessagesListener RML ;
    public static HashMap< String , ArrayList<Record> > Messages = new HashMap<>();
    public static ArrayList<contacts> Chats = new ArrayList<>() ;
    public static ArrayList<Message> mydata_img = new ArrayList<Message>();
    public static ArrayList<String> Blocklist = new ArrayList<>() , Blockedby = new ArrayList<>();
    public static void setChats(JSONObject jsonObject)
    {

        try {
            JSONObject Data = jsonObject.getJSONObject("data");
            JSONArray contents=Data.getJSONArray("data");
            for(int i=0;i<contents.length();i++)
            {
                Chats.add(new contacts(contents.get(i).toString(),"user"));
                //JSONObject myjson = JSON.Create_Reques("chat_history","username1", websocket.user_name ,"username2",contents.get(i).toString(),"indx",0);
                //websocket.socket_thread.send(myjson.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
       if(RDL!=null)
        {
            RDL.ReciveData();
        }
    }
    public static void setChats2(JSONArray contents)
    {

        try {
            for(int i=0;i<contents.length();i++)
            {
                Chats.add(new contacts(contents.get(i).toString(),"user"));
                //JSONObject myjson = JSON.Create_Reques("chat_history","username1", websocket.user_name ,"username2",contents.get(i).toString(),"indx",0);
                //websocket.socket_thread.send(myjson.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(RDL!=null)
        {
            RDL.ReciveData();
        }
    }
    public static void setChatMessages (JSONObject jsonObject , int Size)
    {
        try {
            JSONObject content = jsonObject.getJSONObject("data");
            String User = content.getString("User");
            String Details = content.getString("details");
            Log.e("Record : " , Details);
            if(Details.equals("Records Found"))
            {
                JSONArray chat = content.getJSONArray("data");
                ArrayList<Message> Temp = new ArrayList<>();
                Temp = Message.fromJsonArray(chat);
                for(int i = 0 ; i < Temp.size() ; i++)
                {
                    if(Temp.get(i).type.equals("image_message"))
                        mydata_img.add(Temp.get(i));
                    if(!(Temp.get(i).delivered) && Temp.get(i).reciver.equals(websocket.user_name))
                    {
                        Temp.get(i).delivered = true;
                        String Sender = Temp.get(i).sender;
                        Temp.get(i).ID2 = Temp.get(i).ID1;
                        Temp.get(i).date_received = mdformat.format(calendar.getTime()).toString();
                        Temp.get(i).ID1 = ID;
                        try {
                            JSONObject res = JSON.Create_Reques("delivery_report","sender",Temp.get(i).sender,"reciver",Temp.get(i).reciver,
                                    "date_received",Temp.get(i).date_received,"ID1",Temp.get(i).ID1,"ID2",Temp.get(i).ID2 , "Record" , Temp.get(i).Record);
                            websocket.socket_thread.send(res.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ID++;
                    }
                    Log.e("Recode is " , Temp.get(i).toString());
                }
                if(!Messages.containsKey(User))
                {
                    Messages.put(User,new ArrayList<Record>());
                }
                Messages.get(User).add(new Record(Temp , Size));
                if(chat.length()>0)
                {
                    if (RML != null)
                        RML.Recive();
                    if (Messages.get(User).size() == 1 && chat.length() > 0) {
                        if (RDL != null) {
                            RDL.LastMessage(Temp.get(0), User);
                        }
                    }
                }
            }
            else
            {
                if(!Messages.containsKey(User))
                {
                    Messages.put(User,new ArrayList<Record>());
                    Messages.get(User).add(new Record(new ArrayList<Message>() , 0));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void setChatMessages2 (JSONArray chat , String User ,  int Size)
    {
            if(chat.length()>0)
            {
                ArrayList<Message> Temp = new ArrayList<>();
                Temp = Message.fromJsonArray(chat);
                for(int i = 0 ; i < Temp.size() ; i++)
                {
                    if(Temp.get(i).type.equals("image_message"))
                        mydata_img.add(Temp.get(i));
                    if(!(Temp.get(i).delivered) && Temp.get(i).reciver.equals(websocket.user_name))
                    {
                        Temp.get(i).delivered = true;
                        String Sender = Temp.get(i).sender;
                        Temp.get(i).ID2 = Temp.get(i).ID1;
                        Temp.get(i).date_received = mdformat.format(calendar.getTime()).toString();
                        Temp.get(i).ID1 = ID;
                        try {
                            JSONObject res = JSON.Create_Reques("delivery_report","sender",Temp.get(i).sender,"reciver",Temp.get(i).reciver,
                                    "date_received",Temp.get(i).date_received,"ID1",Temp.get(i).ID1,"ID2",Temp.get(i).ID2 , "Record" , Temp.get(i).Record);
                            websocket.socket_thread.send(res.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ID++;
                    }
                    Log.e("Recode is " , Temp.get(i).toString());
                }
                if(!Messages.containsKey(User))
                {
                    Messages.put(User,new ArrayList<Record>());
                }
                Messages.get(User).add(new Record(Temp , Size));
                if(chat.length()>0)
                {
                    if (RML != null)
                        RML.Recive();
                    if (Messages.get(User).size() == 1 && chat.length() > 0) {
                        if (RDL != null) {
                            RDL.LastMessage(Temp.get(0), User);
                        }
                    }
                }
            }
            else
            {
                if(!Messages.containsKey(User))
                {
                    Messages.put(User,new ArrayList<Record>());
                    Messages.get(User).add(new Record(new ArrayList<Message>() , 0));
                }
            }

    }
    public static void SendMessgae(Message Mes , int Size)
    {
        String Reciver = Mes.reciver;
        if(Mes.type.equals("image_message"))
            mydata_img.add(Mes);
        if(Messages.containsKey(Reciver))
        {
            if(Messages.get(Reciver).get(0).Size + Size <= Memory.MaxRecSize)
            {
                Log.e("Size Of Send Message Is ",Integer.toString(Size));
                Messages.get(Reciver).get(0).Add_Message(Mes,Size);
                Log.e("Messages in List After Add : ","Are ");
                for(int i = 0 ; i < Messages.get(Reciver).get(0).Messages.size() ; i++ )
                {
                    Log.e("Message Kza " , Messages.get(Reciver).get(0).Messages.get(i).toString());
                }
            }
            else
            {
                Log.e("Record","I Created A new Record Because Size Is " + Integer.toString(Size));
                ArrayList<Message> Temp = new ArrayList<>();
                Temp.add(Mes);
                Messages.get(Reciver).add(0,new Record(Temp,Size));
            }
            if(RDL!=null)
            {
                RDL.LastMessage(Mes,Reciver);
            }
        }

    }
    public static void ReciveMessage(JSONObject obj , int Size )
    {

        Message Mes = new Message(obj);
        Mes.delivered = true;
        String Sender = Mes.sender;
        Mes.ID2 = Mes.ID1;
        Mes.date_received = mdformat.format(calendar.getTime()).toString();
        Mes.ID1 = ID;
        if(!Messages.containsKey(Sender))
        {
            contacts C = new contacts(Sender,"user");
            Chats.add(C);
            Data.Messages.put(Sender,new ArrayList<Record>());
            Data.Messages.get(Sender).add(new Record(new ArrayList<Message>() , 0));
            if(RDL!=null)
            {
                RDL.ReciveData();
            }

        }
        if(Messages.containsKey(Sender))
        {
            if(Messages.get(Sender).get(0).Size + Size <= Memory.MaxRecSize)
            {
                Messages.get(Sender).get(0).Add_Message(Mes,Size);
            }
            else
            {
                ArrayList<Message> Temp = new ArrayList<>();
                Temp.add(Mes);
                Messages.get(Sender).add(0,new Record(Temp,Size));
            }
            if(RDL!=null)
            {
                RDL.LastMessage(Mes,Sender);
            }
            if(RML!=null)
            {
                if(Mes.sender.equals(Message_List.name)) {
                    RML.ReciveOneMessage(Mes, ID);
                    final MediaPlayer mp = MediaPlayer.create(Message_List.cntx,R.raw.incoming);
                    mp.start();

                }
            }
            ID++;
        }
        try {
            JSONObject res = JSON.Create_Reques("delivery_report","sender",Mes.sender,"reciver",Mes.reciver,
                    "date_received",Mes.date_received,"ID1",Mes.ID1,"ID2",Mes.ID2 , "Record" , Mes.Record);
            websocket.socket_thread.send(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void Send_Report(JSONObject obj)
    {
        Message Mes = new Message(obj);
        Long id = Mes.ID1;
        String User = Mes.reciver;
        int Rec_idx = Record.Binary_Search(id,Messages.get(User));
        Log.e("Bla ","Send Report For Message User : " + User + " Id  :  " + Long.toString(id));
        if(Rec_idx!=-1)
        {
            Log.e("Bla ","Record Found With Indx  : " + Integer.toString(Rec_idx));
            int Message_id = Message.Binary_Search(Messages.get(User).get(Rec_idx).Messages,id);
            if(Message_id!=-1)
            {
                Log.e("Bla ","Message Found in Record With Indx  : " + Integer.toString(Message_id));
                Messages.get(User).get(Rec_idx).Messages.get(Message_id).sent = true;
                Messages.get(User).get(Rec_idx).Messages.get(Message_id).Record = Mes.Record;
                if(RML!=null)
                {
                    RML.MessageStateChanged(id,0);
                    final MediaPlayer mp = MediaPlayer.create(Message_List.cntx,R.raw.send);
                    mp.start();
                }
            }
        }
    }
    public static void Recive_Image_Message(JSONObject obj , int Size )
    {

        Message Mes = new Message(obj);
        String Sender = Mes.sender;
        Mes.ID2 = Mes.ID1;
        Mes.date_received = mdformat.format(calendar.getTime()).toString();
        Mes.ID1 = ID;
        mydata_img.add(Mes);
        try {
            JSONObject res = JSON.Create_Reques("delivery_report","sender",Mes.sender,"reciver",Mes.reciver,
                    "date_received",Mes.date_received,"ID1",Mes.ID1,"ID2",Mes.ID2,"Record",Mes.Record);
            websocket.socket_thread.send(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Messages.containsKey(Sender))
        {
            if(Messages.get(Sender).get(0).Size + Size <= Memory.MaxRecSize)
            {
                Messages.get(Sender).get(0).Add_Message(Mes,Size);
            }
            else
            {
                ArrayList<Message> Temp = new ArrayList<>();
                Temp.add(Mes);
                Messages.get(Sender).add(0,new Record(Temp,Size));
            }
            if(RDL!=null)
            {
                RDL.LastMessage(Mes,Sender);
            }
            if(RML!=null)
            {
                if(Mes.sender.equals(Message_List.name))
                    RML.ReciveOneImageMessage(Mes , ID);
            }
            ID++;
        }

    }
    public static void Recive_File_Message(JSONObject obj , int Size )
    {

        Message Mes = new Message(obj);
        String Sender = Mes.sender;
        Mes.ID2 = Mes.ID1;
        Mes.date_received = mdformat.format(calendar.getTime()).toString();
        Mes.ID1 = ID;
        try {
            JSONObject res = JSON.Create_Reques("delivery_report","sender",Mes.sender,"reciver",Mes.reciver,
                    "date_received",Mes.date_received,"ID1",Mes.ID1,"ID2",Mes.ID2,"Record",Mes.Record);
            websocket.socket_thread.send(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Messages.containsKey(Sender))
        {
            if(Messages.get(Sender).get(0).Size + Size <= Memory.MaxRecSize)
            {
                Messages.get(Sender).get(0).Add_Message(Mes,Size);
            }
            else
            {
                ArrayList<Message> Temp = new ArrayList<>();
                Temp.add(Mes);
                Messages.get(Sender).add(0,new Record(Temp,Size));
            }
            if(RDL!=null)
            {
                RDL.LastMessage(Mes,Sender);
            }
            if(RML!=null)
            {
                if(Mes.sender.equals(Message_List.name))
                    RML.ReciveOneFileMessage(Mes , ID);
            }
            ID++;
        }

    }
    public static void Image_Downloaded(Long id , String User , String Path)
    {
        Log.e("User is " , User  );
        int Rec_idx = Record.Binary_Search(id,Messages.get(User));
        Log.e("Bla ","Send Report For Message User : " + User + " Id  :  " + Long.toString(id));
        if(Rec_idx!=-1)
        {
            Log.e("Bla ","Record Found With Indx  : " + Integer.toString(Rec_idx));
            int Message_id = Message.Binary_Search(Messages.get(User).get(Rec_idx).Messages,id);
            if(Message_id!=-1)
            {
                Log.e("Bla ","Message Found in Record With Indx  : " + Integer.toString(Message_id));
                Messages.get(User).get(Rec_idx).Messages.get(Message_id).Path2 = Path;
                //Messages.get(User).get(Rec_idx).Messages.get(Message_id).Downloaded = true;
                if(RML!=null)
                {
                    RML.Image_Downloaded(id,Path);
                }
            }
        }

    }
    public static void Delivary_Report(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            String User = data.getString("reciver");
            if(User.equals(websocket.user_name))
            {
                return;
            }
            long id = data.getLong("ID2");
            Log.e("USer",User);
            int Rec_idx = Record.Binary_Search(id,Messages.get(User));
            Log.e("Bla ","Deliver Report For Message User : " + User + " Id  :  " + Long.toString(id));
            if(Rec_idx!=-1)
            {
                Log.e("Bla ","Record Found With Indx  : " + Integer.toString(Rec_idx));
                int Message_id = Message.Binary_Search(Messages.get(User).get(Rec_idx).Messages,id);
                if(Message_id!=-1)
                {
                    Log.e("Bla ","Message Found in Record With Indx  : " + Integer.toString(Message_id));
                    Messages.get(User).get(Rec_idx).Messages.get(Message_id).delivered = true;
                    Messages.get(User).get(Rec_idx).Messages.get(Message_id).ID2 = data.getLong("ID1");
                    if(RML!=null)
                    {
                        RML.MessageStateChanged(id,0);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void Read_Report(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            String User = data.getString("reciver");
            if(User.equals(websocket.user_name))
            {
                return;
            }
            long id = data.getLong("ID2");
            Log.e("USer",User);
            int Rec_idx = Record.Binary_Search(id,Messages.get(User));
            Log.e("Bla ","Read Report For Message User : " + User + " Id  :  " + Long.toString(id));
            if(Rec_idx!=-1)
            {
                Log.e("Bla ","Record Found With Indx  : " + Integer.toString(Rec_idx));
                int Message_id = Message.Binary_Search(Messages.get(User).get(Rec_idx).Messages,id);
                if(Message_id!=-1)
                {
                    Log.e("Bla ","Message Found in Record With Indx  : " + Integer.toString(Message_id));
                    Messages.get(User).get(Rec_idx).Messages.get(Message_id).readed = true;
                    if(RML!=null)
                    {
                        RML.MessageStateChanged(id,0);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void DeleteReport(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            String User = data.getString("username2");
            if(User.equals(websocket.user_name))
                User =  data.getString("username1");
            long id = data.getLong("ID2");
            Log.e("USer",User);
            int Rec_idx = Record.Binary_Search(id,Messages.get(User));
            Log.e("Bla ","Delete Report For Message User : " + User + " Id  :  " + Long.toString(id));
            if(Rec_idx!=-1)
            {
                Log.e("Bla ","Record Found With Indx  : " + Integer.toString(Rec_idx));
                int Message_id = Message.Binary_Search(Messages.get(User).get(Rec_idx).Messages,id);
                if(Message_id!=-1)
                {
                    Log.e("Bla ","Message Found in Record With Indx  : " + Integer.toString(Message_id));
                    Messages.get(User).get(Rec_idx).Messages.get(Message_id).title = "This Message Was Deleted";
                    Messages.get(User).get(Rec_idx).Messages.get(Message_id).Deleted = true;
                    if(RML!=null)
                    {
                        RML.MessageStateChanged(id,0);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void setBlocklist(JSONArray block)
    {
        for(int i = 0 ; i < block.length() ; i++)
        {
            try {
                Blocklist.add(block.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setBlockedby(JSONArray blocked)
    {
        for(int i = 0 ; i < blocked.length() ; i++)
        {
            try {
                Blockedby.add(blocked.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void Clear_History(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            Boolean Res = data.getBoolean("Res");
            String User = data.getString("username2");
            if(Res)
            {
                if(Messages.containsKey(User))
                {
                    Messages.get(User).clear();
                    Messages.put(User,new ArrayList<Record>());
                    Messages.get(User).add(new Record(new ArrayList<Message>() , 0));
                    if(RML!=null)
                    {
                        if(User.equals(Message_List.name))
                        {
                            RML.Clear_History();
                        }
                    }
                    if(RDL!=null)
                    {
                        RDL.LastMessage("",User);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void Block(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            String User = data.getString("username2");
            boolean Res = data.getBoolean("Res");
            if(Res){
            Blocklist.add(User);
            if(RML!=null)
            {
                if(Message_List.name.equals(User))
                {
                    RML.Block();
                }
            }}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void UnBlock(JSONObject obj)
    {
        try {
            JSONObject data = obj.getJSONObject("data");
            String User = data.getString("username2");
            boolean Res = data.getBoolean("Res");
            if(Res){
                Blocklist.remove(User);
                if(RML!=null)
                {
                    if(Message_List.name.equals(User))
                    {
                        RML.UnBlock();
                    }
                }}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
