package assasingh.nearmev2.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beaumoaj on 10/02/15.
 */
public class GooglePlaceList {
    private String status;
    private String next_page_token;
    private List<GooglePlace> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GooglePlace> getResults() {
        return results;
    }

    public int getSize(){
        return results.size();
    }

    public void setResults(List<GooglePlace> results) {
        this.results = results;
    }


    public List<String> getPlaceNames() {
        List<String> result = new ArrayList<String>();
        for (GooglePlace place : results) {
            result.add(place.toString());
        }
        return result;
    }

    public List<String> getPlaceLatLng(){
        List<String> result = new ArrayList<String>();
        for (GooglePlace place : results){
            result.add(place.getLatLng());
        }
        return result;
    }

    public List<Double> getLongitudes(){
        List<Double> result = new ArrayList<Double>();
        for(GooglePlace g : results){
            result.add(g.getLongitude());
        }
        return result;
    }

    public List<Double> getLatitudes(){
        List<Double> result = new ArrayList<Double>();
        for(GooglePlace g : results){
            result.add(g.getLongitude());
        }
        return result;
    }

}

