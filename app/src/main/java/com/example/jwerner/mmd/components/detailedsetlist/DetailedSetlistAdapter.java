package com.example.jwerner.mmd.components.detailedsetlist;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jwerner.mmd.R;

import java.util.List;

/**
 * Created by jwerner on 2/20/15.
 */
public class DetailedSetlistAdapter extends ArrayAdapter {
    private final Context mContext;
    private final int id;
    private final List<String[]> items;

    public DetailedSetlistAdapter(Context context, List<String[]> list) {
        super(context, R.layout.list_item_detailed, list);
        mContext = context;
        id = R.layout.list_item_detailed;
        items = list;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.main_list_item_textview);

        String[] item = items.get(position);
        if (item != null) {
            String title = item[0];
            String content = item[1];

            SpannableString spannableString = new SpannableString(title + "\n" + content.trim());
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
            text.setText(spannableString);

        }

        return mView;
    }

}
