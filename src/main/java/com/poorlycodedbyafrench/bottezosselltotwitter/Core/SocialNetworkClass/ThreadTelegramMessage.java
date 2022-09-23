/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

/**
 *
 * @author david
 */
public class ThreadTelegramMessage implements CreatorThreadSocialNetworkInterface{

    private BotModeEnum mode;
    
    private LinkedHashMap<Sale, String> messageSaver;
    
    private LinkedHashMap<Contract,String> contracts;
    
    private TelegramSocialNetwork telegram;
    
    public ThreadTelegramMessage(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract,String> contracts, TelegramSocialNetwork telegram) {
        this.mode = mode;
        this.messageSaver = messageSaver;
        this.contracts = contracts;
        this.telegram = telegram;
    }
    
    @Override
    public Object call() throws Exception {

        DecimalFormat df = new DecimalFormat("##.00");
        
        if (mode == BotModeEnum.Stat) {
            for (Contract contract : contracts.keySet()) {
                SendMessage statMessage = new SendMessage(telegram.getChannelId(), contracts.get(contract));
                
                telegram.send(statMessage);
            }
        }
        else if (mode == BotModeEnum.Sale) {
            for (Sale aSale : messageSaver.keySet()) {
                                
                BaseRequest saleMessage ;
                
                if (BotConfiguration.getConfiguration().isIpfs()) {    
                    saleMessage = new SendPhoto(telegram.getChannelId(),"https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                    saleMessage = ((SendPhoto)saleMessage).caption(messageSaver.get(aSale));
                }
                else{
                    saleMessage = new SendMessage(telegram.getChannelId(),messageSaver.get(aSale));
                }
                
                telegram.send(saleMessage);
            }
        }

        return SocialNetworkEnum.Telegram;
    }
    
}
