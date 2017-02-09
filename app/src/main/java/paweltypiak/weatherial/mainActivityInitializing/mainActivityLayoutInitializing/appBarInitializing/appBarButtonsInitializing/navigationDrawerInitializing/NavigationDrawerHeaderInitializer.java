package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class NavigationDrawerHeaderInitializer {

    public NavigationDrawerHeaderInitializer(Activity activity, NavigationView navigationView){
        View headerView=navigationView.getHeaderView(0);
        ImageView logoImageView=(ImageView)headerView.findViewById(R.id.navigation_drawer_header_logo_image);
        setLogoImageViewHeight(activity,logoImageView);
        setLogoDrawable(activity,logoImageView);
    }

    private void setLogoImageViewHeight(Activity activity,ImageView logoImageView){
        LinearLayout.LayoutParams logoImageViewLayoutParams
                =(LinearLayout.LayoutParams)logoImageView.getLayoutParams();
        final float logoHeightMultiplier=0.3f;
        int imageViewHeight= (int)(UsefulFunctions.getScreenHeight(activity)*logoHeightMultiplier);
        logoImageViewLayoutParams.height=imageViewHeight;
        logoImageView.setLayoutParams(logoImageViewLayoutParams);
    }

    private void setLogoDrawable(Activity activity, ImageView logoImageView){
        logoImageView.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.logo_navigation_drawer));
    }
}
