/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeDefault;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Ad.AdCampaign;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author david
 */
public class TelegramSocialNetwork extends SocialNetwork {

    private transient TelegramBot telegramInstance;

    private String channelId;

    private String apiKey;

    private String profileName;

    public TelegramSocialNetwork() {
        profileName = "";
    }
    
    public TelegramSocialNetwork(TelegramBot telegramInstance, String channelID) {
        this.telegramInstance = telegramInstance;
        this.channelId = channelID;
        profileName = "Generic bot";
    }

    public void initiateValue(String apiKey, String channelId, String profileName) {
        this.apiKey = apiKey;
        this.channelId = channelId;
        this.profileName = profileName;
    }

    @Override
    public SocialNetworkEnum getName() {
        return SocialNetworkEnum.Telegram;
    }

    public String getProfileName() {
        return profileName;
    }

    public void instanceTelegram() {
        this.telegramInstance = new TelegramBot(apiKey);
        
        DeleteMyCommands deleteCmds = new DeleteMyCommands();
        BaseResponse responseDelete = telegramInstance.execute(deleteCmds);
        
        SetMyCommands cmdsGeneral = new SetMyCommands(TelegramSocialNetworkFullCommands.getGeneralTelegramCommands(false));

        telegramInstance.execute(cmdsGeneral);
        TelegramSocialNetworkFullCommands.addUpdatesListener(telegramInstance);
    }

    public TelegramBot getTelegramInstance() {
        return telegramInstance;
    }

    
    
    public synchronized void send(BaseRequest message) throws Exception {
        telegramInstance.execute(message);

        TimeUnit.SECONDS.sleep(20);
    }

    @Override
    public CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract, String> contracts, Bot theBot, AdCampaign adCampaign) {
        return new ThreadTelegramMessage(mode, messageSaver, contracts, this, theBot, adCampaign);
    }

    public String getChannelId() {
        return channelId;
    }

    @Override
    public void start() throws Exception {
        sendStatusMessage("The bot is running !");
    }

    @Override
    public void stop() throws Exception {
        sendStatusMessage("The bot is no longer running");
    }

    @Override
    public void check() throws Exception {
        sendStatusMessage("Profile check !");
    }

    private void sendStatusMessage(String text) throws Exception {
        Instant currentHour = Instant.now();
        telegramInstance.execute(new SendMessage(channelId, currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : " + text));
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return profileName;
    }

    @Override
    public void turnOff() {
        telegramInstance.shutdown();
    }

    @Override
    public void instance() {
        instanceTelegram();
    }
    
}
