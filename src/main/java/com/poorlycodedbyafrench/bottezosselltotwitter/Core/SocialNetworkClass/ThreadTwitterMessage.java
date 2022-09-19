/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.CreatorThreadSocialNetworkInterface;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Random;
import twitter4j.StatusUpdate;
import twitter4j.UploadedMedia;

/**
 *
 * @author david
 */
public class ThreadTwitterMessage implements CreatorThreadSocialNetworkInterface {

    private BotModeEnum mode;
    
    private LinkedHashMap<Sale, String> messageSaver;
    
    private LinkedHashMap<Contract,String> contracts;
    
    private TwitterSocialNetwork twitter;

    public ThreadTwitterMessage(BotModeEnum mode, LinkedHashMap<Sale, String> messageSaver, LinkedHashMap<Contract,String> contracts, TwitterSocialNetwork twitter) {
        this.mode = mode;
        this.messageSaver = messageSaver;
        this.contracts = contracts;
        this.twitter = twitter;
    }

    @Override
    public SocialNetworkEnum call() throws Exception {
        if (mode == BotModeEnum.Stat) {
            for (String contractMessage : contracts.values()) {
                twitter.send(new StatusUpdate(contractMessage));
            }
        }
        else if (mode == BotModeEnum.Sale) {

            Random rand = new Random();
            for (Sale aSale : messageSaver.keySet()) {

                
                StatusUpdate newStatus = new StatusUpdate(messageSaver.get(aSale));
                if (BotConfiguration.getConfiguration().isIpfs()) {
                    try {
                        URL newURL = new URL("https://cloudflare-ipfs.com/" + aSale.getIpfs().replace(":/", ""));
                        URLConnection urlConn = newURL.openConnection();
                        urlConn.setConnectTimeout(10000);
                        urlConn.setReadTimeout(10000);
                        urlConn.setAllowUserInteraction(false);
                        urlConn.setDoOutput(true);

                        InputStream ipfsMedia = urlConn.getInputStream();
                        
                        String ipfsName = aSale.getPathname() == null ? aSale.getName() : aSale.getPathname();
                        
                        UploadedMedia media = twitter.uploadAMedia(ipfsName, ipfsMedia);
                        long mediaId = media.getMediaId();

                        newStatus.setMediaIds(mediaId);
                        ipfsMedia.close();
                        urlConn = null;
                        newURL = null;
                    } catch (Exception ex) {
                        LogManager.getLogManager().writeLog(TwitterSocialNetwork.class.getName(), ex);
                    }
                }
                twitter.send(newStatus);
            }
        }
        
        return SocialNetworkEnum.Twitter;
    }
    
}
