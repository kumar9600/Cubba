package com.mcg.mcemil.cubbacubba.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mcg.mcemil.cubbacubba.R;
import com.mcg.mcemil.cubbacubba.connection.Connector;
import com.mcg.mcemil.cubbacubba.ui.Adapters.ContactAdapter;
import com.mcg.mcemil.cubbacubba.ui.Components.Contact;
import com.mcg.mcemil.cubbacubba.ui.Components.Inbox;

import java.util.ArrayList;

public class NewChatActivity extends AppCompatActivity {
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;
    private Connector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat_screen);

        connector = MainActivity.getConnector();

        //contact list
        ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactList = new ArrayList<Contact>();
        contactList = addContactsToList(connector.getFriendList());
        contactAdapter = new ContactAdapter(this, contactList);
        contactListView.setAdapter(contactAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = (Contact) parent.getItemAtPosition(position);
                Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
                String toUsername = c.getContactName();
                intent.putExtra("toUsername", toUsername + "@188.166.47.65");

                intent.putExtra("threadID", getThreadID(toUsername + "@188.166.47.65"));
                startActivity(intent);
            }
        });

        Button bNewChannel = (Button) findViewById(R.id.bNewChannel);
        bNewChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private ArrayList<Contact> addContactsToList(ArrayList<String> stringContactList){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        System.out.println("Haloz");
        for(String c : stringContactList){
            contacts.add(new Contact(c));
            System.out.println("alo" + c);
        }
        return contacts;
    }

    private String getThreadID(String username){
        for(Inbox i : MainActivity.getInboxList()){
            if(i.getInboxName().equals(username))
                return i.getThreadID();
        }
        return null;
    }
}
