package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;
import paweltypiak.matweather.dataDownloading.DataDownloader;
import paweltypiak.matweather.dataDownloading.DownloadCallback;
import paweltypiak.matweather.dataProcessing.DataInitializer;
import paweltypiak.matweather.jsonHandling.Channel;

public class FirstLaunchLoadingFragment extends Fragment implements DownloadCallback{
    private int choosenOption;
    private String differentLocationName;
    private DialogInitializer dialogInitializer;
    private AlertDialog noLocalizationResultsDialog;
    private AlertDialog localizationResultsDialog;
    private AlertDialog internetFailureDialog;
    private AlertDialog exitNoLocalizationResultDialog;
    private AlertDialog exitLocalizationResultDialog;
    private AlertDialog exitInternetFailureDialog;
    private DataInitializer dataInitializer;
    private String location;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private ChooseLocationAgainListener locationListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChooseLocationAgainListener) {
            locationListener = (ChooseLocationAgainListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.first_launch_loading_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDialogs();
        getLayoutResources();
        downloadData();
    }

    public static FirstLaunchLoadingFragment newInstance(int choosenOption, String differentLocationName, Activity activity) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putInt(activity.getString(R.string.extras_choosen_location_option_key), choosenOption);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    private void getExtras(){
        choosenOption = getArguments().getInt(getString(R.string.extras_choosen_location_option_key), 0);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
    }

    private void getLayoutResources(){
        loadingBar=(ProgressBar)getActivity().findViewById(R.id.first_launch_loading_fragment_progress_circle);
        messageTextView=(TextView)getActivity().findViewById(R.id.first_launch_loading_fragment_message);
    }

    private void downloadData(){
        UsefulFunctions.setViewVisible(loadingBar);
        UsefulFunctions.setViewVisible(messageTextView);
        location=differentLocationName;
        DataDownloader downloader=new DataDownloader(location,this);   //downloading weather data for Poznan
    }

    @Override
    public void ServiceSuccess(Channel channel) {

        dataInitializer = new DataInitializer(getActivity(),channel); //initializing weather data from JSON
        UsefulFunctions.setViewInvisible(loadingBar);
        UsefulFunctions.setViewInvisible(messageTextView);
        localizationResultsDialog=dialogInitializer.initializeLocalizationResultsDialog(1,dataInitializer,loadMainActivityRunnable,showLocationFragmentRunnable,showExitLocalizationResultDialogRunnable);
        localizationResultsDialog.show();
        //dialog ze znalezieniem

    }

    @Override
    public void ServiceFailure(int errorCode) {
        //failure handling
        UsefulFunctions.setViewInvisible(loadingBar);
        UsefulFunctions.setViewInvisible(messageTextView);
        if(errorCode==1) {internetFailureDialog.show();}
        else{noLocalizationResultsDialog.show();}
    }

    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(getActivity());
        exitLocalizationResultDialog=dialogInitializer.initializeExitDialog(true,showLocalizationResultDialogRunnable);
        exitNoLocalizationResultDialog=dialogInitializer.initializeExitDialog(true,showNoLocalizationResultDialogRunnable);
        exitInternetFailureDialog=dialogInitializer.initializeExitDialog(true,showInternetFailureDialogRunnable);
        noLocalizationResultsDialog=dialogInitializer.initializeNoLocalizationResultsDialog(true,showLocationFragmentRunnable,showExitNoLocalizationResultDialogRunnable);
        internetFailureDialog=dialogInitializer.initializeInternetFailureDialog(true,downloadRunnable,showExitInternetFailureDialogRunnable);
    }

    private Runnable showExitNoLocalizationResultDialogRunnable = new Runnable() {
        public void run() {
            exitNoLocalizationResultDialog.show();
        }
    };

    private Runnable showExitLocalizationResultDialogRunnable = new Runnable() {
        public void run() {
            exitLocalizationResultDialog.show();
        }
    };

    private Runnable showExitInternetFailureDialogRunnable = new Runnable() {
        public void run() {
            exitInternetFailureDialog.show();
        }
    };

    private Runnable showInternetFailureDialogRunnable = new Runnable() {
        public void run() {
            internetFailureDialog.show();
        }
    };

    private Runnable showLocalizationResultDialogRunnable = new Runnable() {
        public void run() {localizationResultsDialog.show();
        }
    };

    private Runnable showNoLocalizationResultDialogRunnable = new Runnable() {
        public void run() {noLocalizationResultsDialog.show();
        }
    };

    private Runnable loadMainActivityRunnable = new Runnable() {
        public void run() {
            UsefulFunctions.setViewVisible(loadingBar);
            messageTextView.setText(getString(R.string.first_launch_layout_loading_header_loading_content));
            UsefulFunctions.setViewVisible(messageTextView);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(getString(R.string.extras_data_initializer_key),dataInitializer);
            startActivity(intent);

            getActivity().finish();

        }
    };



    private Runnable downloadRunnable = new Runnable() {
        public void run() {
            downloadData();}
    };
    private Runnable showLocationFragmentRunnable = new Runnable() {
        public void run() {locationListener.showLocationFragment();}
    };
    public interface ChooseLocationAgainListener {void showLocationFragment();}
}
