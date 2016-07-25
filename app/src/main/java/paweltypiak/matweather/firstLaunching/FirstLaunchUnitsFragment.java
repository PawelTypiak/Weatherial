package paweltypiak.matweather.firstLaunching;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
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
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchUnitsFragment extends Fragment{
    private int units[]={0,0,0,0,0};
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_units_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences=UsefulFunctions.getSharedPreferences(getActivity());
        initializeLayout();
    }

    private void initializeLayout() {
        for(int i=0;i<5;i++){
           final FirstLaunchSpinner spinner=initializeSpinner(i);
            setWhiteSpinnerArrow(spinner);
            setSpinnerRippleView(spinner,i);
            setLayoutVisible(spinner,i);
            setSpinnerListener(spinner,i);
        }
    }

    private FirstLaunchSpinner initializeSpinner(int id){
        FirstLaunchSpinner spinner = (FirstLaunchSpinner) getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_"+(id+1), "id", getActivity().getPackageName()));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.first_launch_spinner_item,
                getActivity().getResources().getStringArray(getActivity().getResources().getIdentifier("first_launch_layout_units_array_"+(id+1), "array", getActivity().getPackageName())));
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
        final RelativeLayout spinnerLayout = (RelativeLayout)getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_"+(id+1)+"_layout", "id", getActivity().getPackageName()));
        final View selectableView=getActivity().findViewById(getActivity().getResources().getIdentifier("first_launch_units_fragment_spinner_"+(id+1)+"_selectable_view", "id", getActivity().getPackageName()));
        ViewTreeObserver observer = spinnerLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int spinnerLayoutHeight = spinnerLayout.getHeight();
                ViewGroup.LayoutParams selectableViewParams = (selectableView).getLayoutParams();
                selectableViewParams.height=spinnerLayoutHeight;
                selectableView.setLayoutParams(selectableViewParams );
                ViewTreeObserver obs = spinnerLayout.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });
        selectableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    private void setLayoutVisible(final Spinner spinner, final int id) {
        ImageView unitsImageView = (ImageView) getActivity().findViewById(getResources().getIdentifier("first_launch_units_fragment_spinner_" + (id + 1) + "_image", "id", getActivity().getPackageName()));
        Picasso.with(getActivity().getApplicationContext()).load(getActivity().getResources().getIdentifier("drawable/units_icon_" + (id + 1), null, getActivity().getPackageName())).transform(new UsefulFunctions().new setDrawableColor(getActivity().getResources().getColor(R.color.white))).into(unitsImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                UsefulFunctions.setViewVisible(spinner);
                if(id==4){
                    TextView textView=(TextView)getActivity().findViewById(R.id.first_launch_units_fragment_header_text);
                    UsefulFunctions.setViewVisible(textView);
                }
            }
            @Override
            public void onError() {
            }
        });
    }

    private void setSpinnerListener(final FirstLaunchSpinner spinner, final int id){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                units[id]=i;
                sharedPreferences.edit().putString(getString(R.string.shared_preferences_units_key), buildStringFromIntArray(units)).commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String buildStringFromIntArray(int[] intArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < intArray.length; i++) {
            stringBuilder.append(intArray[i]).append(",");
        }
        return stringBuilder.toString();
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
        public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener) {
            this.listener = listener;
        }
    }
}
