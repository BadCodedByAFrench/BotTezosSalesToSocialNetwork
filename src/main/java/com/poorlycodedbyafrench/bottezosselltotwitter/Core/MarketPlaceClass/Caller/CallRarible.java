/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Caller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.LastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Address;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class CallRarible implements CallMarketPlaceInterface {

    private static HashMap<String, String> allCollectionsNames;

    private BotModeEnum mode;

    private List<String> contracts;

    private LastRefresh lastrefresh;

    public CallRarible(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) {
        this.mode = mode;
        this.contracts = contracts;
        this.lastrefresh = lastrefresh;
        CallRarible.allCollectionsNames = new HashMap<String, String>();
    }

    /**
     * We send the request to get all the sell from the previous hour
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    private String sendRequest(List<String> contracts) throws IOException, InterruptedException, URISyntaxException {

        String buildIn = "";

        for (String contract : contracts) {
            if (!buildIn.equals("")) {
                buildIn += ",";
            }
            buildIn += "TEZOS:" + contract;

        }

        String POST_PARAMS = "";

        if (this.mode != BotModeEnum.ListingAndBidding) {
            POST_PARAMS = "https://api.rarible.org/v0.1/activities/byCollection?type=SELL&collection=" + buildIn + "&size=50&sort=LATEST_FIRST";
        } else {
            POST_PARAMS = "https://api.rarible.org/v0.1/activities/byCollection?type=LIST&collection=" + buildIn + "&size=50&sort=LATEST_FIRST";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(POST_PARAMS))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * We analyze the data from the JSON
     *
     * @param responseJson
     * @return
     */
    private HashMap<String, Sale> analyseJson(String responseJson) throws IOException, InterruptedException, URISyntaxException {
        HashMap<String, Sale> sellList = new HashMap<String, Sale>();
        List<String> idString = new ArrayList<String>();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ssz");

        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        JsonArray transactionJSON = resJson.getAsJsonArray("activities");

        for (Object c : transactionJSON) {
            JsonObject transac = (JsonObject) c;

            String source = transac.get("source").getAsString();
            if (source.equals("RARIBLE")) {

                String idtransaction = transac.get("id").getAsString();
                String timestamp = transac.get("date").getAsString();

                if (this.mode != BotModeEnum.ListingAndBidding) {

                    String sellerAdress = transac.get("seller").getAsString().split(":")[1];

                    String sellerDomain = null;

                    /* No domain/name in the result
                    if(!seller.get("name").isJsonNull()){
                        sellerDomain = seller.get("name").getAsString(); 
                    }*/
                    String buyerAdress = transac.get("buyer").getAsString().split(":")[1];

                    String buyerDomain = null;

                    /* No domain/name in the result
                    if(!buyer.get("name").isJsonNull()){
                        buyerDomain = buyer.get("name").getAsString(); 
                    }*/
                    double price = transac.get("price").getAsDouble();

                    JsonObject nft = (JsonObject) transac.getAsJsonObject("nft");

                    Long id = nft.getAsJsonObject("type").get("tokenId").getAsLong();

                    String contract = nft.getAsJsonObject("type").get("contract").getAsString().split(":")[1];

                    String path = null;

                    String collectionName = "";

                    if (allCollectionsNames.containsKey(contract)) {
                        collectionName = allCollectionsNames.get(contract);
                    } else {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI("https://api.rarible.org/v0.1/collections/TEZOS:" + contract))
                                .timeout(Duration.of(10, SECONDS))
                                .GET()
                                .build();

                        HttpResponse<String> responseCollection = HttpClient
                                .newBuilder()
                                .build()
                                .send(request, HttpResponse.BodyHandlers.ofString());

                        JsonObject resCollectionJson = JsonParser.parseString(responseCollection.body()).getAsJsonObject();

                        collectionName = resCollectionJson.get("name").getAsString();
                        if (collectionName.equals("Unnamed Collection") || collectionName.isBlank()) {
                            collectionName = contract;
                        }

                        allCollectionsNames.put(contract, collectionName);
                    }

                    //The following lines will be fill later
                    String tokenname = "";

                    String ipfs = "";

                    idString.add("TEZOS:" + contract + ":" + id);

                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.Unknown, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                } else {
                    String sellerAdress = transac.get("maker").getAsString().split(":")[1];

                    String sellerDomain = null;

                    /* No domain/name in the result
                    if(!seller.get("name").isJsonNull()){
                        sellerDomain = seller.get("name").getAsString(); 
                    }*/
                    String buyerAdress = "";

                    String buyerDomain = null;

                    /* No domain/name in the result
                    if(!buyer.get("name").isJsonNull()){
                        buyerDomain = buyer.get("name").getAsString(); 
                    }*/
                    
                    double price = transac.get("price").getAsDouble();
                    
                    
                    JsonObject nfttransac = (JsonObject) transac.getAsJsonObject("make");

                    

                    Long id = nfttransac.getAsJsonObject("type").get("tokenId").getAsLong();

                    String contract = nfttransac.getAsJsonObject("type").get("contract").getAsString().split(":")[1];

                    String path = null;

                    String collectionName = "";

                    if (allCollectionsNames.containsKey(contract)) {
                        collectionName = allCollectionsNames.get(contract);
                    } else {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI("https://api.rarible.org/v0.1/collections/TEZOS:" + contract))
                                .timeout(Duration.of(10, SECONDS))
                                .GET()
                                .build();

                        HttpResponse<String> responseCollection = HttpClient
                                .newBuilder()
                                .build()
                                .send(request, HttpResponse.BodyHandlers.ofString());

                        JsonObject resCollectionJson = JsonParser.parseString(responseCollection.body()).getAsJsonObject();

                        collectionName = resCollectionJson.get("name").getAsString();
                        if (collectionName.equals("Unnamed Collection") || collectionName.isBlank()) {
                            collectionName = contract;
                        }

                        allCollectionsNames.put(contract, collectionName);
                    }

                    //The following lines will be fill later
                    String tokenname = "";

                    String ipfs = "";

                    idString.add("TEZOS:" + contract + ":" + id);

                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewList, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                }
            }
        }

        String ids = String.join("\",\"", idString);

        String POST_PARAMS = "{ \"ids\" : [\"" + ids + "\"]}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.rarible.org/v0.1/items/byIds"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.of(10, SECONDS))
                .POST(HttpRequest.BodyPublishers.ofString(POST_PARAMS))
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject resNFTJson = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonArray items = resNFTJson.getAsJsonArray("items");

        for (Object c : items) {
            JsonObject item = (JsonObject) c;
            String name = item.getAsJsonObject("meta").get("name").getAsString();
            JsonArray contents = item.getAsJsonObject("meta").getAsJsonArray("content");
            String ipfs = "";

            for (Object d : contents) {
                JsonObject content = (JsonObject) d;

                if (content.get("@type").getAsString().equals("IMAGE")) {

                    String tempipfs = content.get("url").getAsString();
                    ipfs = tempipfs.substring(tempipfs.indexOf("/ipfs/") + 1);
                }
            }

            String contract = item.get("contract").toString().split(":")[1].replace("\"", "");
            Long id = item.get("tokenId").getAsLong();

            for (Sale aSale : sellList.values()) {
                if (aSale.getId().longValue() == id.longValue() && aSale.getContract().equals(contract)) {
                    aSale.setName(name);
                    aSale.setIpfs(ipfs);
                }
            }
        }

        return sellList;
    }

    @Override
    public MarketPlaceEnum getName() {
        return MarketPlaceEnum.Rarible;
    }

    @Override
    public HashMap<MarketPlaceEnum, HashMap<String, Sale>> call() throws Exception {
        HashMap<String, Sale> raribleSale = new HashMap<String, Sale>();
        HashMap<MarketPlaceEnum, HashMap<String, Sale>> saleReturn = new HashMap<>();

        String responseJson = sendRequest(contracts);
        raribleSale.putAll(analyseJson(responseJson));

        saleReturn.put(MarketPlaceEnum.Rarible, raribleSale);
        return saleReturn;
    }

}
