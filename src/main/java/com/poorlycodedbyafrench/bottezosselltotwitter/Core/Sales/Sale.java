/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
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
    private SaleTypeEnum type;
    
    /**
     * Marketplace of the sale
     * Could be replace by an Enum
     */
    private MarketPlaceEnum marketplace;

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
    
    /**
     * Wallet of the buyer
     */
    private Address buyer;
    
    /**
     * Wallet of the seller
     */
    private Address seller;
    
    /**
     * IPFS file of the NFT
     */
    private String ipfs; 
    
    /**
     * Id of the transaction
     */
    private Long idtransaction;
    
    public Sale(String name, Long id, double price, SaleTypeEnum type, MarketPlaceEnum marketplace, String pathname, Instant timestamp, String contract, String collectionName, Address buyer, Address seller, String ipfs, Long idtransaction) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.type = type;
        this.marketplace = marketplace;
        this.pathname = pathname;
        this.timestamp = timestamp;
        this.contract = contract;
        this.collectionName = collectionName;
        this.buyer = buyer;
        this.seller = seller;
        this.ipfs = ipfs;
        this.idtransaction = idtransaction;
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

    public SaleTypeEnum getType() {
        return type;
    }

    public MarketPlaceEnum getMarketplace() {
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
    
    public Address getBuyer() {
        return buyer;
    }

    public Address getSeller() {
        return seller;
    }

    public String getIpfs() {
        return ipfs;
    }

    public Long getIdtransaction() {
        return idtransaction;
    }

    
    @Override
    public String toString() {
        return "Title : " + name + " Id : " + id + " Price : " + price  ; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    /**
     * Depending of the configuration, we sort by the msot oldiest timestamp or biggest price
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Sale o) {
        
        if(BotConfiguration.getConfiguration().getOrderBy() == 0){
            return this.getTimestamp().compareTo(o.getTimestamp());
        }
        else {
            return Double.compare(o.getPrice(), this.getPrice());
        }
    }
}
