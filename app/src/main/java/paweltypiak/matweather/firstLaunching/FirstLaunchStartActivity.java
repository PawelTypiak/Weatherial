package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchStartActivity extends AppCompatActivity {

    AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch_start);
        setIcons();
        initializeDialog();
        setMainActivity();
    }

    private void setIcons(){
        ImageView appIconImageView;
        ImageView buttonImageView;
        appIconImageView=(ImageView)findViewById(R.id.first_launch_welcome_app_icon_image);
        buttonImageView=(ImageView)findViewById(R.id.first_launch_welcome_start_button_image);
        Picasso.with(getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(appIconImageView);
        Picasso.with(getApplicationContext()).load(R.drawable.next_arrow_icon).transform(new UsefulFunctions().new setDrawableColor(this.getResources().getColor(R.color.white))).fit().centerInside().into(buttonImageView);
    }

    private void initializeDialog(){
        DialogInitializer dialogInitializer=new DialogInitializer(this);
        exitDialog=dialogInitializer.initializeExitDialog();
    }

    private void setMainActivity() {
        CardView startButtonCardView = (CardView) findViewById(R.id.first_launch_welcome_start_button_cardView);
        startButtonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstLaunchStartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitDialog.show();
    }
}
