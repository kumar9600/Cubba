package com.mcg.mcemil.cubbacubba.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.mcg.mcemil.cubbacubba.ui.Components.Inbox;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mcemil on 30.11.2015.
 */
public class Connector {
    private XMPPTCPConnectionConfiguration config;
    private AbstractXMPPConnection connection;
    public static final String HOST = "188.166.47.65";
    public static final int PORT = 5222;
    public static final String SERVICE = "AndroidArsenal";
    private String username;
    private String password;
    private String t = "Connector";


    public Connector(String userName, String passWord){
        username = userName;
        password = passWord;

        config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setServiceName(SERVICE)
                .setHost(HOST)
                .setPort(PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        connection = new XMPPTCPConnection(config);

    }

    private class ConnectionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if(connection.isConnected())
                return null;
            try {
                connection.connect();
                Log.d(t, "Connecting...");
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(t, "Connection: " + connection.isConnected());
            login();
        }
    }

    public void connectServer(){
        ConnectionTask connectionThread = new ConnectionTask();
        connectionThread.execute();
    }

    private void login(){
        try {
            connection.login();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(t, "Logged!");

    }

    /**
     * Creates chat without threadID
     * @param toUsername
     * @param ml
     * @return
     */
    public Chat createChatWithoutThreadID (String toUsername, ChatMessageListener ml){
        // Assume we've created an XMPPConnection name "connection"._
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);

        Chat newChat = chatmanager.createChat(toUsername, ml);

        return newChat;
    }

    public Chat getChatWithThreadID (String toUsername,String threadID){
        // Assume we've created an XMPPConnection name "connection"._
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);

        Chat newChat = chatmanager.getThreadChat(threadID);

        return newChat;
    }

    public Chat createChatWithThreadID (String toUsername,String threadID, ChatMessageListener ml){
        // Assume we've created an XMPPConnection name "connection"._
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);

        Chat newChat = chatmanager.createChat(toUsername, threadID, ml);

        return newChat;
    }




    public ArrayList<String> getFriendList(){

        ArrayList<String> friendList = new ArrayList<String>();
        Roster roster = Roster.getInstanceFor(connection);
        boolean success = true;

        Collection<RosterEntry> entries = roster.getEntries();
        if (!roster.isLoaded())
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
                success = false;

            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                success = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
                success = false;
            }
        Presence presence;
        if(!success)
            Log.d(t,"Failed");
        Log.d(t, "Users: " + entries.size());
        for(RosterEntry entry : entries) {
            presence = roster.getPresence(entry.getUser());
            Log.d(t, "@" + entry.getUser());
            friendList.add(entry.getUser());
        }

        return friendList;
    }

    public AbstractXMPPConnection getConnection(){
        return connection;
    }

    private Inbox isChatCreated(ArrayList<Inbox> inboxList , String toUsername){
        for(Inbox inbox: inboxList){
            if(inbox.getInboxName().equals(toUsername))
                return inbox;
        }
        return null;
    }

    /**
     * Main chat manager
     */
    public void createChatManagerListener(ChatManagerListener cml){
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);
        chatmanager.addChatListener(cml);
        Log.d(t, "ChatManagerListener created");
    }

}
