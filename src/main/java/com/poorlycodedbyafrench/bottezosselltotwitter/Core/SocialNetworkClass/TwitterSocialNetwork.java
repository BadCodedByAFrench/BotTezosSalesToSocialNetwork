/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.concurrent.TimeUnit;

/**
 * Represent Twitter
 * @author david
 */
public class TwitterSocialNetwork implements SocialNetworkInterface{

    /**
     * Twitter instance bind to an acoount 
     */
    
    private Twitter twitterInstance;
    
    /**
     * Contract of the collection
     */
    private String contract;
    
    /**
     * We instance the connection with the twitter account and we set up the path name
     * @param publicConsumerKey
     * @param privateConsumerKey
     * @param publicAccessKey
     * @param privateAccessKey
     * @param contract 
     */
    public void instanceTwitter(String publicConsumerKey, String privateConsumerKey, String publicAccessKey, String privateAccessKey, String contract ){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey(publicConsumerKey)
        .setOAuthConsumerSecret(privateConsumerKey)
        .setOAuthAccessToken(publicAccessKey)
        .setOAuthAccessTokenSecret(privateAccessKey);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitterInstance = tf.getInstance();
        
        this.contract = contract;
    }
    
    @Override
    public void send(List<Sale> newSales)throws TwitterException, InterruptedException {
        
        int countAvoidTwitterDuplicate = 1;
        
        for(Sale aSale : newSales){
            
            if (countAvoidTwitterDuplicate <= 45){
                String status = "";
                status = countAvoidTwitterDuplicate + " " + aSale.getTimestamp().substring(0, 19) + " : " + aSale.getName() + " has been sold for " + aSale.getPrice() + " XTZ on " + aSale.getMarketplace();

                if (aSale.getMarketplace().equals("Objkt")){
                    if (aSale.getPathname() == null){
                        status += " \nhttps://objkt.com/asset/"+ this.contract + "/"+ aSale.getId();
                    }
                    else{
                        status += " \nhttps://objkt.com/asset/"+ aSale.getPathname() + "/"+ aSale.getId();
                    }

                }

                status += " \n#tezos #nft #"+aSale.getMarketplace();

                if (aSale.getPathname() != null){
                    status += " #"+aSale.getPathname();
                }

                Status newTweet = twitterInstance.updateStatus(status);

                TimeUnit.MINUTES.sleep(1);
                countAvoidTwitterDuplicate++;
            }
        }
    }
    
}
