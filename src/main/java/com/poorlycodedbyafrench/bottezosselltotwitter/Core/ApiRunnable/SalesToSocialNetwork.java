/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.MainForm.MainBotForm;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
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
    
    /**
     * Know if the bot should run
     */
    private boolean isActive;
    
    public SalesToSocialNetwork(DefaultTableModel model) {
        this.newSell = new ArrayList<Sale>();
        this.marketplaces = new ArrayList<CallMarketPlaceInterface>();
        this.socialNetworks = new ArrayList<SocialNetworkInterface>();
        
        this.model = model;
        this.isActive = false;
    }

    public void setMarketplaces(List<CallMarketPlaceInterface> marketplaces) {
        this.marketplaces = marketplaces;
    }

    public void setSocialNetworks(List<SocialNetworkInterface> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
    /**
     * if the bot is active
     * We get all the sale from marketplace
     * And we send them to socialNetwork
     */
    @Override
    public void run() {
        long numberOfSale = 0;
        
        if(this.isActive){
            
            if (model.getRowCount() >= 72){
                model.setRowCount(24);
            }
            
            List<Sale> allNewSell = new ArrayList<Sale>();
            for(CallMarketPlaceInterface oneMarkeplace : marketplaces){

                try {
                    numberOfSale = allNewSell.size();
                    allNewSell.addAll(oneMarkeplace.query());
                    model.insertRow(0, new Object[]{oneMarkeplace.getClass().getSimpleName(),"OK" ,allNewSell.size() - numberOfSale });
                } catch (IOException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getClass().getSimpleName(),"Error : network issue" ,ex.getMessage()});
                } catch (URISyntaxException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getClass().getSimpleName(),"Error : URI issue" ,ex.getMessage()});
                } catch (InterruptedException ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getClass().getSimpleName(),"Error : interrupted issue" ,ex.getMessage()});;
                } catch (Exception ex) {
                    model.insertRow(0, new Object[]{oneMarkeplace.getClass().getSimpleName(),"Error : unable to get sale" ,ex.getMessage()});
                    Logger.getLogger(SalesToSocialNetwork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            for (SocialNetworkInterface oneSocialNetwork : this.socialNetworks){
                
                try {
                    oneSocialNetwork.send(allNewSell);
                    model.insertRow(0, new Object[]{oneSocialNetwork.getClass().getSimpleName(),"OK" ,allNewSell.size()});
                } catch (TwitterException ex) {
                    model.insertRow(0, new Object[]{oneSocialNetwork.getClass().getSimpleName(),"Error : unable to send a tweet" ,ex.getMessage()});
                    Logger.getLogger(SalesToSocialNetwork.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    model.insertRow(0, new Object[]{oneSocialNetwork.getClass().getSimpleName(),"Error : unable to send a tweet" ,ex.getMessage()});
                    Logger.getLogger(SalesToSocialNetwork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
