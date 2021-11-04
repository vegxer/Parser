package parsing;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public abstract class NestingLevel {
    protected boolean isFinal = false;
    private int startPoint, endPoint;
    private Elements elements;

    public NestingLevel(int startPoint, int endPoint) {
        setStartPoint(startPoint);
        setEndPoint(endPoint);
    }

    public NestingLevel(int startPoint, int endPoint, Elements elements) {
        this(startPoint, endPoint);
        setElements(elements);
    }

    public abstract NestingLevel getNextLvl(Element currElement);
    public abstract Element getElementById(int id) throws IOException;


    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        if (startPoint < 1)
            throw new IllegalArgumentException();
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        if (endPoint < startPoint)
            throw new IllegalArgumentException();
        this.endPoint = endPoint;
    }

    public Elements getElements() {
        return elements.clone();
    }

    public Element getElement(int index) {
        return elements.get(index).clone();
    }

    public void setElements(Elements elements) {
        if (elements == null)
            throw new NullPointerException();
        this.elements = elements.clone();
    }

    public boolean isFinal() {
        return isFinal;
    }
}
