package paweltypiak.matweather.firstLaunching;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchUnitsFragment extends Fragment{

    private int units[]={0,0,0,0,0};
    private TextView unitsHeaderTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_units_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLayout();
    }

    private void initializeLayout() {
        unitsHeaderTextView=(TextView)getActivity().findViewById(R.id.first_launch_units_fragment_header_text);
        for(int i=0;i<5;i++){
           final FirstLaunchSpinner spinner=initializeSpinner(i);
            setWhiteSpinnerArrow(spinner);
            setSpinnerRippleView(spinner,i);
            setImagesVisibleAndClickable(spinner,i);
            setSpinnerListener(spinner,i);
        }
    }

    private FirstLaunchSpinner initializeSpinner(int id){
        //initialize custom spinner
        FirstLaunchSpinner spinner = (FirstLaunchSpinner) getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_"+id, "id", getActivity().getPackageName()));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.first_launch_spinner_item,
                getActivity().getResources().getStringArray(getActivity().getResources().getIdentifier("units_array_"+id, "array", getActivity().getPackageName())));
        arrayAdapter.setDropDownViewResource(R.layout.first_launch_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return spinner;
    }

    private void setWhiteSpinnerArrow(FirstLaunchSpinner spinner){
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
        spinnerDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
    }

    private void setSpinnerRippleView(final FirstLaunchSpinner spinner,int id){
        //set custom ripple view when spinner is clicked
        final RelativeLayout spinnerLayout = (RelativeLayout)getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_layout_"+id, "id", getActivity().getPackageName()));
        final View selectableView=getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_selectable_view_"+id, "id", getActivity().getPackageName()));
        ViewTreeObserver observer = spinnerLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int spinnerLayoutHeight = spinnerLayout.getHeight();
                ViewGroup.LayoutParams selectableViewParams = (selectableView).getLayoutParams();
                selectableViewParams.height=spinnerLayoutHeight;
                selectableView.setLayoutParams(selectableViewParams );
                ViewTreeObserver obs = spinnerLayout.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });
        selectableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

    }

    private void setImagesVisibleAndClickable(final Spinner spinner, final int id) {
        //set layout visibility after all icons are loaded
        ImageView unitsImageView = (ImageView) getActivity().findViewById(getResources().getIdentifier("first_launch_units_fragment_spinner_image_" + id, "id", getActivity().getPackageName()));
        setImagesClickable(spinner,unitsImageView);
        Picasso.with(getActivity().getApplicationContext()).load(getActivity().getResources().getIdentifier("drawable/units_icon_" + id, null, getActivity().getPackageName())).transform(new UsefulFunctions().new setDrawableColor(getActivity().getResources().getColor(R.color.white))).into(unitsImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                UsefulFunctions.setViewVisible(spinner);
                if(id==4){
                    UsefulFunctions.setViewVisible(unitsHeaderTextView);
                }
            }
            @Override
            public void onError() {
            }
        });
    }

    private void setImagesClickable(final Spinner spinner,ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    private void setSpinnerListener(final FirstLaunchSpinner spinner, final int id){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("units", "selected unit: "+spinner.getSelectedItem().toString());
                units[id]=i;
                SharedPreferencesModifier.setUnits(getActivity(),units);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public static class FirstLaunchSpinner extends Spinner {
        OnItemSelectedListener listener;
        public FirstLaunchSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public FirstLaunchSpinner(Context context) {
            super(context);
        }
        @Override
        public void setSelection(int position) {
            super.setSelection(position);
            if (listener != null)
                listener.onItemSelected(null, null, position, 0);
        }
    }
}
