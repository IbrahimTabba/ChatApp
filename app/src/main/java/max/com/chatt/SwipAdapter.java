package max.com.chatt;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwipAdapter extends PagerAdapter {

    //private int[] images ={R.drawable.send,R.drawable.snap,R.drawable.read};
    private Context context;
    private LayoutInflater layoutInflater;
    public SwipAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getCount() {
        return Data.mydata_img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_swip_adapter,container,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView textView = (TextView)view.findViewById(R.id.no);
        Log.e("position", String.valueOf(position));
        Message temp = Data.mydata_img.get(position);
        if(temp.sender.equals(websocket.user_name))
        {
            imageView.setImageBitmap(BitmapFactory.decodeFile(temp.Path1));
        }
        else
        {
            imageView.setImageBitmap(BitmapFactory.decodeFile(temp.Path2));
        }
        textView.setText("Image : " + (position +1));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}

