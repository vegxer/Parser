package parsing.model;

public class Review {
    private int grade;
    private String reviewerName, review, pros, cons;

    public Review(int grade, String reviewerName, String review, String pros, String cons) {
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
        this.review = splitText(review);
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        if (pros == null)
            throw new NullPointerException();
        this.pros = splitText(pros);
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        if (cons == null)
            throw new NullPointerException();
        this.cons = splitText(cons);
    }

    public String splitText(String text) {
        StringBuilder str = new StringBuilder(text);

        for (int i = 100; i < str.length(); i += 100) {
            if (str.indexOf(" ", i) > 0)
                str.setCharAt(str.indexOf(" ", i), '\n');
            else
                str.insert(i, "-\n");
        }

        return str.toString();
    }
}
