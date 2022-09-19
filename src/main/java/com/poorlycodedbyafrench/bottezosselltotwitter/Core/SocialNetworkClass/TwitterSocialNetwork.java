/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.util.List;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import javax.swing.table.DefaultTableModel;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.UploadedMedia;

/**
 * Represent Twitter
 *
 * @author david
 */
public class TwitterSocialNetwork implements SocialNetworkInterface {

    /**
     * Twitter instance bind to an acoount
     */
    private Twitter twitterInstance;

    /**
     * Name of the social network
     */
    private SocialNetworkEnum name;

    public TwitterSocialNetwork() {
        name = SocialNetworkEnum.Twitter;
    }

    /**
     * Table of main window
     */
    private DefaultTableModel model;

    /**
     * We instance the connection with the twitter account and we set up the
     * path name
     *
     * @param publicConsumerKey
     * @param privateConsumerKey
     * @param publicAccessKey
     * @param privateAccessKey
     * @param contract
     */
    public void instanceTwitter(String publicConsumerKey, String privateConsumerKey, String publicAccessKey, String privateAccessKey, DefaultTableModel model) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(publicConsumerKey)
                .setOAuthConsumerSecret(privateConsumerKey)
                .setOAuthAccessToken(publicAccessKey)
                .setOAuthAccessTokenSecret(privateAccessKey);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitterInstance = tf.getInstance();

        this.model = model;
    }

    public synchronized void send(StatusUpdate newStatus) throws TwitterException, InterruptedException, MalformedURLException, IOException {
        Status newTweet = twitterInstance.updateStatus(newStatus);
        TimeUnit.MINUTES.sleep(1);
    }
    
    public synchronized UploadedMedia uploadAMedia (String ipfsName, InputStream ipfsMedia) throws Exception {
        return twitterInstance.uploadMedia(ipfsName, ipfsMedia);
    }

    @Override
    public SocialNetworkEnum getName() {
        return name;
    }

    @Override
    public void start() throws TwitterException {
        Instant currentHour = Instant.now();

        twitterInstance.updateStatus(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is running !");
    }

    @Override
    public void stop() throws TwitterException {
        Instant currentHour = Instant.now();

        twitterInstance.updateStatus(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : The bot is no longer running");
    }

    @Override
    public CreatorThreadSocialNetworkInterface createThreadSocialNetwork(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract, String> contracts) {
        return new ThreadTwitterMessage(mode, messageSaver,  contracts, this);
    }

}
