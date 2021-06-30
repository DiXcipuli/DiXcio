package com.DiXcipuli.DiXcio;

import java.io.Serializable;

public class WordItem implements Serializable {

    private String wordLanguage1;

    private String wordLanguage2;

    private Integer count;
    private Integer success;
    private Float percentage;

    private String category;
    private String date;
    private Integer bookmarked;

    public WordItem(String wordLanguage1, String wordLanguage2, Integer count, Integer success, Float percentage, String category, String date, Integer bookmarked) {
        this.wordLanguage1 = wordLanguage1;
        this.wordLanguage2 = wordLanguage2;

        this.count = count;
        this.success = success;
        this.percentage = percentage;

        this.category = category;
        this.date = date;
        this.bookmarked = bookmarked;
    }

    public void setWordLanguage1(String wordLanguage1) {
        this.wordLanguage1 = wordLanguage1;
    }

    public void setWordLanguage2(String wordLanguage2) {
        this.wordLanguage2 = wordLanguage2;
    }

    @Override
    public String toString() {
        return wordLanguage1 + " | " + wordLanguage2 + count + success + percentage;
    }

    public String getInfo(){
        return "Word encountered " + count + " time(s), success rate: " + percentage + "%";
    }

    public String toCSV(){
        return wordLanguage1 + ";" +  wordLanguage2 + "\n";
    }

    public String toTheString(){
        return wordLanguage1;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getSuccess() {
        return success;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public String getDate() {
        return date;
    }

    public Integer getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Integer bookmarked) {
        this.bookmarked = bookmarked;
    }

    public String getWordLanguage1() {
        return wordLanguage1;
    }

    public String getWordLanguage2() {
        return wordLanguage2;
    }

    public String getCategory() {
        return category;
    }
}
