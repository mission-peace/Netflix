package com.netflix.title;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class TitleFileIteratorTest {

    private static final String fileInput = "2008\tUS\tStarship troopers 3\n2008\tUK\tNaming Pluto";

    @Test
    public void testIterator() {
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes(StandardCharsets.UTF_8));
        TitleFileIterator titleFileIterator = new TitleFileIterator(inputStream);
        Assert.assertTrue(titleFileIterator.hasNext());
        TitleInfo titleInfo = titleFileIterator.next();
        Assert.assertEquals("2008 US Starship troopers 3", titleInfo.getTitle());
        Set<String> expectedTokens = new HashSet<>();
        expectedTokens.add("starship");
        expectedTokens.add("3");
        expectedTokens.add("troopers");
        expectedTokens.add("starship troopers 3");
        Assert.assertEquals(expectedTokens, titleInfo.getTokens());

        Assert.assertTrue(titleFileIterator.hasNext());
        titleInfo = titleFileIterator.next();
        Assert.assertEquals("2008 UK Naming Pluto", titleInfo.getTitle());
        expectedTokens = new HashSet<>();
        expectedTokens.add("naming pluto");
        expectedTokens.add("naming");
        expectedTokens.add("pluto");
        Assert.assertEquals(expectedTokens, titleInfo.getTokens());
    }
}
