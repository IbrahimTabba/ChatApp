package max.com.chatt;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Path;
        import android.media.ThumbnailUtils;
        import android.net.Uri;
        import android.os.Build;
        import android.os.StrictMode;
        import android.provider.MediaStore;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.view.ActionMode;
        import android.support.v7.widget.Toolbar;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.text.format.DateUtils;
        import android.util.Base64;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.Display;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.lang.reflect.Method;
        import java.sql.Time;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.Objects;
        import java.util.TimeZone;


        import cz.msebera.android.httpclient.protocol.HTTP;

        import static android.R.attr.bitmap;
        import static android.R.attr.button;
        import static android.R.attr.constantSize;
        import static android.R.attr.externalService;
        import static android.R.attr.notificationTimeout;
        import static android.R.attr.theme;
        import static android.R.id.message;
        import static java.security.AccessController.getContext;

public class Message_List extends AppCompatActivity implements ReciveMessagesListener {
    public static int num_recored = 0;
    public Message SupposeToDelete;
    static boolean b1,b2;
    static EditText editText;
    static ListView listView;
    private Image_Compresser im = new Image_Compresser();
    public static String name, img;
    public static ListResources2 listResources2;
    public static boolean CanRecive = false;
    public ArrayList<Message> mydata_new = new ArrayList<Message>();
    SimpleDateFormat mdformat = new SimpleDateFormat("h:mm a",Locale.US);
    public static Context cntx;
    static Button button;
    static Button button_img,button_unblock;
    String callingActivity;
    public boolean blocked;
    int[] images_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num_recored = 0;
        CanRecive = true;
        Log.e("record",Integer.toString(num_recored));
        //cntx =this.getApplicationContext();
        cntx = this;
        im.context = Message_List.this;
        num_recored = 0;
        Data.RML = this;
        setContentView(R.layout.activity_message__list);
        images_file= new int[]{R.drawable.doc, R.drawable.pdf, R.drawable.ppt, R.drawable.xls, R.drawable.zip, R.drawable.rtf, R.drawable.wav, R.drawable.gif, R.drawable.jpg, R.drawable.txt, R.drawable.gp, R.drawable.unknow_file};
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        button= (Button) findViewById(R.id.button_chatbox_send);
        button_img=(Button) findViewById(R.id.image_button);
        button_unblock=(Button) findViewById(R.id.un_block);
        listView = (ListView) findViewById(R.id.listofmess);
        ImageView imageView = (ImageView) findViewById(R.id.img);
        TextView textView = (TextView) findViewById(R.id.name);
        callingActivity =getIntent().getExtras().getString("calling-activity");
        switch (callingActivity) {
            case "Chats": {
                name = getIntent().getExtras().getString("name");
                img = getIntent().getExtras().getString("img");
                textView.setText(name);
                //imageView.setImageResource(Integer.parseInt(img));
            break;}
            case "Info": {
                name = getIntent().getExtras().getString("name");
                textView.setText(name);
                img = getIntent().getExtras().getString("img");
                if(img==null)
                    imageView.setImageResource(R.drawable.scarlett);
                else{
                    Bitmap bitmap = BitmapFactory.decodeFile(img);
                    imageView.setImageBitmap(bitmap);
                }

                break;}
        }
        editText = (EditText) findViewById(R.id.edittext_chatbox);
        listResources2 = new ListResources2(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Toast.makeText(Message_List.this,"",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 && listIsAtTop() && CanRecive && Data.Messages.containsKey(name)  ) {
                    CanRecive = false;
                    Toast.makeText(Message_List.this,"I AM On Top" + num_recored,Toast.LENGTH_SHORT).show();
                    GetChatData();
                } else {

                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if( mydata_new.get(position).type.equals("image_message"))
                {
                    int i=0;
                    Intent intent = new Intent(view.getContext(), viewPager.class);
                    while (i < Data.mydata_img.size())
                    {
                        if(Data.mydata_img.get(i).equals(mydata_new.get(position)))
                        {
                            long img = i;
                            Log.e("Krez","is Hmaaaar");
                            intent.putExtra("img", img);
                            break;
                        }
                        else
                            i++;
                    }
                    startActivityForResult(intent, position);
                }
                else if(mydata_new.get(position).type.equals("file_message"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setType("text/plain");
                    File url = null;
                    if(Build.VERSION.SDK_INT>=24)
                    {
                        try {
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        }
                        catch (Exception e)
                        {
                            Log.e("Cant","Disable The Fucking Shotu");
                        }
                    }
                    if(mydata_new.get(position).reciver.equals(websocket.user_name))
                    {
                        if(mydata_new.get(position).Path2.equals(""))
                            return;
                         url = new File(mydata_new.get(position).Path2);
                    }
                    else
                    {
                        if(mydata_new.get(position).Path1.equals(""))
                            return;
                        url = new File(mydata_new.get(position).Path1);
                    }
                    Uri uri = Uri.fromFile(url);
                    if (url.toString().contains(".doc") || url.toString().contains(".docx"))
                    {
                        intent.setDataAndType(uri, "application/msword");
                    }
                    else if(url.toString().contains(".pdf"))
                    {
            // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                    }
                    else if(url.toString().contains(".ppt") || url.toString().contains(".pptx"))
                    {
            // Powerpoint file
                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    }
                    else if(url.toString().contains(".xls") || url.toString().contains(".xlsx"))
                    {
                        // Excel file
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                    }
                    else if(url.toString().contains(".zip") || url.toString().contains(".rar"))
                    {
                        // WAV audio file
                        intent.setDataAndType(uri, "application/x-wav");
                    }
                    else if(url.toString().contains(".rtf"))
                    {
                        // RTF file
                        intent.setDataAndType(uri, "application/rtf");
                    }
                    else if(url.toString().contains(".wav") || url.toString().contains(".mp3"))
                    {
                        intent.setDataAndType(uri, "audio/x-wav");
                    }
                    else if(url.toString().contains(".gif"))
                    {
                    // GIF file
                        intent.setDataAndType(uri, "image/gif");
                    }
                    else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png"))
                    {
                        intent.setDataAndType(uri, "image/jpeg");
                    }
                    else if(url.toString().contains(".txt"))
                    {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");
                    }
                    else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
             // Video files
                        intent.setDataAndType(uri, "video/*");
                    } else
                        {
                        intent.setDataAndType(uri, "*/*");
                    }
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra(Intent.EXTRA_TEXT, "some data");
                    startActivity(Intent.createChooser(intent, "Open with"));
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Message_List.cntx,"LongClick",Toast.LENGTH_LONG).show();
                if(!mydata_new.get(i).Deleted){
                SupposeToDelete = mydata_new.get(i);
                registerForContextMenu( adapterView );
                openContextMenu( adapterView );}
                return true;
            }

            /*@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if( mydata_new.get(position).type.equals("image_message"))
                {
                    int i=0;
                    Intent intent = new Intent(view.getContext(), viewPager.class);
                    while (i < Data.mydata_img.size())
                    {
                        if(Data.mydata_img.get(i).equals(mydata_new.get(position)))
                        {
                            long img = i;
                            Log.e("Krez","is Hmaaaar");
                            intent.putExtra("img", img);
                            break;
                        }
                        else
                            i++;
                    }
                    //  ByteArrayOutputStream bs = new ByteArrayOutputStream();

                    //  img.compress(Bitmap.CompressFormat.PNG, 50, bs);

                    //intent.putExtra("img",img);
                    startActivityForResult(intent, position);
                }
            }*/
        });
        blocked = (Data.Blocklist.contains(name));
        if(blocked)
            Block();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listofmess) {
            MenuInflater inflater = getMenuInflater();
            if(SupposeToDelete.sender.equals(websocket.user_name))
                inflater.inflate(R.menu.message_options, menu);
            else
                inflater.inflate(R.menu.message_options2, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delet_for_me:
            {
                try {
                    JSONObject obj = JSON.Create_Reques("delete_forme","username1",websocket.user_name,
                            "username2",name , "ID2",SupposeToDelete.ID1,"ID1",SupposeToDelete.ID2,
                            "Record",SupposeToDelete.Record);
                    Toast.makeText(Message_List.this,SupposeToDelete.ID1+"",Toast.LENGTH_SHORT).show();
                    Log.e("Req Is :", obj.toString());
                    websocket.socket_thread.send(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
            case R.id.delet_for_me2:
            {
                try {
                    JSONObject obj = JSON.Create_Reques("delete_forme","username1",websocket.user_name,
                            "username2",name , "ID2",SupposeToDelete.ID1,"ID1",SupposeToDelete.ID2,
                            "Record",SupposeToDelete.Record);
                    Toast.makeText(Message_List.this,SupposeToDelete.ID1+"",Toast.LENGTH_SHORT).show();
                    Log.e("Req Is :", obj.toString());
                    websocket.socket_thread.send(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
            case R.id.delet_for_everyone:
            {
                try {
                    JSONObject obj = JSON.Create_Reques("delete_foreveryone","username1",websocket.user_name,
                            "username2",name , "ID2",SupposeToDelete.ID1,"ID1",SupposeToDelete.ID2,
                            "Record",SupposeToDelete.Record);
                    Log.e("Req Is :", obj.toString());
                    websocket.socket_thread.send(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
                //some code
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        callingActivity =getIntent().getExtras().getString("calling-activity");
        switch (callingActivity) {
            case "Info": {
                getMenuInflater().inflate(R.menu.menu3, menu);
                break;
            }
            case "Chats": {
                {
                    if(blocked)
                        getMenuInflater().inflate(R.menu.blocked, menu);
                    else
                        getMenuInflater().inflate(R.menu.menu, menu);

                }

            break;
            }
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.About_group:
                //startActivity(new Intent(this,Start_game.class));
                return true;
            case R.id.Games:
                startActivity(new Intent(this,Start_game.class));
                return true;
            case R.id.Block:
                Block(websocket.user_name,name);
                return true;
            case R.id.Clear_Masseges:
                Clear_chat(websocket.user_name,websocket.receiver);
                return true;
            case R.id.un_block:
            {
                try {
                    JSONObject obj = JSON.Create_Reques("unblock","username1",websocket.user_name,"username2",
                            name);
                    websocket.socket_thread.send(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean listIsAtTop() {
        if (listView.getChildCount() == 0) return true;
        return listView.getChildAt(0).getTop() == 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Data.RML = null;
        Log.e("I Am Destroyed","Kza Mza ");
    }


    public void back(View view) {
        startActivity(new Intent(Message_List.this, Home.class));
    }


    int PICK_PHOTO_FOR_AVATAR = 4;

    public void pickImage() {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream inputStream  , inputStream3 , inputStream4;
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                String Path2 = im.compressImage(getRealPathFromURI(data.getData().toString()));
                Send_Image_Message2(Path2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start_game(View view) {
        startActivity(new Intent(view.getContext(),Start_game.class));
    }

    @Override
    public void Recive() {
        GetChatData();
    }

    @Override
    public void ReciveOneMessage(Message message , Long ID) {
        receive(message , ID);
    }

    @Override
    public void MessageStateChanged(Long ID, int state) {
        listResources2.notifyDataSetChanged();
    }

    @Override
    public void ReciveOneImageMessage(Message message ,  Long ID) {
        receive_image(message,ID);
    }

    @Override
    public void ReciveOneFileMessage(Message Message, Long ID) {
        receive_file(Message,ID);
    }

    @Override
    public void Image_Downloaded(Long ID, String Path) {
        listResources2.notifyDataSetChanged();
    }

    @Override
    public void Image_Sellected(String Path) {
        Send_Image_Message2(Path);
    }

    @Override
    public void File_Sellected(String Path) {
        Send_file(Path);

    }

    @Override
    public void Clear_History() {
        listResources2.clearData();
        listView.setAdapter(listResources2);
    }

    @Override
    public void Block() {
        button.setVisibility(View.GONE);
        button_img.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        button_unblock.setVisibility(View.VISIBLE);
        blocked = true;
    }

    @Override
    public void UnBlock() {
        button.setVisibility(View.VISIBLE);
        button_img.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        button_unblock.setVisibility(View.GONE);
        blocked = false;
    }

    public void Un_Block(View view) {
        try {
            JSONObject obj = JSON.Create_Reques("unblock","username1",websocket.user_name,"username2",
                    name);
            websocket.socket_thread.send(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    int extensions(String extension)
    {
        if(extension.equals(".doc") || extension.equals(".docx"))
            return 0;
        else if(extension.equals(".pdf"))
            return 1;
        else if(extension.equals(".ppt"))
            return 2;
        else if(extension.equals(".xls") || extension.equals(".xlsx"))
            return 3;
        else if(extension.equals(".zip") || extension.equals(".rar"))
            return 4;
        else if(extension.equals(".rtf"))
            return 5;
        else if(extension.equals(".wav") || extension.equals(".mp3"))
            return 6;
        else if(extension.equals(".gif"))
            return 7;
        else if(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png"))
            return 8;
        else if(extension.equals(".txt"))
            return 9;
        else if(extension.equals(".3gp") || extension.equals(".mpg") || extension.equals(".mpeg") || extension.equals(".mp4") || extension.equals(".avi"))
            return 10;
        return 11;
    }

    class ListResources2 extends BaseAdapter
    {
        Context context;
        ListResources2(Context context) {
            this.context = context;
        }
        public void add_message(Message Mes) {
            mydata_new.add(Mes);
        }
        public void add_message(Message Mes , int idx) {
            mydata_new.add(idx,Mes);
        }
        public void clearData() {
            // clear the data
            mydata_new.clear();
        }
        @Override
        public int getCount() {
            return mydata_new.size();
        }
        @Override
        public Object getItem(int position) {
            return mydata_new.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Message temp = mydata_new.get(position);
            boolean sent = (temp.sender.equals(websocket.user_name));
            if (sent)
            {
                View row2 = inflater.inflate(R.layout.item_message_sent, null);
                TextView Title = (TextView) row2.findViewById(R.id.text_message_body);
                TextView Time = (TextView) row2.findViewById(R.id.text_message_time);
                ImageView Statue = (ImageView) row2.findViewById(R.id.report);
                ImageView Image = (ImageView) row2.findViewById(R.id.imageView);

                LinearLayout linearLayout_file=(LinearLayout)row2.findViewById(R.id.file);
                TextView textView_file=(TextView)row2.findViewById(R.id.name_file_sent);
                ImageView imageView_file=(ImageView)row2.findViewById(R.id.img_file_send);


                Title.setText(temp.title);
                if(temp.Deleted)
                {
                    Time.setVisibility(View.GONE);
                    Statue.setVisibility(View.GONE);
                    Image.setVisibility(View.GONE);
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);
                }
                else {
                if(temp.type.equals("text_message"))
                {
                    Image.setVisibility(View.GONE);
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);
                }
                else if(temp.type.equals("image_message"))
                {
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);

                    Title.setVisibility(View.GONE);
                    if(!temp.Path1.equals("") && temp.sent)
                    {
                        Log.e("Image State" , String.valueOf(temp.Downloaded));
                        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(temp.Path1), 200, 200);
                        Image.setImageBitmap(bitmap2);
                    }
                    else if(!temp.Thumbnail.equals(""))
                    {
                        Image.setImageBitmap(Decode(temp.Thumbnail));
                    }
                }
                else if(temp.type.equals("file_message"))
                {
                    Image.setVisibility(View.GONE);
                    Title.setVisibility(View.GONE);
                    textView_file.setText(temp.fileName);
                    String extension=temp.fileName.substring(temp.fileName.lastIndexOf("."));
                    imageView_file.setImageResource(images_file[extensions(extension)]);

                }
                Time.setText(temp.date_sent);
                Statue.setImageResource(R.drawable.wait2);
                if(temp.readed)
                    Statue.setImageResource(R.drawable.read);
                else if(temp.delivered)
                    Statue.setImageResource(R.drawable.receive);
                else if(temp.sent)
                    Statue.setImageResource(R.drawable.send);}
                return row2;
            }
            else
            {
                View row2 = inflater.inflate(R.layout.item_message_received, null);
                TextView Title = (TextView) row2.findViewById(R.id.text_message_body);
                registerForContextMenu(Title);
                TextView Time = (TextView) row2.findViewById(R.id.text_message_time);
                ImageView Image = (ImageView) row2.findViewById(R.id.imageView);

                LinearLayout linearLayout_file=(LinearLayout)row2.findViewById(R.id.file2);
                TextView textView_file=(TextView)row2.findViewById(R.id.name_file_rec);
                ImageView imageView_file=(ImageView)row2.findViewById(R.id.img_file_rec);

                Title.setText(temp.title);
                if(temp.Deleted)
                {
                    Time.setVisibility(View.GONE);
                    Image.setVisibility(View.GONE);
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);

                }
                else {
                if(temp.type.equals("text_message"))
                {
                    Image.setVisibility(View.GONE);
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);

                }
                else if(temp.type.equals("image_message"))
                {
                    if(!temp.Downloaded)
                    {
                        TestHttp.MakeRequest(websocket.user_name , name , temp.fileName,temp.ID1);
                        temp.Downloaded = true;
                    }
                    linearLayout_file.setVisibility(View.GONE);
                    textView_file.setVisibility(View.GONE);
                    imageView_file.setVisibility(View.GONE);

                    Title.setVisibility(View.GONE);
                    if(!temp.Path2.equals(""))
                    {
                        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(temp.Path2), 200, 200);
                        Image.setImageBitmap(bitmap2);
                        //Image.setImageBitmap(BitmapFactory.decodeFile(temp.Path2));
                    }
                    else if(!temp.Thumbnail.equals(""))
                    {
                        Image.setImageBitmap(Decode(temp.Thumbnail));
                    }

                }
                else if(temp.type.equals("file_message")) {

                    Image.setVisibility(View.GONE);
                    Title.setVisibility(View.GONE);
                    textView_file.setText(temp.fileName);
                    String extension=temp.fileName.substring(temp.fileName.lastIndexOf("."));
                    imageView_file.setImageResource(images_file[extensions(extension)]);
                }

                Time.setText(temp.date_received);
                if(!temp.readed)
                {
                    Log.e("I Am","Sending Reading Report");
                    try {
                        JSONObject res = JSON.Create_Reques("read_report","sender",temp.sender,"reciver",temp.reciver,
                                "date_received",temp.date_received,"ID1",temp.ID1,"ID2",temp.ID2 , "Record",temp.Record);
                        websocket.socket_thread.send(res.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                temp.sent = temp.delivered = temp.readed = true;}
                return row2;
            }
        }
        public int Binary_Search(Long ID)
        {
            int l = 0 , r = mydata_new.size()-1 , mid;
            if(r<l)
                return -1;
            while (r-l>1)
            {
                mid = (r+l)/2;
                if(mydata_new.get(mid).ID1<=ID)
                    l = mid;
                else
                    r = mid;
            }
            if(mydata_new.get(l).ID1==ID)
                return l;
            if(mydata_new.get(r).ID1==ID)
                return r;
            return -1;
        }
    }

    //================Send_Methods===========================
    public void Send_Image_Message2(String Path)
    {

        Calendar calendar = Calendar.getInstance();
        //Bitmap bitmap=Decode(Z1);
        //bitmap = Image_Compresser.blur(bitmap,3,cntx);
        Bitmap bitmap = BitmapFactory.decodeFile(Path);
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap, 64, 64);
        try
        {
            bitmap2 = Image_Compresser.blur(bitmap2,3,cntx);
        }
        catch (Exception e)
        {

        }
        String Z1 =  EncodeImage3(bitmap2);
        String FileName = FileNameFromPath(Path);
        Message Mes = new Message("Image",mdformat.format(calendar.getTime()).toString(),"image_message",websocket.user_name,
                name,Data.ID,-1,Z1,FileName,Path,"");
        String Mess = Mes.toJson().toString();
        Data.SendMessgae(Mes,Mess.length()-9);
        listResources2.add_message(Mes);
        listView.setAdapter(listResources2);
        Data.ID++;
        TestHttp.SendPost(websocket.user_name,name,Path,Mess);
        /*if(bitmap==null)
        {
            Toast.makeText(Message_List.this,"image is null",Toast.LENGTH_LONG).show();
            return null;
        }
        messages mm = new messages("image", calendar,bitmap, "");
        try {
            JSONObject myjson = JSON.Create_Reques("image_message","content",Z1,"send_date",mdformat.format(calendar.getTime()).toString(),
                    "receive_date","","reciver", websocket.receiver , "sender",websocket.user_name ,
                    "send",false , "receive2" , false ,"read" , false , "ID" , Data.ID , "file_name" , FileName , "type","image_message","Path",Path);
            Data.SendMessgae(myjson,myjson.toString().length());
            mm.status = "send";
            mm.ID = Data.ID++;
            listResources2.add_message(mm);
            listView.setAdapter(listResources2);
            return  myjson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }
    //----------------

    public void Send_Image(View view) {
        Intent in = new Intent(Message_List.this,Pop.class);
        startActivity(in);

         //pickImage();
    }

    //----------------
    public void send(View view) {
        Calendar calendar = Calendar.getInstance();
        Message Mes = new Message(editText.getText().toString(),mdformat.format(calendar.getTime()).toString(),"text_message",
                websocket.user_name,name,Data.ID,-1);
        editText.setText("");
        listResources2.add_message(Mes);
        listView.setAdapter(listResources2);
        JSONObject obj = Mes.toJson();
        String Str = obj.toString();
        Data.SendMessgae(Mes,Str.length()-9);
        websocket.socket_thread.send(Str);
        Data.ID++;
    }

    public void Send_file(String Path) {

        Calendar calendar = Calendar.getInstance();
        String FileName = FileNameFromPath(Path);
        Message Mes = new Message("File", mdformat.format(calendar.getTime()).toString(), "file_message", websocket.user_name,
                name, Data.ID, -1, "", FileName, Path, "");
        String Mess = Mes.toJson().toString();
        Data.SendMessgae(Mes, Mess.length() - 9);
        listResources2.add_message(Mes);
        listView.setAdapter(listResources2);
        Data.ID++;
        TestHttp.SendPost(websocket.user_name, name, Path, Mess);
    }
    //-----------------

    public static void send_num_recored(int numm_recored)
    {
        try {
            JSONObject myjson = JSON.Create_Reques("chat_history","username1", websocket.user_name ,"username2",websocket.receiver,"indx",numm_recored);
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //================Recive Methods===========================

    static void  Recieve_request_for_play(JSONObject jsonObject)
    {

        //  Toast.makeText(context,"start",Toast.LENGTH_LONG).show();
        //Toast.makeText(cntx,"start",Toast.LENGTH_LONG).show();

        try {
            JSONObject data2 = jsonObject.getJSONObject("data");
            JSONObject data = data2.getJSONObject("data");
            String symbol=data.getString("symbol");
            String P = data.getString("sender");
            Data.Player = P;
            Log.e("Hello",P);
            if(symbol.equals("X"))
                Start_game.s="O";
            else
                Start_game.s="X";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void receive(Message message , Long ID) {
        listResources2.add_message(message);
        listView.setAdapter(listResources2);
        /*Calendar calendar = Calendar.getInstance();
        try {
            messages rr = new messages("", calendar, "");
            rr.message = message.title;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.US);
            Date date = sdf.parse(content.getString("send_date"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            rr.time = cal;
            rr.status = "receive";
            rr.ID = ID;
            listResources2.add_message(rr);
            listView.setAdapter(listResources2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }
    //-----------------
    public static void receive_image(Message message , Long ID) {
        listResources2.add_message(message);
        listView.setAdapter(listResources2);
        TestHttp.MakeRequest(websocket.user_name , name , message.fileName,ID);
        /*Calendar calendar = Calendar.getInstance();
        try {
            JSONObject content = jsonObject.getJSONObject("data");
            Bitmap bitmap = Decode(content.getString("content"));
            messages rr = new messages("image", calendar, bitmap, "");
            rr.message = "image";
            rr.ID = ID;
            rr.image = bitmap;// = content.getString("content");
            String File_Name = content.getString("file_name");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a",Locale.US);
            Date date = sdf.parse(content.getString("send_date"));
            Calendar cal = Calendar.getInstance();
            TestHttp.MakeRequest(websocket.user_name , name , File_Name,ID);
            cal.setTime(date);
            rr.time = cal;
            rr.status = "receive";
            listResources2.add_message(rr);
            listView.setAdapter(listResources2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public static void receive_file(Message message , Long ID) {
        listResources2.add_message(message);
        listView.setAdapter(listResources2);
        TestHttp.MakeRequest(websocket.user_name , name , message.fileName,ID);}


    //-----------------
    public static void GetChatData()
    {
        if(Data.Messages.containsKey(name) &&  num_recored < Data.Messages.get(name).size())
        {
            ArrayList<Message> Temp = Data.Messages.get(name).get(num_recored).Messages;
            if(Temp.size()==0)
                return;
            for(int i=Temp.size()-1;i>=0;i--)
            {
                listResources2.add_message(Temp.get(i),0);
            }
            listView.setAdapter(listResources2);
            listView.setSelection(Temp.size());
            num_recored++;
            CanRecive = true;
        }
        else
        {
            send_num_recored(num_recored);
        }

    }
    //===================  Utilitys  ==========================
    public String FileNameFromPath(String Path)
    {
        int idx = 0;
        for(int i = Path.length()-1 ; i>= 0 ; i--)
        {
            if(Path.charAt(i)=='/')
            {
                idx = i ;
                break;
            }
        }
        return Path.substring(idx+1);
    }
    //__________ Decode Base64 String image __________________________

    public static Bitmap Decode(String imageString) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        String imageString2;
        imageString2 = imageString.substring(imageString.indexOf(",")+1);
        imageBytes = Base64.decode(imageString2, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    // _________ Encode Bitmap to Base64 String Image  _______________

    public String EncodeImage(InputStream input , int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        if(size>51200)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        else
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  "data:image/jpeg;base64,"+imageString;
    }

    public String EncodeImage2(String Path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(Path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  "data:image/jpeg;base64,"+imageString;
    }
    public String EncodeImage3(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Bitmap bitmap = BitmapFactory.decodeFile(Path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  "data:image/jpeg;base64,"+imageString;
    }

    //__________     Size of Stream    _______________________________

    public int size_of_stream(InputStream in) throws IOException {
        int chunk = 0 , size = 0;
        byte[] buffer = new byte[1024];
        while((chunk = in.read(buffer)) != -1){
            size += chunk;
        }
        return size;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static void recieve_Clear_chat(JSONObject jsonObject)
    {

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Boolean statue = data.getBoolean("statue");
            if(statue==true)
            {
                listResources2.clearData();
                listResources2.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void recieve_Block(JSONObject jsonObject)
    {

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Boolean statue = data.getBoolean("statue");
            if(statue==true)
            {
                button.setVisibility(View.GONE);
                button_img.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                button_unblock.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void recieve_UnBlock(JSONObject jsonObject)
    {

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Boolean statue = data.getBoolean("statue");
            if(statue==true)
            {
                button.setVisibility(View.GONE);
                button_img.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                button_unblock.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public  void Block(String user_1,String user_2)
    {
        try {
            JSONObject myjson = JSON.Create_Reques("block","username1", websocket.user_name ,"username2",name);
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void UnBlock(String user_1,String user_2)
    {
        try {
            JSONObject myjson = JSON.Create_Reques("unblock","data","username1", websocket.user_name ,"username2",websocket.receiver);
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public  void Clear_chat(String user_1,String user_2)
    {
        try {
            JSONObject myjson = JSON.Create_Reques("clear_history","username1", websocket.user_name ,"username2",name);
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //============== Un Used Methods ==============================

    //============== Data Class Methods =====================



}

