/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.SalesHistoryManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import twitter4j.TwitterException;

/**
 * Thread that get the data from the marketplace and share it on social network
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
     * if the bot is active
     * We get all the sale from marketplace
     * And we send them to socialNetwork
     */
    @Override
    public void run() {
        long numberOfSale = 0;
        
            if (model.getRowCount() >= 100){
                model.setRowCount(10);
            }
            
            HashMap<String, Sale> allNewSell = new HashMap<String, Sale>();
            List<MarketPlaceEnum> errorMarketPlaceToIgnore = new ArrayList<MarketPlaceEnum>();
            
            for(MarketPlace oneMarkeplace : marketplaces.values()){

                try {
                    numberOfSale = allNewSell.size();
                    allNewSell.putAll(oneMarkeplace.getCalledMarketPlace().query(mode, oneMarkeplace.getContracts(), oneMarkeplace.getLastrefresh()));
                    model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(),"OK" ,allNewSell.size() - numberOfSale });
                } catch (IOException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(),"Error : network issue" ,ex.getMessage()});
                    errorMarketPlaceToIgnore.add(oneMarkeplace.getMarketplace());
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (URISyntaxException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(),"Error : URI issue" ,ex.getMessage()});
                    errorMarketPlaceToIgnore.add(oneMarkeplace.getMarketplace());
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (InterruptedException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(),"Error : interrupted issue" ,ex.getMessage()});
                    errorMarketPlaceToIgnore.add(oneMarkeplace.getMarketplace());
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (Exception ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getMarketplace().toString(),"Error : unable to get sale" ,ex.getMessage()});
                    errorMarketPlaceToIgnore.add(oneMarkeplace.getMarketplace());
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                }
            }
            
            //Get only the new one
            List<Sale> filteredList = SalesHistoryManager.getSalesHistoryManager().checkNewSales(allNewSell.values(), this.mode, marketplaces);
            SalesHistoryManager.getSalesHistoryManager().removeOldestSales(this.mode, marketplaces, allNewSell, errorMarketPlaceToIgnore);
            
            //sort all the sales depend of configuration setting
            Collections.sort(filteredList);
            
            //Set the last successful refresh
            for(MarketPlace oneMarkeplace : marketplaces.values()){
                oneMarkeplace.setLastRefresh(filteredList, mode);
            }
            
            HashMap<String, Long> balance = null;
            try {
                balance = NetworkMessageManager.getMessageManager().getBalanceRoyaltyWallet(marketplaces,filteredList);
            } catch (IOException ex) {
                model.insertRow(0, new Object[]{"Royalty","Error : unable to get royalty" ,ex.getMessage()});
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            } catch (InterruptedException ex) {
                model.insertRow(0, new Object[]{"Royalty","Error : unable to get royalty" ,ex.getMessage()});
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            }
            
            HashMap<Sale, String> messageSaver = new HashMap<Sale,String>();
            for (SocialNetworkInterface oneSocialNetwork : this.socialNetworks){
                try {
                    oneSocialNetwork.send(filteredList, this.mode, messageSaver, balance);
                    model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(),"OK" ,filteredList.size()});
                } catch (TwitterException ex) {
                    model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(),"Error : unable to send a tweet" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (Exception ex) {
                    model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(),"Error : unable to send a message" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                }
            }
            
            messageSaver.clear();
            messageSaver = null;
    }
    
}
