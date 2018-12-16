package data.instructions;

import data.util.FMhelper;
import domain.FileManager;
import exception.InputFileException;
import exception.LogException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FMImplement implements FileManager {
    private File logName;
    public FMImplement(){
        logName = new File("log.txt");
    }

    public ArrayList<Operation> readInstructions(String filename) throws InputFileException{
        if(!filename.endsWith(".txt")){
            throw new InputFileException("[ Incorrect format of file \"" + filename + "\". program works just with \".txt\" files ]");
        }
        ArrayList<Operation> instruction = new ArrayList<Operation>();
        List<String> strings = FMhelper.getStrings(filename);//fixme txt check
            for (String s : strings){
                instruction.add(FMhelper.getOperation(s));
            }
            if (instruction.isEmpty()){
                throw new InputFileException("[ Input file is empty! ]");
            }
        return instruction;
    }

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
