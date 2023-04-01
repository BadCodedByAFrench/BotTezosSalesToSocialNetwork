/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import java.util.HashMap;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author david
 */
public class GenericBot {

    private HashMap<String, Bot> botsForTheServer;

    private String id;

    private transient TextChannel discordHome;
    
    private String channelId;
    
    private SocialNetworkEnum origin;
    
    private String idDiscordChannel;
    
    private int countMessage;

    public GenericBot(String id, SocialNetworkEnum origin) {
        this.id = id;
        this.origin = origin;
        this.botsForTheServer = new HashMap<>();
        this.countMessage = 0;
    }

    public void addCount(int nbToAdd){
        countMessage += nbToAdd;
    }
    
    public SocialNetworkEnum getOrigin() {
        return origin;
    }
    
    public String getId() {
        return id;
    }

    public TextChannel getDiscordHome() {
        return discordHome;
    }

    public void setDiscordHome(TextChannel discordHome) {
        this.discordHome = discordHome;
        if(discordHome != null){
            this.idDiscordChannel = this.discordHome.getId();
        }
        else{
            this.idDiscordChannel = "";
        }
    }

    public String getIdDiscordChannel() {
        return idDiscordChannel;
    }
    
    
    
    public String getChannelId() {
        return channelId;
    }
    
    
    
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public HashMap<String, Bot> getBotsForTheServer() {
        return botsForTheServer;
    }

    public void addBot(String name) {
        botsForTheServer.put(name, new Bot(name, BotTypeEnum.Generic, id));
    }

    public void removeBot(String name) {
        botsForTheServer.remove(name);
    }

    public int getCountMessage() {
        return countMessage;
    }
    
    
}
