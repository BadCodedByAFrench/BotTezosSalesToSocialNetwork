/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Manager that create the message from the sales
 * @author david
 */
public class NetworkMessageManager {

    private static NetworkMessageManager messageManager;

    private NetworkMessageManager() {

    }

    public synchronized static NetworkMessageManager getMessageManager() {

        if (messageManager == null) {
            messageManager = new NetworkMessageManager();
        }

        return messageManager;
    }

    /**
     * Function that create the contracts for the statistic
     * @param newSales
     * @return 
     */
    public List<Contract> createContractList(List<Sale> newSales) {
        List<Contract> contracts = new ArrayList<Contract>();
        for (Sale aSale : newSales) {

            boolean contractExist = false;

            for (Contract contract : contracts) {
                if (contract.getContract().equals(aSale.getContract())) {
                    contract.addSale(aSale);
                    contractExist = true;
                }
            }

            if (!contractExist) {
                Contract newContract = new Contract(aSale);
                contracts.add(newContract);
            }
        }
        return contracts;
    }

    /**
     * Function to create the statistic message
     * @param contract
     * @param df
     * @param previousUTCHour
     * @param countAvoidTwitterDuplicate
     * @return 
     */
    public String createStatMessage(Contract contract, DecimalFormat df, Instant previousUTCHour, int countAvoidTwitterDuplicate, HashMap<String, Long> balance) {

        String status = "";

        if (BotConfiguration.getConfiguration().isSecurityIdStats()) {
            status += countAvoidTwitterDuplicate + " ";
        }

        status += "Stat for " + contract.getName() + " since " + previousUTCHour.toString().substring(5, 7) + "-" + previousUTCHour.toString().substring(8, 10) + "-" + previousUTCHour.toString().substring(0, 4) + " at " + previousUTCHour.toString().substring(11, 16) + " UTC : ";
        status += "\nNumber of sales : " + contract.getNbSale();
        status += "\nTotal xtz : " + df.format(contract.getTotalprice()).replace(',', '.');

        if (BotConfiguration.getConfiguration().isMinPriceStat()) {
            status += "\nMin price : " + df.format(contract.getMin()).replace(',', '.');
        }

        if (BotConfiguration.getConfiguration().isMaxPriceStat()) {
            status += "\nMax price : " + df.format(contract.getMax()).replace(',', '.');
        }

        if (BotConfiguration.getConfiguration().isAvgPriceStat()) {
            status += "\nAverage price : " + df.format(contract.getAvg()).replace(',', '.');
        }
        
        if(BotConfiguration.getConfiguration().isRoyaltywalletstat() && balance != null){
            if(balance.containsKey(contract.getContract())){
                status += "\n"+BotConfiguration.getConfiguration().getNameroyaltywallet()+" : " + df.format(balance.get(contract.getContract())/1000000.0).replace(',', '.') + " XTZ";
            }
        }
        
        if (contract.getMarketplace().contains(MarketPlaceEnum.Objkt)) {
            status += "\nObjkt Link : ";
            if (contract.getPath() == null) {
                status += " https://objkt.com/collection/" + contract.getContract();
            } else {
                status += " https://objkt.com/collection/" + contract.getPath();
            }
        }
        else if (contract.getMarketplace().contains(MarketPlaceEnum.Teia)) {
            status += "\nTeia Link : ";
            status += " https://teia.art/tz/" + contract.getContract();
        }
        else if (contract.getMarketplace().contains(MarketPlaceEnum.fxhash)) {
            status += "\nFxHash Link : ";
            status += " https://www.fxhash.xyz/generative/" + contract.getContract();
        }
        

        if (BotConfiguration.getConfiguration().getHashtags().size() > 0) {
            status += "\n";
        }

        for (String hashtag : BotConfiguration.getConfiguration().getHashtags()) {
            status += "#" + hashtag + " ";
        }

        for (MarketPlaceEnum m : contract.getMarketplace()) {
            status += " #" + m.toString();
        }

        if (contract.getPath() != null) {
            status += " #" + contract.getPath();
        }

        return status;
    }

    /**
     * Function to create the sale message
     * @param aSale
     * @param df
     * @param rand
     * @param countAvoidTwitterDuplicate
     * @return 
     */
    public String createSaleMessage(Sale aSale, DecimalFormat df, Random rand, int countAvoidTwitterDuplicate, HashMap<String, Long> balance) {
        String status = "";
        if (BotConfiguration.getConfiguration().isSecurityIdSales()) {
            status += countAvoidTwitterDuplicate + " ";
        }

        if (BotConfiguration.getConfiguration().getSentences().size() > 0) {
            status += BotConfiguration.getConfiguration().getSentences().get(rand.nextInt(BotConfiguration.getConfiguration().getSentences().size()));
        }

        if (BotConfiguration.getConfiguration().isSaleType() && aSale.getType() != SaleTypeEnum.Unknown) {
            status += " : " + aSale.getType().getType();
        }

        status += " " + aSale.getName() + " has been sold for " + df.format(aSale.getPrice()).replace(',', '.') + " XTZ on " + aSale.getMarketplace().toString();

        if (BotConfiguration.getConfiguration().isAdress()) {

            String buyerAdress = aSale.getBuyer().getTezdomain() == null || aSale.getBuyer().getTezdomain().isBlank() ? aSale.getBuyer().getAdress() : aSale.getBuyer().getTezdomain();
            String sellerAdress = aSale.getSeller().getTezdomain() == null || aSale.getSeller().getTezdomain().isBlank() ? aSale.getSeller().getAdress() : aSale.getSeller().getTezdomain();

            if (buyerAdress.length() > 10) {
                buyerAdress = buyerAdress.substring(0, 4) + ".." + buyerAdress.substring(buyerAdress.length() - 4, buyerAdress.length());
            }

            if (sellerAdress.length() > 10) {
                sellerAdress = sellerAdress.substring(0, 4) + ".." + sellerAdress.substring(sellerAdress.length() - 4, sellerAdress.length());
            }

            status += "\nBuyer : " + buyerAdress;
            status += "\nSeller : " + sellerAdress;

        }
        
        if(BotConfiguration.getConfiguration().isRoyaltywalletsale() && balance != null){
            if(balance.containsKey(aSale.getContract())){
                status += "\n"+BotConfiguration.getConfiguration().getNameroyaltywallet()+" : " + df.format(balance.get(aSale.getContract())/1000000.0).replace(',', '.') + " XTZ";
            }
        }
        

        if (aSale.getMarketplace() == MarketPlaceEnum.Objkt) {
            if (aSale.getPathname() == null) {
                status += " \nhttps://objkt.com/asset/" + aSale.getContract() + "/" + aSale.getId();
            } else {
                status += " \nhttps://objkt.com/asset/" + aSale.getPathname() + "/" + aSale.getId();
            }

        }
        else if (aSale.getMarketplace() == MarketPlaceEnum.Teia) {
            status += " \nhttps://teia.art/objkt/" + aSale.getId();
        }
        else if (aSale.getMarketplace() == MarketPlaceEnum.fxhash) {
            status += " \nhttps://www.fxhash.xyz/gentk/" + aSale.getId();
        }

        status += "\n" + aSale.getTimestamp().toString().substring(5, 7) + "-" + aSale.getTimestamp().toString().substring(8, 10) + "-" + aSale.getTimestamp().toString().substring(0, 4) + " at " + aSale.getTimestamp().toString().substring(11, 16) + " UTC";

        if (BotConfiguration.getConfiguration().getHashtags().size() > 0) {
            status += "\n";
        }

        for (String hashtag : BotConfiguration.getConfiguration().getHashtags()) {
            status += "#" + hashtag + " ";
        }

        status += "#" + aSale.getMarketplace();

        if (aSale.getPathname() != null) {
            status += " #" + aSale.getPathname();
        }

        return status;
    }
    
    public HashMap<String, Long> getBalanceRoyaltyWallet (HashMap<MarketPlaceEnum, MarketPlace> marketplaces, List<Sale> saleList) throws MalformedURLException, IOException, InterruptedException{
        HashMap<String, Long> balance = new HashMap<String, Long>();
        
        String stringurl = "https://api.tzkt.io/v1/accounts/replaceaddress/balance";
        
        List<String> allContractsPresent = new ArrayList<String>();
        
        for(Sale aSale : saleList){
            
            if(!allContractsPresent.contains(aSale.getContract())){
                allContractsPresent.add(aSale.getContract());
            }
        }
        
        for(MarketPlace mp : marketplaces.values()){
            for(String contract : mp.getContracts()){
                if(allContractsPresent.contains(contract) && mp.getRoyaltywallet().containsKey(contract) && !mp.getRoyaltywallet().get(contract).isBlank()){
                    
                    HttpClient client = HttpClient.newHttpClient();
                    
                    HttpRequest request = HttpRequest.newBuilder(
                            URI.create(stringurl.replace("replaceaddress", mp.getRoyaltywallet().get(contract))))
                            .build();
                            
                    String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                    
                    balance.put(contract, Long.parseLong(response));
                    TimeUnit.SECONDS.sleep(1);
                }
            }            
        }
        
        
        return balance;
    }
}
