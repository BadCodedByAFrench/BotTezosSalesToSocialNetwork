/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author david
 */
public class TelegramSocialNetwork implements SocialNetworkInterface{

    private TelegramBot telegramInstance;
    
    private String channelId;
    
    @Override
    public SocialNetworkEnum getName() {
        return SocialNetworkEnum.Telegram;
    }
    
    public void instanceTelegram(String apiKey, String channelId) {
        this.telegramInstance = new TelegramBot(apiKey);
        this.channelId = channelId;
    }
    
    public synchronized void send(BaseRequest message) throws Exception {       
        telegramInstance.execute(message);

        TimeUnit.SECONDS.sleep(20);
    }
    @Override
    public void start() throws Exception {
        Instant currentHour = Instant.now();
        telegramInstance.execute(new SendMessage(channelId, currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is running !"));
    }

    @Override
    public void stop() throws Exception {
        Instant currentHour = Instant.now();
        telegramInstance.execute(new SendMessage(channelId, currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is no longer running"));
    }

    @Override
    public CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract, String> contracts) {
        return new ThreadTelegramMessage(mode, messageSaver,  contracts, this);
    }

    public String getChannelId() {
        return channelId;
    }
 
    
}
