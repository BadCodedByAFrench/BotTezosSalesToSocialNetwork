/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;

/**
 * Wallet of a buyer/seller
 * @author david
 */
public class Address {
    
    /**
     * tezos adress
     */
    
    private String adress; 
    
    /**
     * Tezos domain
     */
    private String tezdomain;

    public Address(String adress, String tezdomain) {
        this.adress = adress;
        this.tezdomain = tezdomain;
    }

    public String getAdress() {
        return adress;
    }

    public String getTezdomain() {
        return tezdomain;
    }
}
