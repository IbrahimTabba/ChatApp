package max.com.chatt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Start_game extends AppCompatActivity {
    static String s="X";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        toolbar.setTitle(getResources().getString(R.string.Start_Game));

        Button X,O;
        X=(Button) findViewById(R.id.x);
        O=(Button) findViewById(R.id.o);

        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s="X";
            }
        });

        O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s="O";

            }
        });


    }

    public void start(View view) {
        Tic_Tac_Toe.Send_Opponennt_request();
        startActivity(new Intent(view.getContext(),Tic_Tac_Toe.class));
        finish();
    }
}
