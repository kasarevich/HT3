import data.instructions.FMImplement;
import domain.Manager;
import presentation.ConsoleUi;

public class App {
    public static void main(String[] args) {
        Manager.getInstance().setUI(new ConsoleUi());
        Manager.getInstance().setFileManager(new FMImplement());
        Manager.getInstance().start("D:/instr.txt");


    }
}
