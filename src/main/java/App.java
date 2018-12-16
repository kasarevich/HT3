import data.instructions.FMImplement;
import data.web.WebManagerImplement;
import domain.AppManager;
import presentation.ConsoleUi;

public class App {
    /**
     * Точка входа в программу.
     * Устанавливаются конфигурационные параметры
     * @param args [0] - путь к файлу с инструкциями.
     *             Пример файла с инструкциями :
     *    inputExample.txt :
     *             open "https://kinogo.by/7672-community_1-2-3-4-5-6-sezon.html" "10"
     *             checkPageTitle "Однокурсники 1-6 сезон смотреть онлайн бесплатно в HD 720p"
     *             checkPageContains "Сериал рассказывает о студенческом которое состоит из школьных лузеров"
     *             checkLinkPresentByHref "/film/kriminal/"
     *             checkLinkPresentByName "комедии"
     */
    public static void main(String[] args) {
        AppManager manager = new AppManager();
        manager.setUI(new ConsoleUi());
        manager.setFileManager(new FMImplement());
        manager.setWebManager(new WebManagerImplement());
        try {
            manager.start(args[0]);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
