package presentation;

import domain.UI;

/**
 * Консольная реализация пользовательского интерфейса
 */
public class ConsoleUi implements UI {
    public void showMessage(String message) {
        System.out.println(message);
    }
}
