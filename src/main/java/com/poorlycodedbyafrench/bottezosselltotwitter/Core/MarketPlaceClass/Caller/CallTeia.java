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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class CallTeia implements CallMarketPlaceInterface {

    private BotModeEnum mode;

    private List<String> contracts;

    private LastRefresh lastrefresh;

    public CallTeia(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) {
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
    private String sendRequest(List<String> contracts, Instant previousUTCHour) throws IOException, InterruptedException, URISyntaxException {

        String buildIn = "";

        for (String contract : contracts) {
            if (!buildIn.equals("")) {
                buildIn += ",";
            }
            buildIn += "\\\"" + contract + "\\\"";

        }

        String POST_PARAMS = "";
        if (this.mode != BotModeEnum.ListingAndBidding) {
            POST_PARAMS = "{ \"query\" : \"query Query {  trade(where: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}, token: {creator: {address: {_in: [" + buildIn + " ] }}}}) {    timestamp    swap_id    token_id    seller {      address      name    }    buyer {      address      name    }    swap {      price      token {        display_uri        creator {          address          name        }        title      }    }  }}\"}";
        } else {
            POST_PARAMS = "{ \"query\" : \"query MyQuery {  swap(order_by: {timestamp: desc}, where: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}, token: {creator: {address: {_in: [" + buildIn + "] }}}}) {    price    timestamp    status    token {      id      title      display_uri      creator {        address        name      }    }    id    creator {      address      name    }  }}\"}";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api-v5.teia.rocks/v1/graphql"))
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
                .ofPattern("uuuu-MM-dd'T'HH:mm:ssz");

        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        if (this.mode != BotModeEnum.ListingAndBidding) {
            JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("trade");

            for (Object c : contractsJSON) {
                JsonObject trade = (JsonObject) c;

                String path = null;

                Long id = trade.get("token_id").getAsLong();
                String timestamp = trade.get("timestamp").getAsString();

                String idtransaction = trade.get("swap_id").getAsString();

                JsonObject seller = (JsonObject) trade.getAsJsonObject("seller");

                String sellerAdress = seller.get("address").getAsString();

                String sellerDomain = null;
                if (!seller.get("name").isJsonNull()) {
                    sellerDomain = seller.get("name").getAsString();
                }

                JsonObject buyer = (JsonObject) trade.getAsJsonObject("buyer");

                String buyerAdress = buyer.get("address").getAsString();

                String buyerDomain = null;
                if (!buyer.get("name").isJsonNull()) {
                    buyerDomain = buyer.get("name").getAsString();
                }

                JsonObject swap = (JsonObject) trade.getAsJsonObject("swap");
                double price = swap.get("price").getAsDouble() / 1000000.0;

                JsonObject token = (JsonObject) swap.getAsJsonObject("token");

                String tokenname = token.get("title").getAsString();

                String ipfs = token.get("display_uri").getAsString();

                String contract = token.get("creator").getAsJsonObject().get("address").getAsString();

                String collectionName = contract;
                if (!token.get("creator").getAsJsonObject().get("name").isJsonNull()) {
                    collectionName = token.get("creator").getAsJsonObject().get("name").getAsString();
                }

                sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.ListedSale, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
            }
        } else {
            JsonArray swapJSON = resJson.getAsJsonObject("data").getAsJsonArray("swap");

            for (Object c : swapJSON) {
                JsonObject swap = (JsonObject) c;

                String path = null;

                double price = swap.get("price").getAsDouble() / 1000000.0;
                String timestamp = swap.get("timestamp").getAsString();
                String idtransaction = swap.get("id").getAsString();

                JsonObject seller = (JsonObject) swap.getAsJsonObject("creator");

                String sellerAdress = seller.get("address").getAsString();

                String sellerDomain = null;
                if (!seller.get("name").isJsonNull()) {
                    sellerDomain = seller.get("name").getAsString();
                }

                String buyerAdress = "";

                String buyerDomain = null;

                JsonObject token = (JsonObject) swap.getAsJsonObject("token");
                Long id = token.get("id").getAsLong();

                String tokenname = token.get("title").getAsString();

                String ipfs = token.get("display_uri").getAsString();

                String contract = token.get("creator").getAsJsonObject().get("address").getAsString();

                String collectionName = contract;
                if (!token.get("creator").getAsJsonObject().get("name").isJsonNull()) {
                    collectionName = token.get("creator").getAsJsonObject().get("name").getAsString();
                }

                sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewList, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
            }
        }

        return sellList;
    }

    @Override
    public MarketPlaceEnum getName() {
        return MarketPlaceEnum.Teia;
    }

    @Override
    public HashMap<MarketPlaceEnum, HashMap<String, Sale>> call() throws Exception {
        HashMap<String, Sale> HENSale = new HashMap<String, Sale>();
        HashMap<MarketPlaceEnum, HashMap<String, Sale>> saleReturn = new HashMap<>();

        Instant previousUTCHour;

        if (mode == BotModeEnum.Sale) {
            previousUTCHour = lastrefresh.getLastSucessfullSaleRefresh().minus(1, ChronoUnit.HOURS);
        } else if (mode == BotModeEnum.Stat) {
            previousUTCHour = lastrefresh.getLastSucessfullStatRefresh().minus(1, ChronoUnit.HOURS);
        } else {
            previousUTCHour = lastrefresh.getLastSucessfullListingAndBiddingRefresh().minus(1, ChronoUnit.HOURS);
        }

        String responseJson = sendRequest(contracts, previousUTCHour);
        HENSale.putAll(analyseJson(responseJson));

        saleReturn.put(MarketPlaceEnum.Teia, HENSale);
        return saleReturn;
    }

}
