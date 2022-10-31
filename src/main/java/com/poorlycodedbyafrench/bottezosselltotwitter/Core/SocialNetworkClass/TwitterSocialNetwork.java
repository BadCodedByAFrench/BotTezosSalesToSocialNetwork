/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.UploadedMedia;

/**
 * Represent Twitter
 *
 * @author david
 */
public class TwitterSocialNetwork extends SocialNetwork {

    /**
     * Twitter instance bind to an acoount
     */
    private transient Twitter twitterInstance;

    /**
     * Name of the social network
     */
    private SocialNetworkEnum name;

    private String publicConsumerKey;

    private String privateConsumerKey;

    private String publicAccessKey;

    private String privateAccessKey;

    private String profileName;

    public TwitterSocialNetwork() {
        name = SocialNetworkEnum.Twitter;
        profileName = "";
    }

    public void initiateValue(String publicConsumerKey, String privateConsumerKey, String publicAccessKey, String privateAccessKey, String profileName) {
        this.publicConsumerKey = publicConsumerKey;
        this.privateConsumerKey = privateConsumerKey;
        this.publicAccessKey = publicAccessKey;
        this.privateAccessKey = privateAccessKey;
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }

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
    public void instanceTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(publicConsumerKey)
                .setOAuthConsumerSecret(privateConsumerKey)
                .setOAuthAccessToken(publicAccessKey)
                .setOAuthAccessTokenSecret(privateAccessKey);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitterInstance = tf.getInstance();
    }

    public synchronized void send(StatusUpdate newStatus) throws TwitterException, InterruptedException, MalformedURLException, IOException {
        Status newTweet = twitterInstance.updateStatus(newStatus);
        TimeUnit.MINUTES.sleep(1);
    }

    public synchronized UploadedMedia uploadAMedia(String ipfsName, InputStream ipfsMedia) throws Exception {
        return twitterInstance.uploadMedia(ipfsName, ipfsMedia);
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
        return new ThreadTwitterMessage(mode, messageSaver, contracts, this, theBot);
    }

    @Override
    public void check() throws Exception {
        sendStatusMessage("Profile check !");
    }

    private void sendStatusMessage(String text) throws Exception {
        Instant currentHour = Instant.now();
        twitterInstance.updateStatus(currentHour.toString().substring(5, 7) + "-" + currentHour.toString().substring(8, 10) + "-" + currentHour.toString().substring(0, 4) + " at " + currentHour.toString().substring(11, 16) + " UTC : " + text);
    }

    public String getPublicConsumerKey() {
        return publicConsumerKey;
    }

    public String getPrivateConsumerKey() {
        return privateConsumerKey;
    }

    public String getPublicAccessKey() {
        return publicAccessKey;
    }

    public String getPrivateAccessKey() {
        return privateAccessKey;
    }

    @Override
    public String toString() {
        return profileName;
    }

    @Override
    public void turnOff() {
        //Didn't found anything about how kill a Twitter Class so I suppose there's nothing to do and once it's in garbage collector it's OK....;
    }

    @Override
    public void instance() {
        instanceTwitter();
    }
}
