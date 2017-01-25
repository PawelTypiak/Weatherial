package paweltypiak.matweather.dialogsInitializing.dialogInitializers.noDifferentLocationSelectedDialogInitializing;

import android.support.v7.app.AlertDialog;

class showDifferentLocationDialogRunnable implements Runnable{

    private AlertDialog searchDialog;

    showDifferentLocationDialogRunnable(AlertDialog searchDialog){
        this.searchDialog=searchDialog;
    }

    @Override
    public void run() {
        searchDialog.show();
    }
}
