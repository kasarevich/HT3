package data.web;

import domain.WebManager;
import exception.WebException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebManagerImplement implements WebManager {
    private Document document = null;

    public boolean openUrl(String url, int timeout) throws WebException {
        try {
            document = Jsoup.connect(url).timeout(timeout).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebException("[ Error : opening of the page \"" + url + "\" has failed ]\n\t[ check the internet connection or correctness of the URL ]");
        }
        return document != null;
    }

    public boolean checkLinkByHref(String href) throws WebException {
        if(document == null){
            throw new WebException("[ Error : html document wasn't opened. Search of the link \"" + href + "\" is impossible ]\n\t[ Check the instructions order in the input file ]");
        }
        for(Element link : getLinks()){
            if(link.attr("href").equalsIgnoreCase(href)){
                return true;
            }
        }
        return false;
    }

    public boolean checkLinkByName(String name) throws WebException {
        if(document == null){
            throw new WebException("[ Error : html document wasn't opened. Search of the name \"" + name + "\" is impossible ]\n\t[ Check the instructions order in the input file ]");
        }
        for(Element link : getLinks()){
            if(link.text().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean checkTitle(String title) throws WebException {
        if(document == null){
            throw new WebException("[ Error : html document wasn't opened. Search of the title \"" + title + "\" is impossible ]\n\t[ Check the instructions order in the input file ]");
        }
        return document.title().equalsIgnoreCase(title);
    }

    public boolean checkPageContains(String text) throws WebException {
        if(document == null){
            throw new WebException("[ Error : html document wasn't opened. Search of the text \"" + text + "\" is impossible ]\n\t[ Check the instructions order in the input file ]");
        }
        Elements elements = document.getElementsContainingOwnText(text);
        return !elements.isEmpty();
    }

    private Elements getLinks(){
        return document.getElementsByTag("a");
    }
}
