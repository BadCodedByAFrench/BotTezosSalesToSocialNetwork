/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlace;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class Contract {
    
    private String contract;
    
    private String name;
    
    private String path;
    
    private double totalprice;
    
    private long nbSale;
    
    private double min;
    
    private double max;

    private List<MarketPlace> marketplace;
    
    public Contract(Sale firstSale) {
        this.marketplace = new ArrayList<MarketPlace>();
        this.marketplace.add(firstSale.getMarketplace());
        this.contract = firstSale.getContract();
        this.name = firstSale.getCollectionName();
        this.path = firstSale.getPathname();
        this.totalprice = firstSale.getPrice();
        this.nbSale = 1;
        this.min = firstSale.getPrice();
        this.max = firstSale.getPrice();
    }

    public String getContract() {
        return contract;
    }

    public String getPath() {
        return path;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public long getNbSale() {
        return nbSale;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String getName() {
        return name;
    }
    
    public void addSale(Sale newSale){
        
        this.totalprice += newSale.getPrice();
        this.nbSale++;
        
        if (newSale.getPrice() < min){
            min = newSale.getPrice();
        }
        else if(newSale.getPrice() > max){
            max = newSale.getPrice();
        }
        
        if(!marketplace.contains(newSale.getMarketplace())){
            marketplace.add(newSale.getMarketplace());
        }
    }

    public List<MarketPlace> getMarketplace() {
        return marketplace;
    }
    
    public double getAvg(){
        return this.totalprice/this.nbSale;
    }
    
}
