/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.data.ResultObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.nalazek.githubsearch.data.JsonObjects.*;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResponsePartitioned;

/**
 * Produces SearchResult's. Do not use directly, to avoid throwing {@link InvalidJsonObjectException}.
 * Instead use {@link ResultCreator}.
 */
public class SearchResultFactory implements ResultFactory {

    private static final SearchResultFactory instance = new SearchResultFactory();


    private SearchResultFactory() {}


    public static SearchResultFactory getInstance() {
        return instance;
    }



    @Override
    public SearchResult[] makeResults(ResponsePartitioned responsePartitioned) throws InvalidJsonObjectException {

        JsonObject jsonObject = responsePartitioned.getJsonObject();
        if(jsonObject instanceof Itemable)
            return createResultsArray((Itemable)jsonObject);
        else if(jsonObject == null)
            throw new InvalidJsonObjectException("Expected Itemable JsonObject! Got: null");
        else
            throw new InvalidJsonObjectException("Expected Itemable JsonObject! Got: "
                    + jsonObject.getClass().getName()
                    + ". Try another using another ResultFactory");
    }



    @Override
    public List<SearchResult> makeResults(ResponsePackage responsePackage) throws InvalidJsonObjectException {

        ArrayList<ResponsePartitioned> responsesPartitioned = responsePackage.getResponses();
        List<SearchResult> results = new ArrayList<>();

        for(ResponsePartitioned responsePartitioned : responsesPartitioned) {
            SearchResult[] searchResults = makeResults(responsePartitioned);
            List<SearchResult> loopResults = Arrays.asList(searchResults);
            results.addAll(loopResults);
        }
        return results;
    }



    private SearchResult[] createResultsArray(Itemable itemable) throws InvalidJsonObjectException {
        List<? extends JsonItem> jsonItems = itemable.getItems();
        return iterateOverItemsAndCreateResults(jsonItems);
    }



    private SearchResult[] iterateOverItemsAndCreateResults(List<? extends JsonItem> jsonItems) throws InvalidJsonObjectException {

        int size = jsonItems.size();
        SearchResult[] results = new SearchResult[size];
        int i = 0;

        for(JsonItem item : jsonItems) {
            results[i++] = createResult(item);
        }
        return results;
    }



    private SearchResult createResult(JsonItem item) throws InvalidJsonObjectException {

            if (item instanceof JsonRepoSearchResult.Item) {
                return createRepoSearchResult((JsonRepoSearchResult.Item) item);
            }
            else if (item instanceof JsonUserSearchResult.Item) {
                return createUserSearchResult((JsonUserSearchResult.Item) item);
            }
            else throw new InvalidJsonObjectException("JsonItem object not recognized! Got: " + item.getClass().getName());
    }



    private RepoSearchResult createRepoSearchResult(JsonRepoSearchResult.Item item) {
        return new RepoSearchResult(item.getName(), item.getHtmlUrl(), item.getUrl(), item.getId());
    }



    private UserSearchResult createUserSearchResult(JsonUserSearchResult.Item item) {

        String starredURL = item.getStarredUrl().contains("{") ?
                (item.getStarredUrl().split("\\{"))[0] : item.getStarredUrl();

        return new UserSearchResult(
                item.getLogin(),
                item.getHtmlUrl(),
                item.getUrl(),
                starredURL,
                item.getAvatarUrl(),
                item.getId());
    }
}

