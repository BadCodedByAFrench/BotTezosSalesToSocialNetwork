/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum;

/**
 *
 * @author david
 */
public enum SaleType {
    ListedSale("Listed sale"),
    Offer("Offer"),
    EnglishAuction("English auction"),
    DutchAuction("Dutch auction");
    
    private String type;
    
    SaleType(String type){
        this.type = type;
    }
    
    public String getType(){
        return type;
    }
}
