/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

/**
 * Class that keep the last successfuls refresh
 *
 * @author david
 */
public class LastRefresh {

    /**
     * Last timestamp of sucessful sales refresh
     */
    private transient Instant lastSucessfullSaleRefresh;

    /**
     * Last timestamp of sucessful stats refresh
     */
    private transient Instant lastSucessfullStatRefresh;

    /**
     * Last timestamp of sucessful listing and bidding refresh
     */
    private transient Instant lastSucessfullListingAndBiddingRefresh;

    private transient Instant startTime;

    public LastRefresh() {
    }

    /**
     * Function to refresh the instant at start/restart
     */
    public void resetRefresh(MarketPlaceProfile mpP) {
        this.lastSucessfullSaleRefresh = Instant.now().minus(mpP.getRefreshSalesTime(), ChronoUnit.valueOf(mpP.getRefreshSales().toString().toUpperCase()));

        this.lastSucessfullStatRefresh = Instant.now().minus(mpP.getRefreshSalesStats(), ChronoUnit.valueOf(mpP.getRefreshStats().toString().toUpperCase()));

        this.lastSucessfullListingAndBiddingRefresh = Instant.now().minus(mpP.getRefreshListingAndBiddingTime(), ChronoUnit.valueOf(mpP.getRefreshListingAndBidding().toString().toUpperCase()));

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

    public Instant getLastSucessfullListingAndBiddingRefresh() {
        return lastSucessfullListingAndBiddingRefresh;
    }

    public void setLastSucessfullListingAndBiddingRefresh(Instant lastSucessfullListingAndBiddingRefresh) {
        this.lastSucessfullListingAndBiddingRefresh = lastSucessfullListingAndBiddingRefresh;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setLastRefresh(Collection<Sale> newSales, BotModeEnum mode) {

        if (newSales.size() != 0) {

            Instant lasttimeStamp = null;

            for (Sale aSale : newSales) {
                if (lasttimeStamp == null) {
                    lasttimeStamp = aSale.getTimestamp();
                } else if (aSale.getTimestamp().compareTo(lasttimeStamp) > 0) {
                    lasttimeStamp = aSale.getTimestamp();
                }
            }

            if (lasttimeStamp != null) {
                if (mode == BotModeEnum.Stat && lasttimeStamp.isAfter(this.getLastSucessfullStatRefresh())) {
                    this.setLastSucessfullStatRefresh(lasttimeStamp);
                } else if (mode == BotModeEnum.Sale && lasttimeStamp.isAfter(this.getLastSucessfullSaleRefresh())) {
                    this.setLastSucessfullSaleRefresh(lasttimeStamp);
                } else if (mode == BotModeEnum.ListingAndBidding && lasttimeStamp.isAfter(this.getLastSucessfullListingAndBiddingRefresh())) {
                    this.setLastSucessfullListingAndBiddingRefresh(lasttimeStamp);
                }
            }
        }
    }

}
