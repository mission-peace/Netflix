package com.netflix.title;

import java.util.Collection;

/**
 * Pairs title with all the tokens of this title.
 */
public class TitleInfo {
    private final String yearCountryOfTitle;
    private final String title;
    private final Collection<String> tokens;

    TitleInfo(final String yearCountryOfTitle, final String title, final Collection<String> tokens) {
        this.yearCountryOfTitle = yearCountryOfTitle;
        this.title = title;
        this.tokens = tokens;
    }
    public String getTitle() {
        return title;
    }

    public String getFullTitle() {
        return yearCountryOfTitle + " " + title;
    }
    public Collection<String> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "TitleInfo{" +
                "title='" + title + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
