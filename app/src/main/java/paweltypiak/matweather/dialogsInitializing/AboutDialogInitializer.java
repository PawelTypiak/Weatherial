package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;

public class AboutDialogInitializer {

    public static AlertDialog initializeAboutDialog(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_about,null);
        TextView aboutDesctiptionPart1=(TextView)dialogView.findViewById(R.id.about_dialog_text_part1);
        TextView aboutDesctiptionPart2=(TextView)dialogView.findViewById(R.id.about_dialog_text_part2);
        TextView aboutDesctiptionPart3=(TextView)dialogView.findViewById(R.id.about_dialog_text_part3);
        aboutDesctiptionPart1.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart2.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart3.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart1.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        aboutDesctiptionPart2.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        aboutDesctiptionPart3.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.about_dialog_app_icon_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.logo_small).fit().centerInside().into(iconImageView);
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                null,
                0,
                null,
                false,
                null,
                null,
                null,
                null,
                activity.getString(R.string.about_dialog_negative_button),
                null);
        AlertDialog aboutDialog = alertDialogBuilder.getAlertDialog();
        return aboutDialog;
    }

}
