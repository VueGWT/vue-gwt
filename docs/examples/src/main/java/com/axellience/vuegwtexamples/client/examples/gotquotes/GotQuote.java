package com.axellience.vuegwtexamples.client.examples.gotquotes;

public class GotQuote
{
    private final String text;
    private final String author;
    private final int season;
    private final int episode;

    public GotQuote(String text, String author, int season, int episode)
    {
        this.text = text;
        this.author = author;
        this.season = season;
        this.episode = episode;
    }

    public String getText()
    {
        return text;
    }

    public String getAuthor()
    {
        return author;
    }

    public int getSeason()
    {
        return season;
    }

    public int getEpisode()
    {
        return episode;
    }
}
