/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import javax.swing.table.DefaultTableModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Class that send sale to a discord channel
 * @author david
 */
public class DiscordSocialNetwork implements SocialNetworkInterface {

    /**
     * Name of the social network
     */
    private SocialNetwork name;

    /**
     * Class that connect to Discord API
     */
    private JDA jda;

    /**
     * Represent the text channel wjhere will send the message
     */
    private TextChannel textChannel;

    /**
     * Table of main window
     */
    private DefaultTableModel model;

    
    public DiscordSocialNetwork() {
        name = SocialNetwork.Discord;
    }

    /**
     * We build the connection to Discord thanks to the token and the name of the channel
     * @param token
     * @param channelName
     * @param model
     * @throws LoginException
     * @throws InterruptedException 
     */
    public void instanceDiscord(String token, String channelName, DefaultTableModel model) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(token.trim()).build().awaitReady();
        textChannel = jda.getTextChannelsByName(channelName.trim(), true).get(0);
        this.model = model;
    }

    @Override
    public synchronized void send(List<Sale> newSales, int mode, HashMap<Sale, String> messageSaver) throws Exception {
        int countAvoidTwitterDuplicate = 1;
        DecimalFormat df = new DecimalFormat("##.00");

        Instant previousUTCHour = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesStats(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshStats().toString().toUpperCase()));

        EmbedBuilder eb = new EmbedBuilder();

        if (mode == 1) {
            for (Contract contract : NetworkMessageManager.getMessageManager().createContractList(newSales)) {

                eb.setTitle("Stat for " + contract.getName());
                eb.setDescription(NetworkMessageManager.getMessageManager().createStatMessage(contract, df, previousUTCHour, countAvoidTwitterDuplicate));
                eb.setColor(Color.RED);
                textChannel.sendMessageEmbeds(eb.build()).queue();

                TimeUnit.SECONDS.sleep(20);

                //System.out.println(status);
                countAvoidTwitterDuplicate++;
            }
        }
        if (mode == 0) {

            Random rand = new Random();
            for (Sale aSale : newSales) {
                
                String message = "";
                
                if(messageSaver.containsKey(aSale)){
                    message = messageSaver.get(aSale);
                }
                else{
                    message = NetworkMessageManager.getMessageManager().createSaleMessage(aSale, df, rand, countAvoidTwitterDuplicate);
                    messageSaver.put(aSale, message);
                }
                
                eb.setTitle(aSale.getName() + " has been sold for " + df.format(aSale.getPrice()).replace(',', '.') + " XTZ");
                eb.setDescription(message);
                eb.setColor(Color.BLUE);

                if (BotConfiguration.getConfiguration().isIpfs()) {    
                    eb.setImage("https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                }
                
                textChannel.sendMessageEmbeds(eb.build()).queue();

                TimeUnit.SECONDS.sleep(20);

                //System.out.println(message);
                countAvoidTwitterDuplicate++;
            }
        }
    }

    @Override
    public SocialNetwork getName() {
        return name;
    }

    @Override
    public void start() throws Exception {
        Instant currentHour = Instant.now();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Start !");
        eb.setDescription(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is running !");
        eb.setColor(Color.WHITE);

        textChannel.sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public void stop() throws Exception {
        Instant currentHour = Instant.now();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Stop !");
        eb.setDescription(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is no longer running");
        eb.setColor(Color.black);

        textChannel.sendMessageEmbeds(eb.build()).queue();
    }
}
