package model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcrLine {

    @SerializedName("boundingBox")
    @Expose
    private String boundingBox;
    @SerializedName("words")
    @Expose
    private List<OcrWord> words = null;

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<OcrWord> getWords() {
        return words;
    }

    public void setWords(List<OcrWord> words) {
        this.words = words;
    }

}