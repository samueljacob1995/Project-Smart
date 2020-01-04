package model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction {

    @SerializedName("probability")
    @Expose
    private Double probability;
    @SerializedName("tagId")
    @Expose
    private String tagId;
    @SerializedName("tagName")
    @Expose
    private String tagName;

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Prediction(Double probability, String tagId, String tagName) {
        this.probability = probability;
        this.tagId = tagId;
        this.tagName = tagName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getProbability() + " === " + this.getTagName();
    }
}