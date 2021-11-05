package parsing.model;

import time.Date;

public class News {
    private String name, text;
    private Date date;
    private Image image;


    public News() {}

    public News(String name, String text, Date date, Image image) {
        setName(name);
        setText(text);
        setDate(date);
        setImage(image);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null)
            throw new NullPointerException();
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new NullPointerException();
        this.date = date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if (image == null)
            throw new NullPointerException();
        this.image = image;
    }
}
