/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotThreadManageNewSale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceProfileStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author david
 */
public class MarketPlaceProfile {

    /**
     * Units of sales refresh (Minutes/ Hours / Days)
     */
    private TimeUnit refreshSales;

    /**
     * How many time we want to wait between 2 sale refresh
     */
    private int refreshSalesTime;

    /**
     * Units of stats refresh (Hours / Days)
     */
    private TimeUnit refreshStats;

    /**
     * How many time we want to wait between 2 stat refresh
     */
    private int refreshSalesStats;

    /**
     * Units of listing and bidding refresh (Minutes/ Hours / Days)
     */
    private TimeUnit refreshListingAndBidding;

    private MarketPlaceProfileStatusEnum status;

    /**
     * How many time we want to wait between 2 Listing And Bidding refresh
     */
    private int refreshListingAndBiddingTime;

    private HashMap<MarketPlaceEnum, LastRefresh> marketPlaceLastRefresh;

    private transient List<Bot> subscribersSales;

    private transient List<Bot> subscribersStat;

    private transient List<Bot> subscribersListingAndBidding;

    private String name;

    /**
     * Tool that will execute query every X minutes/hours/days
     */
    private transient ScheduledThreadPoolExecutor executor;

    private transient ScheduledFuture<?> scheduledFutureSales;
    private transient ScheduledFuture<?> scheduledFutureStat;
    private transient ScheduledFuture<?> scheduledFutureListingAndBidding;

    private transient SalesToSocialNetwork apiHandlerSales;

    private transient SalesToSocialNetwork apiHandlerStat;

    private transient SalesToSocialNetwork apiHandlerListingAndBidding;

    public MarketPlaceProfile() {

        subscribersSales = new ArrayList<Bot>();
        subscribersStat = new ArrayList<Bot>();
        subscribersListingAndBidding = new ArrayList<Bot>();

        marketPlaceLastRefresh = new HashMap<MarketPlaceEnum, LastRefresh>();
        marketPlaceLastRefresh.put(MarketPlaceEnum.Teia, new LastRefresh());
        marketPlaceLastRefresh.put(MarketPlaceEnum.fxhash, new LastRefresh());
        marketPlaceLastRefresh.put(MarketPlaceEnum.Objkt, new LastRefresh());
        marketPlaceLastRefresh.put(MarketPlaceEnum.Rarible, new LastRefresh());

        name = "";
        refreshSales = TimeUnit.MINUTES;
        refreshSalesTime = 1;

        refreshStats = TimeUnit.DAYS;
        refreshSalesStats = 1;

        refreshListingAndBidding = TimeUnit.MINUTES;
        refreshListingAndBiddingTime = 1;

        apiHandlerSales = new SalesToSocialNetwork(BotModeEnum.Sale, this);
        apiHandlerStat = new SalesToSocialNetwork(BotModeEnum.Stat, this);
        apiHandlerListingAndBidding = new SalesToSocialNetwork(BotModeEnum.ListingAndBidding, this);

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);

        status = MarketPlaceProfileStatusEnum.Ready;
    }

    public boolean hasSubscriber() {
        return subscribersSales.size() > 0 || subscribersStat.size() > 0 || subscribersListingAndBidding.size() > 0;
    }

    public void addSubscriber(BotModeEnum type, Bot newBot) {
        if (type == BotModeEnum.Sale && !subscribersSales.contains(newBot)) {
            subscribersSales.add(newBot);
        } else if (type == BotModeEnum.Stat && !subscribersStat.contains(newBot)) {
            subscribersStat.add(newBot);
        } else if (type == BotModeEnum.ListingAndBidding && !subscribersListingAndBidding.contains(newBot)) {
            subscribersListingAndBidding.add(newBot);
        }

        if (this.status == MarketPlaceProfileStatusEnum.Ready) {
            this.start();
        }
    }

    public void removeSubscriber(BotModeEnum type, Bot newBot) {
        if (type == BotModeEnum.Sale && subscribersSales.contains(newBot)) {
            subscribersSales.remove(newBot);
        } else if (type == BotModeEnum.Stat && subscribersStat.contains(newBot)) {
            subscribersStat.remove(newBot);
        } else if (type == BotModeEnum.ListingAndBidding && subscribersListingAndBidding.contains(newBot)) {
            subscribersListingAndBidding.remove(newBot);
        }

        if (!this.hasSubscriber()) {
            this.stop();
        }
    }

    public HashMap<MarketPlaceEnum, LastRefresh> getMarketPlaceLastRefresh() {
        return marketPlaceLastRefresh;
    }

    public void start() {
        
        for(LastRefresh refresher : marketPlaceLastRefresh.values()){
            refresher.resetRefresh(this);
        }
        
        scheduledFutureSales = executor.scheduleAtFixedRate(apiHandlerSales, 0, this.getRefreshSalesTime(), this.getRefreshSales());

        scheduledFutureStat = executor.scheduleAtFixedRate(apiHandlerStat, 0, this.getRefreshSalesStats(), this.getRefreshStats());

        scheduledFutureListingAndBidding = executor.scheduleAtFixedRate(apiHandlerListingAndBidding, 0, this.getRefreshListingAndBiddingTime(), this.getRefreshListingAndBidding());

        this.status = MarketPlaceProfileStatusEnum.Running;
    }

    public void stop() {

        scheduledFutureSales.cancel(true);

        scheduledFutureStat.cancel(true);

        scheduledFutureListingAndBidding.cancel(true);

        this.status = MarketPlaceProfileStatusEnum.Ready;
    }

    public String isUsed() {
        String allUseByBots = "";

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {
            if (aBot.getMpProfile() == this) {

                if (aBot.getBotStatus() == BotStatusEnum.Running || aBot.getBotStatus() == BotStatusEnum.Ready) {
                    if (!allUseByBots.equals("")) {
                        allUseByBots += ", ";
                    }

                    allUseByBots += aBot.getName();
                }
            }
        }

        return allUseByBots;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public HashMap<MarketPlaceEnum, List<String>> updateMarketPlaceList(BotModeEnum mode) {
        HashMap<MarketPlaceEnum, List<String>> allSearchedContracts = new HashMap<MarketPlaceEnum, List<String>>();

        for (Bot aBot : returnListByMode(mode)) {

            for (MarketPlaceEnum mpEnum : aBot.getMarketplaces().keySet()) {

                if (!allSearchedContracts.containsKey(mpEnum)) {
                    allSearchedContracts.put(mpEnum, new ArrayList<String>());
                }
                allSearchedContracts.get(mpEnum).addAll(aBot.getMarketplaces().get(mpEnum).getAllContractsString());

            }

        }

        return allSearchedContracts;
    }

    private List<Bot> returnListByMode(BotModeEnum mode) {
        if (mode == BotModeEnum.Sale) {
            return this.subscribersSales;
        }
        if (mode == BotModeEnum.Stat) {
            return this.subscribersStat;
        }
        return this.subscribersListingAndBidding;
    }

    public void receiveNewSales(BotModeEnum mode, HashMap<MarketPlaceEnum, HashMap<String, Sale>> newSales, ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck) {

        for (MarketPlaceEnum mpEnum : newSales.keySet()) {
            this.marketPlaceLastRefresh.get(mpEnum).setLastRefresh(newSales.get(mpEnum).values(), mode);
        }

        HashMap<Bot, HashMap<String, Sale>> newSalesPerBot = new HashMap<>();

        for (Bot aBot : returnListByMode(mode)) {
            HashMap<String, Sale> allSalesForBotByMP = new HashMap<>();
            for (MarketPlaceEnum mpEnum : aBot.getMarketplaces().keySet()) {
                if (newSales.containsKey(mpEnum)) {
                    List<String> allContractFromBotByMp = aBot.getMarketplaces().get(mpEnum).getAllContractsString();
                    for (Sale aSale : newSales.get(mpEnum).values()) {
                        if (allContractFromBotByMp.contains(aSale.getContract())) {
                            allSalesForBotByMP.put(aSale.getIdtransaction(), aSale);
                        }
                    }

                }
            }
            newSalesPerBot.put(aBot, allSalesForBotByMP);
        }

        for (Bot aBot : newSalesPerBot.keySet()) {
            aBot.addLog("New sales", "OK", String.valueOf(newSalesPerBot.get(aBot).size()));
            BotThreadManageNewSale botThread = new BotThreadManageNewSale(newSalesPerBot.get(aBot), mode, aBot, finishedMarketPlaceToCheck);
            botThread.run();
        }
    }

    public String getName() {
        return name;
    }

    public void setRefreshSales(TimeUnit refreshSales) {
        this.refreshSales = refreshSales;
    }

    public void setRefreshSalesTime(int refreshSalesTime) {
        this.refreshSalesTime = refreshSalesTime;
    }

    public void setRefreshStats(TimeUnit refreshStats) {
        this.refreshStats = refreshStats;
    }

    public void setRefreshSalesStats(int refreshSalesStats) {
        this.refreshSalesStats = refreshSalesStats;
    }

    public void setRefreshListingAndBidding(TimeUnit refreshListingAndBidding) {
        this.refreshListingAndBidding = refreshListingAndBidding;
    }

    public void setRefreshListingAndBiddingTime(int refreshListingAndBiddingTime) {
        this.refreshListingAndBiddingTime = refreshListingAndBiddingTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeUnit getRefreshSales() {
        return refreshSales;
    }

    public int getRefreshSalesTime() {
        return refreshSalesTime;
    }

    public TimeUnit getRefreshStats() {
        return refreshStats;
    }

    public int getRefreshSalesStats() {
        return refreshSalesStats;
    }

    public TimeUnit getRefreshListingAndBidding() {
        return refreshListingAndBidding;
    }

    public int getRefreshListingAndBiddingTime() {
        return refreshListingAndBiddingTime;
    }

    public void setStatus(MarketPlaceProfileStatusEnum status) {
        this.status = status;
    }

    public String fullDetail(){
        return this.name + " (Sale : " + this.refreshSalesTime + " " + this.refreshSales + ", Stat : "+ this.refreshSalesStats + " " + this.refreshStats + ", Listing & Bidding : "+ this.refreshListingAndBiddingTime + " " + this.refreshListingAndBidding +")";
    }
}
