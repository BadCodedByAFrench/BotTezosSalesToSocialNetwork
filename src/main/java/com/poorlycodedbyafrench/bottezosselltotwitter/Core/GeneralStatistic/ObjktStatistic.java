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
public class ObjktStatistic implements StatisticCallerInterface {

    private List<String> contracts;

    public ObjktStatistic(List<String> contracts) {
        this.contracts = contracts;
    }

    @Override
    public MarketPlaceEnum getMp() {
        return MarketPlaceEnum.Objkt;
    }

    @Override
    public HashMap<MarketPlaceEnum, List<GeneralStatistic>> call() throws Exception {
        List<GeneralStatistic> objktStat = new ArrayList<>();

        String responseJson = sendRequest();
        objktStat.addAll(analyseJson(responseJson));

        HashMap<MarketPlaceEnum, List<GeneralStatistic>> statReturn = new HashMap<>();
        statReturn.put(MarketPlaceEnum.Objkt, objktStat);

        return statReturn;
    }

    private String sendRequest() throws IOException, InterruptedException, URISyntaxException {

        String buildIn = "";

        for (String contract : contracts) {
            if (!buildIn.equals("")) {
                buildIn += ",";
            }
            buildIn += "\\\"" + contract + "\\\"";

        }

        String POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(    where: {contract: {_in: [" + buildIn + "]}}  ) {  path  floor_price    active_auctions    active_listing    volume_24h    volume_total    items    name    contract    dutch_auctions(where: {status: {_eq: \\\"active\\\"}}) {     token {     token_id      }    }    english_auctions(where: {status: {_eq: \\\"active\\\"}}) { token {     token_id      }  }  }}\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://data.objkt.com/v3/graphql"))
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
        JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("fa");

        for (Object c : contractsJSON) {
            JsonObject contractJSON = (JsonObject) c;

            String contract = contractJSON.get("contract").getAsString();
            String name = contractJSON.get("name").getAsString();
            Long countActiveAuctions = contractJSON.get("active_auctions").getAsLong();
            Long countActiveListing = contractJSON.get("active_listing").getAsLong();
            Double floor_price = contractJSON.get("floor_price").getAsDouble() / 1000000.0;
            Long itemNumber = contractJSON.get("items").getAsLong();
            Double allTimeVolume = contractJSON.get("volume_total").getAsDouble() / 1000000.0;
            Double lastDayVolume = contractJSON.get("volume_24h").getAsDouble() / 1000000.0;

            String path = contract;
            if (!contractJSON.get("path").isJsonNull()) {
                path = contractJSON.get("path").getAsString();
            }

            JsonArray dutchAuctionJSON = contractJSON.getAsJsonArray("dutch_auctions");
            List<String> idDutchAuction = new ArrayList<>();
            for (Object d : dutchAuctionJSON) {
                JsonObject dutchAuction = (JsonObject) d;
                idDutchAuction.add(dutchAuction.getAsJsonObject("token").get("token_id").getAsString());
            }

            JsonArray englishAuctionJSON = contractJSON.getAsJsonArray("english_auctions");
            List<String> idEnglishAuction = new ArrayList<>();
            for (Object e : englishAuctionJSON) {
                JsonObject englishAuction = (JsonObject) e;
                idEnglishAuction.add(englishAuction.getAsJsonObject("token").get("token_id").getAsString());
            }

            dataToReturn.add(new GeneralStatistic(contract, name, allTimeVolume, lastDayVolume, floor_price, itemNumber, countActiveAuctions, countActiveListing, idEnglishAuction, idDutchAuction, path));
        }

        return dataToReturn;
    }

}
