/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Caller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.LastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.List;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Address;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

/**
 * Call OBJKT Api
 *
 * @author david
 */
public class CallObjkt implements CallMarketPlaceInterface {

    private BotModeEnum mode;

    private List<String> contracts;

    private LastRefresh lastrefresh;

    public CallObjkt(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) {
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
    private String sendRequest(int requestCase, List<String> contracts, Instant previousUTCHour) throws IOException, InterruptedException, URISyntaxException {

        String POST_PARAMS = "";

        String buildIn = "";

        for (String contract : contracts) {
            if (!buildIn.equals("")) {
                buildIn += ",";
            }
            buildIn += "\\\"" + contract + "\\\"";

        }

        if (this.mode != BotModeEnum.ListingAndBidding) {
            switch (requestCase) {
                case 0:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_in: [ " + buildIn + " ] }}) { contract collection_id  name path  tokens(where: {fulfilled_asks: {timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}}, order_by: {timestamp: desc}) {      name   token_id  display_uri  fulfilled_asks(where: {timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}) {        id        timestamp        price      buyer {     address   tzdomain }  seller {   address   tzdomain }    }    }  }}\"}";
                    break;
                case 1:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_in: [ " + buildIn + " ]}}) { contract collection_id  name path  tokens (where: { offers: {_and: {update_timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}, status: {_eq: \\\"concluded\\\"}}}}) {      name   token_id display_uri  offers(where: {_and: {status: {_eq: \\\"concluded\\\"}}, update_timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}})  {        id        update_timestamp        price   buyer {    address   tzdomain }  seller {   address   tzdomain }    }    }  }}\"}";
                    break;
                case 2:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  dutch_auction_sale(where: {_and: {dutch_auction: {fa: {contract: {_in: [ " + buildIn + " ]}}, timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}}}) {    price    id    timestamp  buyer {    address   tzdomain }  seller {   address   tzdomain }   token {      token_id    name display_uri  }  dutch_auction {      fa {   name     path  contract    }    }  }}\"}";
                    break;
                case 3:
                    POST_PARAMS = " { \"query\" : \"query MyQuery {  fa(where: {contract: {_in: [ " + buildIn + " ]}}) { contract   collection_id    name path    tokens(where: {english_auctions: {highest_bid: {_gt: \\\"0\\\"}, end_time: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}, status: {_eq: \\\"concluded\\\"}}}) {      name      token_id   display_uri   english_auctions(where: {highest_bid: {_gt: \\\"0\\\"}, end_time: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}, status: {_eq: \\\"concluded\\\"}}) {        highest_bid        id        end_time  highest_bidder {   address   tzdomain }  seller {   address   tzdomain }    }    }  }} \"}";
                    break;
            }
        } else {
            switch (requestCase) {
                case 0:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  listing (where: {status: {_eq: \\\"active\\\"}, fa_contract: { _in:  [ " + buildIn + " ]}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {    status    timestamp    price    id    fa_contract    seller {      address      tzdomain    }    token {      name      token_id      display_uri    }    fa {      name      path    }  }}\"}";
                    break;
                case 1:
                    POST_PARAMS = "{ \"query\" : \"  query MyQuery {  fa(where: {english_auctions: {status: {_eq: \\\"active\\\"}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}, contract: {_in: [" + buildIn + "]}}) {    english_auctions(where: {status: {_eq: \\\"active\\\"}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {      timestamp      end_time      id      seller {        address        tzdomain      }      token {        token_id        display_uri        name      }      reserve      status    }    contract    path    name  }}\"}";
                    break;
                case 2:
                    POST_PARAMS = "{ \"query\" : \"  query MyQuery {  fa(where: {dutch_auctions: {status: {_eq: \\\"active\\\"}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}, contract: {_in: [" + buildIn + "]}}) {    dutch_auctions(where: {status: {_eq: \\\"active\\\"}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {      timestamp      end_time      id      seller {        address        tzdomain      }      token {        token_id        display_uri        name      }      start_price      status    }    contract    path    name  }}\"}";
                    break;
                case 3:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  offer(where: {collection_offer: {_in: ["+ buildIn +" ]}, status: {_eq: \\\"active\\\"}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {    collection_offer    fa {      name      path      contract    }    price    buyer {      address      tzdomain    }    timestamp    id  }}\"} ";
                    break;
                case 4:
                    POST_PARAMS = "{ \"query\" : \"query MyQuery {  english_auction_bid(where: {auction: {status: {_eq: \\\"active\\\"}, fa_contract: {_in: [ " + buildIn  + " ]}}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {    bidder {      address      tzdomain    }    id    timestamp    amount    auction {      token {        display_uri        token_id        name      }      fa {        contract        name        path      }      status    }  }}\"}";
                    break;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://data.objkt.com/v2/graphql"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.of(5, SECONDS))
                .POST(HttpRequest.BodyPublishers.ofString(POST_PARAMS))
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, BodyHandlers.ofString());

        return response.body();
    }

    /**
     * We analyze the data from the JSON
     *
     * @param responseJson
     * @return
     */
    private HashMap<String, Sale> analyseJson(String responseJson, int requestCase) {
        HashMap<String, Sale> sellList = new HashMap<String, Sale>();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ssz");

        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        if (this.mode != BotModeEnum.ListingAndBidding) {
            if (requestCase != 2) {
                JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("fa");

                for (Object c : contractsJSON) {
                    JsonObject contractJSON = (JsonObject) c;

                    String path = null;
                    if (!contractJSON.get("path").isJsonNull()) {
                        path = contractJSON.get("path").getAsString();
                    }

                    String collectionName = contractJSON.get("name").getAsString();
                    String contract = contractJSON.get("contract").getAsString();
                    JsonArray allNewSellToken = contractJSON.getAsJsonArray("tokens");

                    for (Object s : allNewSellToken) {
                        JsonObject token = (JsonObject) s;

                        String tokenname = token.get("name").getAsString();
                        Long id = token.get("token_id").getAsLong();
                        String timestamp = "";
                        double price = 0;
                        String idtransaction;

                        String buyerAdress = "";
                        String buyerDomain = null;

                        String sellerAdress = "";
                        String sellerDomain = null;

                        String ipfs = token.get("display_uri").getAsString();

                        switch (requestCase) {
                            case 0:
                                JsonArray allNewTrasacts = token.getAsJsonArray("fulfilled_asks");
                                for (Object t : allNewTrasacts) {

                                    buyerAdress = "";
                                    buyerDomain = null;
                                    sellerAdress = "";
                                    sellerDomain = null;

                                    JsonObject transaction = (JsonObject) t;
                                    price = transaction.get("price").getAsDouble() / 1000000.0;
                                    timestamp = transaction.get("timestamp").getAsString();
                                    idtransaction = transaction.get("id").getAsString();

                                    buyerAdress = transaction.getAsJsonObject("buyer").get("address").getAsString();
                                    sellerAdress = transaction.getAsJsonObject("seller").get("address").getAsString();

                                    if (!transaction.getAsJsonObject("buyer").get("tzdomain").isJsonNull()) {
                                        buyerDomain = transaction.getAsJsonObject("buyer").get("tzdomain").getAsString();
                                    }
                                    if (!transaction.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                                        sellerDomain = transaction.getAsJsonObject("seller").get("tzdomain").getAsString();
                                    }

                                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.ListedSale, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                                }
                                break;
                            case 1:
                                JsonArray allNewOffer = token.getAsJsonArray("offers");
                                for (Object t : allNewOffer) {

                                    buyerAdress = "";
                                    buyerDomain = null;
                                    sellerAdress = "";
                                    sellerDomain = null;

                                    JsonObject offer = (JsonObject) t;
                                    price = offer.get("price").getAsDouble() / 1000000.0;
                                    timestamp = offer.get("update_timestamp").getAsString();
                                    idtransaction = offer.get("id").getAsString();

                                    buyerAdress = offer.getAsJsonObject("buyer").get("address").getAsString();
                                    sellerAdress = offer.getAsJsonObject("seller").get("address").getAsString();

                                    if (!offer.getAsJsonObject("buyer").get("tzdomain").isJsonNull()) {
                                        buyerDomain = offer.getAsJsonObject("buyer").get("tzdomain").getAsString();
                                    }
                                    if (!offer.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                                        sellerDomain = offer.getAsJsonObject("seller").get("tzdomain").getAsString();
                                    }

                                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.Offer, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                                }
                                break;
                            case 3:
                                JsonArray allNewEnglishAuction = token.getAsJsonArray("english_auctions");
                                for (Object t : allNewEnglishAuction) {

                                    buyerAdress = "";
                                    buyerDomain = null;
                                    sellerAdress = "";
                                    sellerDomain = null;

                                    JsonObject englishAuction = (JsonObject) t;
                                    price = englishAuction.get("highest_bid").getAsDouble() / 1000000.0;
                                    timestamp = englishAuction.get("end_time").getAsString();
                                    idtransaction = englishAuction.get("id").getAsString();

                                    buyerAdress = englishAuction.getAsJsonObject("highest_bidder").get("address").getAsString();
                                    sellerAdress = englishAuction.getAsJsonObject("seller").get("address").getAsString();

                                    if (!englishAuction.getAsJsonObject("highest_bidder").get("tzdomain").isJsonNull()) {
                                        buyerDomain = englishAuction.getAsJsonObject("highest_bidder").get("tzdomain").getAsString();
                                    }
                                    if (!englishAuction.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                                        sellerDomain = englishAuction.getAsJsonObject("seller").get("tzdomain").getAsString();
                                    }

                                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.EnglishAuction, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                                }
                                break;

                        }
                    }
                }
            } else {
                JsonArray success_dutch_auctions = resJson.getAsJsonObject("data").getAsJsonArray("dutch_auction_sale");

                for (Object c : success_dutch_auctions) {
                    JsonObject success_dutch_auction = (JsonObject) c;
                    String timestamp = success_dutch_auction.get("timestamp").getAsString();
                    double price = success_dutch_auction.get("price").getAsDouble() / 1000000.0;
                    String idtransaction = success_dutch_auction.get("id").getAsString();

                    String buyerAdress = success_dutch_auction.getAsJsonObject("buyer").get("address").getAsString();
                    String buyerDomain = null;

                    String sellerAdress = success_dutch_auction.getAsJsonObject("seller").get("address").getAsString();
                    String sellerDomain = null;

                    if (!success_dutch_auction.getAsJsonObject("buyer").get("tzdomain").isJsonNull()) {
                        buyerDomain = success_dutch_auction.getAsJsonObject("buyer").get("tzdomain").getAsString();
                    }

                    if (!success_dutch_auction.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                        sellerDomain = success_dutch_auction.getAsJsonObject("seller").get("tzdomain").getAsString();
                    }

                    String path = null;
                    String contract = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("contract").getAsString();
                    if (!success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").isJsonNull()) {
                        path = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").getAsString();
                    }

                    String collectionName = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("name").getAsString();

                    Long id = success_dutch_auction.getAsJsonObject("token").get("token_id").getAsLong();
                    String tokenname = success_dutch_auction.getAsJsonObject("token").get("name").getAsString();
                    String ipfs = success_dutch_auction.getAsJsonObject("token").get("display_uri").getAsString();
                    sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.DutchAuction, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                }
            }
        } else {
            switch (requestCase) {
                case 0:
                    JsonArray newlistings = resJson.getAsJsonObject("data").getAsJsonArray("listing");

                    for (Object c : newlistings) {
                        JsonObject aListing = (JsonObject) c;
                        String timestamp = aListing.get("timestamp").getAsString();
                        double price = aListing.get("price").getAsDouble() / 1000000.0;
                        String idtransaction = aListing.get("id").getAsString();

                        String buyerAdress = "";
                        String buyerDomain = null;

                        String sellerAdress = aListing.getAsJsonObject("seller").get("address").getAsString();
                        String sellerDomain = null;

                        if (!aListing.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                            sellerDomain = aListing.getAsJsonObject("seller").get("tzdomain").getAsString();
                        }

                        String path = null;
                        String contract = aListing.get("fa_contract").getAsString();

                        if (!aListing.getAsJsonObject("fa").get("path").isJsonNull()) {
                            path = aListing.getAsJsonObject("fa").get("path").getAsString();
                        }

                        String collectionName = aListing.getAsJsonObject("fa").get("name").getAsString();

                        Long id = aListing.getAsJsonObject("token").get("token_id").getAsLong();
                        String tokenname = aListing.getAsJsonObject("token").get("name").getAsString();
                        String ipfs = aListing.getAsJsonObject("token").get("display_uri").getAsString();
                        sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewList, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                    }

                    break;
                case 1:
                    JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("fa");

                    for (Object c : contractsJSON) {
                        JsonObject contractJSON = (JsonObject) c;

                        String path = null;
                        if (!contractJSON.get("path").isJsonNull()) {
                            path = contractJSON.get("path").getAsString();
                        }

                        String collectionName = contractJSON.get("name").getAsString();
                        String contract = contractJSON.get("contract").getAsString();
                        JsonArray allNewEnglishAuction = contractJSON.getAsJsonArray("english_auctions");

                        for (Object s : allNewEnglishAuction) {
                            JsonObject englishAuction = (JsonObject) s;

                            String timestamp = englishAuction.get("timestamp").getAsString();
                            double price = englishAuction.get("reserve").getAsDouble() / 1000000.0;
                            String idtransaction = englishAuction.get("id").getAsString();

                            String buyerAdress = "";
                            String buyerDomain = null;

                            String sellerAdress = "";
                            String sellerDomain = null;

                            sellerAdress = englishAuction.getAsJsonObject("seller").get("address").getAsString();

                            if (!englishAuction.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                                sellerDomain = englishAuction.getAsJsonObject("seller").get("tzdomain").getAsString();
                            }

                            
                            JsonObject token = englishAuction.getAsJsonObject("token");
                            
                            String tokenname = token.get("name").getAsString();
                            Long id = token.get("token_id").getAsLong();

                            String ipfs = token.get("display_uri").getAsString();

                            sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewEnglishAuction, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                        }
                    }

                    break;
                case 2:
                    JsonArray contractsJSONDutchAuction = resJson.getAsJsonObject("data").getAsJsonArray("fa");

                    for (Object c : contractsJSONDutchAuction) {
                        JsonObject contractJSON = (JsonObject) c;

                        String path = null;
                        if (!contractJSON.get("path").isJsonNull()) {
                            path = contractJSON.get("path").getAsString();
                        }

                        String collectionName = contractJSON.get("name").getAsString();
                        String contract = contractJSON.get("contract").getAsString();
                        JsonArray allNewDutchAuction = contractJSON.getAsJsonArray("dutch_auctions");

                        for (Object s : allNewDutchAuction) {
                            JsonObject dutchAuction = (JsonObject) s;

                            String timestamp = dutchAuction.get("timestamp").getAsString();
                            double price = dutchAuction.get("start_price").getAsDouble() / 1000000.0;
                            String idtransaction = dutchAuction.get("id").getAsString();

                            String buyerAdress = "";
                            String buyerDomain = null;

                            String sellerAdress = "";
                            String sellerDomain = null;

                            sellerAdress = dutchAuction.getAsJsonObject("seller").get("address").getAsString();

                            if (!dutchAuction.getAsJsonObject("seller").get("tzdomain").isJsonNull()) {
                                sellerDomain = dutchAuction.getAsJsonObject("seller").get("tzdomain").getAsString();
                            }

                            
                            JsonObject token = dutchAuction.getAsJsonObject("token");
                            
                            String tokenname = token.get("name").getAsString();
                            Long id = token.get("token_id").getAsLong();

                            String ipfs = token.get("display_uri").getAsString();

                            sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewDutchAuction, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                        }
                    }
                    break;
                case 3:
                    JsonArray newCollectionOffer = resJson.getAsJsonObject("data").getAsJsonArray("offer");

                    for (Object c : newCollectionOffer) {
                        JsonObject anOffer = (JsonObject) c;
                        String timestamp = anOffer.get("timestamp").getAsString();
                        double price = anOffer.get("price").getAsDouble() / 1000000.0;
                        String idtransaction = anOffer.get("id").getAsString();

                        String buyerAdress = anOffer.getAsJsonObject("buyer").get("address").getAsString();
                        String buyerDomain = null;

                        String sellerAdress = "";
                        String sellerDomain = null;

                        if (!anOffer.getAsJsonObject("buyer").get("tzdomain").isJsonNull()) {
                            sellerDomain = anOffer.getAsJsonObject("buyer").get("tzdomain").getAsString();
                        }

                        String path = null;
                        String contract = anOffer.get("collection_offer").getAsString();

                        if (!anOffer.getAsJsonObject("fa").get("path").isJsonNull()) {
                            path = anOffer.getAsJsonObject("fa").get("path").getAsString();
                        }

                        String collectionName = anOffer.getAsJsonObject("fa").get("name").getAsString();

                        Long id = (long) 0;
                        String tokenname = "";
                        String ipfs = "";
                        sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewFloorOffer, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                    }
                    break;
                case 4:
                    JsonArray newBiddings = resJson.getAsJsonObject("data").getAsJsonArray("english_auction_bid");

                    for (Object c : newBiddings) {
                        JsonObject aBidding = (JsonObject) c;
                        String timestamp = aBidding.get("timestamp").getAsString();
                        double price = aBidding.get("amount").getAsDouble() / 1000000.0;
                        String idtransaction = aBidding.get("id").getAsString();

                        String buyerAdress = aBidding.getAsJsonObject("bidder").get("address").getAsString();
                        String buyerDomain = null;

                        String sellerAdress = "";
                        String sellerDomain = null;

                        if (!aBidding.getAsJsonObject("bidder").get("tzdomain").isJsonNull()) {
                            buyerDomain = aBidding.getAsJsonObject("bidder").get("tzdomain").getAsString();
                        }

                        String path = null;
                        String contract = aBidding.getAsJsonObject("auction").getAsJsonObject("fa").get("contract").getAsString();

                        if (!aBidding.getAsJsonObject("auction").getAsJsonObject("fa").get("path").isJsonNull()) {
                            path = aBidding.getAsJsonObject("auction").getAsJsonObject("fa").get("path").getAsString();
                        }

                        String collectionName = aBidding.getAsJsonObject("auction").getAsJsonObject("fa").get("name").getAsString();

                        Long id = aBidding.getAsJsonObject("auction").getAsJsonObject("token").get("token_id").getAsLong();
                        String tokenname = aBidding.getAsJsonObject("auction").getAsJsonObject("token").get("name").getAsString();
                        String ipfs = aBidding.getAsJsonObject("auction").getAsJsonObject("token").get("display_uri").getAsString();
                        sellList.put(idtransaction, new Sale(tokenname, id, price, SaleTypeEnum.NewBidding, this.getName(), path, OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs, idtransaction));
                    }
                    
                    break;
            }
        }
        return sellList;
    }

    @Override
    public MarketPlaceEnum getName() {
        return MarketPlaceEnum.Objkt;
    }

    @Override
    public HashMap<MarketPlaceEnum, HashMap<String, Sale>> call() throws Exception {
        HashMap<String, Sale> objktSale = new HashMap<String, Sale>();
        Instant previousUTCHour;
        if (mode == BotModeEnum.Sale) {
            previousUTCHour = lastrefresh.getLastSucessfullSaleRefresh().minus(1, ChronoUnit.HOURS);
        } else if (mode == BotModeEnum.Stat) {
            previousUTCHour = lastrefresh.getLastSucessfullStatRefresh().minus(1, ChronoUnit.HOURS);
        } else{
            previousUTCHour = lastrefresh.getLastSucessfullListingAndBiddingRefresh().minus(1, ChronoUnit.HOURS);
        }

        if(mode != BotModeEnum.ListingAndBidding){
            for (int i = 0; i < 4; i++) {
                String responseJson = sendRequest(i, contracts, previousUTCHour);
                objktSale.putAll(analyseJson(responseJson, i));
            }
        }
        else{
            for (int i = 0; i < 5; i++) {
                boolean doRequest = true;
                if (i == 0 && !BotConfiguration.getConfiguration().isActivateListing()){
                    doRequest = false;
                }
                else if (i == 1 && !BotConfiguration.getConfiguration().isActivateEnglishAuction()){
                    doRequest = false;
                }
                else if (i == 2 && !BotConfiguration.getConfiguration().isActivateDutchAuction()){
                    doRequest = false;
                }
                else if (i == 3 && !BotConfiguration.getConfiguration().isActivateFloorOffer()){
                    doRequest = false;
                }
                else if (i == 4 && !BotConfiguration.getConfiguration().isActivateBidding()){
                    doRequest = false;
                }
                
                if(doRequest){
                    String responseJson = sendRequest(i, contracts, previousUTCHour);
                    objktSale.putAll(analyseJson(responseJson, i));
                }
            }
        }

        HashMap<MarketPlaceEnum, HashMap<String, Sale>> saleReturn = new HashMap<>();
        saleReturn.put(MarketPlaceEnum.Objkt, objktSale);

        return saleReturn;
    }

}
