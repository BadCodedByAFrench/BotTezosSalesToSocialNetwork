/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.awt.Color;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Class that send sale to a discord channel
 *
 * @author david
 */
public class DiscordSocialNetwork extends SocialNetwork {

    /**
     * Name of the social network
     */
    private SocialNetworkEnum name;

    /**
     * Class that connect to Discord API
     */
    private transient JDA jda;

    /**
     * Represent the text channel wjhere will send the message
     */
    private transient TextChannel textChannel;

    private String token;

    private String channelName;

    private String profileName;

    public DiscordSocialNetwork() {
        name = SocialNetworkEnum.Discord;
        profileName = "";
    }

    public void initiateValue(String token, String channelName, String profileName) {
        this.token = token;
        this.channelName = channelName;
        this.profileName = profileName;
    }

    /**
     * We build the connection to Discord thanks to the token and the name of
     * the channel
     *
     * @param token
     * @param channelName
     * @param model
     * @throws LoginException
     * @throws InterruptedException
     */
    public void instanceDiscord() throws LoginException, InterruptedException, Exception {
        jda = JDABuilder.createDefault(token.trim()).build().awaitReady();
        List<TextChannel> allTextChannels = jda.getTextChannels();

        for (TextChannel oneTextChannel : allTextChannels) {

            if (textChannel == null && oneTextChannel.getName().toLowerCase().contains(channelName.trim().toLowerCase())) {
                textChannel = oneTextChannel;
            }
        }

        if (textChannel == null) {
            throw new Exception("The channel do not exist");
        }
    }

    public synchronized void send(EmbedBuilder eb) throws Exception {

        textChannel.sendMessageEmbeds(eb.build()).queue();

        TimeUnit.SECONDS.sleep(20);
    }

    @Override
    public SocialNetworkEnum getName() {
        return name;
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
    public CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract, String> contracts, Bot theBot) {
        return new ThreadDiscordMessage(mode, messageSaver, contracts, this, theBot);
    }

    @Override
    public void check() throws Exception {
        sendStatusMessage("Profile check !");
    }

    private void sendStatusMessage(String text) throws Exception {
        Instant currentHour = Instant.now();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(text);
        eb.setDescription(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : " + text);
        eb.setColor(Color.black);

        textChannel.sendMessageEmbeds(eb.build()).queue();
    }

    public String getToken() {
        return token;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public String getProfileName() {
        return profileName;
    }

    @Override
    public String toString() {
        return profileName;
    }

    @Override
    public void turnOff() {
        jda.shutdown();
    }

    @Override
    public void instance() {
        try {
            instanceDiscord();
        } catch (InterruptedException ex) {
            LogManager.getLogManager().writeLog(DiscordSocialNetwork.class.getName(), ex);
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(DiscordSocialNetwork.class.getName(), ex);
        }
    }
}
