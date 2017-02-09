package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

class ShowSearchDialogRunnable implements Runnable {

    private Activity activity;

    public ShowSearchDialogRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        AlertDialog searchDialog=new SearchDialogInitializer(activity,1,null).getSearchDialog();
        searchDialog.show();
    }
}
