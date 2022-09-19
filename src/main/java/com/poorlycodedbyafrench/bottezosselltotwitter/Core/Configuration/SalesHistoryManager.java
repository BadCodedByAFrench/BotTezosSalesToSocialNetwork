/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class SalesHistoryManager {
    
    private HashMap<String, Sale> salesHistory;
    private HashMap<String, Sale> statHistory;
    
    private static SalesHistoryManager salesHistoryManager;
    
    private SalesHistoryManager(){
        salesHistory = new HashMap<String, Sale>();
        statHistory = new HashMap<String, Sale>();
    }
    
    public static SalesHistoryManager getSalesHistoryManager(){
        if(salesHistoryManager == null){
            salesHistoryManager = new SalesHistoryManager();
        }
        
        return salesHistoryManager;
    }
    
    public List<Sale> checkNewSales(Collection<Sale> newSalesList, BotModeEnum mode, HashMap<MarketPlaceEnum,MarketPlace> marketplaces){
        
        List<Sale> saleToShow = new ArrayList<Sale>();
        
        if (mode == BotModeEnum.Sale){
            for(Sale aSale : newSalesList){
                if(!salesHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(marketplaces.get(aSale.getMarketplace()).getLastrefresh().getStartTime())){
                    
                    boolean sellerCheck =true;
                    boolean itemCheck = true;
                    
                    if(!marketplaces.get(aSale.getMarketplace()).getSellerList().get(aSale.getContract()).isEmpty()  && !marketplaces.get(aSale.getMarketplace()).getSellerList().get(aSale.getContract()).contains(aSale.getSeller().getAdress()) ){
                        sellerCheck = false;
                    }
                    
                    if(!marketplaces.get(aSale.getMarketplace()).getItemList().get(aSale.getContract()).isEmpty() && !marketplaces.get(aSale.getMarketplace()).getItemList().get(aSale.getContract()).contains(aSale.getId().toString())){
                        itemCheck = false;
                    }
                    
                    if(itemCheck && sellerCheck){
                        saleToShow.add(aSale);
                        salesHistory.put(aSale.getIdtransaction(), aSale);
                    }
                }
            }
        }
        else if (mode == BotModeEnum.Stat ){
            for(Sale aSale : newSalesList){
                if(!statHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(marketplaces.get(aSale.getMarketplace()).getLastrefresh().getStartTime())){
                    saleToShow.add(aSale);
                    statHistory.put(aSale.getIdtransaction(), aSale);
                }
            }
        }
        
        return saleToShow;
    }
    
    public void removeOldestSales(BotModeEnum mode, HashMap<MarketPlaceEnum,MarketPlace> marketplaces, HashMap<String,Sale> allNewSales, ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck){
        List<String> idsToDelete = new ArrayList<String>();
        
        if(mode == BotModeEnum.Stat){
            for(Sale aSale : statHistory.values()){ 
                if(!allNewSales.containsKey(aSale.getIdtransaction()) && finishedMarketPlaceToCheck.contains(aSale.getMarketplace())){
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for(String id : idsToDelete){
                statHistory.remove(id);
            }
        }
        else if (mode == BotModeEnum.Sale){
            for(Sale aSale : salesHistory.values()){
                if(!allNewSales.containsKey(aSale.getIdtransaction()) && finishedMarketPlaceToCheck.contains(aSale.getMarketplace())){
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for(String id : idsToDelete){
                salesHistory.remove(id);
            }
        }
    }
}
