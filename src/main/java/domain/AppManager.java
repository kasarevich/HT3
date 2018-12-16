package domain;

import data.instructions.Operation;
import exception.InputFileException;
import exception.LogException;
import exception.WebException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppManager {
    private static AppManager instance;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");

    private UI ui;
    private FileManager fm;
    private WebManager wm;
    private ArrayList<Operation> script;

    public static AppManager getInstance(){
        if(instance == null){
            instance = new AppManager();
        }
        return instance;
    }
    private AppManager() {
    }

    public void setUI(UI ui){
        this.ui = ui;
    }
    public void setFileManager(FileManager fm){
        this.fm = fm;
    }
    public void setWebManager(WebManager wm){
        this.wm = wm;
    }

    public void start(String filename) {
        log("[ parsing \"" + filename + "\" started ]");
        try {
            script = fm.readInstructions(filename);
        } catch (InputFileException e) {
            log(e.getMessage());
            return;
        }
        log("[ instructions from file \"" + filename + "\" is ready ]");
        for(Operation operation : script){
            switch (operation.getType()){

                case open:{
                    try {
                        open(operation.getValue(), operation.getTimeout());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    break;
                }

                case checkPageTitle:{
                    try {
                    checkPageTitle(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    break;
                }

                case checkPageContains:{
                    try {
                    checkPageContains(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    break;
                }
                case checkLinkPresentByHref:{
                    try{
                    checkLinkPresentByHref(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    break;
                }
                case checkLinkPresentByName:{
                    try{
                    checkLinkPresentByName(operation.getValue());
                    }catch (WebException e){
                    log(e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    private void open(String url, int timeout) throws WebException {
            if(wm.openUrl(url, timeout)){
                log("[ ok \"" + url + "\" opened ]");
            }else {
                log("[ fail \"" + url + "\" opened ]");
            }
    }

    private void checkLinkPresentByHref(String href) throws WebException {
            if(wm.checkLinkByHref(href)){
                log("[ ok link \"" + href + "\" found ]");
            }else {
                log("[ fail link \"" + href + "\" not found ]");
            }
    }

    private void checkLinkPresentByName(String name) throws WebException {
            if(wm.checkLinkByName(name)){
                log("[ ok name \"" + name + "\" found ]");
            }else {
                log("[ fail name \"" + name + "\" not found ]");
            }
    }

    private void checkPageTitle(String title) throws WebException {
            if(wm.checkTitle(title)){
                log("[ ok title \"" + title + "\" found ]");
            }else {
                log("[ fail title \"" + title + "\" not found ]");
            }
    }

    private void checkPageContains(String text) throws WebException {
            if(wm.checkPageContains(text)){
                log("[ ok page contains \"" + text + "\" found ]");
            }else {
                log("[ fail page don't contains \"" + text + "\" not found ]");
            }
    }
    

    private void log(String message){
        try {
            ui.showMessage(time() + "\t" + message);
            fm.writeLog("\t" + message + "\t" +  time());
        } catch (LogException e) {
            ui.showMessage(time() + "\t" + e.getMessage());
        }
    }

    private String time(){
        return sdf.format(new Date());
    }

}
