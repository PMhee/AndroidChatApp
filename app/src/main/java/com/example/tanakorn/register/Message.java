package com.example.tanakorn.register;

/**
 * Created by tanakorn on 5/5/2016 AD.
 */
public class Message {
    private String form;
    private String to;
    private String message;
    public Message(String form,String to,String message){
        this.form = form;
        this.to = to;
        this.message = message;
    }
    public String getForm(){
        return form;
    }
    public void setForm(String form){
        this.form = form;
    }
    public String getTo(){
        return to;
    }
    public void setTo(String to){
        this.to = to;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
