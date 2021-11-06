package time;

import java.util.HashMap;

public class Date {
    private int year, month, date, hours, minutes;


    public Date() {}

    public Date(int year, int month, int date, int hours, int minutes) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.hours = hours;
        this.minutes = minutes;
    }


    @Override
    public String toString() {
        return addZero(date) + "." + addZero(month + 1) + "." + year + ", " + addZero(hours) + ":" + addZero(minutes);
    }

    private String addZero(int num) {
        return (num / 10 == 0 ? "0" : "") + num;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 0 || month > 11)
            throw new IllegalArgumentException();
        this.month = month;
    }

    public void addMonths(int count) {
        if (month + count > 11)
            ++year;
        year += (count - (11 - month)) / 12;
        month = (month + count) % 12;
    }

    public void setMonth(String month) {
        HashMap<String, Integer> monthToNum = new HashMap<>()
        {{
            put("ЯНВАРЬ", 0); put("ФЕВРАЛЬ", 1); put("МАРТ", 2); put("АПРЕЛЬ", 3); put("МАЙ", 4); put("ИЮНЬ", 5);
            put("ИЮЛЬ", 6); put("АВГУСТ", 7); put("СЕНТЯБРЬ", 8); put("ОКТЯБРЬ", 9); put("НОЯБРЬ", 10); put("ДЕКАБРЬ", 11);
            put("ЯНВАРЯ", 0); put("ФЕВРАЛЯ", 1); put("МАРТА", 2); put("АПРЕЛЯ", 3); put("МАЯ", 4); put("ИЮНЯ", 5);
            put("ИЮЛЯ", 6); put("АВГУСТА", 7); put("СЕНТЯБРЯ", 8); put("ОКТЯБРЯ", 9); put("НОЯБРЯ", 10); put("ДЕКАБРЯ", 11);
            put("JAN", 0); put("FEB", 1); put("MAR", 2); put("APR", 3); put("MAY", 4); put("JUN", 5);
            put("JUL", 6); put("AUG", 7); put("SEP", 8); put("OCT", 9); put("NOV", 10); put("DEC", 11);
        }};
        this.month = monthToNum.get(month.toUpperCase());
    }

    public static int getDaysInMonth(int month, int year) {
        if (month == 1) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                return 29;
            else
                return 28;
        }

        return new int[] { 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }[month];
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        if (date < 0 || date > getDaysInMonth(month, year))
            throw new IllegalArgumentException();
        this.date = date;
    }

    public void decrementDate() {
        if (date > 1)
            --date;
        else {
            month = (month + 11) % 12;
            date = getDaysInMonth(month, year);
            if (date == 31 && month == 11)
                --year;
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if (hours < 0 || hours > 23)
            throw new IllegalArgumentException();
        this.hours = hours;
    }

    public void incrementHours() {
        if (hours < 23)
            ++hours;
        else {
            hours = 0;
            if (getDaysInMonth(month, year) == date) {
                date = 1;
                month = (month + 13) % 12;
                if (month == 0)
                    ++year;
            }
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59)
            throw new IllegalArgumentException();
        this.minutes = minutes;
    }
}
