package max.com.chatt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_Friends extends AppCompatActivity implements Search_Freind_Listener {
    ArrayList<contacts> mydata= new ArrayList<contacts>();
    static ListResources_search listResources;
    static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        websocket.SFL = this;
        setContentView(R.layout.activity_search__friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(getResources().getString(R.string.Search_Friend));

        EditText t=(EditText)findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(new ListResources_search(this));
        listResources = new ListResources_search(this);

        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    JSONObject myjson = JSON.Create_Reques("match_search","query",s.toString());
                    websocket.socket_thread.send(myjson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final contacts contacts = listResources.get_contact(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setMessage("Add Freind");
                alertDialogBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent=new Intent(getApplicationContext(),Home.class);
                        websocket.receiver=listResources.get_contact(position).name;
                        try {
                            JSONObject myjson3 = JSON.Create_Reques("add_freind","username1",websocket.user_name,"username2",contacts.name);
                            websocket.socket_thread.send(myjson3.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //startActivityForResult(intent,position);

                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    public static void fill_contacts3(JSONObject jsonObject)
    {
        listResources.Clear();
        listResources.notifyDataSetChanged();
        try {
            JSONObject Data = jsonObject.getJSONObject("data");
            JSONArray contents=Data.getJSONArray("data");
            for(int i=0;i<contents.length();i++)
            {
                contacts temp = new contacts(contents.get(i).toString(),"user");
                listResources.add_contact(temp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(listResources);
    }

    @Override
    public void Search_Result(JSONObject obj) {
        fill_contacts3(obj);
    }

    @Override
    public void AddFreindResult(JSONObject obj) {
        try {
            boolean Res = obj.getBoolean("Res");
            String User = obj.getString("User");
            if(Res)
            {
                Toast.makeText(this,User+" Added Succsefully",Toast.LENGTH_SHORT).show();
                contacts C = new contacts(User,"user");
                Data.Chats.add(C);
                Data.Messages.put(User,new ArrayList<Record>());
                Data.Messages.get(User).add(new Record(new ArrayList<Message>() , 0));
            }
            else
            {
                Toast.makeText(this,"Cannot Add  "+User , Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        websocket.SFL = null;
    }

    class ListResources_search extends BaseAdapter {

        Context context;

        ListResources_search(Context context) {
            this.context = context;
        }

        public void add_contact(contacts contact) {
            mydata.add(contact);
        }

        public void Clear() {mydata.clear();}

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
            CircleImageView imageView = (CircleImageView) row.findViewById(R.id.user_image);
            contacts temp = mydata.get(position);
            textView.setText(temp.name);
            new DownloadImageTask(imageView).execute(TestHttp.MedisServerip+"/ProfilePic/"+temp.name);
            return row;
        }
    }

}
