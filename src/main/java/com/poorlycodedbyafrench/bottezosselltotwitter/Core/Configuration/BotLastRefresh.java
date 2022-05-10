/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Class that keep the last successfuls refresh
 * @author david
 */
public class BotLastRefresh {
    
    /**
     * Last timestamp of sucessful sales refresh
     */
    private Instant lastSucessfullSaleRefresh;
    
    /**
     * Last timestamp of sucessful stats refresh
     */
    private Instant lastSucessfullStatRefresh;
    
    private Instant startTime;
    
    /**
     * Random singleton
     */
    private static BotLastRefresh lastRefresh;
    
    private BotLastRefresh(){
        resetRefresh();
    }
    
    /**
     * Function to refresh the instant at start/restart
     */
    public void resetRefresh(){
        this.lastSucessfullSaleRefresh = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesTime(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshSales().toString().toUpperCase()));
        
        this.lastSucessfullStatRefresh = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesStats(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshStats().toString().toUpperCase()));
        
        this.startTime = Instant.now();
    }
    
    /**
     * We look after the most recent timestamp before to update
     * @return 
     */
    public synchronized static BotLastRefresh getLastRefresh(){
        if (lastRefresh == null){
            lastRefresh = new BotLastRefresh();
        }
        
        return lastRefresh;
    }
    
    public static void sendToCollector(){
        lastRefresh = null;
    }

    public Instant getLastSucessfullSaleRefresh() {
        return lastSucessfullSaleRefresh;
    }

    private void setLastSucessfullSaleRefresh(Instant lastSucessfullSaleRefresh) {
        this.lastSucessfullSaleRefresh = lastSucessfullSaleRefresh;
    }

    public Instant getLastSucessfullStatRefresh() {
        return lastSucessfullStatRefresh;
    }

    private void setLastSucessfullStatRefresh(Instant lastSucessfullStatRefresh) {
        this.lastSucessfullStatRefresh = lastSucessfullStatRefresh;
    }

    public static void setLastRefresh(BotLastRefresh lastRefresh) {
        BotLastRefresh.lastRefresh = lastRefresh;
    }

    public Instant getStartTime() {
        return startTime;
    }
    
    public void setLastRefresh(List<Sale> newSales, int mode){
        
        
        if (newSales.size() != 0){
            Instant lasttimeStamp = newSales.get(0).getTimestamp();

            for(Sale aSale : newSales ){
                if(aSale.getTimestamp().compareTo(lasttimeStamp) > 0){
                    lasttimeStamp = aSale.getTimestamp();
                }
            }
            
            if (mode == 1 && lasttimeStamp.isAfter(this.getLastSucessfullStatRefresh())){
                this.setLastSucessfullStatRefresh(lasttimeStamp);
            }
            else if(lasttimeStamp.isAfter(this.getLastSucessfullSaleRefresh())){
                this.setLastSucessfullSaleRefresh(lasttimeStamp);
            }
        }
    }
}
