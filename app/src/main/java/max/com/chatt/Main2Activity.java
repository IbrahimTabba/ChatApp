package max.com.chatt;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.*;
import static android.content.Intent.getIntent;

public class Main2Activity extends AppCompatActivity implements LoginLitener {

    ProgressBar PB ;
    EditText password;
    public static EditText username;
    public static Context cntx;
    public static ImageView im ;
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private static final int REQUEST_ID_CAMERA = 300;
    private static final int REQUEST_READ_ENTERNAL = 400;
    private static final int REQUEST_WRITE_ENTERNAL = 400;
    JSONObject myjson,info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //HttpClient Hp ;
        super.onCreate(savedInstanceState);
        if(Data.LogedIn)
        {
            startActivity(new Intent(this,Home.class));
            finish();
        }
        websocket.LS = this;
        this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
       // this.askPermission(REQUEST_READ_ENTERNAL,
        //        Manifest.permission.);
        setContentView(R.layout.activity_main2);
        TextView signup = (TextView)findViewById(R.id.signup);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        PB = (ProgressBar) findViewById(R.id.progressBar);
        im = (ImageView)  findViewById(R.id.imageView4);
        cntx = this.getApplicationContext();
        //new DownloadImageTask((ImageView) findViewById(R.id.imageView4))
        //        .execute("http://192.168.0.5:8000/ibrahim_tabba/ahmad_kreez/1");
        //TestHttp.MakeRequest();
        //Bitmap bm = ((BitmapDrawable)im.getDrawable()).getBitmap();
        //im.setImageBitmap(bm);
        //TestHttp.SendPost();
        //TestHttp.SendPost( ((BitmapDrawable)im.getDrawable()).getBitmap());
    }

    public void onclick(View view) {
       startActivity(new Intent(Main2Activity.this,MainActivity.class));
        finish();
    }

    public void onclick1(View view)
    {
        try {
            PB.setVisibility(View.VISIBLE);
            myjson = JSON.Create_Reques("login","username",username.getText().toString(),"password",password.getText().toString());
            websocket.islogedon = true;
            websocket.password = password.getText().toString();
            websocket.user_name=username.getText().toString();
            websocket.socket_thread.send(myjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //finish();
    }
    public static boolean Login(final JSONObject obj) throws JSONException {
        JSONObject data = obj.getJSONObject("data");
        boolean statue = data.getBoolean("statue");
        String details = data.getString("details");
        Long ID = data.getLong("ID");
        if(ID > Data.ID)
            Data.ID = ID;
        Log.e("I Recived Id And It Is " , String.valueOf(ID));
        Toast.makeText(cntx,details,Toast.LENGTH_LONG).show();
        return statue;
    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                String txt = intent.getExtras().getString("connection_established");
            Toast.makeText(Main2Activity.this,txt,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(mReceiver, new IntentFilter(websocket.BROADCAST_FILTER));
    }
    @Override
    protected void onDestroy() {
        //unregisterReceiver(mReceiver);
        super.onDestroy();
        websocket.LS = null;
    }
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    @Override
    public void Logedin(JSONObject obj) {
        JSONObject data = null;
        try {
            data = obj.getJSONObject("data");
            boolean statue = data.getBoolean("statue");
            String details = data.getString("details");
            Toast.makeText(this,details,Toast.LENGTH_SHORT).show();
            PB.setVisibility(View.GONE);
            if(statue)
            {
                Data.LogedIn = true;
                Long ID = data.getLong("ID");
                if(ID > Data.ID)
                    Data.ID = ID;
                startActivity(new Intent(this,Home.class));
                Data.first_name = obj.getString("first_name");
                Data.last_name = obj.getString("last_name");
                JSONObject LoginPackage = obj.getJSONObject("LoginPackage");
                JSONArray chat_list = LoginPackage.getJSONArray("chat_list");
                JSONArray chat_history = LoginPackage.getJSONArray("chat_history");
                Data.setChats2(chat_list);
                for(int i = 0 ; i < chat_list.length() ; i++)
                {
                    String User = chat_list.getString(i);
                    Data.setChatMessages2(chat_history.getJSONArray(i),User,chat_history.getJSONArray(i).toString().length());
                }
                Data.setBlocklist(LoginPackage.getJSONArray("blocklist"));
                Data.setBlockedby(LoginPackage.getJSONArray("blockedby"));
                //JSONArray chat_list
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
