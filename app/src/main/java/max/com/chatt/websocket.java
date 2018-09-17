package max.com.chatt;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static max.com.chatt.Tic_Tac_Toe.context;

/**
 * Created by CEC on 03-Jun-18.
 */

public class websocket extends AppCompatActivity {
    private static String Websocketip = "ws://192.168.0.5:3000";
    public static LoginLitener LS;
    public static SignupListener SL;
    public static Search_Freind_Listener SFL;
    public static final String BROADCAST_FILTER = "ManageConection_broadcast_receiver_intent_filter";
    static socket_thread sk = new socket_thread();
    public  static  String user_name ,receiver,password ;
    public static boolean islogedon = false;
    private static Thread Connect_Thread;
    public static void connect()
    {
        Connect_Thread = new Thread(sk.Connect_Runnable);
        Connect_Thread.start();
    }
    public static class socket_thread extends AppCompatActivity
    {
        private static boolean Connected = false;
        public static  WebSocket ws = null;
        private static OkHttpClient client;
        private final class EchoWebSocketListener extends WebSocketListener {
            private static final int NORMAL_CLOSURE_STATUS = 1000;
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                //error = false;
                Connected = true;
                Connect_Thread.interrupt();
                if(islogedon)
                {
                    try {
                        JSONObject myjson2 = JSON.Create_Reques("login","username",user_name,"password",password);
                        ws.send(myjson2.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*JSONObject object = new JSONObject();
                try {
                    object.put("type","nothing");
                    object.put("data","nothing");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ws.send(object.toString());*/
            }
            @Override
            public void onMessage(WebSocket webSocket, final String text) {
                JSONObject message = null;
                try {
                    message =   new JSONObject(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Message",text);
                try {
                    final JSONObject finalMessage = message;
                    switch (message.getString("type"))
                    {
                        case  "text_message" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Data.ReciveMessage(finalMessage,Memory.SizeOf(text));
                                                  //Message_List.receive(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case  "image_message" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  //Data.ReciveMessage(finalMessage,Memory.SizeOf(text));
                                                  Data.Recive_Image_Message(finalMessage,Memory.SizeOf(text));
                                                  //Message_List.receive_image(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case "chat_history" :
                        {
                            Log.e("Size Of Chat History " , Integer.toString(Memory.SizeOf(text)));
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Data.setChatMessages(finalMessage , Memory.SizeOf(text));
                                                  //Message_List.receive_array(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case "chat_list" :
                        {
                            //Data.setChats(finalMessage);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Data.setChats(finalMessage);
                                                  //Chats.fill_contacts(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case "login":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  if(LS!=null)
                                                  {
                                                      LS.Logedin(finalMessage);
                                                  }
                                              }
                                          }
                            );
                            break;
                        }
                        case "signup":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  if(SL!=null)
                                                  {
                                                      SL.Recive(finalMessage);
                                                  }
                                              }
                                          }
                            );
                            break;
                        }
                        case "friends_list" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Friendes.fill_contacts2(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case "match_search" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  if(SFL!=null)
                                                      SFL.Search_Result(finalMessage);

                                                 // Search_Friends.fill_contacts3(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }
                        case "add_freind" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  if(SFL!=null)
                                                      SFL.AddFreindResult(finalMessage);
                                              }
                                          }
                            );
                            break;
                        }

                        case "StartGame_XO" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  String name_sender ="";
                                                  AlertDialog.Builder alertDialogBuilder = null;
                                                    if(Data.RML!=null)
                                                        alertDialogBuilder = new AlertDialog.Builder(Message_List.cntx);
                                                  else
                                                        alertDialogBuilder = new AlertDialog.Builder(Home.cntx);
                                                  alertDialogBuilder.setMessage("Do you want to play with your friend :" +" "+ name_sender);
                                                  alertDialogBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                                                      @Override
                                                      public void onClick(DialogInterface arg0, int arg1) {
                                                          Message_List.Recieve_request_for_play(finalMessage);
                                                          if(Data.RML!=null)
                                                            Message_List.cntx.startActivity(new Intent(Message_List.cntx ,Tic_Tac_Toe.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                          else
                                                              Home.cntx.startActivity(new Intent(Home.cntx ,Tic_Tac_Toe.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                          //  Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                                      }
                                                  });

                                                  alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                                      @Override
                                                      public void onClick(DialogInterface dialog, int which) {
                                                           //finish();
                                                      }
                                                  });

                                                  AlertDialog alertDialog = alertDialogBuilder.create();
                                                  alertDialog.show();
                                                  //Message_List.Recieve_request_for_play(finalMessage);

                                                  //Message_List.cntx.startActivity(new Intent(Message_List.cntx ,Tic_Tac_Toe.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                                              }
                                          }
                            );
                            break;
                        }
                        case "Game_XO" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Tic_Tac_Toe.recive_from_Opponennt(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "block" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Block(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "clear_chat" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Message_List.recieve_Clear_chat(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "unblock" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.UnBlock(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case  "send_report" :
                        {
                            Log.e("Report Recived",text);
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Send_Report(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "delivery_report":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Delivary_Report(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "read_report":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Read_Report(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "delete_message":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.DeleteReport(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                        case "file_message":
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Recive_File_Message(finalMessage,Memory.SizeOf(text));

                                              }
                                          }
                            );
                            break;
                        }
                        case "clear_history" :
                        {
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  Data.Clear_History(finalMessage);

                                              }
                                          }
                            );
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                //output("Receiving bytes : " + bytes.hex());
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null);
                Connected = false;
                connect();
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Connect_Thread.interrupt();
                Connected = false;
                connect();
            }
        }
        public Runnable Connect_Runnable = new Runnable()
        {
            @Override
            public void run() {
                try {
                    while (!Connected)
                    {
                        try
                        {
                            client = new OkHttpClient();
                            Request request = new Request.Builder().url(Websocketip).build();
                            EchoWebSocketListener listener = new EchoWebSocketListener();
                            ws = client.newWebSocket(request, listener);
                            client.dispatcher().executorService().shutdown();
                        }
                        catch (Exception e)
                        {

                        }
                        Thread.sleep(10000);
                    }

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        };
        public static void send(final String message)
        {
            //ws.s
            Runnable Send_Runnable = new Runnable()
            {
                @Override
                public void run() {
                    try {
                        while (!Connected)
                        {
                            Thread.sleep(500);
                        }
                        ws.send(message);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(Send_Runnable).start();
        }

    }

}
