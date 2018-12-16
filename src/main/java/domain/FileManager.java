package domain;

import data.instructions.Operation;
import exception.InputFileException;
import exception.LogException;

import java.io.File;
import java.util.ArrayList;

public interface FileManager {
    ArrayList<Operation> readInstructions(String filename) throws InputFileException;
    void writeLog(String log) throws LogException;
}
