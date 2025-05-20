package com.example.project3;

public class WorkoutModel {
    private String id;
    private String title;
    private String description;
    private String benefits;
    private String steps;
    private String setsReps;
    private int imageResId;
    private String timer;

    public WorkoutModel(String id, String title, String description, String benefits, String steps, String setsReps, int imageResId, String timer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.benefits = benefits;
        this.steps = steps;
        this.setsReps = setsReps;
        this.imageResId = imageResId;
        this.timer = timer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBenefits() {
        return benefits;
    }

    public String getSteps() {
        return steps;
    }

    public String getSetsReps() {
        return setsReps;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTimer() {
        return timer;
    }
}
