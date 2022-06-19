/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Class that keep the last successfuls refresh
 * @author david
 */
public class LastRefresh {
    
    /**
     * Last timestamp of sucessful sales refresh
     */
    private Instant lastSucessfullSaleRefresh;
    
    /**
     * Last timestamp of sucessful stats refresh
     */
    private Instant lastSucessfullStatRefresh;
    
    private Instant startTime;
    
    public LastRefresh(){
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
    

    public Instant getLastSucessfullSaleRefresh() {
        return lastSucessfullSaleRefresh;
    }

    public void setLastSucessfullSaleRefresh(Instant lastSucessfullSaleRefresh) {
        this.lastSucessfullSaleRefresh = lastSucessfullSaleRefresh;
    }

    public Instant getLastSucessfullStatRefresh() {
        return lastSucessfullStatRefresh;
    }

    public void setLastSucessfullStatRefresh(Instant lastSucessfullStatRefresh) {
        this.lastSucessfullStatRefresh = lastSucessfullStatRefresh;
    }

    public Instant getStartTime() {
        return startTime;
    }
}
