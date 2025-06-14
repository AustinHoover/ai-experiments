package io.github.austinhoover.kobold.response;

import java.util.List;

/**
 * Body of a response from kobold
 */
public class KoboldResponseBody {
    
    /**
     * The generation results
     */
    private List<KoboldResponseObj> results;

    /**
     * Gets the results from the body
     * @return The results
     */
    public List<KoboldResponseObj> getResults(){
        return results;
    }

}
