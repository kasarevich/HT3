package domain;

import data.instructions.Operation;
import exception.InputFileException;
import exception.LogException;
import presentation.UI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Manager {
    private static Manager instance;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");

    private UI ui;
    private FileManager fm;
    private ArrayList<Operation> script;

    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;
    }
    private Manager() {
    }

    public void setUI(UI ui){
        this.ui = ui;
    }
    public void setFileManager(FileManager fm){
        this.fm = fm;
    }

    public void start(String filename) {
        log("[ parsing \"" + filename + "\" started ]");
        try {
            script = fm.readInstrFile(filename);
        } catch (InputFileException e) {
            log(e.getMessage());
            return;
        }
        log("[ instructions from file \"" + filename + "\" is ready ]");
        for(Operation operation : script){
            switch (operation.getType()){
                case open:{
                    open();
                    break;
                }
                case checkPageTitle:{
                    checkPageTitle();
                    break;
                }
                case checkPageContains:{
                    checkPageContains();
                    break;
                }
                case checkLinkPresentByHref:{
                    checkLinkPresentByHref();
                    break;
                }
                case checkLinkPresentByName:{
                    checkLinkPresentByName();
                    break;
                }
            }
        }
    }

    private void open(){

    }

    private void checkLinkPresentByHref(){

    }

    private void checkLinkPresentByName(){

    }

    private void checkPageTitle(){

    }

    private void checkPageContains() {

    }



    private void log(String message){
        try {
            ui.showMessage("\t" + message + "\t" +  time());
            fm.writeLog("\t" + message + "\t" +  time());
        } catch (LogException e) {
            ui.showMessage("\t" + e.getMessage() + "\t" +  time());
        }
    }

    private String time(){
        return sdf.format(new Date());
    }

}
