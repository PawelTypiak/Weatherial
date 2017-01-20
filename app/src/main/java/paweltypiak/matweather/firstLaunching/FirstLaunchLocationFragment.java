package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import paweltypiak.matweather.dialogsInitializing.dialogInitializers.NoDifferentLocationSelectedDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchLocationFragment extends Fragment {

    private AlertDialog differentLocationDialog;
    private AlertDialog emptyLocationNameDialog;
    //private DialogInitializer dialogInitializer;
    private RadioButton differentLocationRadioButton;
    private int selectedDefeaultLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_launch_defeault_location, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //dialogInitializer=new DialogInitializer(getActivity());
        initializeRadioGroup();
    }


    private void initializeRadioGroup(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_location_fragment_radio_group);
        final RadioButton currentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_current_location_radio_button);
        differentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_different_location_radio_button);
        radioGroup.check(currentLocationRadioButton.getId());
        selectedDefeaultLocationOption =0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_location_fragment_current_location_radio_button) {
                    selectedDefeaultLocationOption =0;
                    Log.d("defeault_location", "current location");

                } else if (i == R.id.first_launch_location_fragment_different_location_radio_button) {
                    selectedDefeaultLocationOption =1;
                    Log.d("defeault_location", "different location");
                }
            }
        });
        setDifferentLocationRadioButtonOnClickListener(differentLocationRadioButton);
    }

    private void setDifferentLocationRadioButtonOnClickListener(final RadioButton radioButton){
        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SearchDialogInitializer searchDialogInitializer
                        =new SearchDialogInitializer(
                        getActivity(),
                        0,
                        radioButton);
                //differentLocationDialog=dialogInitializer.initializeSearchDialog(0,radioButton);
                differentLocationDialog=searchDialogInitializer.getSearchDialog();
                differentLocationDialog.show();
                //UsefulFunctions.showKeyboard(getActivity());
            }
        });
    }

    public String getDifferentLocationName(){
        String differentLocationString=differentLocationRadioButton.getText().toString();
        Log.d("different_location_name", differentLocationString);
        return differentLocationString;
    }

    public int getSelectedDefeaultLocationOption() {return selectedDefeaultLocationOption;}

    public void showNoDifferentLocationSelectedDialog(){
        if(emptyLocationNameDialog==null) {

            //emptyLocationNameDialog=dialogInitializer.initializeNoDifferentLocationSelectedDialog();
            emptyLocationNameDialog= NoDifferentLocationSelectedDialogInitializer.getNoDifferentLocationSelectedDialog(getActivity(),differentLocationDialog);
        }
        emptyLocationNameDialog.show();
    }
}
