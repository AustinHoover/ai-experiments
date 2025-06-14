package io.github.austinhoover.kobold;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;

import com.google.gson.Gson;

import io.github.austinhoover.kobold.response.KoboldResponseBody;

/**
 * Main object for interfacing with a koboldcpp endpoint
 */
public class Kobold {
    
    /**
     * Endpoint for generating
     */
    private static final String ENDPOINT_GENERATE = "/api/v1/generate";

    /**
     * Default local address
     */
    private static final String DEFAULT_LOCAL_ADDRESS = "127.0.0.1";

    /**
     * Default port
     */
    private static final int DEFAULT_PORT = 5001;

    /**
     * Address to connect to
     */
    private String address = DEFAULT_LOCAL_ADDRESS;

    /**
     * Port to connect at
     */
    private Integer port = DEFAULT_PORT;

    /**
     * Creates a kobold connection
     * @param address The address
     * @param port The port
     */
    public Kobold(String address, int port){
        this.address = address;
        this.port = port;
    }

    /**
     * Creates a kobold connection using default connection settings
     */
    public Kobold(){
    }

    /**
     * Generates text based on a prompt
     * @param prompt The prompt
     * @return The resulting text
     */
    public String generate(String prompt){
        return this.generate(new KoboldRequestBody(prompt));
    }

    /**
     * Generates text based on a prompt
     * @param requestBody The request body
     * @return The resulting text
     */
    public String generate(KoboldRequestBody requestBody){
        String rVal = null;
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String bodyText = gson.toJson(requestBody);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://" + this.address + ":" + this.port + ENDPOINT_GENERATE)).POST(BodyPublishers.ofString(bodyText)).build();
        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            String responseRaw = response.body();
            KoboldResponseBody responseBody = gson.fromJson(responseRaw, KoboldResponseBody.class);
            rVal = responseBody.getResults().get(0).getText();
        } catch (IOException e) {
            throw new Error(e);
        } catch (InterruptedException e) {
            throw new Error(e);
        }
        return rVal;
    }

    /**
     * Requests something from the kobold api
     * @param request The request
     * @return The response
     */
    public String request(String request){
        String bodyText = "Request:\n" + request + "\nResponse:\n";
        KoboldRequestBody requestBody = new KoboldRequestBody(bodyText);
        requestBody.setStopSequence(Arrays.asList(new String[]{"\nRequest:","\nResponse:","\nRequest "}));
        return this.generate(requestBody);
    }

}
