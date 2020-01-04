package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcrRegion {

    @SerializedName("boundingBox")
    @Expose
    private String boundingBox;
    @SerializedName("lines")
    @Expose
    private List<OcrLine> lines = null;

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<OcrLine> getLines() {
        return lines;
    }

    public void setLines(List<OcrLine> lines) {
        this.lines = lines;
    }

}