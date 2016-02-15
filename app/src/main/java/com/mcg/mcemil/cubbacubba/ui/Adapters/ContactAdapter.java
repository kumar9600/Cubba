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
import com.mcg.mcemil.cubbacubba.ui.Components.Contact;

import java.util.List;

/**
 * Created by mcemil on 29.11.2015.
 */
public class ContactAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater ;
    private List<Contact> contactList;

    public ContactAdapter(Activity activity, List<Contact> iList){
        layoutInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        contactList = iList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View contactItem;

        contactItem = layoutInflater.inflate(R.layout.contact_list_item,null);
        TextView textView = (TextView) contactItem.findViewById(R.id.contactName);
        ImageView imageView = (ImageView) contactItem.findViewById(R.id.contactSymbol);

        Contact contact = contactList.get(position);

        textView.setText(contact.getContactName());
        imageView.setImageDrawable(contact.getContactSymbol());

        return contactItem;
    }
}
