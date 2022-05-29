/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class SalesHistoryManager {
    
    private HashMap<Long, Sale> salesHistory;
    private HashMap<Long, Sale> statHistory;
    
    private static SalesHistoryManager salesHistoryManager;
    
    private SalesHistoryManager(){
        salesHistory = new HashMap<Long, Sale>();
        statHistory = new HashMap<Long, Sale>();
    }
    
    public static SalesHistoryManager getSalesHistoryManager(){
        if(salesHistoryManager == null){
            salesHistoryManager = new SalesHistoryManager();
        }
        
        return salesHistoryManager;
    }
    
    public List<Sale> checkNewSales(List<Sale> newSalesList, int mode, HashMap<String, List<String>> sellerFilter, HashMap<String, List<String>> itemFilter){
        
        List<Sale> saleToShow = new ArrayList<Sale>();
        
        if (mode == 0){
            for(Sale aSale : newSalesList){
                if(!statHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(BotLastRefresh.getLastRefresh().getStartTime()) ){
                    
                    boolean sellerCheck =true;
                    boolean itemCheck = true;
                    
                    if(!sellerFilter.get(aSale.getContract()).isEmpty() && !sellerFilter.get(aSale.getContract()).contains(aSale.getSeller().getAdress())){
                        sellerCheck = false;
                    }
                    
                    if( !itemFilter.get(aSale.getContract()).isEmpty() && !itemFilter.get(aSale.getContract()).contains(aSale.getId().toString())){
                        itemCheck = false;
                    }
                    
                    if(itemCheck && sellerCheck){
                        saleToShow.add(aSale);
                        statHistory.put(aSale.getIdtransaction(), aSale);
                    }
                }
            }
        }
        else if (mode == 1 ){
            for(Sale aSale : newSalesList){
                if(!salesHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(BotLastRefresh.getLastRefresh().getStartTime())){
                    saleToShow.add(aSale);
                    salesHistory.put(aSale.getIdtransaction(), aSale);
                }
            }
        }
        
        
        return saleToShow;
    }
    
    public void removeOldestSales(int mode){
        List<Long> idsToDelete = new ArrayList<Long>();
        
        if(mode == 1){
            for(Sale aSale : statHistory.values()){
                if(aSale.getTimestamp().isBefore(BotLastRefresh.getLastRefresh().getLastSucessfullStatRefresh())){
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for(Long id : idsToDelete){
                statHistory.remove(id);
            }
        }
        else if (mode == 0){
            for(Sale aSale : salesHistory.values()){
                if(aSale.getTimestamp().isBefore(BotLastRefresh.getLastRefresh().getLastSucessfullSaleRefresh())){
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for(Long id : idsToDelete){
                salesHistory.remove(id);
            }
        }
    }
}
