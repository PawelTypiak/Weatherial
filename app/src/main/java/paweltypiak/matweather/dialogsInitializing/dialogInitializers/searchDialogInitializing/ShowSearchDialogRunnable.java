package paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;

import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class ShowSearchDialogRunnable implements Runnable {

    private Activity activity;

    public ShowSearchDialogRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        AlertDialog searchDialog=new SearchDialogInitializer(activity,1,null).getSearchDialog();
        searchDialog.show();
        //UsefulFunctions.showKeyboard(activity);
    }
}
