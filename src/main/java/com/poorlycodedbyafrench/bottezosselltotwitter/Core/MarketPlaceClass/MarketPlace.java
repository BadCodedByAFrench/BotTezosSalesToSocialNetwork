/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class MarketPlace implements Serializable {
    
    private MarketPlaceEnum marketplace;
    
    private HashMap<String, SearchContractSales> allContractsFromThisMarketPlace; 

    public MarketPlace(MarketPlaceEnum marketplace) {
        this.marketplace = marketplace;
        
        
        allContractsFromThisMarketPlace = new HashMap<String,SearchContractSales>();
    }

    public MarketPlaceEnum getMarketplace() {
        return marketplace;
    }

    public List<String> getAllContractsString(){
        ArrayList<String> allContracts = new ArrayList<String>();
        
        for(String aSearchedContracts : allContractsFromThisMarketPlace.keySet()){
            allContracts.add(aSearchedContracts);
        }
        
        return allContracts;
    }
    
    public HashMap<String, SearchContractSales> getAllContractsFromThisMarketPlace() {
        return allContractsFromThisMarketPlace;
    }

    @Override
    public String toString() {
        return marketplace.toString();
    }
    
}
