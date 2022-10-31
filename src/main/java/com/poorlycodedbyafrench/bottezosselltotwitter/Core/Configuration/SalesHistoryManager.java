/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class SalesHistoryManager {

    private HashMap<String, Sale> salesHistory;
    private HashMap<String, Sale> statHistory;
    private HashMap<String, Sale> listingAndBiddingHistory;

    public SalesHistoryManager() {
        salesHistory = new HashMap<String, Sale>();
        statHistory = new HashMap<String, Sale>();
        listingAndBiddingHistory = new HashMap<String, Sale>();
    }

    public List<Sale> checkNewSales(Collection<Sale> newSalesList, BotModeEnum mode, Bot theBot) {

        List<Sale> saleToShow = new ArrayList<Sale>();

        if (mode == BotModeEnum.Sale) {
            for (Sale aSale : newSalesList) {
                if (!salesHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(theBot.getStartTime())) {

                    boolean sellerCheck = true;
                    boolean itemCheck = true;

                    if (!theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().isEmpty() && !theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().contains(aSale.getSeller().getAdress())) {
                        sellerCheck = false;
                    }

                    if (!theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getItemFilter().isEmpty() && !theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getItemFilter().contains(aSale.getId().toString())) {
                        itemCheck = false;
                    }

                    if (itemCheck && sellerCheck) {
                        saleToShow.add(aSale);
                        salesHistory.put(aSale.getIdtransaction(), aSale);
                    }
                }
            }
        } else if (mode == BotModeEnum.Stat) {
            for (Sale aSale : newSalesList) {
                if (!statHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(theBot.getStartTime())) {
                    saleToShow.add(aSale);
                    statHistory.put(aSale.getIdtransaction(), aSale);
                }
            }
        } else if (mode == BotModeEnum.ListingAndBidding) {
            for (Sale aSale : newSalesList) {
                if (!listingAndBiddingHistory.containsKey(aSale.getIdtransaction()) && aSale.getTimestamp().isAfter(theBot.getStartTime())) {
                    
                    boolean buyerorsellerCheck = true;
                    boolean itemCheck = true;

                    if (aSale.getType() != SaleTypeEnum.NewFloorOffer && aSale.getType() != SaleTypeEnum.NewBidding) {
                        if (!theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().isEmpty() && !theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().contains(aSale.getSeller().getAdress())) {
                            buyerorsellerCheck = false;
                        }
                    } else {
                        if (!theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().isEmpty() && !theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getSellerFilter().contains(aSale.getBuyer().getAdress())) {
                            buyerorsellerCheck = false;
                        }
                    }

                    if (aSale.getType() != SaleTypeEnum.NewFloorOffer) {
                        if (!theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getItemFilter().isEmpty() && !theBot.getMarketplaces().get(aSale.getMarketplace()).getAllContractsFromThisMarketPlace().get(aSale.getContract()).getItemFilter().contains(aSale.getId().toString())) {
                            itemCheck = false;
                        }
                    }

                    if (itemCheck && buyerorsellerCheck) {
                        saleToShow.add(aSale);
                        listingAndBiddingHistory.put(aSale.getIdtransaction(), aSale);
                    }
                }
            }
        }

        return saleToShow;
    }

    public void removeOldestSales(BotModeEnum mode, HashMap<String, Sale> allNewSales, ArrayList<MarketPlaceEnum> finishedMarketPlaceToCheck) {
        List<String> idsToDelete = new ArrayList<String>();

        if (mode == BotModeEnum.Stat) {
            for (Sale aSale : statHistory.values()) {
                if (!allNewSales.containsKey(aSale.getIdtransaction()) && finishedMarketPlaceToCheck.contains(aSale.getMarketplace())) {
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for (String id : idsToDelete) {
                statHistory.remove(id);
            }
        } else if (mode == BotModeEnum.Sale) {
            for (Sale aSale : salesHistory.values()) {
                if (!allNewSales.containsKey(aSale.getIdtransaction()) && finishedMarketPlaceToCheck.contains(aSale.getMarketplace())) {
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for (String id : idsToDelete) {
                salesHistory.remove(id);
            }
        } else if (mode == BotModeEnum.ListingAndBidding) {
            for (Sale aSale : listingAndBiddingHistory.values()) {
                if (!allNewSales.containsKey(aSale.getIdtransaction()) && finishedMarketPlaceToCheck.contains(aSale.getMarketplace())) {
                    idsToDelete.add(aSale.getIdtransaction());
                }
            }

            for (String id : idsToDelete) {
                listingAndBiddingHistory.remove(id);
            }
        }
    }
}
