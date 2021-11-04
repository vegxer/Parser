package parsing.model;

import java.util.ArrayList;

public class Reviews {
    private String name;
    private final ArrayList<Review> reviews;


    public Reviews(String shopName) {
        reviews = new ArrayList<>();
        setName(shopName);
    }


    public void addReview(Review review) {
        if (review == null)
            throw new NullPointerException();
        reviews.add(review);
    }

    public ArrayList<Review> getReviews() {
        return (ArrayList<Review>)reviews.clone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException();
        this.name = name;
    }

}
