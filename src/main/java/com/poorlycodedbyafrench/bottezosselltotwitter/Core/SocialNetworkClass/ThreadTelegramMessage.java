/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
    
    private Bot theBot;
    
    public ThreadTelegramMessage(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract,String> contracts, TelegramSocialNetwork telegram, Bot theBot) {
        this.mode = mode;
        this.messageSaver = messageSaver;
        this.contracts = contracts;
        this.telegram = telegram;
        this.theBot = theBot;
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
        else if (mode == BotModeEnum.Sale || mode == BotModeEnum.ListingAndBidding) {
            for (Sale aSale : messageSaver.keySet()) {
                                
                BaseRequest saleMessage ;
                boolean ipfsIsGoodSize = false;
                
                if(this.theBot.isIpfs()){
                    try {
                        URL newURL = new URL("https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                        URLConnection urlConn = newURL.openConnection();
                        urlConn.setConnectTimeout(10000);
                        urlConn.setReadTimeout(10000);
                        urlConn.setAllowUserInteraction(false);
                        urlConn.setDoOutput(true);

                        InputStream ipfsMedia = urlConn.getInputStream();
                                                
                        byte[] mediaIPFS = org.apache.commons.io.IOUtils.toByteArray(ipfsMedia);
                        
                        //I know it's ridiculous but I try to know the size of the picture
                        if (mediaIPFS.length < 5242880){ 
                            ipfsIsGoodSize = true;
                            ipfsMedia.close();
                            urlConn = null;
                            newURL = null;
                        }
                    }catch(Exception ex){
                        LogManager.getLogManager().writeLog(TelegramSocialNetwork.class.getName(), ex);
                    }
                }
                
                if (ipfsIsGoodSize && aSale.getType() != SaleTypeEnum.NewFloorOffer) {    
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
