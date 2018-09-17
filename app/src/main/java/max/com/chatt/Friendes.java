package max.com.chatt;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

import java.io.Serializable;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class Friendes extends AppCompatActivity {
    ArrayList<contacts> mydata= new ArrayList<contacts>();
    static ListResources3 listResources3;
    static contacts contacts;
    static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Select contact");

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ListResources3(this));
        listResources3 = new ListResources3(this);
        //contacts = new contacts("",R.drawable.snap);
        try {
            JSONObject myjson = JSON.Create_Reques("friends_list","username",websocket.user_name);
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), Message_List.class);
                contacts = listResources3.get_contact(position);
//                Toast.makeText(Friendes.this,contacts[0].img,Toast.LENGTH_SHORT).show();
                intent.putExtra("name", String.valueOf(contacts.name));
                //intent.putExtra("img", String.valueOf(contacts.img));
                startActivityForResult(intent, position);
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(Friendes.this, Home.class));
    }


    public static void fill_contacts2(JSONObject jsonObject)
    {
        try {
            JSONObject Data = jsonObject.getJSONObject("data");
            JSONArray contents=Data.getJSONArray("data");
            for(int i=0;i<contents.length();i++)
            {
                //contacts = new contacts("",R.drawable.snap);
                contacts.name = contents.get(i).toString();
                //contacts.img = R.drawable.snap;
                listResources3.add_contact(contacts);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(listResources3);
    }

    class ListResources3 extends BaseAdapter {

        Context context;

        ListResources3(Context context) {
            this.context = context;
        }

        public void add_contact(contacts contact) {
            mydata.add(contact);
        }

        contacts get_contact(int position) {
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
            ImageView imageView = (ImageView) row.findViewById(R.id.user_image);
            contacts temp = mydata.get(position);

            textView.setText(temp.name);
            //imageView.setImageResource(temp.img);

            return row;
        }
    }

}