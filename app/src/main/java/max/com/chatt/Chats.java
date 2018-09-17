package max.com.chatt;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.JarOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.getIntent;
import static android.view.View.GONE;

public class Chats  extends Fragment implements ReciveDataListener {
    FloatingActionButton fab1;
    String name,img;
    public static ArrayList<contacts> mydata=new ArrayList<contacts>();;
    static ListResources listResources3;
    static ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
       return inflater.inflate(R.layout.activity_chats, viewGroup, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        listResources3.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.RDL=null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Message_List.b1=true;
        setHasOptionsMenu(true);
        listView=(ListView)view.findViewById(R.id.list);
        listView.setAdapter(new ListResources(getContext()));
        listResources3=new ListResources(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(view.getContext(),Message_List.class);
                contacts temp  =listResources3.get_contact2(position);
                websocket.receiver=listResources3.get_contact2(position).name;
                intent.putExtra("name", String.valueOf(temp.name));
                intent.putExtra("type", String.valueOf(temp.type));
                intent.putExtra("calling-activity","Chats");
                try {
                    JSONObject myjson3 = JSON.Create_Reques("start_private_chat","username1",websocket.user_name,"username2",temp.name);
                    websocket.socket_thread.send(myjson3.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent,position);
            }
        });
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(),Search_Friends.class));
            }
        });
        listView.setAdapter(listResources3);
        Data.RDL=this;
        GetData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.creat_group:
                startActivity(new Intent(getContext(),Add_for_group.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void GetData()
    {
        mydata = Data.Chats;
        listResources3.notifyDataSetChanged();
        GetLastMessages();
    }
    public void GetLastMessages()
    {
        for(int i = 0 ; i < mydata.size() ; i++)
        {
            contacts val = mydata.get(i);
            String name = val.name;
            if(Data.Messages.containsKey(name))
            {
                int Len = Data.Messages.get(name).get(0).Messages.size();
                    if(Len>0)
                        mydata.get(i).LastMessage = Data.Messages.get(name).get(0).Messages.get(Len-1).title;
            }
        }
    }
    public void SetLastMessage(Message Mes , String User)
    {
        for(int i = 0 ; i < Data.Chats.size() ; i++)
        {
            if(Data.Chats.get(i).name.equals(User))
            {
                String Message = Mes.title;
                contacts C = Data.Chats.get(i);
                C.LastMessage = Message;
                Data.Chats.remove(i);
                Data.Chats.add(0,C);
                listResources3.notifyDataSetChanged();
                break;
            }
        }
    }
    public void SetLastMessage(String Mes , String User)
    {
        for(int i = 0 ; i < Data.Chats.size() ; i++)
        {
            if(Data.Chats.get(i).name.equals(User))
            {
                String Message = Mes;
                contacts C = Data.Chats.get(i);
                C.LastMessage = Message;
                Data.Chats.remove(i);
                Data.Chats.add(0,C);
                listResources3.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void ReciveData() {
        Log.e("SomeThing","Listedned Succsefully");
        GetData();
    }

    @Override
    public void LastMessage(Message Mes , String user) {
        SetLastMessage(Mes,user);
    }

    @Override
    public void LastMessage(String Mes, String user) {
        SetLastMessage(Mes,user);
    }


    class ListResources extends BaseAdapter
    {
        Context context;
        ListResources(Context context) {
            this.context = context;
        }
        public void add_contact(contacts contact) {
            mydata.add(contact);
        }
        public void Clear() {mydata.clear();}
        contacts get_contact2(int position )
        {
            return mydata.get(position);
        }

        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View row = inflater.inflate(R.layout.list_view, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.name);
            TextView LastM = (TextView) row.findViewById(R.id.lastMessage);
            CircleImageView Profile = (CircleImageView) row.findViewById(R.id.user_image);
            contacts temp = mydata.get(position);
            new DownloadImageTask(Profile).execute(TestHttp.MedisServerip+"/ProfilePic/"+temp.name);
            textView.setText(temp.name);
            LastM.setText(temp.LastMessage);
            return row;
        }
    }
}





