/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot;

import java.util.HashMap;

/**
 *
 * @author david
 */
public class BotManager {

    private static BotManager botManager;

    private HashMap<String, Bot> allBots;

    private static Bot currentBot;

    public Bot getCurrentBot() {
        return currentBot;
    }

    public void setCurrentBot(Bot currentBot) {
        this.currentBot = currentBot;
    }

    private BotManager() {
        allBots = new HashMap<>();
    }

    public static BotManager getBotManager() {
        if (botManager == null) {
            botManager = new BotManager();
        }

        return botManager;
    }

    public void addBot(Bot newBot) {
        allBots.put(newBot.getName(), newBot);
    }

    public void removeBot(Bot removeBot) {
        allBots.remove(removeBot.getName());
    }

    public HashMap<String, Bot> getAllBots() {
        return allBots;
    }

    public void importBots (HashMap<String, Bot> allBots){
        this.allBots = allBots;
    }
}
