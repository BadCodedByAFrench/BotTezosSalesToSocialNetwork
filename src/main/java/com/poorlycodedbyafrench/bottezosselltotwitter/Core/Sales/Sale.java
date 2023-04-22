/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import java.time.Instant;
import java.util.Comparator;

/**
 * Represent the sale of an NFT
 *
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
    private String id;

    /**
     * Price of the sale
     */
    private double price;

    /**
     * Type of the sale (listed / offer / english auction / dutch auction) Could
     * be replace by an Enum
     */
    private SaleTypeEnum type;

    /**
     * Marketplace of the sale Could be replace by an Enum
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
    private String idtransaction;

    public Sale(String name, String id, double price, SaleTypeEnum type, MarketPlaceEnum marketplace, String pathname, Instant timestamp, String contract, String collectionName, Address buyer, Address seller, String ipfs, String idtransaction) {
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

    public String getId() {
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

    public String getIdtransaction() {
        return idtransaction;
    }

    @Override
    public String toString() {
        return "Title : " + name + " Id : " + id + " Price : " + price; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    /**
     * Depending of the configuration, we sort by the most oldiest timestamp or
     * biggest price
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Sale o) {

        return this.getTimestamp().compareTo(o.getTimestamp());

    }

    private static Comparator<Sale> priceComparator = new Comparator<Sale>() {
        @Override
        public int compare(Sale s1, Sale s2) {
            return Double.compare(s2.getPrice(), s1.getPrice());
        }
    };

    private static Comparator<Sale> timeStampComparator = new Comparator<Sale>() {
        @Override
        public int compare(Sale s1, Sale s2) {
            return s1.getTimestamp().compareTo(s2.getTimestamp());
        }
    };

    public static Comparator<Sale> getPriceComparator() {
        return priceComparator;
    }

    public static Comparator<Sale> getTimeStampComparator() {
        return timeStampComparator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIpfs(String ipfs) {
        this.ipfs = ipfs;
    }

}
