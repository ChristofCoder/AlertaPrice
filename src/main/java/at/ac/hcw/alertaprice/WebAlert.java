package at.ac.hcw.alertaprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;

public class WebAlert {
    private int id;
    private String name;
    private String url;
    private String cssSelector;
    private String originalValue;
    private String stringCreatedAt;

    public WebAlert(int id, String name, String url, String cssSelector) throws IOException { // throws error if website connection is rejected
        this.id = id;
        this.name = name;
        this.url = url;
        this.cssSelector = cssSelector;
        this.originalValue = getCurrentValue();
        this.stringCreatedAt = LocalDate.now().toString();
    }

    public WebAlert() {
        // this empty constructor is needed for gson
    }

    public String getCurrentValue() throws IOException { // throws error if website connection is rejected
        Document doc = Jsoup.connect(url).get(); // loads website into doc
        Elements elements = doc.select(cssSelector); // saves all searched elements

        if (elements.isEmpty()) { // output if searched element is not found
            return "Nicht gefunden";
        }

        return elements.first().text(); // returns only the first found element (w/o HTML-tags)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getStringCreatedAt() {
        return stringCreatedAt;
    }

    public void setStringCreatedAt(LocalDate createdAt) {
        this.stringCreatedAt = LocalDate.now().toString();
    }

}
