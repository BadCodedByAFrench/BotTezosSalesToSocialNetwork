/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator.MarketPlaceCreator;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Contract;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.ArrayList;
import java.util.List;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Thread that get the data from the marketplace and share it on social network
 *
 * @author david
 */
public class SalesToSocialNetwork implements Runnable {

    /**
     * List of all the marketplace
     */
    private HashMap<MarketPlaceEnum, List<String>> marketplaces;

    /**
     * List of all the social network
     */
    private List<SocialNetworkInterface> socialNetworks;

    private BotModeEnum mode;

    private MarketPlaceProfile mpProfile;

    public SalesToSocialNetwork(BotModeEnum mode, MarketPlaceProfile mpProfile) {
        this.marketplaces = new HashMap<MarketPlaceEnum, List<String>>();
        this.socialNetworks = new ArrayList<SocialNetworkInterface>();

        this.mode = mode;
        this.mpProfile = mpProfile;
    }

    public HashMap<MarketPlaceEnum, List<String>> getMarketplaces() {
        return marketplaces;
    }

    public void setSocialNetworks(List<SocialNetworkInterface> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    /**
     * Function that create the contracts with all sales inside
     *
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

                    if (aSale.getMarketplace() != MarketPlaceEnum.Rarible && contract.getContract().equals(contract.getName())) {
                        contract.setName(aSale.getCollectionName());
                    }

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
     * if the bot is active We get all the sale from marketplace And we send
     * them to socialNetwork
     */
    @Override
    public void run() {

        this.marketplaces = mpProfile.updateMarketPlaceList(mode);
        /*if (model.getRowCount() >= 100) {
                model.setRowCount(10);
            }*/

        HashMap<MarketPlaceEnum, HashMap<String, Sale>> allNewSell = new HashMap<MarketPlaceEnum, HashMap<String, Sale>>();
        ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck = new ArrayList<MarketPlaceEnum>();

        ExecutorService multithreadMarketPlace = Executors.newFixedThreadPool(marketplaces.values().size());
        List<Future<HashMap<MarketPlaceEnum, HashMap<String, Sale>>>> allMarketPlaceFutures = new ArrayList<>();

        for (MarketPlaceEnum mpEnum : marketplaces.keySet()) {

            if (marketplaces.get(mpEnum).size() > 0) {
                Future<HashMap<MarketPlaceEnum, HashMap<String, Sale>>> futurNewSales = multithreadMarketPlace.submit(MarketPlaceCreator.getCreatorThread(mpEnum).callMarketPlace(mode, marketplaces.get(mpEnum), mpProfile.getMarketPlaceLastRefresh().get(mpEnum)));
                allMarketPlaceFutures.add(futurNewSales);
            }
        }

        for (Future<HashMap<MarketPlaceEnum, HashMap<String, Sale>>> aFutur : allMarketPlaceFutures) {
            try {
                HashMap<MarketPlaceEnum, HashMap<String, Sale>> newSalesMarketPlace = aFutur.get();

                for (MarketPlaceEnum aMarketPlace : newSalesMarketPlace.keySet()) {
                    allNewSell.putAll(newSalesMarketPlace);
                    finishedMarketPlaceToCheck.add(aMarketPlace);
                }
            } catch (ExecutionException ex) {
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            } catch (Exception ex) {
                LogManager.getLogManager().writeLog(SalesToSocialNetwork.class.getName(), ex);
            }
        }

        multithreadMarketPlace.shutdown();

        mpProfile.receiveNewSales(mode, allNewSell, finishedMarketPlaceToCheck);
    }
}
