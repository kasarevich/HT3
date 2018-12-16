package domain;

import exception.WebException;

public interface WebManager {
    boolean openUrl(String url, int timeout) throws WebException;
    boolean checkLinkByHref(String href) throws WebException;
    boolean checkLinkByName(String name) throws WebException;
    boolean checkTitle(String title) throws WebException;
    boolean checkPageContains(String text) throws WebException;
}
