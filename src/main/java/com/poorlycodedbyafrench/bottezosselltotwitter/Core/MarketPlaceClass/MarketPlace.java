/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class MarketPlace {
    
    private MarketPlaceEnum marketplace;
    
    private List<String> contracts;
    
    private HashMap<String, List<String>> sellerList;
    
    private HashMap<String, List<String>> itemList;
    
    private HashMap<String, String> royaltywallet;

    private transient CallMarketPlaceInterface calledMarketPlace;
    
    private transient LastRefresh lastrefresh;

    public MarketPlace(MarketPlaceEnum marketplace, CallMarketPlaceInterface calledMarketPlace) {
        this.marketplace = marketplace;
        this.calledMarketPlace = calledMarketPlace;
        this.lastrefresh = new LastRefresh();
        
        contracts = new ArrayList<String>();
        sellerList = new HashMap<String, List<String>>();
        itemList = new HashMap<String, List<String>>();
        royaltywallet = new HashMap<String, String>();
    }

    public MarketPlaceEnum getMarketplace() {
        return marketplace;
    }

    public List<String> getContracts() {
        return contracts;
    }
    
    public void addFilter(String contract, String filter, int type){
        if (type == 0){
            sellerList.get(contract).add(filter);
        }
        else if(type == 1){
            itemList.get(contract).add(filter);
        }
    }
    
    public void removeFilter(String contract, String filter, int type){
       if (type == 0){
            sellerList.get(contract).remove(filter);
        }
        else if(type == 1){
            itemList.get(contract).remove(filter);
        } 
    }
    
    public void addContract(String contract){
        if(!contracts.contains(contract)){
            contracts.add(contract);
            sellerList.put(contract, new ArrayList<String>());
            itemList.put(contract, new ArrayList<String>());
            royaltywallet.put(contract, new String());
        }
    }
    
    public void removeContract(String contract){
        if( contracts.contains(contract)){
            contracts.remove(contract);
            sellerList.remove(contract);
            itemList.remove(contract);
            royaltywallet.remove(contract);
        }
    }

    public CallMarketPlaceInterface getCalledMarketPlace() {
        return calledMarketPlace;
    }

    public HashMap<String, List<String>> getSellerList() {
        return sellerList;
    }

    public HashMap<String, List<String>> getItemList() {
        return itemList;
    }

    public LastRefresh getLastrefresh() {
        return lastrefresh;
    }
    
    public void resetLastRefresh(){
        this.lastrefresh.resetRefresh();
    }

    public void setContracts(List<String> contracts) {
        this.contracts = contracts;
    }

    public void setSellerList(HashMap<String, List<String>> sellerList) {
        this.sellerList = sellerList;
    }

    public void setItemList(HashMap<String, List<String>> itemList) {
        this.itemList = itemList;
    }

    public HashMap<String, String> getRoyaltywallet() {
        return royaltywallet;
    }

    public void setRoyaltywallet(HashMap<String, String> royaltywallet) {
        this.royaltywallet = royaltywallet;
    }
    
    public void setLastRefresh(List<Sale> newSales, BotModeEnum mode){
        
        if (newSales.size() != 0){
            
            Instant lasttimeStamp = null;

            for(Sale aSale : newSales ){
                if(aSale.getMarketplace().equals(this.getMarketplace())){
                    if(lasttimeStamp == null){
                        lasttimeStamp = aSale.getTimestamp();
                    }
                    else if(aSale.getTimestamp().compareTo(lasttimeStamp) > 0){
                        lasttimeStamp = aSale.getTimestamp();
                    }
                }
            }
            
            if(lasttimeStamp != null){
                if (mode == BotModeEnum.Stat && lasttimeStamp.isAfter(lastrefresh.getLastSucessfullStatRefresh())){
                    lastrefresh.setLastSucessfullStatRefresh(lasttimeStamp);
                }
                else if(lasttimeStamp.isAfter(lastrefresh.getLastSucessfullSaleRefresh())){
                    lastrefresh.setLastSucessfullSaleRefresh(lasttimeStamp);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return marketplace.toString();
    }
}
