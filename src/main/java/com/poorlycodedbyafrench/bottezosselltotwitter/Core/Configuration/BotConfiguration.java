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
        
        sentences = new ArrayList<String>();
        sentences.add("YeeHaw another sale!");
        sentences.add("Boom another one sold");
        sentences.add("Another one on the run");
        
        hashtags = new ArrayList<String>();
        hashtags.add("Nft");
        hashtags.add("Tezos");
    }
    
    /**
     * Export data into an ini file
     */
    public void export() throws IOException, Exception{
        
        
        Wini ini = new Wini();
        
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
    
    
}
