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
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class CallFxhash implements CallMarketPlaceInterface {

    private BotModeEnum mode;

    private List<String> contracts;

    private LastRefresh lastrefresh;

    public CallFxhash(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) {
        this.mode = mode;
        this.contracts = contracts;
        this.lastrefresh = lastrefresh;
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
            buildIn += contract;

        }

        String POST_PARAMS = "";
        if (this.mode != BotModeEnum.ListingAndBidding) {
            POST_PARAMS = "{ \"query\" : \"query MyQuery {  generativeTokens(filters: {id_in: [" + buildIn + "]}) {    id    name    slug   author {      id      name    }  actions(filters: {type_in: [LISTING_V2_ACCEPTED, MINTED_FROM, LISTING_V1_ACCEPTED, OFFER_ACCEPTED, COLLECTION_OFFER_ACCEPTED, AUCTION_FULFILLED]}, take: 50, sort: {createdAt: \\\"DESC\\\"}) {      id      type      numericValue      createdAt      issuer {        id        name      } target {        id        name      }     objkt {        id        name        metadata      }    }  }}\"}";
        } else {
            POST_PARAMS = "{ \"query\" : \"query MyQuery {  generativeTokens(filters: {id_in: [" + buildIn + "]}) {    id    name    slug   author {      id      name    }  actions(filters: {type_in: [LISTING_V1, LISTING_V2, LISTING_V3, COLLECTION_OFFER, AUCTION, AUCTION_BID]}, take: 50, sort: {createdAt: \\\"DESC\\\"}) {      id      type      numericValue      createdAt      issuer {        id        name      } target {        id        name      }     objkt {        id        name        metadata      }    }  }}\"}";
        }
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

    /**
     * We analyze the data from the JSON
     *
     * @param responseJson
     * @return
     */
    private HashMap<String, Sale> analyseJson(String responseJson) {
        HashMap<String, Sale> sellList = new HashMap<String, Sale>();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSz");

        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("generativeTokens");

        for (Object c : contractsJSON) {
            JsonObject generativeTokens = (JsonObject) c;

            JsonArray actions = generativeTokens.getAsJsonArray("actions");

            for (Object a : actions) {
                JsonObject action = (JsonObject) a;

                String timestamp = action.get("createdAt").getAsString();
                String idtransaction = action.get("id").getAsString();

                String saletype = action.get("type").getAsString();

                SaleTypeEnum type = null;
                if (saletype.equals("LISTING_V2_ACCEPTED") || saletype.equals("LISTING_V1_ACCEPTED")) {
                    type = SaleTypeEnum.ListedSale;
                } else if (saletype.equals("MINTED_FROM")) {
                    type = SaleTypeEnum.Mint;
                } else if (saletype.equals("OFFER_ACCEPTED") || saletype.equals("COLLECTION_OFFER_ACCEPTED")) {
                    type = SaleTypeEnum.Offer;
                } else if (saletype.equals("AUCTION_FULFILLED")) {
                    type = SaleTypeEnum.Auction;
                } else if (saletype.equals("LISTING_V1") || saletype.equals("LISTING_V2") || saletype.equals("LISTING_V3")) {
                    type = SaleTypeEnum.NewList;
                } else if (saletype.equals("COLLECTION_OFFER")) {
                    type = SaleTypeEnum.NewFloorOffer;
                } else if (saletype.equals("AUCTION")) {
                    type = SaleTypeEnum.NewDutchAuction;
                } else if (saletype.equals("AUCTION_BID")) {
                    type = SaleTypeEnum.NewBidding;
                } else {
                    type = SaleTypeEnum.Unknown;
                }

                String whosebuyer = "";
                String whoseseller = "";

                if (type == SaleTypeEnum.ListedSale || type == SaleTypeEnum.Auction) {
                    whosebuyer = "issuer";
                    whoseseller = "target";
                } else if (type == SaleTypeEnum.Offer) {
                    whosebuyer = "target";
                    whoseseller = "issuer";
                } else if (type == SaleTypeEnum.Mint) {
                    whosebuyer = "issuer";
                    whoseseller = "author";
                } else if (type == SaleTypeEnum.NewList || type == SaleTypeEnum.NewDutchAuction) {
                    whoseseller = "issuer";
                    whosebuyer = "";
                } else if (type == SaleTypeEnum.NewFloorOffer || type == SaleTypeEnum.NewBidding) {
                    whosebuyer = "issuer";
                    whoseseller = "";
                }

                String sellerAdress = "";

                String sellerDomain = null;

                if (!whoseseller.equals("")) {
                    if (type != SaleTypeEnum.Mint) {
                        JsonObject seller = (JsonObject) action.getAsJsonObject(whoseseller);

                        sellerAdress = seller.get("id").getAsString();

                        if (!seller.get("name").isJsonNull()) {
                            sellerDomain = seller.get("name").getAsString();
                        }
                    } else {
                        JsonObject creator = (JsonObject) generativeTokens.getAsJsonObject(whoseseller);

                        sellerAdress = creator.get("id").getAsString();

                        if (!creator.get("name").isJsonNull()) {
                            sellerDomain = creator.get("name").getAsString();
                        }
                    }
                }

                String buyerAdress = "";

                String buyerDomain = null;

                if (!whosebuyer.equals("")) {
                    JsonObject buyer = (JsonObject) action.getAsJsonObject(whosebuyer);

                    buyerAdress = buyer.get("id").getAsString();

                    if (!buyer.get("name").isJsonNull()) {
                        buyerDomain = buyer.get("name").getAsString();
                    }
                }
                double price = action.get("numericValue").getAsDouble() / 1000000.0;

                
                
                
                if (!action.getAsJsonObject("objkt").isJsonNull()){
                    JsonObject objkt = (JsonObject) action.getAsJsonObject("objkt");
                    
                    String id = objkt.get("id").getAsString();
                    String tokenname = objkt.get("name").getAsString();
                    String ipfs = objkt.getAsJsonObject("metadata").get("displayUri").getAsString();

                    String contract = generativeTokens.get("id").getAsString();
                    String collectionName = generativeTokens.get("name").getAsString();
                    String path = generativeTokens.get("slug").getAsString();

                    sellList.put(idtransaction, new Sale(tokenname, id, price, type, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));

                }
            }
        }

        return sellList;
    }

    @Override
    public MarketPlaceEnum getName() {
        return MarketPlaceEnum.fxhash;
    }

    @Override
    public HashMap<MarketPlaceEnum, HashMap<String, Sale>> call() throws Exception {
        HashMap<String, Sale> fxhashSale = new HashMap<String, Sale>();
        HashMap<MarketPlaceEnum, HashMap<String, Sale>> saleReturn = new HashMap<>();

        String responseJson = sendRequest(contracts);
        fxhashSale.putAll(analyseJson(responseJson));

        saleReturn.put(MarketPlaceEnum.fxhash, fxhashSale);
        return saleReturn;
    }

}
