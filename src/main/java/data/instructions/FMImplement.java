package data.instructions;

import data.util.FMhelper;
import domain.FileManager;
import exception.InputFileException;
import exception.LogException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FMImplement - реализация интерфейса FileManager, который отвечает за работу с файловой системой
 * Методы класса читают файл инструкций и преобразуют в entity - файлы Operation, а так же записывают данные в лог-файл
 */
public class FMImplement implements FileManager {
    private File logName;
    public FMImplement(){
        logName = new File("log.txt");
    }

    /**
     * Метод проверяет расширение файла и доступность, затем построчно читает команды и с помошью класса FMhelper,
     * парсит инструкции в ArrayList entity файлов Operation
     * @param filename - имя файла с командами
     * @return
     * @throws InputFileException - кастомное исключение-обертка ошибок, связанных с получением и парсингом текущего .txt файла.
     */
    public ArrayList<Operation> readInstructions(String filename) throws InputFileException{
        if(!filename.endsWith(".txt")){
            throw new InputFileException("[ Incorrect format of file \"" + filename + "\". program works just with \".txt\" files ]");
        }
        ArrayList<Operation> instruction = new ArrayList<Operation>();
        List<String> strings = FMhelper.getStrings(filename);
            for (String s : strings){
                instruction.add(FMhelper.getOperation(s));
            }
            if (instruction.isEmpty()){
                throw new InputFileException("[ Input file is empty! ]");
            }
        return instruction;
    }

    /**
     * Метод делает запись в файл с логами
     * @param log - сообщение, которое необходимо записать в лог
     * @throws LogException - кастомное исключение-обертка ошибок, вызываемых при записи в лог.
     */
    public void writeLog(String log) throws LogException {
        try {
            FileWriter fw = new FileWriter(logName, true);
        String lineSeparator = System.getProperty("line.separator");
            fw.write(log + lineSeparator);
        fw.close();
        } catch (IOException e) {
            throw new LogException("[ Error of writing \"" + log + "\" in log. Check log file for exists and access ]");
        }

    }



}
