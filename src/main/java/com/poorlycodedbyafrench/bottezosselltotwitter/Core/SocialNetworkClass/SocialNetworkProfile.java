/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class SocialNetworkProfile {

    private DiscordSocialNetwork discord;

    private TelegramSocialNetwork telegram;

    private TwitterSocialNetwork twitter;

    private String name;

    public SocialNetworkProfile() {
        name = "";
    }

    public List<SocialNetworkInterface> getAllSocialNetwork() {

        List<SocialNetworkInterface> allSocialNetwork = new ArrayList<>();

        if (discord != null) {
            allSocialNetwork.add(discord);
        }

        if (twitter != null) {
            allSocialNetwork.add(twitter);
        }

        if (telegram != null) {
            allSocialNetwork.add(telegram);
        }

        return allSocialNetwork;
    }

    public String isUsed() {
        String allUseByBots = "";

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {
            if (aBot.getSnProfile() == this) {

                if (aBot.getBotStatus() == BotStatusEnum.Running || aBot.getBotStatus() == BotStatusEnum.Ready) {
                    if (!allUseByBots.equals("")) {
                        allUseByBots += ", ";
                    }

                    allUseByBots += aBot.getName();
                }
            }
        }

        return allUseByBots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasSubscriber() {
        return false;
//throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public DiscordSocialNetwork getDiscord() {
        return discord;
    }

    public void setDiscord(DiscordSocialNetwork discord) {
        this.discord = discord;
    }

    public TelegramSocialNetwork getTelegram() {
        return telegram;
    }

    public void setTelegram(TelegramSocialNetwork telegram) {
        this.telegram = telegram;
    }

    public TwitterSocialNetwork getTwitter() {
        return twitter;
    }

    public void setTwitter(TwitterSocialNetwork twitter) {
        this.twitter = twitter;
    }

    @Override
    public String toString() {
        return name;
    }

}
