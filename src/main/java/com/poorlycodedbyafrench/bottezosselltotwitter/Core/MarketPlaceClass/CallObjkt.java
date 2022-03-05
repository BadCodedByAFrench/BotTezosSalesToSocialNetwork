/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.ArrayList;
import java.util.List;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Call OBJKT Api
 * @author david
 */
public class CallObjkt implements CallMarketPlaceInterface {
    
    private String contract;
    private MarketPlace name;

    public CallObjkt() {
        this.name = MarketPlace.Objkt;
    }

    
    @Override
    public List<Sale> query()throws IOException, URISyntaxException, InterruptedException{
        
        List<Sale> objktSale = new ArrayList<Sale>();
        
        for(int i = 0; i<4; i++){
            String responseJson = sendRequest(i);
            objktSale.addAll(analyseJson(responseJson, i));
        
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
    private String sendRequest(int requestCase) throws IOException, InterruptedException, URISyntaxException{
        
        Instant previousUTCHour = Instant.now().minus(1, ChronoUnit.HOURS);
        
        
        String POST_PARAMS = "";
       
        switch(requestCase){
            case 0 : 
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+this.contract+"\\\"}}) { collection_id  path  tokens(where: {fulfilled_asks: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}}, order_by: {timestamp: desc}) {      name   token_id   fulfilled_asks(where: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {        id        timestamp        price      }    }  }}\"}";
                break;
            case 1 :
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+this.contract+"\\\"}}) { collection_id  path  tokens (where: { offers: {_and: {update_timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}, status: {_eq: \\\"concluded\\\"}}}}) {      name   token_id  offers(where: {_and: {status: {_eq: \\\"concluded\\\"}}, update_timestamp: {_gte: \\\""+ previousUTCHour.toString() +"\\\"}})  {        id        update_timestamp        price      }    }  }}\"}";
                break;
            case 2 :
                POST_PARAMS = "{ \"query\" : \"query MyQuery {  obj_fulfilled_dutch(where: {_and: {dutch_auction: {fa: {contract: {_eq: \\\"" + this.contract + "\\\"}}, timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}}}) {    price    id    timestamp    token {      token_id    name   }  dutch_auction {      fa {        path      }    }  }}\"}";
                break;
            case 3 :
                POST_PARAMS =" { \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+this.contract+"\\\"}}) {    collection_id    path    tokens(where: {english_auctions: {highest_bid: {_gte: \\\"0\\\"}, end_time: {_gte: \\\""+ previousUTCHour.toString() +"\\\"}, status: {_eq: \\\"concluded\\\"}}}) {      name      token_id      english_auctions(where: {highest_bid: {_gte: \\\"0\\\"}, end_time: {_gte: \\\""+ previousUTCHour.toString() +"\\\"}, status: {_eq: \\\"concluded\\\"}}) {        highest_bid        id        end_time      }    }  }} \"}";
                break;
        }
        
        HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI("https://data.objkt.com/v2/graphql"))
          .header("Content-Type", "application/json")
          .header("Accept", "application/json")
          .timeout(Duration.of(10, SECONDS))
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
    private List<Sale> analyseJson(String responseJson, int requestCase){
        List<Sale> sellList = new ArrayList<Sale>();
        
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm:ssz");
                    
        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

        if (requestCase != 2){
            JsonArray contracts = resJson.getAsJsonObject("data").getAsJsonArray("fa");

            for(Object c : contracts){
                JsonObject contract = (JsonObject) c;

                String path = null;
                if(!contract.get("path").isJsonNull()){
                    path = contract.get("path").getAsString();
                }

                JsonArray allNewSellToken = contract.getAsJsonArray("tokens");

                for(Object s : allNewSellToken){
                    JsonObject token = (JsonObject) s;

                    String tokenname = token.get("name").getAsString(); 
                    Long id = token.get("token_id").getAsLong(); 
                    String timestamp = "";
                    double price = 0;

                    switch (requestCase) {
                        case 0 :
                            JsonArray allNewTrasacts = token.getAsJsonArray("fulfilled_asks");
                            for(Object t : allNewTrasacts){
                                JsonObject transaction = (JsonObject) t;
                                price = transaction.get("price").getAsDouble()/1000000.0;
                                timestamp = transaction.get("timestamp").getAsString();
                                sellList.add(new Sale(tokenname, id, price, SaleType.ListedSale, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant()));
                            }  
                            break;
                        case 1 :
                            JsonArray allNewOffer = token.getAsJsonArray("offers");
                            for(Object t : allNewOffer){
                                JsonObject offer = (JsonObject) t;
                                price = offer.get("price").getAsDouble()/1000000.0;
                                timestamp = offer.get("update_timestamp").getAsString();
                                sellList.add(new Sale(tokenname, id, price, SaleType.Offer, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant()));
                            }
                            break;
                        case 3 :
                            JsonArray allNewEnglishAuction = token.getAsJsonArray("english_auctions");
                            for(Object t : allNewEnglishAuction){
                                JsonObject englishAuction = (JsonObject) t;
                                price = englishAuction.get("highest_bid").getAsDouble()/1000000.0;
                                timestamp = englishAuction.get("end_time").getAsString();
                                sellList.add(new Sale(tokenname, id, price, SaleType.EnglishAuction, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant()));
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
                
                String path = null;
                if(!success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").isJsonNull()){
                    path = success_dutch_auction.getAsJsonObject("dutch_auction").getAsJsonObject("fa").get("path").getAsString();
                }
                
                Long id = success_dutch_auction.getAsJsonObject("token").get("token_id").getAsLong();
                String tokenname = success_dutch_auction.getAsJsonObject("token").get("name").getAsString();
                sellList.add(new Sale(tokenname, id, price, SaleType.DutchAuction, this.getName(), path,  OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER).toInstant()));

            }
        }
        return sellList;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Override
    public MarketPlace getName() {
        return name;
    }
    
    
}
