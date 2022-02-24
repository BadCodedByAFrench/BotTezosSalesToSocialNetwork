/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;

/**
 * Represent the sale of an NFT
 * @author david
 */
public class Sale {
    
    /**
     * Name of the nft
     */
    private String name;
    
    /**
     * Id of the NFT
     */
    private Long id;
    
    /**
     * Price of the sale
     */
    private double price;
    
    /**
     * Type of the sale (listed / offer / english auction / dutch auction)
     * Could be replace by an Enum
     */
    private String type;
    
    /**
     * Marketplace of the sale
     * Could be replace by an Enum
     */
    private String marketplace;

    /**
     * The path of a contract on Objkt
     */
    private String pathname;
    
    /**
     * Get the moment of the sale (as string until I convert into a proper date)
     */
    private String timestamp;
    
    public Sale(String name, Long id, double price, String type, String marketplace, String pathname, String timestamp) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.type = type;
        this.marketplace = marketplace;
        this.pathname = pathname;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    
    public double getPrice() {
        return price;
    }    

    public String getType() {
        return type;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public String getPathname() {
        return pathname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    
    @Override
    public String toString() {
        return "Title : " + name + " Id : " + id + " Price : " + price  ; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
}
