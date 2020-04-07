package br.com.giovani.upcomingmovies.utils;

public class ImageUrlBuilder {
    private static ImageUrlBuilder instance;
    public static final String W300 = "w300";
    public static final String W780 = "w780";
    public static final String W1280 = "w1280";
    public static final String ORIGINAL = "original";

    private ImageUrlBuilder() {
    }

    public static synchronized ImageUrlBuilder getInstance() {
        if (instance == null) {
            instance = new ImageUrlBuilder();
        }
        return instance;
    }

    public String build(String baseUrl, String fileSize, String filePath) {
        final StringBuilder stringBuilder = new StringBuilder(baseUrl);
        stringBuilder
                .append(fileSize)
                .append(filePath);
        return stringBuilder.toString();
    }
}
