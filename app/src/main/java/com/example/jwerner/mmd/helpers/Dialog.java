package com.example.jwerner.mmd.helpers;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jwerner.mmd.R;

/**
 * Created by jwerner on 3/6/15.
 */
public class Dialog {
    public static void showQuestionDialog(Context activityContext, String question, String positiveAction, MaterialDialog.ButtonCallback callback) {
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

}
