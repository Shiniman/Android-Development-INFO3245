package com.example.project;

public class FinanceModel {

    // variables for our finance table
    private int spent;
    private String description;
    private String category;
    private String date;
    private int id;

    // creating getter and setter methods
    public int getSpentAmount() { return spent; }

    public void setSpentAmount(int spent) {
        this.spent = spent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) { this.description = description; }

    public String getCategory() { return category; }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    // constructor
    public FinanceModel(int spent,
                        String description,
                        String category,
                        String date)
    {
        this.spent = spent;
        this.description = description;
        this.category = category;
        this.date = date;
    }
}
