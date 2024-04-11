package com.example.project;

public class BudgetModel {

    // variables for our finance table
    private double income, entertainment, utilities, food, other, savingsGoal;
    private int id;

    // creating getter and setter methods
    public double getIncomeAmount() { return income; }

    public void setIncomeAmount(double income) { this.income = income; }

    public double getEntertainment() { return entertainment; }

    public void setEntertainment (double entertainment) { this.entertainment = entertainment; }

    public double getUtilities() { return utilities; }

    public void setUtilities(double utilities) { this.utilities = utilities; }

    public double getFood() { return food; }

    public void setFood(double food) { this.food = food; }

    public double getOther() { return other; }

    public void setOther(double other) { this.other = other; }

    public double getSavingsPercentage() { return savingsGoal; }

    public void setSavingsPercentage(double savingsGoal) { this.savingsGoal = savingsGoal; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    // constructor
    public BudgetModel(double income,
                       double entertainment,
                       double utilities,
                       double food,
                       double other,
                       double savingsGoal)
    {
        this.income = income;
        this.entertainment = entertainment;
        this.utilities = utilities;
        this.food = food;
        this.other = other;
        this.savingsGoal = savingsGoal;
    }
}