/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleType;
import java.time.Instant;

/**
 * Represent the sale of an NFT
 * @author david
 */
public class Sale implements Comparable<Sale> {
    
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
    private SaleType type;
    
    /**
     * Marketplace of the sale
     * Could be replace by an Enum
     */
    private MarketPlace marketplace;

    /**
     * The path of a contract on Objkt
     */
    private String pathname;
    
    /**
     * Get the moment of the sale (as string until I convert into a proper date)
     */
    private Instant timestamp;
    
    /**
     * Contract related to the sale
     */
    private String contract;
    
    /**
     * Name of the associated collection
     */
    private String collectionName;
    
    public Sale(String name, Long id, double price, SaleType type, MarketPlace marketplace, String pathname, Instant timestamp, String contract, String collectionName) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.type = type;
        this.marketplace = marketplace;
        this.pathname = pathname;
        this.timestamp = timestamp;
        this.contract = contract;
        this.collectionName = collectionName;
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

    public SaleType getType() {
        return type;
    }

    public MarketPlace getMarketplace() {
        return marketplace;
    }

    public String getPathname() {
        return pathname;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getContract() {
        return contract;
    }

    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public String toString() {
        return "Title : " + name + " Id : " + id + " Price : " + price  ; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int compareTo(Sale o) {
        return Double.compare(o.getPrice(), this.getPrice());
    }
    
    
}
