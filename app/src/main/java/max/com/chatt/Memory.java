package max.com.chatt;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CEC on 04-Jul-18.
 */

public class Memory {
    public static final int MaxRecSize = 102400 ;
    public static int SizeOf(String Message)
    {
        int Size  = 0;
        try {
            JSONObject JS = new JSONObject(Message);
            if(JS.getString("type").equals("text_message")||JS.getString("type").equals("image_message"))
            {
                Size = SizeOfMessage(Message);
            }
            else if (JS.getString("type").equals("chat_history"))
            {
                Size = SizeOfChatHistory(Message);
            }
            else if(JS.getString("type").equals("file_message"))
            {
                Size = SizeofFile(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  Size;
    }
    private static int SizeOfMessage(String Message)
    {
        int Size = 0;
        int Sz = Message.substring(Message.indexOf("data")).length();
        if(Sz- 5  > 0)
            Size = Sz;
        return  Size;
    }
    private static int SizeOfChatHistory(String ChatHistory)
    {
        int Size = 0;
        String Temp = ChatHistory.substring(ChatHistory.indexOf("data")+1);
        Log.e("Chat Historry " , Temp.substring(Temp.indexOf("data")));
        int Sz = Temp.substring(Temp.indexOf("data")).length();
        if(Sz-9 > 0)
            Size = Sz-9;
        return  Size;
    }
    private static int SizeofFile(String Message)
    {
        int Size = 0;
        int Sz = Message.substring(Message.indexOf("data")).length();
        if(Sz- 5  > 0)
            Size = Sz;
        return  Size;
    }
}
