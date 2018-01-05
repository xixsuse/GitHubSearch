package pl.nalazek.githubsearch;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 * This interface is used to be applied for classes being used to keep and show results parsed from JSON
 * @author Daniel Nalazek
 */

public interface Showable  {

    void showResults(ArrayList<? extends Result> resultsArray);

    void showError(String error);

    void showBusy();
}