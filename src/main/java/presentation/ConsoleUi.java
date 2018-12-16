package presentation;

import domain.UI;

public class ConsoleUi implements UI {
    public void showMessage(String message) {
        System.out.println(message);
    }
}
