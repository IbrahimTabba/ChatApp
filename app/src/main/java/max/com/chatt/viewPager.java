package max.com.chatt;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class viewPager extends AppCompatActivity {

    ViewPager viewPager;
    SwipAdapter swipAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        long  img = getIntent().getExtras().getLong("img");
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        swipAdapter = new SwipAdapter(this);
        viewPager.setAdapter(swipAdapter);
        viewPager.setCurrentItem((int) img);
    }
}