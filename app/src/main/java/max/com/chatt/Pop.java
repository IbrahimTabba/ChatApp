package max.com.chatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.view.Gravity.END;

public class Pop extends AppCompatActivity  {
    Image_Compresser im = new Image_Compresser();
    ReciveMessagesListener RML = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        im.context = this;
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int hieght =dm.heightPixels;
        getWindow().setLayout((int)( ActionMenuView.LayoutParams.MATCH_PARENT),(int)(ActionMenuView.LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.BOTTOM);
        RML = Data.RML;
        /*LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(
                inflater.inflate(R.layout.activity_pop, null, false),
                300,
                300,
                true);
        View parent = getWindow().getDecorView().getRootView();

       // View parent = findViewById(R.layout.activity_main);
        // The code below assumes that the root container has an id called 'main'
      pw.showAtLocation( parent , Gravity.CENTER, 0, 0);*/
    }

    int PICK_PHOTO_FOR_AVATAR = 10;

    public void galry(View view) {

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
                RML.Image_Sellected(Path2);
                finish();
                //Send_Image_Message2(Path2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 0 && resultCode == Activity.RESULT_OK)
        {
            // Uri selectedImage = data.getData();
            // imageview.setImageURI(selectedImage);
            String Path2 = im.compressImage(getRealPathFromURI(data.getData().toString()));
            RML.Image_Sellected(Path2);
            finish();
        }
        else if (requestCode == 11 && resultCode == Activity.RESULT_OK)
        {
            RML.File_Sellected(getRealPathFromURI(data.getData().toString()));
            finish();
        }
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
    public void camera(View view) {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    public void select_file(View view) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i,11);
    }

}
