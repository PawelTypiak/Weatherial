package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class AuthorDialogInitializer {

    public static AlertDialog initializeAuthorDialog(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author,null);
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.email_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.github_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
        RelativeLayout mailLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_mail_button_layout);
        RelativeLayout githubLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_github_button_layout);
        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsefulFunctions.initializeEmailIntent(
                        activity,
                        activity.getString(R.string.mail_address),
                        null,
                        null,
                        NoEmailApplicationDialogInitializer.initializeNoEmailApplicationDialog(activity));
            }
        });
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsefulFunctions.initializeWebIntent(activity,activity.getString(R.string.github_address));
            }
        });
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.author_dialog_title),
                R.drawable.dialog_author_icon,
                null,
                false,
                null,
                null,
                null,
                null,
                activity.getString(R.string.author_dialog_negative_button),
                null);
        AlertDialog authorDialog = alertDialogBuilder.getAlertDialog();
        return authorDialog;
    }
}
