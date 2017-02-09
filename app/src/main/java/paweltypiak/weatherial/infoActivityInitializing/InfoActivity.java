package paweltypiak.weatherial.infoActivityInitializing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.infoActivityInitializing.infoActivityNestedScrollViewInitializing.InfoActivityNestedScrollViewInitializer;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initializeAppBar();
        initializeNestedScrollView();
    }

    private void initializeAppBar(){
        new InfoActivityAppBarInitializer(this);
    }

    private void initializeNestedScrollView(){
        new InfoActivityNestedScrollViewInitializer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsefulFunctions.setTaskDescription(this);
    }
}
