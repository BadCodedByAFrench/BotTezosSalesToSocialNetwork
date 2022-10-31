/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.LinkedHashMap;

/**
 *
 * @author david
 */
public abstract class SocialNetwork implements SocialNetworkInterface {

    @Override
    public String isUsedByBots() {
        String allUseByBots = "";

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {
            for (SocialNetworkInterface socialNetwork : aBot.getSnProfile().getAllSocialNetwork()) {
                if (socialNetwork == this) {

                    if (aBot.getBotStatus() == BotStatusEnum.Running || aBot.getBotStatus() == BotStatusEnum.Ready) {
                        if (!allUseByBots.equals("")) {
                            allUseByBots += ", ";
                        }

                        allUseByBots += aBot.getName();
                    }
                }
            }
        }

        return allUseByBots;
    }

    @Override
    public String isUsedBySn() {
        String allUseBySn = "";

        for (SocialNetworkProfile aSocialNetworkProfile : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().values()) {
            for (SocialNetworkInterface socialNetwork : aSocialNetworkProfile.getAllSocialNetwork()) {
                if (socialNetwork == this) {

                    if (!allUseBySn.equals("")) {
                        allUseBySn += ", ";
                    }

                    allUseBySn += socialNetwork.getName();
                }
            }
        }

        return allUseBySn;
    }

    @Override
    public abstract SocialNetworkEnum getName();

    @Override
    public abstract String getProfileName();

    @Override
    public abstract void start() throws Exception;

    @Override
    public abstract void stop() throws Exception;

    @Override
    public abstract void check() throws Exception;

    @Override
    public abstract CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract, String> contracts, Bot theBot);
}
