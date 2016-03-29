package fr.tedramoni.malblinder.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Ted on 29/03/2016.
 */
@XmlRootElement(name = "entry")
public class Entry {

    private String title;

    private Integer id;

    private String img;

    public Entry() {
    }

    public String getTitle() {
        return title;
    }

    @XmlElement(name="title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    @XmlElement(name="id")
    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    @XmlElement(name="image")
    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", img='" + img + '\'' +
                '}';
    }
}
