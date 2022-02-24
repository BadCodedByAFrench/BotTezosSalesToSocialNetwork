/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
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

/**
 * Call OBJKT Api
 * @author david
 */
public class CallObjkt implements CallMarketPlaceInterface {
    
    private String contract;
    private long idLastAsk;

    public CallObjkt(String contract, long idLastAsk) {
        this.contract = contract;
        this.idLastAsk = idLastAsk;
    }
    
    public CallObjkt() {
     
    }

    
    @Override
    public List<Sale> query()throws IOException, URISyntaxException, InterruptedException{
        
        String responseJson = sendRequest();
        
        
        
        return analyseJson(responseJson);
    }
    
    /**
     * We send the request to get all the sell from the previous hour
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException 
     */
    private String sendRequest() throws IOException, InterruptedException, URISyntaxException{
        
        Instant previousUTCHour = Instant.now().minus(1, ChronoUnit.HOURS);
        
        
        final String POST_PARAMS = "{ \"query\" : \"query MyQuery {  fa(where: {contract: {_eq: \\\""+this.contract+"\\\"}}) { collection_id  path  tokens(where: {fulfilled_asks: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}}, order_by: {timestamp: desc}) {      name   token_id   fulfilled_asks(where: {timestamp: {_gte: \\\"" + previousUTCHour.toString() + "\\\"}}) {        id        timestamp        price      }    }  }}\"}";
       
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
    private List<Sale> analyseJson(String responseJson){
        List<Sale> sellList = new ArrayList<Sale>();
        
        JsonObject resJson = JsonParser.parseString(responseJson).getAsJsonObject();

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
                    
                    String name = token.get("name").getAsString(); 
                    Long id = token.get("token_id").getAsLong(); 
                    String timestamp = "";
                    double price = 0;

                    JsonArray allNewTrasacts = token.getAsJsonArray("fulfilled_asks");
                    String type = "Listing";
                    for(Object t : allNewTrasacts){
                        JsonObject transaction = (JsonObject) t;
                        price = transaction.get("price").getAsDouble()/1000000.0;
                        timestamp = transaction.get("timestamp").getAsString();
                        sellList.add(new Sale(name, id, price, type, "Objkt", path, timestamp));
                    }
                    
                    
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
    


    public long getIdLastAsk() {
        return idLastAsk;
    }

    public void setIdLastAsk(long idLastAsk) {
        this.idLastAsk = idLastAsk;
    }   
}
