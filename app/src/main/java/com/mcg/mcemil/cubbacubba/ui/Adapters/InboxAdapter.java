package com.mcg.mcemil.cubbacubba.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcg.mcemil.cubbacubba.R;
import com.mcg.mcemil.cubbacubba.ui.Components.Inbox;

import java.util.List;

/**
 * Created by mcemil on 28.11.2015.
 */
public class InboxAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater ;
    private List<Inbox>    inboxList;

    public InboxAdapter(Activity activity, List<Inbox> iList){
        layoutInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        inboxList = iList;
    }

    @Override
    public int getCount() {
        return inboxList.size();
    }

    @Override
    public Object getItem(int position) {
        return inboxList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inboxItem;

        inboxItem = layoutInflater.inflate(R.layout.inbox_list_item,null);
        TextView textView = (TextView) inboxItem.findViewById(R.id.inboxName);
        ImageView imageView = (ImageView) inboxItem.findViewById(R.id.inboxSymbol);

        Inbox inbox = inboxList.get(position);

        textView.setText(inbox.getInboxName());
        imageView.setImageDrawable(inbox.getInboxSymbol());

        return inboxItem;
    }
}
