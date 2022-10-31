/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.LinkedHashMap;

/**
 * Interface that should be use by all of the SocialNetworkEnum
 * @author david
 */
public interface SocialNetworkInterface {
    
     /**
     * Get the name of the social network
     * @return 
     */
    public SocialNetworkEnum getName();
    
    public String getProfileName();
    
    /**
     * Function to say that the bot is starting
     */
    public void start() throws Exception;
    
    /**
     * Function to say that the bot is stopping
     */
    public void stop()throws Exception;
    
    public void check() throws Exception ;
    
    public String isUsedByBots();
    
    public String isUsedBySn();
    
    public void turnOff();
    
    public void instance();
    
    public CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract,String> contracts, Bot theBot);
}
