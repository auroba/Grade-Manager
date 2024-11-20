package com.example.gradeManager;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains methods to hold grade information from the JSON file.
 *
 * @author Auroba Ahmad
 */

public class Grade {
    @SerializedName("name")
    String Name;

    @SerializedName("category")
    String Category;

    @SerializedName("score")
    int Score;


    /**
     * This constructor takes three parameters and initializes the variables to the parameters.
     * @param name initializes the variable Name.
     * @param category initializes the variable Category.
     * @param score initializes the variable Score.
     */
    public Grade(String name, String category, int score) {
        Name = name;
        Category = category;
        Score = score;
    }

    /**
     * Gets the name variable.
     * @return the contents of name.
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the name variable.
     * @param name
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Gets the category variable.
     * @return the contents of category.
     */
    public String getCategory() {
        return Category;
    }

    /**
     * Sets the category variable.
     * @param category
     */
    public void setCategory(String category) {
        Category = category;
    }

    /**
     * Gets the score variable.
     * @return the contents of score.
     */
    public int getScore() {
        return Score;
    }

    /**
     * Sets the score variable.
     * @param score
     */
    public void setScore(int score) {
        Score = score;
    }


}
