package com.mcg.mcemil.cubbacubba.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mcg.mcemil.cubbacubba.R;
import com.mcg.mcemil.cubbacubba.connection.Connector;
import com.mcg.mcemil.cubbacubba.database.DBHelper;
import com.mcg.mcemil.cubbacubba.ui.Adapters.MessagesListAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;



public class ChatActivity extends AppCompatActivity {
    //ui components
    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private String TAG = "ChatActivity";

    // Chat messages list adapter
    private MessagesListAdapter adapter;

    //message list
    private List<Message> listMessages;
    private ArrayList<Message> messageHistory;

    //
    private String toUsername;
    private String threadID;

    private Connector connector;
    private Chat chat;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Intent i = getIntent();

        toUsername = i.getStringExtra("toUsername");
        threadID = i.getStringExtra("threadID");

        connector = MainActivity.getConnector();

        if(threadID == null) {
            chat = connector.createChatWithoutThreadID(toUsername, new McgMessageListener());
        }else{
            chat = connector.getChatWithThreadID(toUsername, threadID);
            if(chat == null)
                chat = connector.createChatWithThreadID(toUsername, threadID, new McgMessageListener());
            else
                chat.addMessageListener(new McgMessageListener());
        }

        db = MainActivity.getDb();

        messageHistory = db.getAllMessagesByInbox(threadID);

        //ui defining
        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        listMessages = new ArrayList<Message>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if input message is null don't send message
                if (inputMsg.getText().toString().equals(""))
                    return;
                Message m = new Message(toUsername, inputMsg.getText().toString());
                m.setFrom(MainActivity.getUsername());

                try {
                    db.createMessage(m,threadID);
                    chat.sendMessage(m);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //add message to list view
                Message m2 = new Message(toUsername, inputMsg.getText().toString());
                m2.setFrom(MainActivity.getUsername());

                appendMessage(m2);

                inputMsg.setText("");
            }
        });

        if(messageHistory != null)
            appendMessagesHistory();

    }



    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * Message listener procces message
     */
    private class McgMessageListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
            Log.d(TAG, "message" + message.toString());
            Log.d(TAG, "Chat.tostring" + chat.toString());

            appendMessage(message);
        }
    }

    private void appendMessagesHistory(){
        for(Message m: messageHistory){
            listMessages.add(m);
        }
        adapter.notifyDataSetChanged();
    }

}
