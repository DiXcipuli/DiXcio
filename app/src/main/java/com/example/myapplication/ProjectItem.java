package com.example.myapplication;

public class ProjectItem{
    private String projectName, language1, language2;

    public String getProjectName() {
        return projectName;
    }

    public String getLanguage1() {
        return language1;
    }

    public String getLanguage2() {
        return language2;
    }

    public ProjectItem(String projectName, String language1, String language2){
        this.projectName = projectName;
        this.language1 = language1;
        this.language2 = language2;
    }

}
