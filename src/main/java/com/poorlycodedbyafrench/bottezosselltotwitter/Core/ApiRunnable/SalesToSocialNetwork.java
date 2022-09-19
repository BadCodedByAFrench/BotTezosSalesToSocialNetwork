/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.SalesHistoryManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Thread that get the data from the marketplace and share it on social network
 *
 * @author david
 */
public class SalesToSocialNetwork implements Runnable {

    /**
     * List of all the marketplace
     */
    private HashMap<MarketPlaceEnum, MarketPlace> marketplaces;

    /**
     * List of all the social network
     */
    private List<SocialNetworkInterface> socialNetworks;

    /**
     * Table of main window
     */
    private DefaultTableModel model;

    private BotModeEnum mode;

    public SalesToSocialNetwork(DefaultTableModel model, BotModeEnum mode) {
        this.marketplaces = new HashMap<MarketPlaceEnum, MarketPlace>();
        this.socialNetworks = new ArrayList<SocialNetworkInterface>();

        this.model = model;
        this.mode = mode;
    }

    public void setMarketplaces(HashMap<MarketPlaceEnum, MarketPlace> marketplaces) {
        this.marketplaces = marketplaces;
    }

    public void setSocialNetworks(List<SocialNetworkInterface> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    /**
     * if the bot is active We get all the sale from marketplace And we send
     * them to socialNetwork
     */
    @Override
    public void run() {
        long numberOfSale = 0;

        if (model.getRowCount() >= 100) {
            model.setRowCount(10);
        }

        HashMap<String, Sale> allNewSell = new HashMap<String, Sale>();
        ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck = new ArrayList<MarketPlaceEnum>();

        ExecutorService multithreadMarketPlace = Executors.newFixedThreadPool(marketplaces.values().size());
        List<Future<HashMap<MarketPlaceEnum,HashMap<String, Sale>>>> allMarketPlaceFutures = new ArrayList<>();
        
        for (MarketPlace oneMarkeplace : marketplaces.values()) {

            Future<HashMap<MarketPlaceEnum,HashMap<String, Sale>>> futurNewSales = multithreadMarketPlace.submit(oneMarkeplace.getCalledMarketPlace().callMarketPlace(mode, oneMarkeplace.getContracts(), oneMarkeplace.getLastrefresh()));
            allMarketPlaceFutures.add(futurNewSales);
        }
        
        for(Future<HashMap<MarketPlaceEnum,HashMap<String, Sale>>> aFutur : allMarketPlaceFutures){
            try {
                HashMap<MarketPlaceEnum,HashMap<String, Sale>> newSalesMarketPlace = aFutur.get();
                
                for(MarketPlaceEnum aMarketPlace : newSalesMarketPlace.keySet()){
                    allNewSell.putAll(newSalesMarketPlace.get(aMarketPlace));
                    model.insertRow(0, new Object[]{aMarketPlace.toString(), "OK", newSalesMarketPlace.get(aMarketPlace).size()});
                    finishedMarketPlaceToCheck.add(aMarketPlace);
                }
            } catch (ExecutionException ex) {
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            } catch (Exception ex) {
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            }
        }

        multithreadMarketPlace.shutdown();
        
        for(MarketPlace oneMarkeplace : marketplaces.values()){
            if(!finishedMarketPlaceToCheck.contains(oneMarkeplace.getMarketplace())){
                model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(), "Failed : send logs to dev", "Log file at the parent folder of your \".exe\" location "});
            }
        }

        //Get only the new one
        List<Sale> filteredList = SalesHistoryManager.getSalesHistoryManager().checkNewSales(allNewSell.values(), this.mode, marketplaces);
        SalesHistoryManager.getSalesHistoryManager().removeOldestSales(this.mode, marketplaces, allNewSell, finishedMarketPlaceToCheck);

        //sort all the sales depend of configuration setting
        Collections.sort(filteredList);

        //Set the last successful refresh
        for (MarketPlace oneMarkeplace : marketplaces.values()) {
            oneMarkeplace.setLastRefresh(filteredList, mode);
        }

        HashMap<String, Long> balance = null;
        try {
            balance = NetworkMessageManager.getMessageManager().getBalanceRoyaltyWallet(marketplaces, filteredList);
        } catch (IOException ex) {
            model.insertRow(0, new Object[]{"Royalty", "Error : unable to get royalty", ex.getMessage()});
            LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
        } catch (InterruptedException ex) {
            model.insertRow(0, new Object[]{"Royalty", "Error : unable to get royalty", ex.getMessage()});
            LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
        }

        LinkedHashMap<Sale, String> messageSaver = new LinkedHashMap<Sale, String>();
        LinkedHashMap<Contract,String> messageContractsSaver = new LinkedHashMap<Contract,String>();

        DecimalFormat df = new DecimalFormat("##.00");
        Random rand = new Random();
                
        if(this.mode == BotModeEnum.Sale){
            int countSale = 1;
            for (Sale aSale : filteredList) {
                messageSaver.put(aSale, NetworkMessageManager.getMessageManager().createSaleMessage(aSale, df, rand, countSale, balance));
                countSale++;
            }
        }
        else{
            Instant previousUTCHour = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesStats(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshStats().toString().toUpperCase()));
            int countStat = 1;
            
            for (Contract contract : NetworkMessageManager.getMessageManager().createContractList(filteredList)){
                messageContractsSaver.put(contract, NetworkMessageManager.getMessageManager().createStatMessage(contract, df, previousUTCHour,countStat, balance));
                countStat++;
            }            
        }

        ExecutorService multithreadSocialNetwork = Executors.newFixedThreadPool(socialNetworks.size());
        ArrayList<Future<SocialNetworkEnum>> allFutureSocialNetwork = new ArrayList<>();
        ArrayList<SocialNetworkEnum> successfullSocialNetwork = new ArrayList<>();
        
        for (SocialNetworkInterface oneSocialNetwork : this.socialNetworks) {

            Future<SocialNetworkEnum> futureSocialNetwork = multithreadSocialNetwork.submit(oneSocialNetwork.createThreadSocialNetwork(mode, messageSaver, messageContractsSaver));
            allFutureSocialNetwork.add(futureSocialNetwork);
        }

        for(Future<SocialNetworkEnum> futureSocialNetwork :allFutureSocialNetwork ){
            try {
                    SocialNetworkEnum theSocialNetwork = futureSocialNetwork.get();
                    successfullSocialNetwork.add(theSocialNetwork);
                    model.insertRow(0, new Object[]{theSocialNetwork.toString(), "OK", filteredList.size()});
                } catch (ExecutionException ex) {
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (Exception ex) {
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                }
        }
        
        multithreadSocialNetwork.shutdown();
        
        for(SocialNetworkInterface oneSocialNetwork : this.socialNetworks){
            if(!successfullSocialNetwork.contains(oneSocialNetwork.getName())){
                model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(), "Failed : send logs to dev", "Log file at the parent folder of your \".exe\" location "});
            }
        }
        
        messageSaver.clear();
        messageContractsSaver.clear();
        messageSaver = null;
        messageContractsSaver = null;
    }

}
