package io.github.austinhoover.backend.kobold;

import java.util.List;

/**
 * Body of a request to kobold
 */
public class KoboldRequestBody {
    
    private Integer max_content_length;

    private Integer max_length;

    private String prompt;

    private Boolean quiet;

    private Double rep_pen;

    private Integer rep_pen_range;

    private Double rep_pen_slope;

    private Double temperature;

    private Integer tfs;

    private Integer top_a;

    private Integer top_k;

    private Double top_p;

    private Integer typical;

    private List<String> stop_sequence;

    private boolean use_default_badwordsids;

    /**
     * Generic constructor
     */
    public KoboldRequestBody(){}

    /**
     * Main constructor for creating a request
     * @param prompt The prompt to send
     */
    public KoboldRequestBody(String prompt){
        this.prompt = prompt;
    }

    public Integer getMaxContentLength() {
        return max_content_length;
    }

    public void setMaxContentLength(Integer max_content_length) {
        this.max_content_length = max_content_length;
    }

    public Integer getMaxLength() {
        return max_length;
    }

    public void setMaxLength(Integer max_length) {
        this.max_length = max_length;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Boolean getQuiet() {
        return quiet;
    }

    public void setQuiet(Boolean quiet) {
        this.quiet = quiet;
    }

    public Double getRepPen() {
        return rep_pen;
    }

    public void setRepPen(Double rep_pen) {
        this.rep_pen = rep_pen;
    }

    public Integer getRepPenRange() {
        return rep_pen_range;
    }

    public void setRepPenRange(Integer rep_pen_range) {
        this.rep_pen_range = rep_pen_range;
    }

    public Double getRepPenSlope() {
        return rep_pen_slope;
    }

    public void setRepPenSlope(Double rep_pen_slope) {
        this.rep_pen_slope = rep_pen_slope;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getTfs() {
        return tfs;
    }

    public void setTfs(Integer tfs) {
        this.tfs = tfs;
    }

    public Integer getTopA() {
        return top_a;
    }

    public void setTopA(Integer top_a) {
        this.top_a = top_a;
    }

    public Integer getTopK() {
        return top_k;
    }

    public void setTopK(Integer top_k) {
        this.top_k = top_k;
    }

    public Double getTopP() {
        return top_p;
    }

    public void setTopP(Double top_p) {
        this.top_p = top_p;
    }

    public Integer getTypical() {
        return typical;
    }

    public void setTypical(Integer typical) {
        this.typical = typical;
    }

    public List<String> getStopSequence() {
        return stop_sequence;
    }

    public void setStopSequence(List<String> stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public boolean isUseDefaultBadwordsids() {
        return use_default_badwordsids;
    }

    public void setUseDefaultBadwordsids(boolean use_default_badwordsids) {
        this.use_default_badwordsids = use_default_badwordsids;
    }

}
