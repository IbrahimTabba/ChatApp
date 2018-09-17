package max.com.chatt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;
import static max.com.chatt.JSON.Create_Reques;

public class Tic_Tac_Toe extends AppCompatActivity {
    static Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    static Button [][] buttons=new Button[3][3];
    public static Context context;
    static boolean turn=false;

    public static void checkGame(){
        checkWinner(b1,b2,b3);
        checkWinner(b4,b5,b6);
        checkWinner(b7,b8,b9);
        checkWinner(b1,b4,b7);
        checkWinner(b2,b5,b8);
        checkWinner(b3,b6,b9);
        checkWinner(b1,b5,b9);
        checkWinner(b3,b5,b7);

    }

    public static void checkWinner(Button b1, Button b2, Button b3){
        if (b1.getText().toString().equals(b2.getText().toString()) && b2.getText().toString().equals(b3.getText().toString()) && b3.getText().toString().equals("X")){
            //Toast.makeText(context,"player 1 is winner ",Toast.LENGTH_LONG).show();
            reset();
        }
        if (b1.getText().toString().equals(b2.getText().toString()) && b2.getText().toString().equals(b3.getText().toString()) && b3.getText().toString().equals("O")){
           // Toast.makeText(context,"player 1 is winner ",Toast.LENGTH_LONG).show();
            reset();
        }
    }
    public static void reset(){
        b1.setText("");
        b2.setText("");
        b3.setText("");
        b4.setText("");
        b5.setText("");
        b6.setText("");
        b7.setText("");
        b8.setText("");
        b9.setText("");
    }

    static  void send_move(int i,int j)
    {
        JSONObject myjson = null;
        try {
            myjson = Create_Reques("Game_XO","i",i,"j",j,"symbol", Start_game.s,"sender", websocket.user_name,"reciver",Data.Player);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        websocket.socket_thread.send(myjson.toString());
    }

    static void recive_from_Opponennt(JSONObject jsonObject)
    {

       try {
            JSONObject data2 = jsonObject.getJSONObject("data");
            JSONObject data = data2.getJSONObject("data");
            int i=data.getInt("i");
            int j=data.getInt("j");
            String s=data.getString("symbol");
          if ( buttons[i][j].getText().toString().equals("")) {
               buttons[i][j].setText(s);
                checkGame();
            }
       } catch (JSONException e) {
            e.printStackTrace();
        }
        turn=true;
    }

    static void Send_Opponennt_request()
    {
        JSONObject myjson = null;
        try {
            Data.Player = websocket.receiver;
            //(String title , String date_sent , String type , String sender , String receiver , long ID1 , long ID2 ,
            //String Thumbnail , String fileName , String Path1 , String Path2 )
            //Message Mes = new Message("text_message","","text_message",websocket.user_name,websocket.receiver,
            myjson = Create_Reques("StartGame_XO","symbol", Start_game.s,"sender", websocket.user_name,"reciver",websocket.receiver);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        websocket.socket_thread.send(myjson.toString());
        turn=true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic__tac__toe);
        context=this.getApplicationContext();
        //Opponennt_request();

        b1=(Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);
        b3=(Button) findViewById(R.id.b3);
        b4=(Button) findViewById(R.id.b4);
        b5=(Button) findViewById(R.id.b5);
        b6=(Button) findViewById(R.id.b6);
        b7=(Button) findViewById(R.id.b7);
        b8=(Button) findViewById(R.id.b8);
        b9=(Button) findViewById(R.id.b9);
        Button[] b={b1,b2,b3,b4,b5,b6,b7,b8,b9};

        int X=0;
        for(int i = 0 ; i < 3 ; i++)
        {
            for(int j = 0 ; j < 3 ; j++)
            {
                buttons[i][j]=b[X];
                X++;
            }

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b1.getText().toString().equals("") && turn==true) {
                    b1.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                           if(buttons[i][j]==b1)
                               send_move(i,j);
                        }

                    }
                    checkGame();
                    turn=false;
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2.getText().toString().equals("")&& turn==true){
                        b2.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b2)
                                send_move(i,j);
                        }

                    }
                        checkGame();
                    turn=false;
                }

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b3.getText().toString().equals("")&& turn==true){
                    b3.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b3)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }

        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b4.getText().toString().equals("")&& turn==true){
                    b4.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b4)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b5.getText().toString().equals("")&& turn==true){
                    b5.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b5)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b6.getText().toString().equals("")&& turn==true) {
                    b6.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b6)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b7.getText().toString().equals("")&& turn==true){
                    b7.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b7)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b8.getText().toString().equals("")&& turn==true){
                    b8.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b8)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b9.getText().toString().equals("")&& turn==true){
                    b9.setText(Start_game.s);
                    for(int i = 0 ; i < 3 ; i++)
                    {
                        for(int j = 0 ; j < 3 ; j++)
                        {
                            if(buttons[i][j]==b9)
                                send_move(i,j);
                        }

                    }
                    checkGame(); turn=false;
                }
            }
        });
    }
}