package paweltypiak.matweather.infoActivityInitializing;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class InfoActivityAppBarInitializer {

    public InfoActivityAppBarInitializer(Activity activity){
        setLogo(activity);
        initializeBackButton(activity);
    }

    private void setLogo(Activity activity){
        ImageView logoImageView=(ImageView)activity.findViewById(R.id.info_activity_logo_image);
        setLogoImageViewsHeight(activity,logoImageView);
        loadLogoImageView(activity,logoImageView);
    }

    private void setLogoImageViewsHeight(Activity activity,ImageView logoImageView){
        float TEXT_LOGO_HEIGHT_FACTOR =0.2f;
        int logoImageViewHeight
                = (int)(activity.getResources().getDisplayMetrics().heightPixels * TEXT_LOGO_HEIGHT_FACTOR);
        RelativeLayout.LayoutParams logoImageViewLayoutParams
                = (RelativeLayout.LayoutParams)logoImageView.getLayoutParams();
        logoImageViewLayoutParams.height = logoImageViewHeight;
        logoImageView.setLayoutParams(logoImageViewLayoutParams);
    }

    private void loadLogoImageView(Activity activity,ImageView logoImageView){
        Picasso.with(activity).load(R.drawable.logo_small).fit().centerInside().noFade().into(logoImageView);
    }

    private void initializeBackButton(final Activity activity){
        Drawable backIconDrawable
                = UsefulFunctions.getColoredDrawable(activity,R.drawable.dialog_edit_icon,R.color.white);
        ImageView backButtonImageView=(ImageView)activity.findViewById(R.id.info_activity_back_button_image);
        backButtonImageView.setImageDrawable(backIconDrawable);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
}
