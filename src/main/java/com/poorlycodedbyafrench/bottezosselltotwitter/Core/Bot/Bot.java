/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.InterfaceLog;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.SalesHistoryManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.SearchContractSales;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.MainForm.MainBotForm;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.ini4j.Wini;

/**
 *
 * @author david
 */
public class Bot implements InterfaceLog{

    /**
     * List of all the marketplaces where we will find sales
     */
    private HashMap<MarketPlaceEnum, MarketPlace> marketplaces;

    private String name;

    private BotStatusEnum botStatus;

    private List<List<String>> recentOperation;

    private transient SalesHistoryManager historyManager;

    private transient Instant startTime;

    private MarketPlaceProfile mpProfile;

    private SocialNetworkProfile snProfile;

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
     * If we want to keep the security Listing And Bidding Id
     */
    private boolean securityIdListingAndBidding;

    /**
     * If we want to see the wallet for Listing And Bidding
     */
    private boolean adressListingAndBidding;

    /**
     * If we want to get the ipfs file for Listing And Bidding (when possible)
     * (Not recommended for big one)
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

    private boolean statRunning;

    private boolean salesRunning;

    private boolean listingAndBiddingRunning;

    public Bot(String name) {
        this.name = name;
        this.botStatus = BotStatusEnum.Uncompleted;
        this.marketplaces = new HashMap<MarketPlaceEnum, MarketPlace>();

        this.marketplaces.put(MarketPlaceEnum.Objkt, new MarketPlace(MarketPlaceEnum.Objkt));
        this.marketplaces.put(MarketPlaceEnum.Teia, new MarketPlace(MarketPlaceEnum.Teia));
        this.marketplaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash));
        this.marketplaces.put(MarketPlaceEnum.Rarible, new MarketPlace(MarketPlaceEnum.Rarible));

        this.historyManager = new SalesHistoryManager();

        this.recentOperation = new ArrayList<>();

        startTime = Instant.now();

        this.securityIdSales = true;
        this.orderBy = 0;
        this.saleType = true;
        this.adress = true;
        this.ipfs = true;

        this.securityIdStats = true;
        this.avgPriceStat = true;
        this.minPriceStat = true;
        this.maxPriceStat = true;

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

    public void start() {

        try {

            for (SocialNetworkInterface oneSocialNetwork : this.getSnProfile().getAllSocialNetwork()) {
                oneSocialNetwork.start();
            }

            if (salesRunning) {
                this.mpProfile.addSubscriber(BotModeEnum.Sale, this);
            }

            if (statRunning) {
                this.mpProfile.addSubscriber(BotModeEnum.Stat, this);
            }

            if (listingAndBiddingRunning) {
                this.mpProfile.addSubscriber(BotModeEnum.ListingAndBidding, this);
            }

            this.botStatus = BotStatusEnum.Running;
            this.startTime = Instant.now();

        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }

    public void addLog(String source, String status, String complement){
        if(recentOperation.size() > 30){
            for(int i = 20; i > 0; i--){
                recentOperation.remove(i);
            }
        }
        ArrayList<String> messageList = new ArrayList<>();
        messageList.add(source);
        messageList.add(status);
        messageList.add(complement);
        
        recentOperation.add(messageList);
    }
    
    public void stop() {

        this.botStatus = BotStatusEnum.Ready;

        if (salesRunning) {
            this.mpProfile.removeSubscriber(BotModeEnum.Sale, this);
        }

        if (statRunning) {
            this.mpProfile.removeSubscriber(BotModeEnum.Stat, this);
        }

        if (listingAndBiddingRunning) {
            this.mpProfile.removeSubscriber(BotModeEnum.ListingAndBidding, this);
        }

        try {
            for (SocialNetworkInterface oneSocialNetwork : this.getSnProfile().getAllSocialNetwork()) {
                oneSocialNetwork.stop();
            }
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }

    public void checkComplete() {

        if (this.botStatus != BotStatusEnum.Running) {
            this.botStatus = BotStatusEnum.Uncompleted;

            if (this.mpProfile != null && this.snProfile != null) {
                this.botStatus = BotStatusEnum.Ready;
            }
        }
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void exportConfig() throws IOException, Exception {
        Wini ini = new Wini();

        //Already changed
        ini.put("BotConfig", "version", 4);

        ini.put("BotConfig", "securityIdSales", securityIdSales);
        ini.put("BotConfig", "orderBy", orderBy);
        ini.put("BotConfig", "saleType", saleType);
        ini.put("BotConfig", "adress", adress);
        ini.put("BotConfig", "ipfs", ipfs);
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
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String saveFile = chooser.getSelectedFile().getAbsolutePath();
            if (!saveFile.toLowerCase().endsWith(".ini")) {
                saveFile += ".ini";
            }
            ini.store(new File(saveFile));
        }
    }

    public void importConfig() throws IOException, Exception {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
            Wini ini = new Wini(chooser.getSelectedFile());

            Integer version = ini.get("BotConfig", "version", Integer.class);

            this.securityIdSales = ini.get("BotConfig", "securityIdSales", boolean.class);
            this.orderBy = ini.get("BotConfig", "orderBy", int.class);
            this.saleType = ini.get("BotConfig", "saleType", boolean.class);
            this.adress = ini.get("BotConfig", "adress", boolean.class);
            this.ipfs = ini.get("BotConfig", "ipfs", boolean.class);
            this.securityIdStats = ini.get("BotConfig", "securityIdStats", boolean.class);
            this.avgPriceStat = ini.get("BotConfig", "avgPriceStat", boolean.class);
            this.minPriceStat = ini.get("BotConfig", "minPriceStat", boolean.class);
            this.maxPriceStat = ini.get("BotConfig", "maxPriceStat", boolean.class);

            String fileSentences = ini.get("BotConfig", "sentences");
            String fileHashtags = ini.get("BotConfig", "hashtags");

            if (fileSentences.isBlank()) {
                this.sentences = new ArrayList<>();
            } else {
                this.sentences = new ArrayList<>(Arrays.asList(fileSentences.split("#&#")));
            }

            if (fileHashtags.isBlank()) {
                this.hashtags = new ArrayList<>();
            } else {
                this.hashtags = new ArrayList<>(Arrays.asList(fileHashtags.split("#&#")));
            }

            if (version != null) {
                this.royaltywalletsale = ini.get("BotConfig", "royaltywalletsale", boolean.class);
                this.royaltywalletstat = ini.get("BotConfig", "royaltywalletstat", boolean.class);

                if (version >= 2) {
                    this.nameroyaltywallet = ini.get("BotConfig", "nameroyaltywallet", String.class);
                }

                if (version >= 3) {
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

                    if (newListSentences.isBlank()) {
                        this.sentencesListing = new ArrayList<>();
                    } else {
                        this.sentencesListing = new ArrayList<>(Arrays.asList(newListSentences.split("#&#")));
                    }

                    if (newEnglishAuctionSentences.isBlank()) {
                        this.sentencesEnglishAuction = new ArrayList<>();
                    } else {
                        this.sentencesEnglishAuction = new ArrayList<>(Arrays.asList(newEnglishAuctionSentences.split("#&#")));
                    }

                    if (newDutchAuctionSentences.isBlank()) {
                        this.sentencesDutchAuction = new ArrayList<>();
                    } else {
                        this.sentencesDutchAuction = new ArrayList<>(Arrays.asList(newDutchAuctionSentences.split("#&#")));
                    }

                    if (newFloorOfferSentences.isBlank()) {
                        this.sentencesFloorOffer = new ArrayList<>();
                    } else {
                        this.sentencesFloorOffer = new ArrayList<>(Arrays.asList(newFloorOfferSentences.split("#&#")));
                    }

                    if (newBiddingSentences.isBlank()) {
                        this.sentencesBidding = new ArrayList<>();
                    } else {
                        this.sentencesBidding = new ArrayList<>(Arrays.asList(newBiddingSentences.split("#&#")));
                    }
                }
            }

        }
    }

    public void importFile() throws IOException, Exception {

        Gson gson = new Gson();
        java.lang.reflect.Type empHashMapType = new TypeToken<HashMap<MarketPlaceEnum, MarketPlace>>() {
        }.getType();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
            Wini ini = new Wini(chooser.getSelectedFile());

            int version = ini.get("MainWindow", "version", int.class);
            String json = ini.get("MainWindow", "marketplaces", String.class);
            
            marketplaces.clear();

            if (version < 4) {
                JsonParser parser = new JsonParser();
                

                JsonObject rootObject = parser.parse(json).getAsJsonObject();

                marketplaces.put(MarketPlaceEnum.Objkt, createMpFromImport(MarketPlaceEnum.Objkt, rootObject));
                marketplaces.put(MarketPlaceEnum.Teia, createMpFromImport(MarketPlaceEnum.Teia, rootObject));

                if (version < 2) {
                    marketplaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash));
                } else {
                    marketplaces.put(MarketPlaceEnum.fxhash, createMpFromImport(MarketPlaceEnum.fxhash, rootObject));
                }

                if (version < 3) {
                    marketplaces.put(MarketPlaceEnum.Rarible, new MarketPlace(MarketPlaceEnum.Rarible));
                } else {
                    marketplaces.put(MarketPlaceEnum.Rarible, createMpFromImport(MarketPlaceEnum.Rarible, rootObject));
                }

            } else {
                HashMap<MarketPlaceEnum, MarketPlace> mps = gson.fromJson(json, empHashMapType);
                this.marketplaces = mps;
            }
        }
    }

    private MarketPlace createMpFromImport(MarketPlaceEnum enumMp, JsonObject rootObject) {
        Gson gson = new Gson();

        java.lang.reflect.Type empHashMapOldFilter = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        java.lang.reflect.Type empHashMapOldRoyalty = new TypeToken<HashMap<String, String>>() {
        }.getType();

        MarketPlace mpToReturn = new MarketPlace(enumMp);

        JsonObject anMp = rootObject.get(enumMp.name()).getAsJsonObject();

        JsonArray contracts = anMp.get("contracts").getAsJsonArray();

        for (Object c : contracts) {
            String contract = ((JsonPrimitive) c).getAsString();
            mpToReturn.getAllContractsFromThisMarketPlace().put(contract, new SearchContractSales(contract));
        }

        HashMap<String, List<String>> sellerList = gson.fromJson(anMp.get("sellerList"), empHashMapOldFilter);

        for (String key : sellerList.keySet()) {

            for (String allSellerFilter : sellerList.get(key)) {
                mpToReturn.getAllContractsFromThisMarketPlace().get(key).addSellerFilter(allSellerFilter);
            }
        }

        HashMap<String, List<String>> itemList = gson.fromJson(anMp.get("itemList"), empHashMapOldFilter);

        for (String key : itemList.keySet()) {

            for (String allItemFilter : itemList.get(key)) {
                mpToReturn.getAllContractsFromThisMarketPlace().get(key).addItemFilter(allItemFilter);
            }
        }

        HashMap<String, String> royaltyWallets = gson.fromJson(anMp.get("royaltywallet"), empHashMapOldRoyalty);

        for (String key : royaltyWallets.keySet()) {
            mpToReturn.getAllContractsFromThisMarketPlace().get(key).setRoyaltyWallet(royaltyWallets.get(key));
        }

        return mpToReturn;
    }

    /**
     * Export data into an ini file
     */
    public void exportFile() throws IOException, Exception {

        Gson gson = new Gson();

        Wini ini = new Wini();

        ini.put("MainWindow", "version", 4);
        ini.put("MainWindow", "marketplaces", gson.toJson(marketplaces));

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String saveFile = chooser.getSelectedFile().getAbsolutePath();
            if (!saveFile.toLowerCase().endsWith(".ini")) {
                saveFile += ".ini";
            }
            ini.store(new File(saveFile));
        }
    }

    public SalesHistoryManager getHistoryManager() {
        return historyManager;
    }

    public HashMap<MarketPlaceEnum, MarketPlace> getMarketplaces() {
        return marketplaces;
    }

    public String getName() {
        return name;
    }

    public BotStatusEnum getBotStatus() {
        return botStatus;
    }

    public void setMarketplaces(HashMap<MarketPlaceEnum, MarketPlace> marketplaces) {
        this.marketplaces = marketplaces;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSecurityIdSales() {
        return securityIdSales;
    }

    public void setSecurityIdSales(boolean securityIdSales) {
        this.securityIdSales = securityIdSales;
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

    public MarketPlaceProfile getMpProfile() {
        return mpProfile;
    }

    public void setMpProfile(MarketPlaceProfile mpProfile) {
        this.mpProfile = mpProfile;
    }

    public boolean isStatRunning() {
        return statRunning;
    }

    public void setStatRunning(boolean statRunning) {
        this.statRunning = statRunning;
    }

    public boolean isSalesRunning() {
        return salesRunning;
    }

    public void setSalesRunning(boolean salesRunning) {
        this.salesRunning = salesRunning;
    }

    public boolean isListingAndBiddingRunning() {
        return listingAndBiddingRunning;
    }

    public void setListingAndBiddingRunning(boolean listingAndBiddingRunning) {
        this.listingAndBiddingRunning = listingAndBiddingRunning;
    }

    public SocialNetworkProfile getSnProfile() {
        return snProfile;
    }

    public void setSnProfile(SocialNetworkProfile snProfile) {
        this.snProfile = snProfile;
    }

    @Override
    public String toString() {
        return this.name; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    public List<List<String>> getRecentOperation() {
        return recentOperation;
    }   

    public void setBotStatus(BotStatusEnum botStatus) {
        this.botStatus = botStatus;
    }

    public void setHistoryManager(SalesHistoryManager historyManager) {
        this.historyManager = historyManager;
    }
    
    
}
