package net.jonathanwerner.leadsheets.widgets;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.helpers.Resources;


public class Tooltip {

    private Context mContext;
    private PopupWindow tipWindow;
    private View tooltipLayout;

    public Tooltip(Context context) {
        this.mContext = context;
        tipWindow = new PopupWindow(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tooltipLayout = inflater.inflate(R.layout.tooltip, null);
    }

    void showToolTip(View anchor, String message) {

        tipWindow.setHeight(LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(LayoutParams.WRAP_CONTENT);

        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        tipWindow.setAnimationStyle(R.anim.abc_fade_in);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());

        tipWindow.setContentView(tooltipLayout);

        TextView textView = (TextView) tooltipLayout.findViewById(R.id.tooltip_text);
        textView.setText(message);
        textView.setOnClickListener(v -> tipWindow.dismiss());

        int screen_pos[] = new int[2];
        // Get location of anchor view on screen
        anchor.getLocationOnScreen(screen_pos);

        // Get rect for anchor view
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                + anchor.getWidth(), screen_pos[1] + anchor.getHeight());

        // Call view measure to calculate how big your view should be.
        tooltipLayout.measure(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);


        ViewCompat.setTranslationZ(tooltipLayout, new Resources(mContext).dipToPixel(8));

//        int contentViewHeight = tooltipLayout.getMeasuredHeight();
        int contentViewWidth = tooltipLayout.getMeasuredWidth();

        // In this case , i dont need much calculation for x and y position of
        // tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well
        // to show left, right, above or below of anchor view
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x,
                position_y);

        tooltipLayout.setOnClickListener(v -> tipWindow.dismiss());
        new Handler().postDelayed(tipWindow::dismiss, 6000);

    }


}
