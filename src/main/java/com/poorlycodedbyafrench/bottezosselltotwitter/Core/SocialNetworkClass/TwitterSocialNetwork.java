/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.UploadedMedia;

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
    
    /**
     * True if we have activate stat mode
     */
    private boolean stat;
    
    /**
     * True if we have activate sales mode
     */
    
    private boolean sales;
    
    public TwitterSocialNetwork(){
        name = SocialNetwork.Twitter;
    }
    
    /**
     * Table of main window
     */
    private DefaultTableModel model;
    
    /**
     * We instance the connection with the twitter account and we set up the path name
     * @param publicConsumerKey
     * @param privateConsumerKey
     * @param publicAccessKey
     * @param privateAccessKey
     * @param contract 
     */
    public void instanceTwitter(String publicConsumerKey, String privateConsumerKey, String publicAccessKey, String privateAccessKey, boolean stat, boolean sales, DefaultTableModel model ){
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
        this.model = model;
    }
    
    @Override
    public synchronized void send(List<Sale> newSales, int mode)throws TwitterException, InterruptedException, MalformedURLException, IOException {
        
        int countAvoidTwitterDuplicate = 1;
        DecimalFormat df = new DecimalFormat("##.00");
        
        Instant previousUTCHour = Instant.now().minus(BotConfiguration.getConfiguration().getRefreshSalesStats(), ChronoUnit.valueOf(BotConfiguration.getConfiguration().getRefreshStats().toString().toUpperCase()));
        
        if(this.stat && mode == 1){
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
                
                if(BotConfiguration.getConfiguration().isSecurityIdStats()){
                    status += countAvoidTwitterDuplicate +" ";
                }
                
                status += "Stat for " + contract.getName() + " since " +previousUTCHour.toString().substring(5, 7) + "-"+ previousUTCHour.toString().substring(8, 10) + "-" + previousUTCHour.toString().substring(0, 4) + " at " + previousUTCHour.toString().substring(11, 16) + " UTC : ";
                status += "\nNumber of sales : " + contract.getNbSale();
                status += "\nTotal xtz : " + df.format(contract.getTotalprice()).replace(',', '.');
                
                if(BotConfiguration.getConfiguration().isMinPriceStat()){
                    status += "\nMin price : " + df.format(contract.getMin()).replace(',', '.');
                }
                
                if(BotConfiguration.getConfiguration().isMaxPriceStat()){
                    status += "\nMax price : " + df.format(contract.getMax()).replace(',', '.');
                }
                
                if(BotConfiguration.getConfiguration().isAvgPriceStat()){
                    status += "\nAverage price : " + df.format(contract.getAvg()).replace(',', '.');
                }
                
                if(contract.getMarketplace().contains(MarketPlace.Objkt)){
                    status += "\nObjkt Link : " ;
                    if (contract.getPath()== null){
                        status += " https://objkt.com/collection/"+ contract.getContract();
                    }
                    else{
                        status += " https://objkt.com/collection/"+ contract.getPath();
                    }
                }
                
                if(BotConfiguration.getConfiguration().getHashtags().size() > 0 ){
                    status += "\n";
                }
                
                for (String hashtag : BotConfiguration.getConfiguration().getHashtags()){
                    status += "#" + hashtag + " ";
                }   
                
                for(MarketPlace m : contract.getMarketplace()){
                    status += " #" + m.toString();
                }
                
                if (contract.getPath() != null){
                    status += " #"+contract.getPath();
                }
                
                Status newTweet = twitterInstance.updateStatus(status);

                TimeUnit.MINUTES.sleep(1);
                
                //System.out.println(status);
                                    
                countAvoidTwitterDuplicate++;
                    
                    
            }
        }
        if(this.sales && mode == 0){
            
            Random rand = new Random();
            for(Sale aSale : newSales){

                    String status = "";
                    if(BotConfiguration.getConfiguration().isSecurityIdSales()){
                        status += countAvoidTwitterDuplicate +" ";
                    }
                    
                    if(BotConfiguration.getConfiguration().getSentences().size() > 0){
                        status += BotConfiguration.getConfiguration().getSentences().get(rand.nextInt(BotConfiguration.getConfiguration().getSentences().size()));
                    }
                    
                    if(BotConfiguration.getConfiguration().isSaleType()){
                        status += " : " + aSale.getType().getType();
                    }
                     
                    
                    status += " " + aSale.getName() + " has been sold for " + df.format(aSale.getPrice()).replace(',', '.') + " XTZ on " + aSale.getMarketplace().toString();

                    if(BotConfiguration.getConfiguration().isAdress()){
                        
                        String buyerAdress = aSale.getBuyer().getTezdomain() != null ? aSale.getBuyer().getTezdomain() : aSale.getBuyer().getAdress();
                        String sellerAdress = aSale.getSeller().getTezdomain() != null ? aSale.getSeller().getTezdomain() : aSale.getSeller().getAdress();
                        
                        if(buyerAdress.length() > 10){
                            buyerAdress = buyerAdress.substring(0,4) + ".." + buyerAdress.substring(buyerAdress.length()-4,buyerAdress.length());
                        }
                        
                        if(sellerAdress.length() > 10){
                            sellerAdress = sellerAdress.substring(0,4) + ".." + sellerAdress.substring(sellerAdress.length()-4,sellerAdress.length());
                        }
                        
                        status += "\nBuyer : " + buyerAdress ;
                        status += "\nSeller : " + sellerAdress ;
                        
                    }
                    
                    
                    if (aSale.getMarketplace() == MarketPlace.Objkt){
                        if (aSale.getPathname() == null){
                            status += " \nhttps://objkt.com/asset/"+ aSale.getContract() + "/"+ aSale.getId();
                        }
                        else{
                            status += " \nhttps://objkt.com/asset/"+ aSale.getPathname() + "/"+ aSale.getId();
                        }

                    }

                    status += "\n"+aSale.getTimestamp().toString().substring(5, 7) + "-"+ aSale.getTimestamp().toString().substring(8, 10) + "-" + aSale.getTimestamp().toString().substring(0, 4) + " at " + aSale.getTimestamp().toString().substring(11, 16)+ " UTC";
                                        
                    if(BotConfiguration.getConfiguration().getHashtags().size() > 0 ){
                        status += "\n";
                    }

                    for (String hashtag : BotConfiguration.getConfiguration().getHashtags()){
                        status += "#" + hashtag + " ";
                    } 
                
                    status += "#"+aSale.getMarketplace();
                    
                    if (aSale.getPathname() != null){
                        status += " #"+aSale.getPathname();
                    }
                    
                    StatusUpdate newStatus = new StatusUpdate(status);
                    
                    if (BotConfiguration.getConfiguration().isIpfs()){
                        try{
                            URL newURL = new URL("https://cloudflare-ipfs.com/"+ aSale.getIpfs().replace(":/", ""));
                            URLConnection urlConn = newURL.openConnection();
                            urlConn.setConnectTimeout(10000);
                            urlConn.setReadTimeout(10000);
                            urlConn.setAllowUserInteraction(false);         
                            urlConn.setDoOutput(true);

                            InputStream ipfsMedia = urlConn.getInputStream();
                            UploadedMedia media =  twitterInstance.uploadMedia(aSale.getPathname(), ipfsMedia );
                            long mediaId = media.getMediaId();

                            newStatus.setMediaIds(mediaId);
                            ipfsMedia.close();
                            urlConn = null;
                            newURL = null;
                        }
                        catch (Exception ex){
                            model.insertRow(0, new Object[]{this.getName().toString(),"Error : unable to send a tweet" ,ex.getMessage()});
                            Logger.getLogger(TwitterSocialNetwork.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    
}
