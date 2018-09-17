package max.com.chatt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class Info_group extends AppCompatActivity {

    static ImageView imageView6;
    EditText editText3;
    Intent act3;
    static String path3;
    String ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);

        editText3=(EditText)findViewById(R.id.groupnameeeeeeee);
        imageView6=(ImageView)findViewById(R.id.imageView6);
        ImageButton button=(ImageButton)findViewById(R.id.imageButton1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent act2 = new Intent(Info_group.this, Pop_image.class);
                act2.putExtra("calling-activity","Info");
                startActivity(act2);
            }
        });

    }

    public void creat_group(View view) {

        act3  = new Intent(Info_group.this, Message_List.class);
        ss=editText3.getText().toString();
        act3.putExtra("name",ss);
        act3.putExtra("img", path3);
        act3.putExtra("calling-activity","Info");
        startActivity(act3);
    }

    static void set_image2(String path)
    {
        path3=path;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView6.setImageBitmap(bitmap);

    }
}
