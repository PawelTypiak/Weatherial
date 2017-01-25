package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;

public class SearchButtonInitializer {

    public SearchButtonInitializer(Activity activity){
        ImageView searchImageView =(ImageView) activity.findViewById(R.id.toolbar_layout_search_button_image);
        setSearchButtonOnClickListener(activity, searchImageView);
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
                    }
                });
    }
}
