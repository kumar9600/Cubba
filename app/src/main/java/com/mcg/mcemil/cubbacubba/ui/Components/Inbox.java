package com.mcg.mcemil.cubbacubba.ui.Components;

/**
 * Created by mcemil on 28.11.2015.
 */
public class Inbox {
    private String inboxName;
    private CharacterDrawable inboxSymbol;
    private String threadID;

    public Inbox(String inboxName, String threadID){
        this.inboxName = inboxName;
        this.threadID = threadID;
        this.inboxSymbol = new CharacterDrawable(inboxName.charAt(0), 0xFF805781);
    }

    public String getInboxName(){
        return inboxName;
    }

    public String getThreadID(){
        return threadID;
    }

    public void setThreadID(String t){
        threadID = t;
    }

    public CharacterDrawable getInboxSymbol(){
        return inboxSymbol;
    }

    public void setInboxName(String inboxName){
        this.inboxName = inboxName;
    }


}
