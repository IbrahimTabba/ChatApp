package max.com.chatt;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import com.loopj.android.http.*;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpTrace;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by CEC on 06-Jul-18.
 */

public class TestHttp extends AppCompatActivity {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static boolean Can = true;
    public static ProfileListener PL;

    public static String MedisServerip = "http://192.168.0.5:8000";
    public static void MakeRequest(final String Reciver , final String Sender  , final String FileName , final Long ID ){
    //String path = "lol";
    //File photo = new File(path);

    //RequestParams params = new RequestParams();
    //try {
    //    params.put("photo", photo);
    //} catch(FileNotFoundException e) {}
        RequestParams Parms = new RequestParams() ;
        Parms.put("name","ibrahim");
        client.get(MedisServerip+"/"+Sender+"/"+Reciver+"/"+FileName, Parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("niniii","Doshtaa");
                //Bu = new ByteArrayInputStream()
                ByteArrayInputStream Bt = new ByteArrayInputStream(bytes);
                Log.e("F  :" , FileName);
                try {
                    String Path = Environment.getExternalStorageDirectory()
                            + "/Android/data/"
                            + Main2Activity.cntx.getApplicationContext().getPackageName()
                            + "/Files/Compressed/"+FileName;
                    FileOutputStream out = new FileOutputStream(Path);
                    out.write(bytes);
                    out.close();
                    Data.Image_Downloaded(ID , Sender , Path);
                } catch (FileNotFoundException e) {
                    Log.e("Exp : " , e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Exp : " , e.toString());
                    e.printStackTrace();
                }
                //String Res = new String(bytes);
                //Log.e("LOL",Integer.toString(bytes.length));
                //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Main2Activity.im.setImageBitmap(bmp);
                //if(Can)
                //{
                //    Can = false;
                //    //TestHttp.SendPost(bytes);
                //}
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("Faiiiilllllll","Doshtaa");
                String S = new String(bytes);
                Log.e("Fail Mesasge",S);

            }
        });
        /*client.post("http://192.168.0.5:8000/", Parms, new JsonResponseHandler()
        {

        });*/
        /*client.post("http://server.com/upload", params, new JsonHttpResponseHandler() {
        ProgressDialog pd;
        @Override
        public void onStart() {
            //String uploadingMessage = getResources().getString(R.string.uploading);
            //pd = new ProgressDialog(MovieActivity.this);
            //pd.setTitle(R.string.please_wait);
            ////pd.setMessage(uploadingMessage);
            //pd.setIndeterminate(false);
           // pd.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            //String status = JsonUtil.getJsonItem(response, "status");
            //String photoID = JsonUtil.getJsonItem(response, "photoID");
            //Toast.makeText(MovieActivity.this, status, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {
            pd.dismiss();
        }
    });*/

    }
    public static void SendPost(String User , String Freind  , String Path , final String Message )
    {
        /*int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] byteArray;
        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteArray = byteBuffer.array();*/
        /*File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + Main2Activity.cntx.getApplicationContext().getPackageName()
                + "/Files/Compressed/2.mp4");*/
        File mediaStorageDir = new File(Path);
        RequestParams Parms = new RequestParams() ;
        //ByteArrayInputStream BS = new ByteArrayInputStream(byteArray);
        try {
            Parms.put("Sex","Anal");
            Parms.put("File",mediaStorageDir);
            Parms.put("Anal","Sex");
        } catch (FileNotFoundException e) {
            Log.e("Fail",e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.e("Fail",e.getMessage());
        }
        client.post(MedisServerip+"/"+User+"/"+Freind,Parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String S = new String(bytes);
                if(S.equals("Uploaded"))
                {
                    websocket.socket_thread.send(Message);
                }
                Log.e("Naniiii",S);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                Log.e("Kzaa", "Bytes Written "+  Long.toString(bytesWritten) + "Total Size : " + Long.toString(totalSize));
            }
        });

    }
    public static void UploadProfile(String User , String Path  )
    {
        File mediaStorageDir = new File(Path);
        RequestParams Parms = new RequestParams() ;
        try {
            Parms.put("File",mediaStorageDir);
        } catch (FileNotFoundException e) {
            Log.e("Fail",e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.e("Fail",e.getMessage());
        }
        client.post(MedisServerip+"/"+User,Parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String S = new String(bytes);
                if(S.equals("Uploaded"))
                {
                    PL.ProfileUpdted();
                }
                Log.e("Naniiii",S);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                Log.e("Kzaa", "Bytes Written "+  Long.toString(bytesWritten) + "Total Size : " + Long.toString(totalSize));
            }
        });

    }
}
