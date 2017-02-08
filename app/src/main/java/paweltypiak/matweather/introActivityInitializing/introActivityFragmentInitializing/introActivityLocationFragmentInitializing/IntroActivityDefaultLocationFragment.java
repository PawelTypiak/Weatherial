package paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.noDifferentLocationSelectedDialogInitializing.NoDifferentLocationSelectedDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;
import paweltypiak.matweather.R;

public class IntroActivityDefaultLocationFragment extends Fragment {

    private AlertDialog differentLocationDialog;
    private AlertDialog emptyLocationNameDialog;
    private RadioButton differentLocationRadioButton;
    private int selectedDefaultLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_default_location, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRadioGroup();
    }

    private void initializeRadioGroup(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.intro_activity_default_location_fragment_radio_group);
        final RadioButton currentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_default_location_fragment_current_location_radio_button);
        differentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_default_location_fragment_different_location_radio_button);
        radioGroup.check(currentLocationRadioButton.getId());
        selectedDefaultLocationOption =0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.intro_activity_default_location_fragment_current_location_radio_button) {
                    selectedDefaultLocationOption =0;
                    Log.d("defeault_location", "current location");

                } else if (i == R.id.intro_activity_default_location_fragment_different_location_radio_button) {
                    selectedDefaultLocationOption =1;
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
                differentLocationDialog=searchDialogInitializer.getSearchDialog();
                differentLocationDialog.show();
            }
        });
    }

    public String getDifferentLocationName(){
        String differentLocationString=differentLocationRadioButton.getText().toString();
        Log.d("different_location_name", differentLocationString);
        return differentLocationString;
    }

    public int getSelectedDefaultLocationOption() {return selectedDefaultLocationOption;}

    public void showNoDifferentLocationSelectedDialog(){
        if(emptyLocationNameDialog==null) {
            emptyLocationNameDialog
                    = NoDifferentLocationSelectedDialogInitializer.
                    getNoDifferentLocationSelectedDialog(getActivity(),differentLocationDialog);
        }
        emptyLocationNameDialog.show();
    }
}
