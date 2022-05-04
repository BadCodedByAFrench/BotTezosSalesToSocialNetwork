/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotLastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.MainForm.ConfigurationMenu;
import java.util.Collections;
import java.util.HashMap;
import twitter4j.TwitterException;

/**
 * Thread that get the data from the marketplace and share it on social network
 * @author david
 */
public class SalesToSocialNetwork implements Runnable {

    /**
     * List of current sale
     */
    private List<Sale> newSell;
    
    /**
     * List of all the marketplace
     */
    private List<CallMarketPlaceInterface> marketplaces;
    
    /**
     * List of all the social network
     */
    private List<SocialNetworkInterface> socialNetworks;

    /**
     * Table of main window
     */
    private DefaultTableModel model;
        
    private int mode;
    
    public SalesToSocialNetwork(DefaultTableModel model, int mode) {
        this.newSell = new ArrayList<Sale>();
        this.marketplaces = new ArrayList<CallMarketPlaceInterface>();
        this.socialNetworks = new ArrayList<SocialNetworkInterface>();
        
        this.model = model;
        this.mode = mode;
    }

    public void setMarketplaces(List<CallMarketPlaceInterface> marketplaces) {
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
        
            if (model.getRowCount() >= 1000){
                model.setRowCount(500);
            }
            
            List<Sale> allNewSell = new ArrayList<Sale>();
            for(CallMarketPlaceInterface oneMarkeplace : marketplaces){

                try {
                    numberOfSale = allNewSell.size();
                    allNewSell.addAll(oneMarkeplace.query(mode));
                    model.insertRow(0, new Object[]{oneMarkeplace.getName().toString(),"OK" ,allNewSell.size() - numberOfSale });
                } catch (IOException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getName().toString(),"Error : network issue" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (URISyntaxException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getName().toString(),"Error : URI issue" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (InterruptedException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getName().toString(),"Error : interrupted issue" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                } catch (Exception ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getName().toString(),"Error : unable to get sale" ,ex.getMessage()});
                    LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
                }
            }
            
            //sort all the sales depend of configuration setting
            Collections.sort(allNewSell);
            
            //Set the last successful refresh
            BotLastRefresh.getLastRefresh().setLastRefresh(allNewSell, mode);
            
            HashMap<Sale, String> messageSaver = new HashMap<Sale,String>();
            for (SocialNetworkInterface oneSocialNetwork : this.socialNetworks){
                
                try {
                    oneSocialNetwork.send(allNewSell, this.mode, messageSaver);
                    model.insertRow(0, new Object[]{oneSocialNetwork.getName().toString(),"OK" ,allNewSell.size()});
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
