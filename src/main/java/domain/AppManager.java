package domain;

import data.instructions.Operation;
import exception.InputFileException;
import exception.LogException;
import exception.WebException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *  class AppManager сщдержит основную бизнес-логику фреймворка
 *  Модуль взаимодействует со слоем данных и UI через интерфейс,
 *  для возможности разной реальзации пользовательского интерфейса или логики слоя данных
 */
public class AppManager{
    private final static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");

    private UI ui;
    private FileManager fm;
    private WebManager wm;
    private ArrayList<Operation> script;

    private int totalCounter;
    private int successCounter;
    private long statTime;

    public AppManager() {
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

    /**
     * Метод получает готовый список инструкций ArrayList<Operation> script из WebManager вызовом метода
     * fm.readInstructions(filename) с именем файла инструкций.
     * Если парсинг прошел успешно, в цикле поочередно начинают исполняться инструкции, собирается общая статистика для лога.
     * Когда все инструкции выполнены, отображается и логируется статистика.
     * @param filename - название файла с инструкциями.
     */
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

    /**
     * Этот метод вызывает openUrl(url, timeout) класса WebManager, который переходит по url
     * и получает html документ. Если документ доступен, тест считается пройденным, статистика обновляется.
     * @param url - искомый URL адрес
     * @param timeout - максимальное время получения ответа
     * @throws WebException - кастомное исключение-обертка всех ошибок, связанных с получением и парсингом html-документа.
     * Сообщение об ошибке записывается в лог и выводится на экран.
     */
    private void open(String url, int timeout) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.openUrl(url, timeout);
        formatLog(result, "open \"" + url + "\" \"" + (timeout/1000) + "\"", start);
        if(result){
            successCounter ++;
        }
    }

    /**
     * Этот метод вызывает checkLinkByHref(href) класса WebManager, который ищет в полученном html-документе
     * ссылку со значением href. Если ссылка найдена, тест считается пройденным, статистика обновляется.
     * @param href - искомая ссылка
     * @throws WebException - кастомное исключение-обертка всех ошибок, связанных с получением и парсингом html-документа.
     * Сообщение об ошибке записывается в лог и выводится на экран.
     */
    private void checkLinkPresentByHref(String href) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkLinkByHref(href);
        formatLog(result,"checkLinkByHref \"" + href +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    /**
     * Этот метод вызывает checkLinkByName(name) класса WebManager, который ищет в полученном html-документе
     * ссылку с текстом name. Если ссылка найдена, тест считается пройденным, статистика обновляется.
     * @param name - текст искомой ссылки
     * @throws WebException - кастомное исключение-обертка всех ошибок, связанных с получением и парсингом html-документа.
     * Сообщение об ошибке записывается в лог и выводится на экран.
     */
    private void checkLinkPresentByName(String name) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkLinkByName(name);
        formatLog(result, "checkLinkPresentByName \"" + name +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    /**
     * Этот метод вызывает checkTitle(title) класса WebManager, который проверяет заголовок полученного html-документа
     * на равенство с title. Если заголовок совпадает, тест считается пройденным, статистика обновляется.
     * @param title - текст искомого заголовка
     * @throws WebException - кастомное исключение-обертка всех ошибок, связанных с получением и парсингом html-документа.
     * Сообщение об ошибке записывается в лог и выводится на экран.
     */
    private void checkPageTitle(String title) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkTitle(title);
        formatLog(result, "checkPageTitle \"" + title +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    /**
     * Этот метод вызывает checkPageContains(text) класса WebManager, который осуществляет поиск в полученном html-документе
     * текст. Если текст найден, тест считается пройденным, статистика обновляется.
     * @param text - искомый текст
     * @throws WebException - кастомное исключение-обертка всех ошибок, связанных с получением и парсингом html-документа.
     * Сообщение об ошибке записывается в лог и выводится на экран.
     */
    private void checkPageContains(String text) throws WebException {
        long start = System.nanoTime();
        boolean result = wm.checkPageContains(text);
        formatLog(result, "checkPageContains \"" + text +"\"", start);
        if(result){
            successCounter ++;
        }
    }

    /**
     * Этот метод передает сообщение для вывода на экран и делает запись в лог.
     * @param message
     */
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
