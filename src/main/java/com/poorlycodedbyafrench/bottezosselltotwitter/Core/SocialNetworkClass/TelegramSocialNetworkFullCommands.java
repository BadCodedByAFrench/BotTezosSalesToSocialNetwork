/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeAllChatAdministrators;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeAllPrivateChats;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeDefault;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic.GeneralStatisticManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class TelegramSocialNetworkFullCommands {

    /**
     * Class that connect to Discord API
     */
    private transient TelegramBot telegramInstance;

    private String apiKey;

    public void initiateValue(String apiKey) {
        this.apiKey = apiKey;
    }

    public TelegramBot getTelegramInstance() {
        return telegramInstance;
    }

    public void instanceTelegram() throws Exception {

        this.telegramInstance = new TelegramBot(apiKey);

        DeleteMyCommands deleteCmds = new DeleteMyCommands();
        //deleteCmds.languageCode("en");

        BaseResponse responseDelete = telegramInstance.execute(deleteCmds);

        if (responseDelete.errorCode() == 404) {
            throw new Exception("Impossible to connect");
        }
        SetMyCommands cmds = new SetMyCommands(TelegramSocialNetworkFullCommands.getGeneralTelegramCommands(true));

        //SetMyCommands cmdsPrivateChat = new SetMyCommands(TelegramSocialNetworkFullCommands.getGeneralTelegramCommands(true));

        //cmds.languageCode("en");
        //cmds.scope(new BotCommandScopeAllChatAdministrators());
        //telegramInstance.execute(cmdsPrivateChat);
        BaseResponse response = telegramInstance.execute(cmds);
        TelegramSocialNetworkFullCommands.addUpdatesListener(telegramInstance);
    }

    public void stop() {
        //According to the doc ( https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html )
        //I don't need to force shutdown for the update listener because it will stop at one moment
        //But I wont catch these exception
        this.telegramInstance.shutdown();
    }

    public static List<String> allCommandsName() {
        List<String> existingsCommands = new ArrayList<>();

        for (BotCommand aBC : getGeneralTelegramCommands(true)) {
            existingsCommands.add(aBC.command());
        }

        return existingsCommands;
    }

    public static BotCommand[] getGeneralTelegramCommands(boolean addComplete) {
        
        if(addComplete){
           BotCommand[] commands = new BotCommand[]{
            new BotCommand("start", "Start (useless, just needed for telegram)"),
            new BotCommand("help", "Get Dev's discord and link to documentation with all the commands"),
            new BotCommand("get_statcollection", "Get statistic from a collection (parameter 1 : collection's name)"),
            new BotCommand("see_availablecollections", "See all collections available to do commands"),
            new BotCommand("get_random_item", "Get a random item from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("get_specific_item", "Get a specific item from a collection (Objkt only) (parameter 1 : collection's name, parameter 2 : nft ID)"),
            new BotCommand("get_english_auctions", "Get all active english auctions from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("get_dutch_auctions", "Get all active dutch auctions from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("settings", "Settings (useless, just needed for telegram)"),
           new BotCommand("check", "Verify if the bot can be runned (parameter 1 : bot's name)"),
            new BotCommand("start_bot", "Start a bot (parameter 1 : bot's name)"),
            new BotCommand("stop_bot", "Stop a bot (parameter 1 : bot's name)"),
            new BotCommand("status", "Status of a bot (parameter 1 : bot's name)"),
            new BotCommand("create_bot", "Create a new bot (parameter 1 : bot's name)"),
            new BotCommand("add_contract", "Add a contract (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract )"),
            new BotCommand("remove_contract", "Remove a contract (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract )"),
            new BotCommand("add_sellerfilter", "Add a seller filter (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract, parameter 4 : filter)"),
            new BotCommand("remove_sellerfilter", "Remove a seller filter (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract, parameter 4 : filter)"),
            new BotCommand("add_itemfilter", "Add an item filter (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract, parameter 4 : filter)"),
            new BotCommand("remove_itemfilter", "Remove an item filter (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract, parameter 4 : filter)"),
            new BotCommand("set_refreshtimer", "Set refresh timer (parameter 1 : bot's name, parameter 2 : refresher's name)"),
            new BotCommand("add_royaltywalletcontract", "Set a royalty wallet bind to a contract (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract, parameter 4 : wallet)"),
            new BotCommand("remove_royaltywalletcontract", "Remove a royalty wallet bind to a contract (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : contract)"),
            new BotCommand("set_salesmode", "Set if you want to see sales message (parameter 1 : bot's name, parameter 2 : state (True/False))"),
            new BotCommand("set_statmode", "Set if you want to see sales message (parameter 1 : bot's name, parameter 2 : state (True/False))"),
            new BotCommand("set_listingandbiddingmode", "Set if you want to see sales message (parameter 1 : bot's name, parameter 2 : state (True/False))"),
            new BotCommand("show_id_salesmessage", "Set if you want to see the ID on sales message (parameter 1 : bot's name, parameter 2 : state (True/False))"),
            new BotCommand("set_orderby_salesmessages", "Choose if you want to order sales by the older time or the maximum price (parameter 1 : bot's name, parameter 2 : 0/1 (timestamp/price))"),
            new BotCommand("show_typesales_messages", "Set if you want to see the type of sale on sale message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_addresssales_messages", "Set if you want to see the buyer/sellet address on sale message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_ipfssales_messages", "Set if you want to see the IPFS image on sale message (when possible) (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_royaltywallet_salesmessages", "Set if you want to see the royalty wallet on sale message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("add_salesentence", "Add a beggining sentence for a sale message (parameter 1 : bot's name, parameter 2 : sentence)"),
            new BotCommand("remove_salesentence", "Remove a beggining sentence for a sale message (parameter 1 : bot's name, parameter 2 : sentence)"),
            new BotCommand("add_hashtag", "Add a hashtag for message (parameter 1 : bot's name, parameter 2 : hashtag)"),
            new BotCommand("remove_hashtag", "Remove a hashtag for message (parameter 1 : bot's name, parameter 2 : hashtag)"),
            new BotCommand("set_royaltylabel", "Set the label of royalty wallet in message (parameter 1 : bot's name, parameter 2 : label)"),
            new BotCommand("show_id_statmessage", "Set if you want to see the ID on stats message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_averageprice_statsmessages", "Set if you want to see the average XTZ amount on stats message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_minprice_statsmessages", "Set if you want to see the minimum XTZ amount on stats message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_maxprice_statsmessages", "Set if you want to see the maxmimum XTZ amount on stats message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_royaltywallet_statsmessages", "Set if you want to see the royalty wallet on stat message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_id_listingandbiddingmessage", "Set if you want to see the ID on listing and bidding message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_ipfs_listingandbidding", "Set if you want to see the IPFS on listing and bidding message (when possible) (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_address_listingandbidding", "Set if you want to see the buyer/seller on listing and bidding message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_listing_listingandbidding", "Set if you want to see the new listing message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_englishauction_list", "Set if you want to see the new english auction message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_dutchauction_list", "Set if you want to see the new dutch auction message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_flooroffer_list", "Set if you want to see the new floor offer message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("show_bidding_list", "Set if you want to see the new bidding auction message (parameter 1 : bot's name, parameter 2 : show (True/False))"),
            new BotCommand("add_listsentence", "Add a sentence for a listing and bidding event (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : sentence)"),
            new BotCommand("remove_listsentence", "Remove a sentence for a listing and bidding event (parameter 1 : bot's name, parameter 2 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 3 : sentence)"),
            new BotCommand("see_refresher", "See all the existing refreshers"),
            new BotCommand("see_allbots", "See all the existing bot"),
            new BotCommand("see_botcontracts", "See all the existing contracts for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botsalesentences", "See all the existing sale sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_bothashtags", "See all the existing hashtag for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botlistingsentences", "See all the existing listing sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botenglishauctionsentences", "See all the existing english auction sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botdutchauctionsentences", "See all the existing dutch auction sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botflooroffersentences", "See all the existing floor offer sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botbiddingsentences", "See all the existing bidding sentences for a bot (parameter 1 : bot's name)"),
            new BotCommand("see_botgeneralconfig", "See the general configuration for a bot (parameter 1 : bot's name)"),
            new BotCommand("import_config", "Import a configuration from another social network (parameter 1 : id)"),
            new BotCommand("remove_config", "Remove a configuration from another social network (parameter 1 : id)"),
            new BotCommand("see_id", "See id of your telegram channel"),
            new BotCommand("quick_start", "Quick start a new bot (parameter 1 : bot's name, parameter 2, refresher's name, parameter 3 : marketplace (Objkt/fxhash/Teia/Rarible), parameter 4 : contract)"),
            new BotCommand("remove_bot", "Remove a bot (parameter 1 : bot's name)"),};

        return commands; 
            
        }
        else{
            BotCommand[] commands = new BotCommand[]{
            new BotCommand("start", "Start (useless, just needed for telegram)"),
            new BotCommand("help", "Get Dev's discord and link to documentation with all the commands"),
            new BotCommand("get_statcollection", "Get statistic from a collection (parameter 1 : collection's name)"),
            new BotCommand("see_availablecollections", "See all collections available to do commands"),
            new BotCommand("get_random_item", "Get a random item from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("get_specific_item", "Get a specific item from a collection (Objkt only) (parameter 1 : collection's name, parameter 2 : nft ID)"),
            new BotCommand("get_english_auctions", "Get all active english auctions from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("get_dutch_auctions", "Get all active dutch auctions from a collection (Objkt only) (parameter 1 : collection's name)"),
            new BotCommand("settings", "Settings (useless, just needed for telegram)"),};

        return commands;
        }
    }

    private static List<String> keyUsed;

    public static void addUpdatesListener(TelegramBot bot) {

        if (keyUsed == null) {
            keyUsed = new ArrayList<>();
        }

        if (!keyUsed.contains(bot.getToken())) {
            keyUsed.add(bot.getToken());
            bot.setUpdatesListener(updates -> {
                for (Update update : updates) {

                    Message message = update.message();
                    if (message != null) {
                        String content = message.text();
                        if (content.startsWith("/")) {
                            String[] commandAndArgument = content.split(" ");

                            String command = commandAndArgument[0].substring(1, commandAndArgument[0].length());

                            if (allCommandsName().contains(command)) {
                                Long chatId = message.chat().id();

                                String answer = doCommand(commandAndArgument, chatId.toString(), command);

                                SendMessage msg = new SendMessage(chatId,
                                        answer);
                                bot.execute(msg);
                            }
                        }
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            });
        }
    }

    private static List<String> getContractsBindToAChatId(String chatId) {
        List<String> contracts = new ArrayList<>();

        for (GenericBot g : GenericBotManager.getGenericBotManager().getGenericsBots().values()) {
            if (g.getChannelId() != null) {
                if (g.getChannelId().equals(chatId)) {
                    for (Bot aBot : g.getBotsForTheServer().values()) {
                        contracts.addAll(aBot.getAllContracts());
                    }

                }
            }
        }

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {

            if (aBot.getSnProfile() != null) {

                if (aBot.getSnProfile().getTelegram() != null) {

                    if (aBot.getSnProfile().getTelegram().getChannelId() != null) {
                        if (aBot.getSnProfile().getTelegram().getChannelId().equals(chatId)) {
                            contracts.addAll(aBot.getAllContracts());
                        }
                    }
                }
            }
        }

        return contracts;
    }

    private synchronized static String doCommand(String[] commandAndArgument, String chatId, String command) {
        String answer = "";

        GenericBotManager.getGenericBotManager().checkChatExist(chatId);
        List<String> authorizedContracts = getContractsBindToAChatId(chatId);

        if (answer.equals("")) {
            try {
                switch (command) {

                    case "start":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().start();
                        break;
                    case "settings":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().setting();
                        break;

                    case "help":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().help();
                        break;
                    case "see_avaiblecollections":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().seeAvailableCollections(authorizedContracts);
                        break;
                    case "get_statcollection":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().getStatCollection(commandAndArgument[1], authorizedContracts);
                        break;
                    case "get_random_item":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().getRandomItem(commandAndArgument[1], authorizedContracts);
                        break;
                    case "get_specific_item":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().getSpecificItem(commandAndArgument[1], Integer.valueOf(commandAndArgument[2]), authorizedContracts);
                        break;
                    case "get_english_auctions":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().getEnglishAuctions(commandAndArgument[1], authorizedContracts);
                        break;
                    case "get_dutch_auctions":
                        answer = GeneralStatisticManager.getGeneralStatisticManager().getDutchAuctions(commandAndArgument[1], authorizedContracts);
                        break;

                    case "check":
                        answer = GenericBotManager.getGenericBotManager().check(commandAndArgument[1], chatId);
                        break;
                    case "start_bot":
                        answer = GenericBotManager.getGenericBotManager().start(commandAndArgument[1], chatId);
                        break;
                    case "stop_bot":
                        answer = GenericBotManager.getGenericBotManager().stop(commandAndArgument[1], chatId);
                        break;
                    case "status":
                        answer = GenericBotManager.getGenericBotManager().status(commandAndArgument[1], chatId);
                        break;
                    case "create_bot":
                        answer = GenericBotManager.getGenericBotManager().createBot(commandAndArgument[1], chatId);
                        break;
                    case "add_contract":
                        answer = GenericBotManager.getGenericBotManager().addContract(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], chatId);
                        break;
                    case "remove_contract":
                        answer = GenericBotManager.getGenericBotManager().removeContract(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], chatId);
                        break;
                    case "add_sellerfilter":
                        answer = GenericBotManager.getGenericBotManager().addSellerFilter(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], commandAndArgument[4], chatId);
                        break;
                    case "remove_sellerfilter":
                        answer = GenericBotManager.getGenericBotManager().removeSellerFilter(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], commandAndArgument[4], chatId);
                        break;
                    case "add_itemfilter":
                        answer = GenericBotManager.getGenericBotManager().addItemFilter(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], commandAndArgument[4], chatId);
                        break;
                    case "remove_itemfilter":
                        answer = GenericBotManager.getGenericBotManager().removeItemFilter(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], commandAndArgument[4], chatId);
                        break;
                    case "set_refreshtimer":
                        answer = GenericBotManager.getGenericBotManager().setMpProfile(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;
                    case "add_royaltywalletcontract":
                        answer = GenericBotManager.getGenericBotManager().setRoyaltyWalletContract(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], commandAndArgument[4], chatId);
                        break;
                    case "remove_royaltywalletcontract":
                        answer = GenericBotManager.getGenericBotManager().setRoyaltyWalletContract(commandAndArgument[1], MarketPlaceEnum.valueOf(commandAndArgument[2]), commandAndArgument[3], "", chatId);
                        break;
                    case "set_salesmode":
                        answer = GenericBotManager.getGenericBotManager().setSalesMode(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "set_statmode":
                        answer = GenericBotManager.getGenericBotManager().setStatMode(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "set_listingandbiddingmode":
                        answer = GenericBotManager.getGenericBotManager().setListingAndBiddingMode(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_id_salesmessage":
                        answer = GenericBotManager.getGenericBotManager().showIdSalesMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "set_orderby_salesmessages":
                        answer = GenericBotManager.getGenericBotManager().setOrderBySalesMessages(commandAndArgument[1], Integer.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_typesales_messages":
                        answer = GenericBotManager.getGenericBotManager().showTypeSalesMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_addresssales_messages":
                        answer = GenericBotManager.getGenericBotManager().showAddressSalesMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_ipfssales_messages":
                        answer = GenericBotManager.getGenericBotManager().showIPFSSalesMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_royaltywallet_salesmessages":
                        answer = GenericBotManager.getGenericBotManager().showRoyaltyWalletSalesMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "add_salesentence":
                        answer = GenericBotManager.getGenericBotManager().addSalesSentence(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;

                    case "remove_salesentence":
                        answer = GenericBotManager.getGenericBotManager().removeSalesSentence(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;
                    case "add_hashtag":
                        answer = GenericBotManager.getGenericBotManager().addHashtag(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;

                    case "remove_hashtag":
                        answer = GenericBotManager.getGenericBotManager().removeHashtag(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;
                    case "set_royaltylabel":
                        answer = GenericBotManager.getGenericBotManager().labelRoyaltyWallet(commandAndArgument[1], rebuildString(commandAndArgument, 2), chatId);
                        break;

                    case "show_id_statmessage":
                        answer = GenericBotManager.getGenericBotManager().showIdStatsMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_averageprice_statsmessages":
                        answer = GenericBotManager.getGenericBotManager().showAveragePriceStatsMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_minprice_statsmessages":
                        answer = GenericBotManager.getGenericBotManager().showMinPriceStatsMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_maxprice_statsmessages":
                        answer = GenericBotManager.getGenericBotManager().showMaxPriceStatsMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_royaltywallet_statsmessages":
                        answer = GenericBotManager.getGenericBotManager().showRoyaltyWalletStatsMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_id_listingandbiddingmessage":
                        answer = GenericBotManager.getGenericBotManager().showIdListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_ipfs_listingandbidding":
                        answer = GenericBotManager.getGenericBotManager().showIPFSListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_address_listingandbidding":
                        answer = GenericBotManager.getGenericBotManager().showAddressListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_listing_listingandbidding":
                        answer = GenericBotManager.getGenericBotManager().showListingListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_englishauction_list":
                        answer = GenericBotManager.getGenericBotManager().showEnglishAuctionListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_dutchauction_list":
                        answer = GenericBotManager.getGenericBotManager().showDutchAuctionListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "show_flooroffer_list":
                        answer = GenericBotManager.getGenericBotManager().showFloorOfferListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;
                    case "show_bidding_list":
                        answer = GenericBotManager.getGenericBotManager().showBiddingListingAndBiddingMessages(commandAndArgument[1], Boolean.valueOf(commandAndArgument[2]), chatId);
                        break;

                    case "add_listsentence":
                        answer = GenericBotManager.getGenericBotManager().addListingAndBiddingSentence(commandAndArgument[1], SaleTypeEnum.valueOf(commandAndArgument[2]), rebuildString(commandAndArgument, 3), chatId);
                        break;
                    case "remove_listsentence":
                        answer = GenericBotManager.getGenericBotManager().removeListingAndBiddingSentence(commandAndArgument[1], SaleTypeEnum.valueOf(commandAndArgument[2]), rebuildString(commandAndArgument, 3), chatId);
                        break;

                    case "see_refresher":
                        answer = GenericBotManager.getGenericBotManager().seeRefresher();
                        break;

                    case "see_allbots":
                        answer = GenericBotManager.getGenericBotManager().seeAllBots(chatId);
                        break;
                    case "see_botcontracts":
                        answer = GenericBotManager.getGenericBotManager().seeBotContracts(commandAndArgument[1], chatId);
                        break;
                    case "see_botsalesentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotSaleSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_bothashtags":
                        answer = GenericBotManager.getGenericBotManager().seeBotHashtags(commandAndArgument[1], chatId);
                        break;
                    case "see_botlistingsentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotNewListingSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_botenglishauctionsentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotNewEnglishAuctionSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_botdutchauctionsentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotNewDutchAuctionSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_botflooroffersentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotNewFloorOfferSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_botbiddingsentences":
                        answer = GenericBotManager.getGenericBotManager().seeBotNewBiddingSentences(commandAndArgument[1], chatId);
                        break;
                    case "see_botgeneralconfig":
                        answer = GenericBotManager.getGenericBotManager().seeBotGeneralConfig(commandAndArgument[1], chatId);
                        break;
                    case "import_config":
                        answer = GenericBotManager.getGenericBotManager().importConfig(commandAndArgument[1], SocialNetworkEnum.Telegram, chatId, null);
                        break;
                    case "remove_config":
                        answer = GenericBotManager.getGenericBotManager().removeConfig(commandAndArgument[1], SocialNetworkEnum.Telegram, chatId);
                        break;
                    case "see_id":
                        answer = "Your id is : " + chatId;
                        break;
                    case "quick_start":
                        answer = GenericBotManager.getGenericBotManager().quickStart(commandAndArgument[1], commandAndArgument[2], MarketPlaceEnum.valueOf(commandAndArgument[3]), commandAndArgument[4], chatId);
                        break;
                    case "remove_bot":
                        answer = GenericBotManager.getGenericBotManager().createBot(commandAndArgument[1], chatId);
                        break;
                    default:
                        answer = "Command not found";
                        break;

                }
            } catch (Exception e) {
                answer = "Fail : One or multiple arguments are missing, please fill all of them";
            }
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            LogManager.getLogManager().writeLog(TelegramSocialNetworkFullCommands.class.getName(), ex);
        }

        return answer;
    }

    private static String rebuildString(String[] commandAndArgument, int startindex) throws Exception {
        String rebuildedString = "";

        for (int i = startindex; i < commandAndArgument.length; i++) {
            if (!rebuildedString.isEmpty()) {
                rebuildedString += " ";
            }
            rebuildedString += commandAndArgument[i];
        }

        return rebuildedString;

    }

}
