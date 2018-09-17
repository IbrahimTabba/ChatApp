package max.com.chatt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Application;
        import android.os.Bundle;
        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MenuItem.OnMenuItemClickListener;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;

        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;
        import okhttp3.WebSocket;
        import okhttp3.WebSocketListener;
        import okio.ByteString;

public class server extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }*/
    private Button start;
    private TextView output;
    private OkHttpClient client;
    private WebSocket My_Socket = null;
    public static WebSocket ws;
    public  static  String user_name,receiver;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            //webSocket.send("Hello, it's SSaurel !");
           // webSocket.send("What's up ?");
            My_Socket = webSocket;
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            JSONObject message = null;
            try {
                message =   new JSONObject(text);
                //output(message.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                switch (message.getString("type"))
                {
                    case  "text_message" :
                    {
                        final JSONObject finalMessage = message;
                        final String T = text;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Data.ReciveMessage(finalMessage,Memory.SizeOf(T));
                                //Message_List.receive(finalMessage);
                            }
                        }


                        );
                        break;
                        //output("zezeeee");

                    }
                    case  "image_message" :
                    {
                        output("image_Message");
                        final JSONObject finalMessage = message;
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              //Message_List.receive_image(finalMessage);
                                          }
                                      }


                        );
                        //output("zezeeee");

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error rrrr : " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_server);
        //client = new OkHttpClient();
        //start();
        //websocket.connect();
        //send("nuuuuudddddeeessss");
        startActivity(new Intent(server.this,MainActivity.class));
    }

    private void start() {
        Request request = new Request.Builder().url("ws://192.168.43.45:3000").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(server.this, txt, Toast.LENGTH_LONG).show();
                //output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    private void send(String Mess) {
        if (ws != null) {
            ws.send(Mess);
        }
    }
}