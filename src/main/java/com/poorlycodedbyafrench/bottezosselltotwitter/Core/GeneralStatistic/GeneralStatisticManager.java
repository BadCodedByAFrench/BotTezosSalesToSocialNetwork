/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author david
 */
public class GeneralStatisticManager {

    private HashMap<String, GeneralStatistic> allStats;

    private static GeneralStatisticManager generalStatisticManager;

    private GeneralStatisticManager() {
        allStats = new HashMap<>();
    }

    public static GeneralStatisticManager getGeneralStatisticManager() {
        if (generalStatisticManager == null) {
            generalStatisticManager = new GeneralStatisticManager();
        }

        return generalStatisticManager;
    }

    public void updateData(GeneralStatistic statistic) {

        if (allStats.containsKey(statistic.getContract())) {
            allStats.replace(statistic.getContract(), statistic);
        } else {
            allStats.put(statistic.getContract(), statistic);
        }
    }

    public HashMap<String, GeneralStatistic> getAllStats() {
        return allStats;
    }

    public String start() {
        return "Thanks for using the bot!\nLook at /help to get a link to the dev's discord and a documentation to all the commands";
    }

    public String help() {
        return "Dev's discord : https://t.co/LlCJxmRwzr , Commands : https://docs.google.com/spreadsheets/d/1sdAywUqe7uM_H1xRUSvXtRvK5Gbmk2jQ/edit?usp=sharing&ouid=100586592199084415995&rtpof=true&sd=true ";
    }

    public String setting() {
        return "There's no real setting for the bot itself";
    }

    public String seeAvailableCollections(List<String> collections) {

        String answer = "";
        for (GeneralStatistic aGeneralStatistic : allStats.values()) {

            if (collections.contains(aGeneralStatistic.getContract())) {

                if (!answer.equals("")) {
                    answer += "\n";
                }
                answer += "For " + aGeneralStatistic.getName() + " use " + aGeneralStatistic.getPath();
            }
        }

        if (answer.equals("")) {
            answer = "No collections available  !";
        }

        return answer;
    }

    public String getStatCollection(String collection, List<String> authorizedContracts) {

        String answer = "";
        for (GeneralStatistic aGeneralStatistic : allStats.values()) {

            if (collection.equals(aGeneralStatistic.getPath()) && authorizedContracts.contains(aGeneralStatistic.getContract())) {

                answer += "Floor price : " + aGeneralStatistic.getFloorPrice();
                answer += "\nAll time volume : " + aGeneralStatistic.getAllTimeVolume();
                answer += "\n24h volume : " + aGeneralStatistic.getLastDayVolume();
                answer += "\nNumber of items : " + aGeneralStatistic.getItemNumber();
                answer += "\nActive listing : " + aGeneralStatistic.getCountActiveListing();
                answer += "\nActive auction : " + aGeneralStatistic.getCountActiveAuctions();
            }
        }

        if (answer.equals("")) {
            answer = "Collection not found !";
        }

        return answer;
    }

    public String getRandomItem(String collection, List<String> authorizedContracts) {

        String answer = "";
        for (GeneralStatistic aGeneralStatistic : allStats.values()) {

            if (collection.equals(aGeneralStatistic.getPath()) && aGeneralStatistic.getListMps().contains(MarketPlaceEnum.Objkt) && authorizedContracts.contains(aGeneralStatistic.getContract()) ) {

                Random rand = new Random();

                answer = "https://objkt.com/asset/" + aGeneralStatistic.getPath() + "/" + rand.nextLong(aGeneralStatistic.getItemNumber());
            }
        }

        if (answer.equals("")) {
            answer = "Collection not found !";
        }

        return answer;
    }

    public String getSpecificItem(String collection, int itemNumber, List<String> authorizedContracts) {

        String answer = "";
        if (itemNumber > 0) {
            for (GeneralStatistic aGeneralStatistic : allStats.values()) {

                if (collection.equals(aGeneralStatistic.getPath()) && aGeneralStatistic.getListMps().contains(MarketPlaceEnum.Objkt) && authorizedContracts.contains(aGeneralStatistic.getContract()) ) {

                    if (itemNumber <= aGeneralStatistic.getItemNumber()) {
                        answer = "https://objkt.com/asset/" + aGeneralStatistic.getPath() + "/" + itemNumber;
                    } else {
                        answer = "The number must be lower or equals than " + aGeneralStatistic.getItemNumber();
                    }
                }
            }

            if (answer.equals("")) {
                answer = "Collection not found !";
            }
        } else {
            answer = "The number must be superior than 0";
        }

        return answer;
    }

    public String getEnglishAuctions(String collection, List<String> authorizedContracts) {
        String answer = "";
        for (GeneralStatistic aGeneralStatistic : allStats.values()) {

            if (collection.equals(aGeneralStatistic.getPath()) && aGeneralStatistic.getListMps().contains(MarketPlaceEnum.Objkt) && authorizedContracts.contains(aGeneralStatistic.getContract()) ) {

                for (String id : aGeneralStatistic.getActiveIdEnglishAuction()) {
                    if (!answer.equals("")) {
                        answer += "\n";
                    }
                    answer += "https://objkt.com/asset/" + aGeneralStatistic.getPath() + "/" + id;
                }

                if (answer.equals("")) {
                    answer = "There's no active english auction !";
                }
            }
        }

        if (answer.equals("")) {
            answer = "Collection not found !";
        }

        return answer;
    }
    
    public String getDutchAuctions(String collection, List<String> authorizedContracts) {
        String answer = "";
        for (GeneralStatistic aGeneralStatistic : allStats.values()) {

            if (collection.equals(aGeneralStatistic.getPath()) && aGeneralStatistic.getListMps().contains(MarketPlaceEnum.Objkt) && authorizedContracts.contains(aGeneralStatistic.getContract()) ) {

                for (String id : aGeneralStatistic.getActiveIdDutchAuction()) {
                    if (!answer.equals("")) {
                        answer += "\n";
                    }
                    answer += "https://objkt.com/asset/" + aGeneralStatistic.getPath() + "/" + id;
                }

                if (answer.equals("")) {
                    answer = "There's no active dutch auction !";
                }
            }
        }

        if (answer.equals("")) {
            answer = "Collection not found !";
        }

        return answer;
    }

    public HashMap<MarketPlaceEnum, List<String>> allContractPerMarketPlace() {
        HashMap<MarketPlaceEnum, List<String>> allContractsPerMp = new HashMap<>();
        allContractsPerMp.put(MarketPlaceEnum.Objkt, new ArrayList<>());
        allContractsPerMp.put(MarketPlaceEnum.fxhash, new ArrayList<>());

        for (GenericBot genericBot : GenericBotManager.getGenericBotManager().getGenericsBots().values()) {
            for (Bot aBot : genericBot.getBotsForTheServer().values()) {

                for (MarketPlaceEnum aMP : aBot.getMarketplaces().keySet()) {

                    if (allContractsPerMp.containsKey(aMP)) {
                        allContractsPerMp.get(aMP).addAll(aBot.getMarketplaces().get(aMP).getAllContractsString());
                    }
                }
            }
        }

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {

            for (MarketPlaceEnum aMP : aBot.getMarketplaces().keySet()) {

                if (allContractsPerMp.containsKey(aMP)) {
                    allContractsPerMp.get(aMP).addAll(aBot.getMarketplaces().get(aMP).getAllContractsString());
                }
            }
        }

        return allContractsPerMp;
    }

    public void clear(MarketPlaceEnum mp) {

        List<String> statToRemove = new ArrayList<>();
        for (GeneralStatistic stat : allStats.values()) {

            if (stat.getListMps().contains(mp)) {

                if (stat.getListMps().size() == 1) {
                    statToRemove.add(stat.getContract());
                } else {
                    stat.getListMps().remove(mp);
                }
            }
        }

        for (String contract : statToRemove) {
            allStats.remove(contract);
        }
    }

    public void add(GeneralStatistic stat, MarketPlaceEnum mp) {
        String contract = stat.getContract();

        if (allStats.containsKey(contract)) {
            allStats.get(contract).getListMps().add(mp);

            if (mp == MarketPlaceEnum.Objkt) {
                allStats.get(contract).replace(stat);
            }
        } else {
            stat.getListMps().add(mp);
            allStats.put(stat.getContract(), stat);
        }
    }
}
