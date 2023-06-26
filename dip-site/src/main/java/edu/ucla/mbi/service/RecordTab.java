package edu.ucla.mbi.service;

/*
   #================================================================
   # $Id:: RecordTab.java 40 2009-05-14 15:06:40Z                  $
   # Version: $Rev:: 40                                            $
   #================================================================
   #
   # Tab model
   #
   #================================================================
*/

public class RecordTab{
    private String label="";
    private String vlabel="";
    private boolean on = false;

    public RecordTab(String label,String vlabel, boolean on,boolean active){
	this.label=label;
	this.vlabel=vlabel;
	this.on=on;
    }

    public RecordTab(String label, boolean on,boolean active){
	this.label=label;
	this.vlabel="";
	this.on=on;
    }

    public String getLabel(){
	return label;
    }

    public void setLabel(String label){
	this.label=label;
    }

    public String getVlabel(){
	return vlabel;
    }

    public void setVlabel(String vlabel){
	this.vlabel=vlabel;
    }

    public boolean isOn(){
	return on;
    }

    public void setOn(boolean on){
	this.on=on;
    }
}
