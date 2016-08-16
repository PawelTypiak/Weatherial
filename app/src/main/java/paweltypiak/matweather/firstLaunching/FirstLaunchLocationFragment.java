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

import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchLocationFragment extends Fragment {

    private AlertDialog differentLocationDialog;
    private AlertDialog emptyLocationNameDialog;
    private DialogInitializer dialogInitializer;
    private RadioButton differentLocationRadioButton;
    private int choosenDefeaultLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_location_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogInitializer=new DialogInitializer(getActivity());
        initializeRadioGroup();
    }


    private void initializeRadioGroup(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_location_fragment_radio_group);
        final RadioButton currentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_current_location_radio_button);
        differentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_different_location_radio_button);
        radioGroup.check(currentLocationRadioButton.getId());
        choosenDefeaultLocationOption =1;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_location_fragment_current_location_radio_button) {
                    choosenDefeaultLocationOption =1;
                    Log.d("defeault location", "current location");

                } else if (i == R.id.first_launch_location_fragment_different_location_radio_button) {
                    choosenDefeaultLocationOption =2;
                    Log.d("defeault location", "different location");
                }
            }
        });
        setDifferentLocationRadioButtonOnClickListener(differentLocationRadioButton);
    }

    private void setDifferentLocationRadioButtonOnClickListener(final RadioButton radioButton){
        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                differentLocationDialog=dialogInitializer.initializeSearchDialog(1,radioButton);
                differentLocationDialog.show();
                UsefulFunctions.showKeyboard(getActivity());
            }
        });
    }

    public String getDifferentLocationName(){
        String differentLocationString=differentLocationRadioButton.getText().toString();
        Log.d("different location name", differentLocationString);
        return differentLocationString;
    }

    public int getChoosenDefeaultLocationOption() {return choosenDefeaultLocationOption;}

    public void showNoDifferentLocationChoosenDialog(){
        if(emptyLocationNameDialog==null) emptyLocationNameDialog=dialogInitializer.initializeNoDifferentLocationChoosenDialog();
        emptyLocationNameDialog.show();
    }
}
