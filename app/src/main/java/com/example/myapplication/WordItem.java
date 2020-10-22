package com.example.myapplication;

public class WordItem {


    private String articleWord1;
    private String wordLanguage1;
    private String word1StoredAt;

    private String articleWord2;
    private String wordLanguage2;
    private String word2StoredAt;

    private Integer count;
    private Integer success;
    private Float percentage;

    private String category;
    private String date;
    private Integer bookmarked;

    public WordItem(String articleWord1, String wordLanguage1, String word1StoredAt, String articleWord2, String wordLanguage2, String word2StoredAt, Integer count, Integer success, Float percentage, String category, String date, Integer bookmarked) {
        this.articleWord1 = articleWord1;
        this.wordLanguage1 = wordLanguage1;
        this.word1StoredAt = word1StoredAt;

        this.articleWord2 = articleWord2;
        this.wordLanguage2 = wordLanguage2;
        this.word2StoredAt = word2StoredAt;

        this.count = count;
        this.success = success;
        this.percentage = percentage;

        this.category = category;
        this.date = date;
        this.bookmarked = bookmarked;
    }

    @Override
    public String toString() {
        String articleWord1Replacement = articleWord1, articleWord2Replacement = articleWord2;
        if(articleWord1.equals("None")){
            articleWord1Replacement = "";
        }

        if(articleWord2.equals("None")){
            articleWord2Replacement = "";
        }

        return articleWord1Replacement + " " + wordLanguage1 + " | " + articleWord2Replacement + " " + wordLanguage2;
    }

    public String toCSV(){
        return articleWord1 + "," + wordLanguage1 + "," + articleWord2 + "," + wordLanguage2 + "\n";
    }

    public String toTheString(){
        return wordLanguage1;
    }

    public String getWord1StoredAt() {
        return word1StoredAt;
    }

    public String getWord2StoredAt() {
        return word2StoredAt;
    }

    public String getArticleWord1() {
        return articleWord1;
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

    public String getDate() {
        return date;
    }

    public Integer getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Integer bookmarked) {
        this.bookmarked = bookmarked;
    }

    public String getArticleWord2() {
        return articleWord2;
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
