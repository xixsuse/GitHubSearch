package pl.nalazek.githubsearch;

/**
 * This data class represents a parsed search result used to fill in the CustomListAdapter
 * @author Daniel Nalazek
 */

public abstract class SearchResult {

    private String title;
    private String description;
    private ExchangeType exchangeType;

    protected SearchResult(String title, String description, ExchangeType exchangeType ) {
        this.title = title;
        this.description = description;
        this.exchangeType = exchangeType;
    }

    public String getName() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ExchangeType getType() {
        return exchangeType;
    }
}
