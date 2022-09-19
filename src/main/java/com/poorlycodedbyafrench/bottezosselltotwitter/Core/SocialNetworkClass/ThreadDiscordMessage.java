/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 *
 * @author david
 */
public class ThreadDiscordMessage implements CreatorThreadSocialNetworkInterface {

    private BotModeEnum mode;
    
    private LinkedHashMap<Sale, String> messageSaver;
    
    private LinkedHashMap<Contract,String> contracts;
    
    private DiscordSocialNetwork discord;

    public ThreadDiscordMessage(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract,String> contracts, DiscordSocialNetwork discord) {
        this.mode = mode;
        this.messageSaver = messageSaver;
        this.contracts = contracts;
        this.discord = discord;
    }
        
    
    @Override
    public SocialNetworkEnum call() throws Exception {
        
        EmbedBuilder eb = new EmbedBuilder();
        DecimalFormat df = new DecimalFormat("##.00");
        
        if (mode == BotModeEnum.Stat) {
            for (Contract contract : contracts.keySet()) {

                eb.setTitle("Stat for " + contract.getName());
                eb.setDescription(contracts.get(contract));
                eb.setColor(Color.RED);
                
                discord.send(eb);
            }
        }
        else if (mode == BotModeEnum.Sale) {

            Random rand = new Random();
            for (Sale aSale : messageSaver.keySet()) {
                                
                eb.setTitle(aSale.getName() + " has been sold for " + df.format(aSale.getPrice()).replace(',', '.') + " XTZ");
                eb.setDescription(messageSaver.get(aSale));
                eb.setColor(Color.BLUE);

                if (BotConfiguration.getConfiguration().isIpfs()) {    
                    eb.setImage("https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                }
                
                discord.send(eb);
            }
        }

        return SocialNetworkEnum.Discord;
    }
    
}
