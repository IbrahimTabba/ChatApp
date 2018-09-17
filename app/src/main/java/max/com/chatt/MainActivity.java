package max.com.chatt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SignupListener {

    TextView login;
    static EditText fullname;
    EditText password;
    EditText username;
    EditText first,secound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        websocket.SL = this;
        if(Data.LogedIn)
        {
            startActivity(new Intent(this,Home.class));
            finish();
        }
        setContentView(R.layout.activity_main);
         login = (TextView)findViewById(R.id.login);
         //fullname=(EditText)findViewById(R.id.);
        first=(EditText)findViewById(R.id.first);
        secound=(EditText)findViewById(R.id.second);
         username=(EditText)findViewById(R.id.username);
         password=(EditText)findViewById(R.id.password);
        websocket.connect();
    }
    public void onclick(View view) {

       startActivity(new Intent(MainActivity.this,Main2Activity.class));
        finish();
    }

    public void onclick1(View view) {

        Log.e("firstName",first.getText().toString());
        Log.e("username",username.getText().toString());
        try {
            JSONObject myjson = JSON.Create_Reques("signup","firstName",first.getText().toString(),"lastName",secound.getText().toString(),"username",username.getText().toString(),
                    "password",password.getText().toString());
            websocket.socket_thread.send(myjson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        websocket.SL = null;
    }

    @Override
    public void Recive(JSONObject obj) {
        try {
            JSONObject data = obj.getJSONObject("data");
            boolean statue = data.getBoolean("verdict");
            String description = data.getString("description");
            Toast.makeText(this,description,Toast.LENGTH_SHORT).show();
            if(statue)
            {
                startActivity(new Intent(this,Main2Activity.class));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
