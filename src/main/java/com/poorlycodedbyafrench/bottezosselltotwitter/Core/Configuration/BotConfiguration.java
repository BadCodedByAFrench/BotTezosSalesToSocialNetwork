/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.ini4j.Wini;

/**
 * Class that keep all the configuration of the bot
 * @author david
 */
public class BotConfiguration {
    
    /**
     * Units of sales refresh (Minutes/ Hours / Days)
     */
    private TimeUnit refreshSales;
    
    /**
     * How many time we want to wait between 2 sale refresh
     */
    private int refreshSalesTime;
    
    /**
     * If we want to keep the security sale Id
     */
    private boolean securityIdSales;
    
    /**
     * In which order we want to order the sales
     */
    private int orderBy;
    
    /**
     * If we want to see the kind of sales (offer/ listed / auctions)
     */
    private boolean saleType;
    
    /**
     * If we want to see the wallets
     */
    private boolean adress;
    
    /**
     * If we want to get the ipfs file (Not recommended for big one)
     */
    private boolean ipfs;
    
    /**
     * Units of stats refresh (Hours / Days)
     */
    
    private TimeUnit refreshStats;
    
    /**
     * How many time we want to wait between 2 stat refresh
     */
    private int refreshSalesStats;

    /**
     * If we want to keep the security stat Id
     */
    
    private boolean securityIdStats;
    
    /**
     * If we want to see the average price
     */
    private boolean avgPriceStat;
    
    /**
     * If we want to see the minimum price
     */
    private boolean minPriceStat;
    
    /**
     * If we want to see the maximum price
     */
    
    private boolean maxPriceStat;
    
    /**
     * List of all sentences for a sale
     */
    private List<String> sentences;
    
    /**
     * List of all the hashtag we want 
     */
    private List<String> hashtags;

    /**
     * Show royalty wallet in sale message
     */
    
    private boolean royaltywalletsale;
    
    /**
     * Show royalty wallet in sale message
     */
    
    private boolean royaltywalletstat;
    
    /**
     * Name show in messages for royalty wallet
     */
    private String nameroyaltywallet;
    
    
    
     /**
     * Units of listing and bidding refresh (Minutes/ Hours / Days)
     */
    private TimeUnit refreshListingAndBidding;
    
    /**
     * How many time we want to wait between 2 Listing And Bidding refresh
     */
    private int refreshListingAndBiddingTime;
    
    /**
     * If we want to keep the security Listing And Bidding Id
     */
    private boolean securityIdListingAndBidding;
    
     /**
     * If we want to see the wallet for Listing And Bidding
     */
    private boolean adressListingAndBidding;
    
    /**
     * If we want to get the ipfs file for Listing And Bidding (when possible) (Not recommended for big one)
     */
    private boolean ipfsListingAndBidding;
    
    /**
     * If we want to get the new listing
     */
    private boolean activateListing;
    
    /**
     * If we want to get the new english auction
     */
    private boolean activateEnglishAuction;
    
    /**
     * If we want to get the new dutch auction
     */
    private boolean activateDutchAuction;
    
    /**
     * If we want to get the new floor offer 
     */
    private boolean activateFloorOffer;
    
    /**
     * If we want to get the new bidding
     */
    private boolean activateBidding;
    
    /**
     * List of all sentences for a new listing
     */
    private List<String> sentencesListing;
    
    /**
     * List of all sentences for a new english auction
     */
    private List<String> sentencesEnglishAuction;
    
    /**
     * List of all sentences for a new dutch auction
     */
    private List<String> sentencesDutchAuction;
    
    /**
     * List of all sentences for a new floor offer
     */
    private List<String> sentencesFloorOffer;
    
    /**
     * List of all sentences for a bidding
     */
    private List<String> sentencesBidding;
    
    /**
     * Random singleton
     */
    private static BotConfiguration configuration;
    
    private BotConfiguration(){
        this.refreshSales = TimeUnit.HOURS;
        this.refreshSalesTime = 1;
        this.securityIdSales = true;
        this.orderBy = 0;
        this.saleType = true;
        this.adress = true;
        this.ipfs = true;
        
        this.refreshStats = TimeUnit.HOURS;
        this.refreshSalesStats = 1;
        this.securityIdStats = true;
        this.avgPriceStat = true;
        this.minPriceStat = true;
        this.maxPriceStat = true;
        
        this.refreshListingAndBidding = TimeUnit.HOURS;
        this.refreshListingAndBiddingTime = 1;
        this.securityIdListingAndBidding = true;
        this.adressListingAndBidding = true;
        this.ipfsListingAndBidding = true;
        this.activateListing = true;
        this.activateEnglishAuction = true;
        this.activateDutchAuction = true;
        this.activateFloorOffer = true;
        this.activateBidding = true;
        
        this.royaltywalletsale = true;
        this.royaltywalletstat = true;
        this.nameroyaltywallet = "Wallet balance";
        
        sentences = new ArrayList<String>();
        sentences.add("YeeHaw another sale!");
        sentences.add("Boom another one sold");
        sentences.add("Another one on the run");
        
        hashtags = new ArrayList<String>();
        hashtags.add("Nft");
        hashtags.add("Tezos");
        
        sentencesListing = new ArrayList<String>();
        sentencesListing.add("Grab it before it's too late !");
        
        sentencesEnglishAuction = new ArrayList<String>();
        sentencesEnglishAuction.add("Don't miss this opportunity !");
        
        sentencesDutchAuction = new ArrayList<String>();
        sentencesDutchAuction.add("Grab it before it's too late !");
        
        sentencesFloorOffer = new ArrayList<String>();
        sentencesFloorOffer.add("Here come the FOMO!");
        
        sentencesBidding = new ArrayList<String>();
        sentencesBidding.add("Hehe, a new bid");
    }
    
    /**
     * Export data into an ini file
     */
    public void export() throws IOException, Exception{
        
        
        Wini ini = new Wini();
        
        ini.put("BotConfig", "version", 4);
        
        ini.put("BotConfig", "refreshSales", refreshSales);
        ini.put("BotConfig", "refreshSalesTime", refreshSalesTime);
        ini.put("BotConfig", "securityIdSales", securityIdSales);
        ini.put("BotConfig", "orderBy", orderBy);
        ini.put("BotConfig", "saleType", saleType);
        ini.put("BotConfig", "adress", adress);
        ini.put("BotConfig", "ipfs", ipfs);
        ini.put("BotConfig", "refreshStats", refreshStats);
        ini.put("BotConfig", "refreshSalesStats", refreshSalesStats);
        ini.put("BotConfig", "securityIdStats", securityIdStats);
        ini.put("BotConfig", "avgPriceStat", avgPriceStat);
        ini.put("BotConfig", "minPriceStat", minPriceStat);
        ini.put("BotConfig", "maxPriceStat", maxPriceStat);
        ini.put("BotConfig", "sentences", String.join("#&#", sentences));
        ini.put("BotConfig", "hashtags", String.join("#&#", hashtags));
        
        
        //version 2
        ini.put("BotConfig", "royaltywalletsale", royaltywalletsale);
        ini.put("BotConfig", "royaltywalletstat", royaltywalletstat);
        
        //version 3
        ini.put("BotConfig", "nameroyaltywallet", nameroyaltywallet);
        
        //version 4
        ini.put("BotConfig", "refreshListingAndBidding", refreshListingAndBidding);
        ini.put("BotConfig", "refreshListingAndBiddingTime", refreshListingAndBiddingTime);
        ini.put("BotConfig", "securityIdListingAndBidding", securityIdListingAndBidding);
        ini.put("BotConfig", "adressListingAndBidding", adressListingAndBidding);
        ini.put("BotConfig", "ipfsListingAndBidding", ipfsListingAndBidding);
        
        ini.put("BotConfig", "activateListing", activateListing);
        ini.put("BotConfig", "activateEnglishAuction", activateEnglishAuction);
        ini.put("BotConfig", "activateDutchAuction", activateDutchAuction);
        ini.put("BotConfig", "activateFloorOffer", activateFloorOffer);
        ini.put("BotConfig", "activateBidding", activateBidding);
        
        ini.put("BotConfig", "sentencesListing", String.join("#&#", sentencesListing));
        ini.put("BotConfig", "sentencesEnglishAuction", String.join("#&#", sentencesEnglishAuction));
        ini.put("BotConfig", "sentencesDutchAuction", String.join("#&#", sentencesDutchAuction));
        ini.put("BotConfig", "sentencesFloorOffer", String.join("#&#", sentencesFloorOffer));
        ini.put("BotConfig", "sentencesBidding", String.join("#&#", sentencesBidding));
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            
            String saveFile = chooser.getSelectedFile().getAbsolutePath();
            if(!saveFile.toLowerCase().endsWith(".ini"))
            {
                saveFile += ".ini";
            }
            ini.store(new File(saveFile));
        }
    }
    
    public void importFile() throws IOException, Exception {
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
            Wini ini = new Wini(chooser.getSelectedFile());
            
            Integer version = ini.get("BotConfig","version", Integer.class);
            
            this.refreshSales = ini.get("BotConfig", "refreshSales", TimeUnit.class);
            this.refreshSalesTime = ini.get("BotConfig", "refreshSalesTime", int.class);
            this.securityIdSales = ini.get("BotConfig", "securityIdSales", boolean.class);
            this.orderBy = ini.get("BotConfig", "orderBy", int.class);
            this.saleType = ini.get("BotConfig", "saleType", boolean.class);
            this.adress = ini.get("BotConfig", "adress", boolean.class);
            this.ipfs = ini.get("BotConfig", "ipfs", boolean.class);
            this.refreshStats = ini.get("BotConfig", "refreshStats", TimeUnit.class);
            this.refreshSalesStats = ini.get("BotConfig", "refreshSalesStats", int.class);
            this.securityIdStats = ini.get("BotConfig", "securityIdStats", boolean.class);
            this.avgPriceStat = ini.get("BotConfig", "avgPriceStat", boolean.class);
            this.minPriceStat = ini.get("BotConfig", "minPriceStat", boolean.class);
            this.maxPriceStat = ini.get("BotConfig", "maxPriceStat", boolean.class);

            String fileSentences = ini.get("BotConfig", "sentences");
            String fileHashtags = ini.get("BotConfig", "hashtags");

            if(fileSentences.isBlank()){
                this.sentences = new ArrayList<>();
            }
            else{
                this.sentences = new ArrayList<> (Arrays.asList(fileSentences.split("#&#"))); 
            }

            if(fileHashtags.isBlank()){
                this.hashtags = new ArrayList<>();
            }
            else{
                this.hashtags = new ArrayList<> (Arrays.asList(fileHashtags.split("#&#"))); 
            }
            
            if (version != null) {
                this.royaltywalletsale = ini.get("BotConfig", "royaltywalletsale", boolean.class);
                this.royaltywalletstat = ini.get("BotConfig", "royaltywalletstat", boolean.class);
                
                if(version >= 3){
                    this.nameroyaltywallet = ini.get("BotConfig", "nameroyaltywallet", String.class);
                }
                
                if(version >= 4){
                    this.refreshListingAndBidding = ini.get("BotConfig", "refreshListingAndBidding", TimeUnit.class);
                    this.refreshListingAndBiddingTime = ini.get("BotConfig", "refreshListingAndBiddingTime", int.class);
                    this.securityIdListingAndBidding = ini.get("BotConfig", "securityIdListingAndBidding", boolean.class);
                    this.adressListingAndBidding = ini.get("BotConfig", "adressListingAndBidding", boolean.class);
                    this.ipfsListingAndBidding = ini.get("BotConfig", "ipfsListingAndBidding", boolean.class);

                    this.activateListing = ini.get("BotConfig", "activateListing", boolean.class);
                    this.activateEnglishAuction = ini.get("BotConfig", "activateEnglishAuction", boolean.class);
                    this.activateDutchAuction = ini.get("BotConfig", "activateDutchAuction", boolean.class);
                    this.activateFloorOffer = ini.get("BotConfig", "activateFloorOffer", boolean.class);
                    this.activateBidding = ini.get("BotConfig", "activateBidding", boolean.class);
                    
                    String newListSentences = ini.get("BotConfig", "sentencesListing");
                    String newEnglishAuctionSentences = ini.get("BotConfig", "sentencesEnglishAuction");
                    String newDutchAuctionSentences = ini.get("BotConfig", "sentencesDutchAuction");
                    String newFloorOfferSentences = ini.get("BotConfig", "sentencesFloorOffer");
                    String newBiddingSentences = ini.get("BotConfig", "sentencesBidding");

                    if(newListSentences.isBlank()){
                        this.sentencesListing = new ArrayList<>();
                    }
                    else{
                        this.sentencesListing = new ArrayList<> (Arrays.asList(newListSentences.split("#&#"))); 
                    }
                    
                    if(newEnglishAuctionSentences.isBlank()){
                        this.sentencesEnglishAuction = new ArrayList<>();
                    }
                    else{
                        this.sentencesEnglishAuction = new ArrayList<> (Arrays.asList(newEnglishAuctionSentences.split("#&#"))); 
                    }
                    
                    if(newDutchAuctionSentences.isBlank()){
                        this.sentencesDutchAuction = new ArrayList<>();
                    }
                    else{
                        this.sentencesDutchAuction = new ArrayList<> (Arrays.asList(newDutchAuctionSentences.split("#&#"))); 
                    }
                    
                    if(newFloorOfferSentences.isBlank()){
                        this.sentencesFloorOffer = new ArrayList<>();
                    }
                    else{
                        this.sentencesFloorOffer = new ArrayList<> (Arrays.asList(newFloorOfferSentences.split("#&#"))); 
                    }
                    
                    if(newBiddingSentences.isBlank()){
                        this.sentencesBidding = new ArrayList<>();
                    }
                    else{
                        this.sentencesBidding = new ArrayList<> (Arrays.asList(newBiddingSentences.split("#&#"))); 
                    }
                }
            }
            
        }
    }
    
    public static BotConfiguration getConfiguration(){
        if (configuration == null){
            configuration = new BotConfiguration();
        }
        
        return configuration;
    }

    public TimeUnit getRefreshSales() {
        return refreshSales;
    }

    public void setRefreshSales(TimeUnit refreshSales) {
        this.refreshSales = refreshSales;
    }

    public boolean isSecurityIdSales() {
        return securityIdSales;
    }

    public void setSecurityIdSales(boolean securityIdSales) {
        this.securityIdSales = securityIdSales;
    }

    public TimeUnit getRefreshStats() {
        return refreshStats;
    }

    public void setRefreshStats(TimeUnit refreshStats) {
        this.refreshStats = refreshStats;
    }

    public boolean isSecurityIdStats() {
        return securityIdStats;
    }

    public void setSecurityIdStats(boolean securityIdStats) {
        this.securityIdStats = securityIdStats;
    }

    public boolean isAvgPriceStat() {
        return avgPriceStat;
    }

    public void setAvgPriceStat(boolean avgPriceStat) {
        this.avgPriceStat = avgPriceStat;
    }

    public boolean isMinPriceStat() {
        return minPriceStat;
    }

    public void setMinPriceStat(boolean minPriceStat) {
        this.minPriceStat = minPriceStat;
    }

    public boolean isMaxPriceStat() {
        return maxPriceStat;
    }

    public void setMaxPriceStat(boolean maxPriceStat) {
        this.maxPriceStat = maxPriceStat;
    }

    public int getRefreshSalesTime() {
        return refreshSalesTime;
    }

    public void setRefreshSalesTime(int refreshSalesTime) {
        this.refreshSalesTime = refreshSalesTime;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isSaleType() {
        return saleType;
    }

    public void setSaleType(boolean saleType) {
        this.saleType = saleType;
    }

    public int getRefreshSalesStats() {
        return refreshSalesStats;
    }

    public void setRefreshSalesStats(int refreshSalesStats) {
        this.refreshSalesStats = refreshSalesStats;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public boolean isAdress() {
        return adress;
    }

    public void setAdress(boolean adress) {
        this.adress = adress;
    }

    public boolean isIpfs() {
        return ipfs;
    }

    public void setIpfs(boolean ipfs) {
        this.ipfs = ipfs;
    }

    public boolean isRoyaltywalletsale() {
        return royaltywalletsale;
    }

    public void setRoyaltywalletsale(boolean royaltywalletsale) {
        this.royaltywalletsale = royaltywalletsale;
    }

    public boolean isRoyaltywalletstat() {
        return royaltywalletstat;
    }

    public void setRoyaltywalletstat(boolean royaltywalletstat) {
        this.royaltywalletstat = royaltywalletstat;
    }

    public String getNameroyaltywallet() {
        return nameroyaltywallet;
    }

    public void setNameroyaltywallet(String nameroyaltywallet) {
        this.nameroyaltywallet = nameroyaltywallet;
    }

    public TimeUnit getRefreshListingAndBidding() {
        return refreshListingAndBidding;
    }

    public void setRefreshListingAndBidding(TimeUnit refreshListingAndBidding) {
        this.refreshListingAndBidding = refreshListingAndBidding;
    }

    public int getRefreshListingAndBiddingTime() {
        return refreshListingAndBiddingTime;
    }

    public void setRefreshListingAndBiddingTime(int refreshListingAndBiddingTime) {
        this.refreshListingAndBiddingTime = refreshListingAndBiddingTime;
    }

    public boolean isSecurityIdListingAndBidding() {
        return securityIdListingAndBidding;
    }

    public void setSecurityIdListingAndBidding(boolean securityIdListingAndBidding) {
        this.securityIdListingAndBidding = securityIdListingAndBidding;
    }

    public boolean isAdressListingAndBidding() {
        return adressListingAndBidding;
    }

    public void setAdressListingAndBidding(boolean adressListingAndBidding) {
        this.adressListingAndBidding = adressListingAndBidding;
    }

    public boolean isIpfsListingAndBidding() {
        return ipfsListingAndBidding;
    }

    public void setIpfsListingAndBidding(boolean ipfsListingAndBidding) {
        this.ipfsListingAndBidding = ipfsListingAndBidding;
    }

    public boolean isActivateListing() {
        return activateListing;
    }

    public void setActivateListing(boolean activateListing) {
        this.activateListing = activateListing;
    }

    public boolean isActivateEnglishAuction() {
        return activateEnglishAuction;
    }

    public void setActivateEnglishAuction(boolean activateEnglishAuction) {
        this.activateEnglishAuction = activateEnglishAuction;
    }

    public boolean isActivateDutchAuction() {
        return activateDutchAuction;
    }

    public void setActivateDutchAuction(boolean activateDutchAuction) {
        this.activateDutchAuction = activateDutchAuction;
    }

    public boolean isActivateFloorOffer() {
        return activateFloorOffer;
    }

    public void setActivateFloorOffer(boolean activateFloorOffer) {
        this.activateFloorOffer = activateFloorOffer;
    }

    public boolean isActivateBidding() {
        return activateBidding;
    }

    public void setActivateBidding(boolean activateBidding) {
        this.activateBidding = activateBidding;
    }

    public List<String> getSentencesListing() {
        return sentencesListing;
    }

    public void setSentencesListing(List<String> sentencesListing) {
        this.sentencesListing = sentencesListing;
    }

    public List<String> getSentencesEnglishAuction() {
        return sentencesEnglishAuction;
    }

    public void setSentencesEnglishAuction(List<String> sentencesEnglishAuction) {
        this.sentencesEnglishAuction = sentencesEnglishAuction;
    }

    public List<String> getSentencesDutchAuction() {
        return sentencesDutchAuction;
    }

    public void setSentencesDutchAuction(List<String> sentencesDutchAuction) {
        this.sentencesDutchAuction = sentencesDutchAuction;
    }

    public List<String> getSentencesFloorOffer() {
        return sentencesFloorOffer;
    }

    public void setSentencesFloorOffer(List<String> sentencesFloorOffer) {
        this.sentencesFloorOffer = sentencesFloorOffer;
    }

    public List<String> getSentencesBidding() {
        return sentencesBidding;
    }

    public void setSentencesBidding(List<String> sentencesBidding) {
        this.sentencesBidding = sentencesBidding;
    }
    
    
}
