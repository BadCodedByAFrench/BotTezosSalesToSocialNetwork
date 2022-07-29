/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum;

/**
 *
 * @author david
 */
public enum SaleTypeEnum {
    ListedSale("Listed sale"),
    Offer("Offer"),
    EnglishAuction("English auction"),
    DutchAuction("Dutch auction"),
    Auction("Auction"),
    Mint("Mint"),
    Unknown("Unknown");
    
    private String type;
    
    SaleTypeEnum(String type){
        this.type = type;
    }
    
    public String getType(){
        return type;
    }
}
