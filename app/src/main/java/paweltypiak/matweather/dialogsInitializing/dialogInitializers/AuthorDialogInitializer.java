package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing.NoEmailApplicationDialogInitializer;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class AuthorDialogInitializer {

    public static AlertDialog getAuthorDialog(Activity activity){
        return initializeAuthorDialog(activity);
    }

    private static AlertDialog initializeAuthorDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeEmailLayout(activity,dialogView);
        initializeGithubLayout(activity,dialogView);
        AlertDialog authorDialog = buildAlertDialog(activity,dialogView);
        return authorDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author,null);
        return dialogView;
    }

    private static void initializeEmailLayout(Activity activity, View dialogView){
        setEmailIcon(activity,dialogView);
        setEmailLayoutOnClickListener(activity,dialogView);
    }

    private static void setEmailIcon(Activity activity, View dialogView){
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.email_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
    }

    private static void setEmailLayoutOnClickListener(final Activity activity,View dialogView){
        RelativeLayout emailLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_email_button_layout);
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEmailIntent(activity);
            }
        });
    }

    public static void initializeEmailIntent(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        String address=activity.getString(R.string.mail_address);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog dialog=NoEmailApplicationDialogInitializer.getNoEmailApplicationDialog(activity);
            dialog.show();
        }
    }

    private static void initializeGithubLayout(Activity activity,View dialogView){
        setGithubIcon(activity,dialogView);
        setGithubLayoutOnClickListener(activity,dialogView);
    }

    private static void setGithubIcon(Activity activity, View dialogView){
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.github_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
    }

    private static void setGithubLayoutOnClickListener(final Activity activity,View dialogView){
        RelativeLayout githubLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_github_button_layout);
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.github_address));
            }
        });
    }

    public static void initializeWebIntent(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private static AlertDialog buildAlertDialog(Activity activity, View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
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
        return alertDialogBuilder.getAlertDialog();
    }
}
