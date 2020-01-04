package model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcrResponseModel {

    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("textAngle")
    @Expose
    private Double textAngle;
    @SerializedName("orientation")
    @Expose
    private String orientation;
    @SerializedName("regions")
    @Expose
    private List<OcrRegion> regions = null;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getTextAngle() {
        return textAngle;
    }

    public void setTextAngle(Double textAngle) {
        this.textAngle = textAngle;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public List<OcrRegion> getRegions() {
        return regions;
    }

    public void setRegions(List<OcrRegion> regions) {
        this.regions = regions;
    }

}