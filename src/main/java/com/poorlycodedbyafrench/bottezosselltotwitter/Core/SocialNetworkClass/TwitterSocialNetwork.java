/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.NetworkMessageManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.List;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
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
    private SocialNetwork name;

    public TwitterSocialNetwork() {
        name = SocialNetwork.Twitter;
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

    @Override
    public synchronized void send(List<Sale> newSales, int mode) throws TwitterException, InterruptedException, MalformedURLException, IOException {
        int countAvoidTwitterDuplicate = 1;
        DecimalFormat df = new DecimalFormat("##.00");

        Instant previousUTCHour = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesStats(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshStats().toString().toUpperCase()));

        if (mode == 1) {
            for (Contract contract : NetworkMessageManager.getMessageManager().createContractList(newSales)) {

                Status newTweet = twitterInstance.updateStatus(NetworkMessageManager.getMessageManager().createStatMessage(contract, df, previousUTCHour, countAvoidTwitterDuplicate));

                TimeUnit.MINUTES.sleep(1);

                //System.out.println(status);
                countAvoidTwitterDuplicate++;
            }
        }
        if (mode == 0) {

            Random rand = new Random();
            for (Sale aSale : newSales) {

                StatusUpdate newStatus = new StatusUpdate(NetworkMessageManager.getMessageManager().createSaleMessage(aSale, df, rand, countAvoidTwitterDuplicate));
                if (BotConfiguration.getConfiguration().isIpfs()) {
                    try {
                        URL newURL = new URL("https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                        URLConnection urlConn = newURL.openConnection();
                        urlConn.setConnectTimeout(10000);
                        urlConn.setReadTimeout(10000);
                        urlConn.setAllowUserInteraction(false);
                        urlConn.setDoOutput(true);

                        InputStream ipfsMedia = urlConn.getInputStream();
                        UploadedMedia media = twitterInstance.uploadMedia(aSale.getPathname(), ipfsMedia);
                        long mediaId = media.getMediaId();

                        newStatus.setMediaIds(mediaId);
                        ipfsMedia.close();
                        urlConn = null;
                        newURL = null;
                    } catch (Exception ex) {
                        model.insertRow(0, new Object[]{this.getName().toString(), "Error : unable to send a tweet", ex.getMessage()});
                        LogManager.getLogManager().writeLog(TwitterSocialNetwork.class.getName(), ex);
                    }
                }
                Status newTweet = twitterInstance.updateStatus(newStatus);

                TimeUnit.MINUTES.sleep(1);

                //System.out.println(status);
                countAvoidTwitterDuplicate++;
            }
        }
    }

    @Override
    public SocialNetwork getName() {
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

}
