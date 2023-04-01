/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class SearchContractSales implements Serializable {
    
    private String contract;
    
    private List<String> sellerFilter;
    
    private List<String> itemFilter;
    
    private String royaltyWallet;

    public SearchContractSales(String contract) {
        this.contract = contract;
        this.sellerFilter = new ArrayList<>();
        this.itemFilter = new ArrayList<>();
        this.royaltyWallet = "";
    }

    public String getContract() {
        return contract;
    }

    public List<String> getSellerFilter() {
        return sellerFilter;
    }

    public List<String> getItemFilter() {
        return itemFilter;
    }

    public String getRoyaltyWallet() {
        return royaltyWallet;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public void setRoyaltyWallet(String royaltyWallet) {
        this.royaltyWallet = royaltyWallet;
    }
    
    public void addSellerFilter(String value){
        sellerFilter.add(value);
    }
    
    public void addItemFilter(String value){
        itemFilter.add(value);
    }
    
    public void removeSellerFilter(String value){
        sellerFilter.remove(value);
    }
    
    public void removeItemFilter(String value){
        itemFilter.remove(value);
    }

    @Override
    public String toString() {
        String toReturn = "Contract : " + contract;
        toReturn += "\n\tSeller filter : " + String.join(", ", sellerFilter);
        toReturn += "\n\tItem filter : " + String.join(", ", itemFilter);
        toReturn += "\n\tRoyalty wallet : " + royaltyWallet;
        
        return toReturn;
    }
    
    
    
}
