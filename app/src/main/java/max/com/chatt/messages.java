package max.com.chatt;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by new laptoop on 22/04/2018.
 */

public class messages {

    String message,status;
    Bitmap image , full_image;
    String Full_Image_Path;
    Calendar time;
    int state_id;
    public int Progreess = 0;
    public Long ID ;
    messages(String message,Calendar time , Bitmap image,String status)
    {
        this.status=status;
        this.message=message;
        this.time=time;
        this.image = image;
        this.state_id = R.drawable.wait2;
        Full_Image_Path = null;
    }
    messages(String message,Calendar time,String status)
    {
        this.status=status;
        this.message=message;
        this.time=time;
        this.state_id = R.drawable.wait2;
        Full_Image_Path = null;
    }
    public void setFull_image(Bitmap bitmap)
    {
        this.full_image = bitmap;
    }
    public void setFull_Image_Path(String Path)
    {
        this.Full_Image_Path = Path;
    }
    public void Make_Send()
    {
        this.state_id = R.drawable.send;
    }
    public void Make_Diliverd()
    {
        this.state_id = R.drawable.receive;
    }
    public void Make_Read()
    {
        this.state_id = R.drawable.read;
    }

}
