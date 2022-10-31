/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author david
 */
public class BotThreadManageNewSale implements Runnable {

    private HashMap<String, Sale> allNewSales;
    private BotModeEnum mode;
    private Bot theCurrentBot;

    private ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck;

    public BotThreadManageNewSale(HashMap<String, Sale> allNewSales, BotModeEnum mode, Bot theCurrentBot, ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck) {
        this.allNewSales = allNewSales;
        this.mode = mode;
        this.theCurrentBot = theCurrentBot;
        this.finishedMarketPlaceToCheck = finishedMarketPlaceToCheck;
    }

    @Override
    public void run() {

        List<Sale> filteredList = theCurrentBot.getHistoryManager().checkNewSales(allNewSales.values(), mode, theCurrentBot);

        if (mode == BotModeEnum.ListingAndBidding) {
            filteredList = filterListingAndBiddingEvent(filteredList);
        }

        theCurrentBot.getHistoryManager().removeOldestSales(mode, allNewSales, finishedMarketPlaceToCheck);

        //sort all the sales depend of configuration setting
        if(theCurrentBot.getOrderBy() == 0){
            Collections.sort(filteredList, Sale.getTimeStampComparator());
        }else{
            Collections.sort(filteredList, Sale.getPriceComparator());
        }
        

        HashMap<String, Long> balance = null;
        try {
            balance = NetworkMessageManager.getMessageManager().getBalanceRoyaltyWallet(theCurrentBot, filteredList);
        } catch (IOException ex) {
            theCurrentBot.addLog("Royalty", "Error : unable to get royalty", ex.getMessage());
            LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
        } catch (InterruptedException ex) {
            theCurrentBot.addLog("Royalty", "Error : unable to get royalty", ex.getMessage());
            LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
        }

        LinkedHashMap<Sale, String> messageSaver = new LinkedHashMap<Sale, String>();
        LinkedHashMap<Contract, String> messageContractsSaver = new LinkedHashMap<Contract, String>();

        DecimalFormat df = new DecimalFormat("##.00");
        Random rand = new Random();

        if (mode == BotModeEnum.Sale) {
            int countSale = 1;
            for (Sale aSale : filteredList) {
                messageSaver.put(aSale, NetworkMessageManager.getMessageManager().createSaleMessage(aSale, df, rand, countSale, balance, theCurrentBot));
                countSale++;
            }
        } else if (mode == BotModeEnum.Stat) {
            Instant previousUTCHour = Instant.now().minus(theCurrentBot.getMpProfile().getRefreshSalesStats(), ChronoUnit.valueOf(theCurrentBot.getMpProfile().getRefreshStats().toString().toUpperCase()));
            int countStat = 1;

            for (Contract contract : NetworkMessageManager.getMessageManager().createContractList(filteredList)) {
                messageContractsSaver.put(contract, NetworkMessageManager.getMessageManager().createStatMessage(contract, df, previousUTCHour, countStat, balance, theCurrentBot));
                countStat++;
            }
        } else if (mode == BotModeEnum.ListingAndBidding) {
            int countListingAndBidding = 1;

            for (Sale aSale : filteredList) {
                messageSaver.put(aSale, NetworkMessageManager.getMessageManager().createListingAndBiddingMessage(aSale, df, rand, countListingAndBidding, balance, theCurrentBot));
                countListingAndBidding++;
            }
        }

        ///////
        //Send new message in socialNetworkProfile
        ExecutorService multithreadSocialNetwork = Executors.newFixedThreadPool(theCurrentBot.getSnProfile().getAllSocialNetwork().size());
        ArrayList<Future<SocialNetworkEnum>> allFutureSocialNetwork = new ArrayList<>();
        ArrayList<SocialNetworkEnum> successfullSocialNetwork = new ArrayList<>();

        for (SocialNetworkInterface oneSocialNetwork : theCurrentBot.getSnProfile().getAllSocialNetwork()) {

            Future<SocialNetworkEnum> futureSocialNetwork = multithreadSocialNetwork.submit(oneSocialNetwork.createThreadSocialNetwork(mode, messageSaver, messageContractsSaver, theCurrentBot));
            allFutureSocialNetwork.add(futureSocialNetwork);
        }

        for (Future<SocialNetworkEnum> futureSocialNetwork : allFutureSocialNetwork) {
            try {
                SocialNetworkEnum theSocialNetwork = futureSocialNetwork.get();
                successfullSocialNetwork.add(theSocialNetwork);
                theCurrentBot.addLog(theSocialNetwork.toString(), "OK", String.valueOf(filteredList.size()));
            } catch (ExecutionException ex) {
                theCurrentBot.addLog(SalesToSocialNetwork.class.getName(), "Error", ex.getMessage());
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            } catch (Exception ex) {
                theCurrentBot.addLog(SalesToSocialNetwork.class.getName(), "Error", ex.getMessage());
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            }
        }

        multithreadSocialNetwork.shutdown();

        for (SocialNetworkInterface oneSocialNetwork : theCurrentBot.getSnProfile().getAllSocialNetwork()) {
            if (!successfullSocialNetwork.contains(oneSocialNetwork.getName())) {
                //model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(), "Failed : send logs to dev", "Log file at the parent folder of your \".exe\" location "});
            }
        }

        messageSaver.clear();
        messageContractsSaver.clear();
        messageSaver = null;
        messageContractsSaver = null;
    }

    private List<Sale> filterListingAndBiddingEvent(List<Sale> allNewSale) {
        List<Sale> filteredListWithoutUnkeepedEvent = new ArrayList<>();

        for (Sale aSale : allNewSale) {
            if (aSale.getType() == SaleTypeEnum.NewList && theCurrentBot.isActivateListing()) {
                filteredListWithoutUnkeepedEvent.add(aSale);
            } else if (aSale.getType() == SaleTypeEnum.NewEnglishAuction && theCurrentBot.isActivateEnglishAuction()) {
                filteredListWithoutUnkeepedEvent.add(aSale);
            } else if (aSale.getType() == SaleTypeEnum.NewDutchAuction && theCurrentBot.isActivateDutchAuction()) {
                filteredListWithoutUnkeepedEvent.add(aSale);
            } else if (aSale.getType() == SaleTypeEnum.NewFloorOffer && theCurrentBot.isActivateFloorOffer()) {
                filteredListWithoutUnkeepedEvent.add(aSale);
            } else if (aSale.getType() == SaleTypeEnum.NewBidding && theCurrentBot.isActivateBidding()) {
                filteredListWithoutUnkeepedEvent.add(aSale);
            }
        }

        return filteredListWithoutUnkeepedEvent;
    }
}
