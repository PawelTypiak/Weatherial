package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class SearchButtonInitializer {

    public SearchButtonInitializer(Activity activity){
        ImageView searchImageView =(ImageView) activity.findViewById(R.id.toolbar_layout_search_button_image);
        setSearchButtonIcon(activity, searchImageView);
        setSearchButtonOnClickListener(activity, searchImageView);
    }

    private void setSearchButtonIcon(Activity activity, ImageView searchImageView){
        Drawable searchIcon=UsefulFunctions.getColoredDrawable(activity, R.drawable.search_icon, ContextCompat.getColor(activity,R.color.white));
        searchImageView.setImageDrawable(searchIcon);
    }

    private void setSearchButtonOnClickListener(final Activity activity,
                                                ImageView searchImageView){
        SearchDialogInitializer searchDialogInitializer
                =new SearchDialogInitializer(
                activity,
                1,
                null);
        final AlertDialog searchDialog=searchDialogInitializer.getSearchDialog();
        searchImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchDialog.show();
                        //UsefulFunctions.showKeyboard(activity);
                    }
                });
    }
}
