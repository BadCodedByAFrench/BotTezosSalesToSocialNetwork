/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 *
 * @author david
 */
public class DiscordCommandSlashListener extends ListenerAdapter {

    @Override
    public synchronized void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String answer = "";
        Guild guild = event.getGuild();
        if (guild != null) {

            answer = GenericBotManager.getGenericBotManager().checkIdExist(guild.getId(), guild);
            List<String> authorizedContracts = getContractsBindToAJDA(event.getTextChannel());

            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.deferReply().setEphemeral(true).queue(); // Tell discord we received the command, send a thinking... message to the user

                if (answer.equals("")) {
                    try {
                        switch (event.getName()) {

                            case "help":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().help();
                                break;
                            case "see_availablecollections":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().seeAvailableCollections(authorizedContracts);
                                break;
                            case "get_statcollection":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getStatCollection(event.getOption("collection").getAsString().trim(), authorizedContracts);
                                break;
                            case "get_random_item":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getRandomItem(event.getOption("collection").getAsString().trim(), authorizedContracts);
                                break;
                            case "get_specific_item":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getSpecificItem(event.getOption("collection").getAsString().trim(), event.getOption("id").getAsInt(), authorizedContracts);
                                break;
                            case "get_english_auctions":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getEnglishAuctions(event.getOption("collection").getAsString().trim(), authorizedContracts);
                                break;
                            case "get_dutch_auctions":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getDutchAuctions(event.getOption("collection").getAsString().trim(), authorizedContracts);
                                break;

                            case "check":
                                answer = GenericBotManager.getGenericBotManager().check(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "start_bot":
                                answer = GenericBotManager.getGenericBotManager().start(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "stop_bot":
                                answer = GenericBotManager.getGenericBotManager().stop(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "status":
                                answer = GenericBotManager.getGenericBotManager().status(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "create_bot":
                                answer = GenericBotManager.getGenericBotManager().createBot(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "add_contract":
                                answer = GenericBotManager.getGenericBotManager().addContract(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), guild.getId());
                                break;
                            case "remove_contract":
                                answer = GenericBotManager.getGenericBotManager().removeContract(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), guild.getId());
                                break;
                            case "add_sellerfilter":
                                answer = GenericBotManager.getGenericBotManager().addSellerFilter(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), event.getOption("filter").getAsString().trim(), guild.getId());
                                break;
                            case "remove_sellerfilter":
                                answer = GenericBotManager.getGenericBotManager().removeSellerFilter(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString(), event.getOption("filter").getAsString().trim(), guild.getId());
                                break;
                            case "add_itemfilter":
                                answer = GenericBotManager.getGenericBotManager().addItemFilter(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), event.getOption("filter").getAsString().trim(), guild.getId());
                                break;
                            case "remove_itemfilter":
                                answer = GenericBotManager.getGenericBotManager().removeItemFilter(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), event.getOption("filter").getAsString().trim(), guild.getId());
                                break;
                            case "set_refreshtimer":
                                answer = GenericBotManager.getGenericBotManager().setMpProfile(event.getOption("botname").getAsString().trim(), event.getOption("refreshername").getAsString().trim(), guild.getId());
                                break;
                            case "add_royaltywalletcontract":
                                answer = GenericBotManager.getGenericBotManager().setRoyaltyWalletContract(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), event.getOption("wallet").getAsString().trim(), guild.getId());
                                break;
                            case "remove_royaltywalletcontract":
                                answer = GenericBotManager.getGenericBotManager().setRoyaltyWalletContract(event.getOption("botname").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), "", guild.getId());
                                break;
                            case "set_salesmode":
                                answer = GenericBotManager.getGenericBotManager().setSalesMode(event.getOption("botname").getAsString().trim(), event.getOption("state").getAsBoolean(), guild.getId());
                                break;
                            case "set_statmode":
                                answer = GenericBotManager.getGenericBotManager().setStatMode(event.getOption("botname").getAsString().trim(), event.getOption("state").getAsBoolean(), guild.getId());
                                break;
                            case "set_listingandbiddingmode":
                                answer = GenericBotManager.getGenericBotManager().setListingAndBiddingMode(event.getOption("botname").getAsString().trim(), event.getOption("state").getAsBoolean(), guild.getId());
                                break;
                            case "set_channel":
                                answer = GenericBotManager.getGenericBotManager().setChannel(guild, event.getTextChannel());
                                break;
                            case "show_id_salesmessage":
                                answer = GenericBotManager.getGenericBotManager().showIdSalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "set_orderby_salesmessages":
                                answer = GenericBotManager.getGenericBotManager().setOrderBySalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("type").getAsInt(), guild.getId());
                                break;
                            case "show_typesales_messages":
                                answer = GenericBotManager.getGenericBotManager().showTypeSalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_addresssales_messages":
                                answer = GenericBotManager.getGenericBotManager().showAddressSalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_ipfssales_messages":
                                answer = GenericBotManager.getGenericBotManager().showIPFSSalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_royaltywallet_salesmessages":
                                answer = GenericBotManager.getGenericBotManager().showRoyaltyWalletSalesMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "add_salesentence":
                                answer = GenericBotManager.getGenericBotManager().addSalesSentence(event.getOption("botname").getAsString().trim(), event.getOption("sentence").getAsString().trim(), guild.getId());
                                break;

                            case "remove_salesentence":
                                answer = GenericBotManager.getGenericBotManager().removeSalesSentence(event.getOption("botname").getAsString().trim(), event.getOption("sentence").getAsString().trim(), guild.getId());
                                break;
                            case "add_hashtag":
                                answer = GenericBotManager.getGenericBotManager().addHashtag(event.getOption("botname").getAsString().trim(), event.getOption("hashtag").getAsString().trim(), guild.getId());
                                break;

                            case "remove_hashtag":
                                answer = GenericBotManager.getGenericBotManager().removeHashtag(event.getOption("botname").getAsString().trim(), event.getOption("hashtag").getAsString().trim(), guild.getId());
                                break;
                            case "set_royaltylabel":
                                answer = GenericBotManager.getGenericBotManager().labelRoyaltyWallet(event.getOption("botname").getAsString().trim(), event.getOption("label").getAsString().trim(), guild.getId());
                                break;

                            case "show_id_statmessage":
                                answer = GenericBotManager.getGenericBotManager().showIdStatsMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_averageprice_statsmessages":
                                answer = GenericBotManager.getGenericBotManager().showAveragePriceStatsMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_minprice_statsmessages":
                                answer = GenericBotManager.getGenericBotManager().showMinPriceStatsMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_maxprice_statsmessages":
                                answer = GenericBotManager.getGenericBotManager().showMaxPriceStatsMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_royaltywallet_statsmessages":
                                answer = GenericBotManager.getGenericBotManager().showRoyaltyWalletStatsMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_id_listingandbiddingmessage":
                                answer = GenericBotManager.getGenericBotManager().showIdListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_ipfs_listingandbidding":
                                answer = GenericBotManager.getGenericBotManager().showIPFSListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_address_listingandbidding":
                                answer = GenericBotManager.getGenericBotManager().showAddressListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_listing_listingandbidding":
                                answer = GenericBotManager.getGenericBotManager().showListingListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_englishauction_list":
                                answer = GenericBotManager.getGenericBotManager().showEnglishAuctionListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_dutchauction_list":
                                answer = GenericBotManager.getGenericBotManager().showDutchAuctionListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "show_flooroffer_list":
                                answer = GenericBotManager.getGenericBotManager().showFloorOfferListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;
                            case "show_bidding_list":
                                answer = GenericBotManager.getGenericBotManager().showBiddingListingAndBiddingMessages(event.getOption("botname").getAsString().trim(), event.getOption("show").getAsBoolean(), guild.getId());
                                break;

                            case "add_listsentence":
                                answer = GenericBotManager.getGenericBotManager().addListingAndBiddingSentence(event.getOption("botname").getAsString().trim(), SaleTypeEnum.valueOf(event.getOption("event").getAsString().trim()), event.getOption("sentence").getAsString(), guild.getId());
                                break;
                            case "remove_listsentence":
                                answer = GenericBotManager.getGenericBotManager().removeListingAndBiddingSentence(event.getOption("botname").getAsString().trim(), SaleTypeEnum.valueOf(event.getOption("event").getAsString().trim()), event.getOption("sentence").getAsString(), guild.getId());
                                break;
                            case "see_refresher":
                                answer = GenericBotManager.getGenericBotManager().seeRefresher();
                                break;

                            case "see_allbots":
                                answer = GenericBotManager.getGenericBotManager().seeAllBots(guild.getId());
                                break;
                            case "see_botcontracts":
                                answer = GenericBotManager.getGenericBotManager().seeBotContracts(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botsalesentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotSaleSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_bothashtags":
                                answer = GenericBotManager.getGenericBotManager().seeBotHashtags(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botlistingsentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotNewListingSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botenglishauctionsentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotNewEnglishAuctionSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botdutchauctionsentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotNewDutchAuctionSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botflooroffersentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotNewFloorOfferSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botbiddingsentences":
                                answer = GenericBotManager.getGenericBotManager().seeBotNewBiddingSentences(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "see_botgeneralconfig":
                                answer = GenericBotManager.getGenericBotManager().seeBotGeneralConfig(event.getOption("botname").getAsString().trim(), guild.getId());
                                break;
                            case "import_config":
                                answer = GenericBotManager.getGenericBotManager().importConfig(event.getOption("botname").getAsString().trim(), SocialNetworkEnum.Telegram, guild.getId(), event.getTextChannel());
                                break;
                            case "remove_config":
                                answer = GenericBotManager.getGenericBotManager().removeConfig(event.getOption("botname").getAsString().trim(), SocialNetworkEnum.Telegram, guild.getId());
                                break;
                            case "see_id":
                                answer = "Your id is : " + guild.getId();
                                break;
                            case "quick_start":
                                answer = GenericBotManager.getGenericBotManager().quickStart(event.getOption("botname").getAsString().trim(), event.getOption("refreshername").getAsString().trim(), MarketPlaceEnum.valueOf(event.getOption("marketplace").getAsString().trim()), event.getOption("contract").getAsString().trim(), guild, event.getTextChannel());
                                break;
                            case "remove_bot":
                                answer = GenericBotManager.getGenericBotManager().removeBot(event.getOption("botname").getAsString().trim(), guild.getId());
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
                    LogManager.getLogManager().writeLog(DiscordCommandSlashListener.class.getName(), ex);
                }

                event.getHook().sendMessage(answer).setEphemeral(true).queue();
            }
            else{
                event.deferReply().setEphemeral(true).queue(); // Tell discord we received the command, send a thinking... message to the user

                if (answer.equals("")) {
                    try {
                        switch (event.getName()) {

                            case "help":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().help();
                                break;
                            case "see_availablecollections":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().seeAvailableCollections(authorizedContracts);
                                break;
                            case "get_statcollection":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getStatCollection(event.getOption("collection").getAsString(), authorizedContracts);
                                break;
                            case "get_random_item":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getRandomItem(event.getOption("collection").getAsString(), authorizedContracts);
                                break;
                            case "get_specific_item":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getSpecificItem(event.getOption("collection").getAsString(), event.getOption("id").getAsInt(), authorizedContracts);
                                break;
                            case "get_english_auctions":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getEnglishAuctions(event.getOption("collection").getAsString(), authorizedContracts);
                                break;
                            case "get_dutch_auctions":
                                answer = GeneralStatisticManager.getGeneralStatisticManager().getDutchAuctions(event.getOption("collection").getAsString(), authorizedContracts);
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
                    LogManager.getLogManager().writeLog(DiscordCommandSlashListener.class.getName(), ex);
                }

                event.getHook().sendMessage(answer).setEphemeral(true).queue();
            }
        }
    }

    private List<String> getContractsBindToAJDA(TextChannel aTextChannel) {
        List<String> contracts = new ArrayList<>();

        for (GenericBot g : GenericBotManager.getGenericBotManager().getGenericsBots().values()) {
            if (g.getDiscordHome() == aTextChannel) {

                for (Bot aBot : g.getBotsForTheServer().values()) {
                    contracts.addAll(aBot.getAllContracts());
                }

            }
        }

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {

            if (aBot.getSnProfile() != null) {

                if (aBot.getSnProfile().getDiscord() != null) {

                    if (aBot.getSnProfile().getDiscord().getTextChannel() == aTextChannel) {
                        contracts.addAll(aBot.getAllContracts());
                    }
                }
            }
        }

        return contracts;
    }
}
