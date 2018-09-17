package max.com.chatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Add_for_group extends Activity implements ReciveDataListener{

    ListView myList;
    Button getChoice;
    String[] listContent = {};
    public ArrayList<contacts> mydata_for_group=new ArrayList<contacts>();
    static ListResources listResources4;
    static contacts contact;
    private static int save = -1;
    static boolean[] colors;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_for_group);

        myList = (ListView)findViewById(R.id.list);
        listResources4=new ListResources(this);
        getChoice = (Button)findViewById(R.id.getchoice);
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myList.setAdapter(listResources4);

        myList.setBackgroundColor(Color.WHITE);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(colors[position]==false) {
                        parent.getChildAt(position).setBackgroundColor(Color.GRAY);
                        colors[position]=true;
                    }
                    else if( colors[position]==true) {
                        parent.getChildAt(position).setBackgroundColor(Color.WHITE);
                        colors[position]=false;
                    }

            }
        });
        getChoice.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                String selected = "";
                int cntChoice = myList.getCount();
                SparseBooleanArray sparseBooleanArray = myList.getCheckedItemPositions();
                for(int i = 0; i < cntChoice; i++){
                    if(sparseBooleanArray.get(i)) {
                        selected += myList.getItemAtPosition(i).toString() + "\n";
                    }
                }
                Toast.makeText(Add_for_group.this, selected, Toast.LENGTH_LONG).show();
                startActivity(new Intent(Add_for_group.this,Info_group.class));

            }});

        myList.setAdapter(listResources4);
        Data.RDL=this;
        GetData();

    }


    public void GetData()
    {

        colors=new boolean[Data.Chats.size()];
        for(int i=0;i<Data.Chats.size();i++)
        {
            Log.e("size",Integer.toString(Data.Chats.size()));
            contacts contact = new contacts(Data.Chats.get(i).name,"group");
            listResources4.add_contact(contact);
        }
        myList.setAdapter(listResources4);
    }

    @Override
    public void ReciveData() {

    }

    @Override
    public void LastMessage(Message Mes, String user) {

    }

    @Override
    public void LastMessage(String Mes, String user) {

    }

    class ListResources extends BaseAdapter
    {
        Context context;
        ListResources(Context context) {
            this.context = context;
        }
        public void add_contact(contacts contact) {
            mydata_for_group.add(contact);
        }
        public void Clear() {mydata_for_group.clear();}
        contacts get_contact2(int position )
        {
            return mydata_for_group.get(position);
        }

        @Override
        public int getCount() {
            return mydata_for_group.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata_for_group.get(position);
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
            ImageView imageView = (ImageView) row.findViewById(R.id.user_image);
            contacts temp = mydata_for_group.get(position);
            textView.setText(temp.name);
            LastM.setText(temp.LastMessage);
            //imageView.setImageResource(temp.img);
            return row;
        }
    }
}
