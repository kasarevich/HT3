package data.web;

import domain.WebManager;
import exception.WebException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Реализация интерфейса WebManager.
 * методы класса WebManagerImplement с помощью библиотеки jsoup получают html-документ по заданному URL-адресу
 * производят необходимые проверки и осуществляют поиск данных в документе
 */
public class WebManagerImplement implements WebManager {
    private Document document = null;

    /**
     * С помощью библиотеки jsoup, метод получает html-документ по заданному URL-адресу c заданным таймаутом
     * @param url URL дрес документа
     * @param timeout - время ожидания
     * @return true, если документ удалось получить, false, если документ пуст
     * @throws WebException кастомное исключение-обертка ошибок, связанных с получением и парсингом html-документа.
     */
    public boolean openUrl(String url, int timeout) throws WebException {
        try {
            document = Jsoup.connect(url).timeout(timeout).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebException("[ Error : opening of the page \"" + url + "\" has failed ]\n\t[ check the internet connection or correctness of the URL ]");
        }
        return document != null;
    }

    /**
     * Метод осуществляет поиск в документе ссылки с атрибутом "href", равным заданному в аргументах
     * @param href - URL, к которому ведет искомый элемент <a href = "@href"> </a>
     * @return true, ссылка найдена, false, если не найдена
     * @throws WebException кастомное исключение-обертка ошибок, связанных с получением и парсингом html-документа,
     * вызывается если документ пуст, что прпоисходит, скорее всего, из-за неверного порядка команд(попытка парсинга не открытого документа).
     */
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

    /**
     * Метод осуществляет поиск в документе ссылки со значением "name", равным заданному в аргументах
     * @param name - текст ссылки ( <a>name</a> )
     * @return true, ссылка найдена, false, если не найдена
     * @throws WebException кастомное исключение-обертка ошибок, связанных с получением и парсингом html-документа,
     * вызывается если документ пуст, что прпоисходит, скорее всего, из-за неверного порядка команд(попытка парсинга не открытого документа).
     */
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

    /**
     * Метод проверяет заголовок полученного документа на совпадение с переменной title, заданной в аргументах
     * @param title - текст заголовка
     * @return true, заголовок совпадает, false, если нет
     * @throws WebException кастомное исключение-обертка ошибок, связанных с получением и парсингом html-документа,
     * вызывается если документ пуст, что прпоисходит, скорее всего, из-за неверного порядка команд(попытка парсинга не открытого документа).
     */
    public boolean checkTitle(String title) throws WebException {
        if(document == null){
            throw new WebException("[ Error : html document wasn't opened. Search of the title \"" + title + "\" is impossible ]\n\t[ Check the instructions order in the input file ]");
        }
        return document.title().equalsIgnoreCase(title);
    }
    /**
     * Метод осуществляет поиск подстроки name без учета регистра в элементах документа, содержащих текстовые данные
     * @param text - искомый текст
     * @return true, текст найден, false, если не найден
     * @throws WebException кастомное исключение-обертка ошибок, связанных с получением и парсингом html-документа,
     * вызывается если документ пуст, что прпоисходит, скорее всего, из-за неверного порядка команд(попытка парсинга не открытого документа).
     */
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
