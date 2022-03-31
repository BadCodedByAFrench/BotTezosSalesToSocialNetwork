/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotLastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleType;
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
import java.util.ArrayList;
import java.util.List;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Address;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Call OBJKT Api
 * @author david
 */
public class CallObjkt implements CallMarketPlaceInterface {
    
    private List<String> contracts;
    private MarketPlace name;

    public CallObjkt() {
        this.name = MarketPlace.Objkt;
    }

    
    @Override
    public List<Sale> query(int mode)throws IOException, URISyntaxException, InterruptedException{
        
        List<Sale> objktSale = new ArrayList<Sale>();
        Instant previousUTCHour;
        if(mode == 0){
            previousUTCHour = BotLastRefresh.getLastRefresh().getLastSucessfullSaleRefresh();
        }
        else {
            previousUTCHour = BotLastRefresh.getLastRefresh().getLastSucessfullStatRefresh();
        }
        
        for (String contract : contracts){
            for(int i = 0; i<4; i++){
                String responseJson = sendRequest(i, contract, previousUTCHour);
                objktSale.addAll(analyseJson(responseJson, i, contract));
            }
        }
            
        return objktSale;
    }
    
    /**
     * We send the request to get all the sell from the previous hour
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException 
     */
    private String sendRequest(int requestCase, String contract, Instant previousUTCHour) throws IOException, InterruptedException, URISyntaxException{
          
        String POST_PARAMS = "";
       
        switch(requestCase){
            case 0 : 
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+contract+"\\\"}}) { collection_id  name path  tokens(where: {fulfilled_asks: {timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}}, order_by: {timestamp: desc}) {      name   token_id  display_uri  fulfilled_asks(where: {timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}) {        id        timestamp        price      buyer {     address   tzdomain }  seller {   address   tzdomain }    }    }  }}\"}";
                break;
            case 1 :
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+contract+"\\\"}}) { collection_id  name path  tokens (where: { offers: {_and: {update_timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}, status: {_eq: \\\"concluded\\\"}}}}) {      name   token_id display_uri  offers(where: {_and: {status: {_eq: \\\"concluded\\\"}}, update_timestamp: {_gt: \\\""+ previousUTCHour.toString() +"\\\"}})  {        id        update_timestamp        price   buyer {    address   tzdomain }  seller {   address   tzdomain }    }    }  }}\"}";
                break;
            case 2 :
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  obj_fulfilled_dutch(where: {_and: {dutch_auction: {fa: {contract: {_eq: \\\"" + contract + "\\\"}}, timestamp: {_gt: \\\"" + previousUTCHour.toString() + "\\\"}}}}) {    price    id    timestamp  buyer {    address   tzdomain }  seller {   address   tzdomain }   token {      token_id    name display_uri  }  dutch_auction {      fa {   name     path      }    }  }}\"}";
                break;
            case 3 :
                POST_PARAMS =" { \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+contract+"\\\"}}) {    collection_id    name path    tokens(where: {english_auctions: {highest_bid: {_gt: \\\"0\\\"}, end_time: {_gt: \\\""+ previousUTCHour.toString() +"\\\"}, status: {_eq: \\\"concluded\\\"}}}) {      name      token_id   display_uri   english_auctions(where: {highest_bid: {_gt: \\\"0\\\"}, end_time: {_gt: \\\""+ previousUTCHour.toString() +"\\\"}, status: {_eq: \\\"concluded\\\"}}) {        highest_bid        id        end_time  highest_bidder {   address   tzdomain }  seller {   address   tzdomain }    }    }  }} \"}";
                break;
        }
        
        HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI("https://data.objkt.com/v2/graphql"))
          .header("Content-Type", "application/json")
          .header("Accept", "application/json")
          .timeout(Duration.of(5, SECONDS))
          .POST(HttpRequest.BodyPublishers.ofString(POST_PARAMS))
          .build(); 

        HttpResponse<String> response =  HttpClient
          .newBuilder()
          .build()
          .send(request, BodyHandlers.ofString());
        
        return response.body();
    }

    /**
     * We analyze the data from the JSON
     * @param responseJson
     * @return 
     */
    private List<Sale> analyseJson(String responseJson, int requestCase, String contract){
        List<Sale> sellList = new ArrayList<Sale>();
        
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm:ssz");
                    
        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        if (requestCase != 2){
            JsonArray contractsJSON = resJson.getAsJsonObject("data").getAsJsonArray("fa");

            for(Object c : contractsJSON){
                JsonObject contractJSON = (JsonObject) c;

                String path = null;
                if(!contractJSON.get("path").isJsonNull()){
                    path = contractJSON.get("path").getAsString();
                }

                String collectionName = contractJSON.get("name").getAsString();
                
                JsonArray allNewSellToken = contractJSON.getAsJsonArray("tokens");

                for(Object s : allNewSellToken){
                    JsonObject token = (JsonObject) s;

                    String tokenname = token.get("name").getAsString(); 
                    Long id = token.get("token_id").getAsLong(); 
                    String timestamp = "";
                    double price = 0;
                    
                    String buyerAdress = "";
                    String buyerDomain = null;
                    
                    String sellerAdress = "";
                    String sellerDomain = null;
                    
                    String ipfs = token.get("display_uri").getAsString();
                    
                    switch (requestCase) {
                        case 0 :
                            JsonArray allNewTrasacts = token.getAsJsonArray("fulfilled_asks");
                            for(Object t : allNewTrasacts){
                                JsonObject transaction = (JsonObject) t;
                                price = transaction.get("price").getAsDouble()/1000000.0;
                                timestamp = transaction.get("timestamp").getAsString();
                                
                                buyerAdress = transaction.getAsJsonObject("buyer").get("address").getAsString();
                                sellerAdress = transaction.getAsJsonObject("seller").get("address").getAsString();
                                
                                if(!transaction.getAsJsonObject("buyer").get("tzdomain").isJsonNull()){
                                   buyerDomain = transaction.getAsJsonObject("buyer").get("tzdomain").getAsString(); 
                                }
                                if(!transaction.getAsJsonObject("seller").get("tzdomain").isJsonNull()){
                                    sellerDomain = transaction.getAsJsonObject("seller").get("tzdomain").getAsString();
                                }
                                
                                
                                sellList.add(new Sale(tokenname, id, price, SaleType.ListedSale, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs));
                            }  
                            break;
                        case 1 :
                            JsonArray allNewOffer = token.getAsJsonArray("offers");
                            for(Object t : allNewOffer){
                                JsonObject offer = (JsonObject) t;
                                price = offer.get("price").getAsDouble()/1000000.0;
                                timestamp = offer.get("update_timestamp").getAsString();
                                
                                buyerAdress = offer.getAsJsonObject("buyer").get("address").getAsString();                              
                                sellerAdress = offer.getAsJsonObject("seller").get("address").getAsString();
                                
                                if(!offer.getAsJsonObject("buyer").get("tzdomain").isJsonNull()){
                                   buyerDomain = offer.getAsJsonObject("buyer").get("tzdomain").getAsString(); 
                                }
                                if(!offer.getAsJsonObject("seller").get("tzdomain").isJsonNull()){
                                    sellerDomain = offer.getAsJsonObject("seller").get("tzdomain").getAsString();
                                }
                                
                                sellList.add(new Sale(tokenname, id, price, SaleType.Offer, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs));
                            }
                            break;
                        case 3 :
                            JsonArray allNewEnglishAuction = token.getAsJsonArray("english_auctions");
                            for(Object t : allNewEnglishAuction){
                                JsonObject englishAuction = (JsonObject) t;
                                price = englishAuction.get("highest_bid").getAsDouble()/1000000.0;
                                timestamp = englishAuction.get("end_time").getAsString();
                                
                                buyerAdress = englishAuction.getAsJsonObject("highest_bidder").get("address").getAsString();
                                sellerAdress = englishAuction.getAsJsonObject("seller").get("address").getAsString();
                                
                                if(!englishAuction.getAsJsonObject("highest_bidder").get("tzdomain").isJsonNull()){
                                   buyerDomain = englishAuction.getAsJsonObject("highest_bidder").get("tzdomain").getAsString(); 
                                }
                                if(!englishAuction.getAsJsonObject("seller").get("tzdomain").isJsonNull()){
                                    sellerDomain = englishAuction.getAsJsonObject("seller").get("tzdomain").getAsString();
                                }
                                
                                sellList.add(new Sale(tokenname, id, price, SaleType.EnglishAuction, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs));
                            }
                            break;
                            
                    }      
                }         
            }
        }
        else{
            JsonArray success_dutch_auctions = resJson.getAsJsonObject("data").getAsJsonArray("obj_fulfilled_dutch");

            for(Object c : success_dutch_auctions){
                JsonObject success_dutch_auction = (JsonObject) c;
                String timestamp = success_dutch_auction.get("timestamp").getAsString();
                double price = success_dutch_auction.get("price").getAsDouble()/1000000.0;
                
                String buyerAdress = success_dutch_auction.getAsJsonObject("buyer").get("address").getAsString();
                String buyerDomain = null;
                    
                String sellerAdress = success_dutch_auction.getAsJsonObject("seller").get("address").getAsString();
                String sellerDomain = null;
                
                if(!success_dutch_auction.getAsJsonObject("seller").get("tzdomain").isJsonNull()){
                    buyerDomain = success_dutch_auction.getAsJsonObject("buyer").get("tzdomain").getAsString();
                }
                
                if(!success_dutch_auction.getAsJsonObject("seller").get("tzdomain").isJsonNull()){
                    sellerDomain =  success_dutch_auction.getAsJsonObject("seller").get("tzdomain").getAsString();  
                }
                                
                String path = null;
                if(!success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").isJsonNull()){
                    path = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").getAsString();
                }
                
                String collectionName = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("name").getAsString();
                
                Long id = success_dutch_auction.getAsJsonObject("token").get("token_id").getAsLong();
                String tokenname = success_dutch_auction.getAsJsonObject("token").get("name").getAsString();
                String ipfs = success_dutch_auction.getAsJsonObject("token").get("display_uri").getAsString();
                sellList.add(new Sale(tokenname, id, price, SaleType.DutchAuction, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant(), contract, collectionName, new Address(buyerAdress, buyerDomain), new Address(sellerAdress, sellerDomain), ipfs));

            }
        }
        return sellList;
    }

    public List<String> getContracts() {
        return contracts;
    }

    public void setContracts(List<String> contracts) {
        this.contracts = contracts;
    }

    @Override
    public MarketPlace getName() {
        return name;
    }
    
    
}
