/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

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
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import twitter4j.Status;

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
     * Name of the social network
     */
    private SocialNetwork name;
    
    private boolean stat;
    
    private boolean sales;
    
    public TwitterSocialNetwork(){
        name = SocialNetwork.Twitter;
    }
    
    /**
     * We instance the connection with the twitter account and we set up the path name
     * @param publicConsumerKey
     * @param privateConsumerKey
     * @param publicAccessKey
     * @param privateAccessKey
     * @param contract 
     */
    public void instanceTwitter(String publicConsumerKey, String privateConsumerKey, String publicAccessKey, String privateAccessKey, boolean stat, boolean sales ){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey(publicConsumerKey)
        .setOAuthConsumerSecret(privateConsumerKey)
        .setOAuthAccessToken(publicAccessKey)
        .setOAuthAccessTokenSecret(privateAccessKey);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitterInstance = tf.getInstance();
        
        this.stat = stat;
        this.sales = sales;
    }
    
    @Override
    public void send(List<Sale> newSales)throws TwitterException, InterruptedException {
        
        int countAvoidTwitterDuplicate = 1;
        DecimalFormat df = new DecimalFormat("##.00");
        
        Instant previousUTCHour = Instant.now().minus(1, ChronoUnit.HOURS);
        
        if(this.stat){
            List<Contract> contracts = new ArrayList<Contract>();
            for(Sale aSale : newSales){
                
                boolean contractExist = false;
                
                for(Contract contract : contracts){  
                    if(contract.getContract().equals(aSale.getContract())){
                        contract.addSale(aSale);
                        contractExist = true;
                    }
                }
                
                if(!contractExist){
                    Contract newContract = new Contract(aSale);
                    contracts.add(newContract);
                }
            }
            
            for(Contract contract : contracts){  
                String status = "";
                
                status = countAvoidTwitterDuplicate + " Stat for " + contract.getName() + " since " +previousUTCHour.toString().substring(0,19) + " : ";
                status += "\nNumber of sales : " + contract.getNbSale();
                status += "\nTotal xtz : " + df.format(contract.getTotalprice());
                status += "\nMin price : " + df.format(contract.getMin());
                status += "\nMax price : " + df.format(contract.getMax());
                status += "\nAverage price : " + df.format(contract.getAvg());
                
                if(contract.getMarketplace().contains(MarketPlace.Objkt)){
                    status += "\nObjkt Link : " ;
                    if (contract.getPath()== null){
                        status += " https://objkt.com/collection/"+ contract.getContract();
                    }
                    else{
                        status += " https://objkt.com/collection/"+ contract.getPath();
                    }
                }
                
                status += " \n#tezos #nft ";
                
                for(MarketPlace m : contract.getMarketplace()){
                    status += " #" + m.toString();
                }
                
                if (contract.getPath() != null){
                    status += " #"+contract.getPath();
                }
                
                Status newTweet = twitterInstance.updateStatus(status);

                TimeUnit.MINUTES.sleep(1);
                
                countAvoidTwitterDuplicate++;
                    
                    
            }
        }
        
        if(this.sales){
            for(Sale aSale : newSales){

                if (countAvoidTwitterDuplicate <= 45){
                    String status = "";
                    status = countAvoidTwitterDuplicate + " " + aSale.getTimestamp().toString().substring(0, 19) + " : " + aSale.getType().getType() + " " + aSale.getName() + " has been sold for " + df.format(aSale.getPrice()) + " XTZ on " + aSale.getMarketplace().toString();

                    if (aSale.getMarketplace() == MarketPlace.Objkt){
                        if (aSale.getPathname() == null){
                            status += " \nhttps://objkt.com/asset/"+ aSale.getContract() + "/"+ aSale.getId();
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

    @Override
    public SocialNetwork getName() {
        return name;
    }
    
    
    
}
