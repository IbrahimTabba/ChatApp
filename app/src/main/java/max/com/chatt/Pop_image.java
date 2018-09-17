package max.com.chatt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
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
import static max.com.chatt.Info_group.set_image2;
import static max.com.chatt.Profile.set_image;

public class Pop_image extends AppCompatActivity {

    static boolean d1,d2;
    Image_Compresser im = new Image_Compresser();
    ReciveMessagesListener RML = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_image);
        im.context = this;
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int hieght =dm.heightPixels;
        getWindow().setLayout((int)( ActionMenuView.LayoutParams.MATCH_PARENT),(int)(ActionMenuView.LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.BOTTOM);
        RML = Data.RML;
    }

    int PICK_PHOTO_FOR_AVATAR = 10;

    public void galry2(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream inputStream  , inputStream3 , inputStream4;
        String callingActivity =getIntent().getExtras().getString("calling-activity");
        Log.e("string",callingActivity);
        switch (callingActivity) {
            case "Profile": {
                if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK ) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    try {
                        //Intent intent = new Intent(Pop_image.this, Profile.class);
                        inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                        String Path2 = im.compressImage2(getRealPathFromURI(data.getData().toString()));
                        TestHttp.UploadProfile(websocket.user_name,Path2);
                        finish();

                /*intent.putExtra("img", Path2 );
                startActivity(intent);*/

                        //Send_Image_Message2(Path2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 0 && resultCode == Activity.RESULT_OK ) {
                    // Uri selectedImage = data.getData();
                    // imageview.setImageURI(selectedImage);
                    String Path2 = im.compressImage2(getRealPathFromURI(data.getData().toString()));
                    TestHttp.UploadProfile(websocket.user_name,Path2);
                    finish();
                }
                break;
            }
            case "Info": {
                if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK ) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    try {
                        //Intent intent = new Intent(Pop_image.this, Profile.class);
                        inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                        String Path2 = im.compressImage(getRealPathFromURI(data.getData().toString()));
                        set_image2(Path2);
                        finish();

                /*intent.putExtra("img", Path2 );
                startActivity(intent);*/

                        //Send_Image_Message2(Path2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 0 && resultCode == Activity.RESULT_OK ) {
                    // Uri selectedImage = data.getData();
                    // imageview.setImageURI(selectedImage);
                    String Path2 = im.compressImage(getRealPathFromURI(data.getData().toString()));
                    set_image2(Path2);
                    finish();
                }
                break;
            }
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
}
