package com.flyerfive.client.data;

public class Font {
    private String family;
    private String variant;
    private String subset;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getSubset() {
        return subset;
    }

    public void setSubset(String subset) {
        this.subset = subset;
    }

    public static Font valueOf(String value) {
        Font f = new Font();
        f.setFamily(value);
        return f;
    }
    
    public String toString() {
        return family;
    }
}
