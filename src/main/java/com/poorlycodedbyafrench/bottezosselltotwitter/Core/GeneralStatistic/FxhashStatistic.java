/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class FxhashStatistic implements StatisticCallerInterface {

    private List<String> contracts;

    public FxhashStatistic(List<String> contracts) {
        this.contracts = contracts;
    }

    
    
    @Override
    public MarketPlaceEnum getMp() {
        return MarketPlaceEnum.fxhash;
    }

    @Override
    public HashMap<MarketPlaceEnum, List<GeneralStatistic>> call() throws Exception {
        List<GeneralStatistic> fxhashStat = new ArrayList<>();

        String responseJson = sendRequest();
        fxhashStat.addAll(analyseJson(responseJson));

        HashMap<MarketPlaceEnum, List<GeneralStatistic>> statReturn = new HashMap<>();
        statReturn.put(MarketPlaceEnum.fxhash, fxhashStat);

        return statReturn;
    }
    
    
    
    private String sendRequest() throws IOException, InterruptedException, URISyntaxException {

        String buildIn = "";

        for (String contract : contracts) {
            if (!buildIn.equals("")) {
                buildIn += ",";
            }
            buildIn += contract;

        }

        String POST_PARAMS = "{ \"query\" : \"query MyQuery {  generativeTokens(filters: {id_in: [" + buildIn + " ]}) { slug   balance    marketStats {      floor      listed      secVolumeTz      secVolumeTz24    }    name    objktsCount    id    supply  }}\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.fxhash.xyz/graphql"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.of(5, SECONDS))
                .POST(HttpRequest.BodyPublishers.ofString(POST_PARAMS))
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private List<GeneralStatistic> analyseJson(String responseJson) {
        List<GeneralStatistic> dataToReturn = new ArrayList<>();

        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();
        JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("generativeTokens"); 

        for (Object c : contractsJSON) {
            JsonObject contractJSON = (JsonObject) c;
            
            String contract = contractJSON.get("id").getAsString();
            String name = contractJSON.get("name").getAsString();
            
            Long countActiveAuctions = Long.valueOf(0);
            Long itemNumber = contractJSON.get("objktsCount").getAsLong();
            
            Long countActiveListing = contractJSON.getAsJsonObject("marketStats").get("listed").getAsLong();
            Double floor_price = 0.0;
                    
            if (!contractJSON.getAsJsonObject("marketStats").get("floor").isJsonNull()){
                floor_price = contractJSON.getAsJsonObject("marketStats").get("floor").getAsDouble()/1000000.0;
            }
            
            Double allTimeVolume = contractJSON.getAsJsonObject("marketStats").get("secVolumeTz").getAsDouble()/1000000.0;
            Double lastDayVolume = contractJSON.getAsJsonObject("marketStats").get("secVolumeTz24").getAsDouble()/1000000.0;
            String path = contractJSON.get("slug").getAsString().trim();
            
            List<String> idDutchAuction = new ArrayList<>();
            List<String> idEnglishAuction = new ArrayList<>();
            
            dataToReturn.add(new GeneralStatistic(contract, name, allTimeVolume, lastDayVolume, floor_price, itemNumber, countActiveAuctions, countActiveListing, idEnglishAuction, idDutchAuction, path));
        }
        
        return dataToReturn;
    }

}
