package com.mcg.mcemil.cubbacubba.ui.Components;

/**
 * Created by mcemil on 29.11.2015.
 */
public class Contact {
    private String contactName;
    private CharacterDrawable contactSymbol;

    public Contact(String contactName){
        this.contactName = contactName;
        this.contactSymbol = new CharacterDrawable(contactName.charAt(0), 0xFF805781);
    }

    public String getContactName(){
        return contactName;
    }

    public CharacterDrawable getContactSymbol(){
        return contactSymbol;
    }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }
}
