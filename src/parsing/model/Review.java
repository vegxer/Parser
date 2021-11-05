package parsing.model;

import time.Date;

public class Review {
    private int grade;
    private String reviewerName, review, pros, cons, shopName;
    private Date date;

    public Review(String shopName, Date date, int grade, String reviewerName, String review, String pros, String cons) {
        setDate(date);
        setShopName(shopName);
        setGrade(grade);
        setReviewerName(reviewerName);
        setReview(review);
        setCons(cons);
        setPros(pros);
    }


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        if (grade < 1 || grade > 5)
            throw new IllegalArgumentException();
        this.grade = grade;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        if (reviewerName == null)
            throw new NullPointerException();
        this.reviewerName = reviewerName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        if (review == null)
            throw new NullPointerException();
        this.review = review;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        if (pros == null)
            throw new NullPointerException();
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        if (cons == null)
            throw new NullPointerException();
        this.cons = cons;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        if (shopName == null)
            throw new NullPointerException();
        this.shopName = shopName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new NullPointerException();
        this.date = date;
    }
}
