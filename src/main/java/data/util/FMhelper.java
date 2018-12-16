package data.util;

import data.instructions.Operation;
import data.instructions.TypesOfOperation;
import exception.InputFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FMhelper {

    public static List<String> getStrings(String filename) throws InputFileException {
        List<String> strings = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null){
                strings.add(line);
            }
            br.close();
            if(strings.isEmpty()){
                throw new InputFileException("[ File { " + filename + "} is empty ]");
            }
        } catch (IOException e) {
            throw new InputFileException("[ File reading error! check path or access rights! Filename: {" + filename + "} ]");
        }
        return strings;
    }

    public static Operation getOperation(String text) throws InputFileException {
        int type = -1;
        String task = null;
        int timeout = -1;

        String[] tasks = text.split("\"");
        for(TypesOfOperation t : TypesOfOperation.values()){
            if(tasks[0].trim().equalsIgnoreCase(t.toString())){
                type = t.ordinal();
                break;
            }
        }
        if (type == -1){
            throw new InputFileException("[ Error of reading instruction { " + text + "} incorrect type of operation. ]");
        }
        try {
            task = tasks[1];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new InputFileException("[ Error of reading instruction { " + text + "} ]\n\t [ Instruction must contains name and task. Example: checkPageTitle \"Google Search Page\" ]");
        }
        if(task == null){
            throw new InputFileException("[ Error of reading instruction { " + text + "} ]\n\t [ Instruction must contains name and task. Example: checkPageTitle \"Google Search Page\" ]");
        }
        if(type == TypesOfOperation.open.ordinal()){
            try{
                timeout = Integer.parseInt(tasks[3])*1000; // [2] = space (open "http://www.google.com" "3")
            }catch (ArrayIndexOutOfBoundsException e){
                throw new InputFileException("[ Error of reading instruction { " + text + "} ]\n\t [ Instruction \"open\" must contains name, url and timeout. Example: open \"http://www.google.com\" \"3\" ]");
            }
        }
        return new Operation(TypesOfOperation.values()[type], task, timeout);
    }

}
