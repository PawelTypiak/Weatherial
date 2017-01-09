package paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class SearchButtonInitializer {

    public SearchButtonInitializer(Activity activity, DialogInitializer dialogInitializer){
        ImageView searchImageView =(ImageView) activity.findViewById(R.id.toolbar_layout_search_button_image);
        setSearchButtonIcon(activity,searchImageView);
        setSearchButtonOnClickListener(activity,searchImageView,dialogInitializer);
    }

    private void setSearchButtonIcon(Activity activity,ImageView searchImageView){
        Drawable searchIcon=UsefulFunctions.getColoredDrawable(activity,R.drawable.search_icon,ContextCompat.getColor(activity,R.color.white));
        searchImageView.setImageDrawable(searchIcon);
    }

    private void setSearchButtonOnClickListener(final Activity activity,ImageView searchImageView,DialogInitializer dialogInitializer){
        final AlertDialog searchDialog=dialogInitializer.initializeSearchDialog(1,null);
        searchImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchDialog.show();
                        UsefulFunctions.showKeyboard(activity);
                    }
                });
    }
}
