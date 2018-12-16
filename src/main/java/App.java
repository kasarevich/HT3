import data.instructions.FMImplement;
import data.web.WebManagerImplement;
import domain.AppManager;
import presentation.ConsoleUi;

public class App {
    public static void main(String[] args) {
        AppManager.getInstance().setUI(new ConsoleUi());
        AppManager.getInstance().setFileManager(new FMImplement());
        AppManager.getInstance().setWebManager(new WebManagerImplement());
        AppManager.getInstance().start("D:/instr.txt");


    }
}
