package com.mcg.mcemil.cubbacubba.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mcg.mcemil.cubbacubba.R;
import com.mcg.mcemil.cubbacubba.connection.Connector;
import com.mcg.mcemil.cubbacubba.database.DBHelper;
import com.mcg.mcemil.cubbacubba.ui.Adapters.InboxAdapter;
import com.mcg.mcemil.cubbacubba.ui.Components.Inbox;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    private static String username ;
    private String password;

    private static ArrayList<Inbox> inboxList;
    private static InboxAdapter inboxAdapter;

    //Connector
    private static Connector connector;
    private AbstractXMPPConnection connection;


    //Database
    private static DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.d(TAG, "username: " + username + " password: " + password);


        //Connection
        connector = new Connector(username,password);
        connector.connectServer();
        connection = connector.getConnection();


        //creating database
        if(db == null)
            db = new DBHelper(getApplicationContext());



        //inbox list
        ListView inboxListView = (ListView) findViewById(R.id.inboxList);
        inboxList = new ArrayList<Inbox>();
        inboxAdapter = new InboxAdapter(this, inboxList);
        inboxListView.setAdapter(inboxAdapter);

        inboxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inbox inbox = (Inbox) parent.getItemAtPosition(position);

                if(inbox.getInboxName().indexOf("conference") == -1)
                {
                    Intent i = new Intent(MainActivity.this, ChatActivity.class);
                    i.putExtra("toUsername", inbox.getInboxName());
                    i.putExtra("threadID", inbox.getThreadID());
                    startActivity(i);

                }else {//Channel Chat
                   // Intent i = new Intent(MainActivity.this, ChannelChatActivity.class);

                  //  i.putExtra("channelname", inbox.getInboxName());

                  //  startActivity(i);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this, NewChatActivity.class);

                startActivity(newIntent);

                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });


        //append all inbox
        appendAllInbox();
        connector.createChatManagerListener(new ChatManagerListenerImpl());


    }

    public static String getUsername(){
        return username;
    }

    private void appendInbox(final Inbox i){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inboxList.add(i);
                inboxAdapter.notifyDataSetChanged();
            }
        });
    }

    public static Connector getConnector(){
        return connector;
    }

    public static ArrayList<Inbox> getInboxList(){
        return inboxList;
    }

    public static InboxAdapter getInboxAdapter(){
        return inboxAdapter;
    }

    public static DBHelper getDb(){
        return db;
    }

    private void appendAllInbox(){
        ArrayList<Inbox> allInbox = db.getAllInbox();
        for(Inbox i : allInbox) {
            appendInbox(i);
        }
    }

    private class ChatManagerListenerImpl implements ChatManagerListener{

        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            String username = chat.getParticipant().substring(0, chat.getParticipant().lastIndexOf("@"));
            Inbox inbox = new Inbox(username + "@188.166.47.65", chat.getThreadID());

            if(!isInboxCreated(inbox)) {
                db.createInbox(inbox);
                appendInbox(inbox);
            }
            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    db.createMessage(message,chat.getThreadID());
                }
            });
        }
    }

    private boolean isInboxCreated(Inbox i) {
        for (Inbox inbox: inboxList){
            System.out.println(inbox.getInboxName() + "e:" + i.getInboxName());
            if(inbox.getInboxName().equals(i.getInboxName())) {
                System.out.println("ee");
                return true;
            }
        }
        return false;
    }

    /*
    private void deleteClosedChats(Inbox i){
        for(Inbox inbox : inboxList){
            if(inbox.getInboxName().equals(i)) {
                inboxList.remove(inbox);
                db.deleteInbox(inbox);
                inboxAdapter.notifyDataSetChanged();
                return;
            }
        }
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);

                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
