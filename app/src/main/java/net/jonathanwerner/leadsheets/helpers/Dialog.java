package net.jonathanwerner.leadsheets.helpers;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import net.jonathanwerner.leadsheets.R;

import rx.functions.Action1;

/**
 * Created by jwerner on 3/6/15.
 */
public class Dialog {
    public static void showQuestionDialog(Context activityContext, String question, String positiveAction,
                                          MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(activityContext)
                .content(question)
                .positiveText(positiveAction)
                .positiveColorRes(R.color.md_amber_700)
                .negativeText("Cancel")
                .negativeColorRes(R.color.md_text_54_secondaryOrIcon)
                .contentColorRes(R.color.md_text_87_normal)
                .callback(callback)
                .show();
    }

    public static void showInfoDialog(Context activityContext, String message) {
        new MaterialDialog.Builder(activityContext)
                .content(message)
                .positiveText("Ok")
                .positiveColorRes(R.color.md_amber_700)
                .show();
    }

    public static void showInputDialog(Context activityContext, String title, String positiveAction,
                                       Action1<String> callback) {
        final View editTextLayout = ((Activity) activityContext).getLayoutInflater().inflate(R.layout.text_edit_dialog, null);

        new MaterialDialog.Builder(activityContext)
                .customView(editTextLayout, false)
                .title(title)
                .positiveText(positiveAction)
                .positiveColorRes(R.color.md_amber_700)
                .negativeText("Cancel")
                .negativeColorRes(R.color.md_text_54_secondaryOrIcon)
                .contentColorRes(R.color.md_text_87_normal)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override public void onPositive(MaterialDialog dialog) {
                        Editable text = ((EditText) dialog.getCustomView().findViewById(R.id.edit_text_dialog)).getText();
                        if (text.length() > 0) callback.call(String.valueOf(text));
                    }
                })
                .show();

//        new AlertDialog.Builder(activityContext)
//                .setTitle(title)
//                .setView(editTextLayout)
//                .setPositiveButton("Ok", (dialog, whichButton) -> {
//                    editText.getText().toString();
//
//                }).setNegativeButton("Cancel", null).show();
//    }
    }

    public static void showSnackbarInfo(Context activityContext, String string) {
        SnackbarManager.show(
                Snackbar.with(activityContext.getApplicationContext())
                        .text(string)
                        .duration(2000)
                        .type(SnackbarType.SINGLE_LINE)
                        .swipeToDismiss(false)
                , (android.app.Activity) activityContext);
    }


}
