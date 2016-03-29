package fr.tedramoni.malblinder.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Ted on 29/03/2016.
 */
@XmlRootElement(name = "anime")
public class SearchList {
    private List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    @XmlElement(name="entry")
    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "SearchList{" +
                "entry=" + entry +
                '}';
    }
}
