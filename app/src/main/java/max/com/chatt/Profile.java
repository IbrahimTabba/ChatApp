package max.com.chatt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile  extends Fragment implements ProfileListener {
    ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){

        return inflater.inflate(R.layout.activity_profile, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TestHttp.PL = this;
        ImageButton button=(ImageButton)view.findViewById(R.id.imageButton);
        imageView=(ImageView)view.findViewById(R.id.imageView6);
        TextView full_name = (TextView)view.findViewById(R.id.full_name);
        TextView name = (TextView)view.findViewById(R.id.name_user);
        full_name.setText(Data.first_name + " " + Data.last_name);
        name.setText(websocket.user_name);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent act2 = new Intent(getContext(), Pop_image.class);
                act2.putExtra("calling-activity","Profile");
                startActivity(act2);
            }
        });
        new DownloadImageTask(imageView).execute(TestHttp.MedisServerip+"/ProfilePic/"+websocket.user_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TestHttp.PL = null;
    }

    static void set_image(String path)
    {


    }

    @Override
    public void ProfileUpdted() {
        new DownloadImageTask(imageView).execute(TestHttp.MedisServerip+"/ProfilePic/"+websocket.user_name);
    }
}
