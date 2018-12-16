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

    private int totalCounter;
    private int successCounter;
    private long statTime;

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
        log("[ instructions from file \"" + filename + "\" are ready ]\n\t\t +++++++++++++ Tests +++++++++++++");

        statTime = System.nanoTime();
        successCounter = 0;
        totalCounter = 0;

        for(Operation operation : script){
            switch (operation.getType()){

                case open:{
                    try {
                        open(operation.getValue(), operation.getTimeout());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    totalCounter++;
                    break;
                }

                case checkPageTitle:{
                    try {
                    checkPageTitle(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    totalCounter++;
                    break;
                }

                case checkPageContains:{
                    try {
                    checkPageContains(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    totalCounter++;
                    break;
                }
                case checkLinkPresentByHref:{
                    try{
                    checkLinkPresentByHref(operation.getValue());
                    }catch (WebException e){
                        log(e.getMessage());
                    }
                    totalCounter++;
                    break;
                }
                case checkLinkPresentByName:{
                    try{
                    checkLinkPresentByName(operation.getValue());
                    }catch (WebException e){
                    log(e.getMessage());
                    }
                    totalCounter++;
                    break;
                }
            }
        }
        printResult();
    }

    private void open(String url, int timeout) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.openUrl(url, timeout);
        formatLog(result, "open \"" + url + "\" \"" + (timeout/1000) + "\"", start);
        if(result){
            successCounter ++;
        }
    }

    private void checkLinkPresentByHref(String href) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkLinkByHref(href);
        formatLog(result,"checkLinkByHref \"" + href +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    private void checkLinkPresentByName(String name) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkLinkByName(name);
        formatLog(result, "checkLinkPresentByName \"" + name +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    private void checkPageTitle(String title) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkTitle(title);
        formatLog(result, "checkPageTitle \"" + title +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    private void checkPageContains(String text) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkPageContains(text);
        formatLog(result, "checkPageContains \"" + text +"\"", start);
        if(result){
            successCounter ++;
        }
    }


    private void log(String message){
        try {
            ui.showMessage(time() + "\t" + message);
            fm.writeLog(time() + "\t" + message);
        } catch (LogException e) {
            ui.showMessage(time() + "\t" + e.getMessage());
        }
    }

    private String time(){
        return "[ " + sdf.format(new Date()) + " ]";
    }

    private double getConsumedTime(long start){
        return (System.nanoTime() - start) / 1000000000d;
    }

    private void formatLog(boolean result, String text, long startTime){
        if(result){
            log(" + [ " + text + " ] " + getConsumedTime(startTime));
        }else {
            log(" ! [ " + text + " ] " + getConsumedTime(startTime));
        }
    }

    public void printResult(){
        double total = getConsumedTime(statTime);
        double average = total/totalCounter;
        ui.showMessage("\n\t\t ------------- Tests -------------");
        log("Total tests: " + totalCounter);
        log("Failed: " + (totalCounter - successCounter));
        log("Total time: " + total);
        log("AverageTime: " + average);
    }
}
