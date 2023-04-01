/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 *
 * @author david
 */
public class DiscordGeneralCommands extends ListenerAdapter {

    public static void updateCommands(Guild g, boolean addComplete) {

        List<SlashCommandData> allCommands = new ArrayList<>();

        //start is a mandatory command for Telegram that I don't see utility for discord
        /*allCommands.add(Commands.slash("start", "Start"));*/
        allCommands.add(Commands.slash("help", "Get Dev's discord and link to documentation with all the commands"));

        //settings is a mandatory command for Telegram that I don't see utility for discord
        /*allCommands.add(Commands.slash("settings", "Create a new bot")
                .addOption(OptionType.STRING, "botname", "The name of the new bot"));*/
        allCommands.add(Commands.slash("get_statcollection", "Get statistic from a collection")
                .addOption(OptionType.STRING, "collection", "Contract to get data"));

        allCommands.add(Commands.slash("see_availablecollections", "See all collections available to do commands"));

        allCommands.add(Commands.slash("get_random_item", "Get a random item from a collection (Objkt only)")
                .addOption(OptionType.STRING, "collection", "Contract to get item"));

        allCommands.add(Commands.slash("get_specific_item", "Get a specific item from a collection (Objkt only)")
                .addOption(OptionType.STRING, "collection", "Collection to get item")
                .addOption(OptionType.INTEGER, "id", "Id of the item"));

        allCommands.add(Commands.slash("get_english_auctions", "Get all active english auctions from a collection (Objkt only)")
                .addOption(OptionType.STRING, "collection", "Collection to get auctions"));

        allCommands.add(Commands.slash("get_dutch_auctions", "Get all active dutch auctions from a collection (Objkt only)")
                .addOption(OptionType.STRING, "collection", "Collection to get auctions"));

        if (addComplete) {

            allCommands.add(Commands.slash("check", "Verify if the bot can be runned")
                    .addOption(OptionType.STRING, "botname", "Bot to check"));

            allCommands.add(Commands.slash("start_bot", "Start a bot")
                    .addOption(OptionType.STRING, "botname", "Bot to start"));

            allCommands.add(Commands.slash("stop_bot", "Stop a bot")
                    .addOption(OptionType.STRING, "botname", "Bot to stop"));

            allCommands.add(Commands.slash("status", "Status of a bot")
                    .addOption(OptionType.STRING, "botname", "Bot to get the status"));

            allCommands.add(Commands.slash("create_bot", "Create a new bot")
                    .addOption(OptionType.STRING, "botname", "The name of the new bot"));

            allCommands.add(Commands.slash("add_contract", "Add a contract")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract to add"));

            allCommands.add(Commands.slash("remove_contract", "Remove a contract")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract to remove"));

            allCommands.add(Commands.slash("add_sellerfilter", "Add a seller filter")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the filter")
                    .addOption(OptionType.STRING, "filter", "filter to add"));

            allCommands.add(Commands.slash("remove_sellerfilter", "Remove a seller filter")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the filter")
                    .addOption(OptionType.STRING, "filter", "filter to remove"));

            allCommands.add(Commands.slash("add_itemfilter", "Add an item filter")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the filter")
                    .addOption(OptionType.STRING, "filter", "filter to add"));

            allCommands.add(Commands.slash("remove_itemfilter", "Remove an item filter")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the filter")
                    .addOption(OptionType.STRING, "filter", "filter to remove"));

            allCommands.add(Commands.slash("set_refreshtimer", "Set refresh timer")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "refreshername", "The name of the refresher"));

            allCommands.add(Commands.slash("add_royaltywalletcontract", "Set a royalty wallet bind to a contract")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the wallet")
                    .addOption(OptionType.STRING, "wallet", "wallet to set"));

            allCommands.add(Commands.slash("remove_royaltywalletcontract", "Remove a royalty wallet bind to a contract")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract bind to the wallet"));

            allCommands.add(Commands.slash("set_salesmode", "Set if you want to see sales message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "state", "True/False"));

            allCommands.add(Commands.slash("set_statmode", "Set if you want to see sales message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "state", "True/False"));

            allCommands.add(Commands.slash("set_listingandbiddingmode", "Set if you want to see sales message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "state", "True/False"));

            allCommands.add(Commands.slash("set_channel", "Set the channel where the bot will send message"));

            allCommands.add(Commands.slash("show_id_salesmessage", "Set if you want to see the ID on sales message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("set_orderby_salesmessages", "Choose if you want to order sales by the older time or the maximum price")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.INTEGER, "type", "marketPlace bind to the contract")
                                    .addChoice("Timestamp", 0)
                                    .addChoice("Price", 1)
                    ));

            allCommands.add(Commands.slash("show_typesales_messages", "Set if you want to see the type of sale on sale message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_addresssales_messages", "Set if you want to see the buyer/sellet address on sale message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_ipfssales_messages", "Set if you want to see the IPFS image on sale message (when possible)")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_royaltywallet_salesmessages", "Set if you want to see the royalty wallet on sale message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("add_salesentence", "Add a beggining sentence for a sale message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "sentence", "Sentence to add"));

            allCommands.add(Commands.slash("remove_salesentence", "Remove a beggining sentence for a sale message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "sentence", "Sentence to remove"));

            allCommands.add(Commands.slash("add_hashtag", "Add a hashtag for message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "hashtag", "Hashtag to add"));

            allCommands.add(Commands.slash("remove_hashtag", "Remove a hashtag for message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "hashtag", "Hashtag to remove"));

            allCommands.add(Commands.slash("set_royaltylabel", "Set the label of royalty wallet in message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "label", "New label"));

            allCommands.add(Commands.slash("show_id_statmessage", "Set if you want to see the ID on stats message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_averageprice_statsmessages", "Set if you want to see the average XTZ amount on stats message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_minprice_statsmessages", "Set if you want to see the minimum XTZ amount on stats message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_maxprice_statsmessages", "Set if you want to see the maxmimum XTZ amount on stats message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_royaltywallet_statsmessages", "Set if you want to see the royalty wallet on stat message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_id_listingandbiddingmessage", "Set if you want to see the ID on listing and bidding message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_ipfs_listingandbidding", "Set if you want to see the IPFS on listing and bidding message (when possible)")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_address_listingandbidding", "Set if you want to see the buyer/seller on listing and bidding message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_listing_listingandbidding", "Set if you want to see the new listing message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_englishauction_list", "Set if you want to see the new english auction message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_dutchauction_list", "Set if you want to see the new dutch auction message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_flooroffer_list", "Set if you want to see the new floor offer message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("show_bidding_list", "Set if you want to see the new bidding auction message")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.BOOLEAN, "show", "True/False"));

            allCommands.add(Commands.slash("add_listsentence", "Add a sentence for a listing and bidding event")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "event", "marketPlace bind to the contract")
                                    .addChoice(SaleTypeEnum.NewList.name(), SaleTypeEnum.NewList.name())
                                    .addChoice(SaleTypeEnum.EnglishAuction.name(), SaleTypeEnum.EnglishAuction.name())
                                    .addChoice(SaleTypeEnum.DutchAuction.name(), SaleTypeEnum.DutchAuction.name())
                                    .addChoice(SaleTypeEnum.NewFloorOffer.name(), SaleTypeEnum.NewFloorOffer.name())
                                    .addChoice(SaleTypeEnum.NewBidding.name(), SaleTypeEnum.NewBidding.name())
                    )
                    .addOption(OptionType.STRING, "sentence", "Sentence to add"));

            allCommands.add(Commands.slash("remove_listsentence", "Remove a sentence for a listing and bidding event")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOptions(
                            new OptionData(OptionType.STRING, "event", "marketPlace bind to the contract")
                                    .addChoice(SaleTypeEnum.NewList.name(), SaleTypeEnum.NewList.name())
                                    .addChoice(SaleTypeEnum.EnglishAuction.name(), SaleTypeEnum.EnglishAuction.name())
                                    .addChoice(SaleTypeEnum.DutchAuction.name(), SaleTypeEnum.DutchAuction.name())
                                    .addChoice(SaleTypeEnum.NewFloorOffer.name(), SaleTypeEnum.NewFloorOffer.name())
                                    .addChoice(SaleTypeEnum.NewBidding.name(), SaleTypeEnum.NewBidding.name())
                    )
                    .addOption(OptionType.STRING, "sentence", "Sentence to remove"));

            allCommands.add(Commands.slash("see_refresher", "See all the existing refreshers"));

            allCommands.add(Commands.slash("see_allbots", "See all the existing bot"));

            allCommands.add(Commands.slash("see_botcontracts", "See all the existing contracts for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botsalesentences", "See all the existing sale sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_bothashtags", "See all the existing hashtag for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botlistingsentences", "See all the existing listing sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botenglishauctionsentences", "See all the existing english auction sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botdutchauctionsentences", "See all the existing dutch auction sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botflooroffersentences", "See all the existing floor offer sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botbiddingsentences", "See all the existing bidding sentences for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            allCommands.add(Commands.slash("see_botgeneralconfig", "See the general configuration for a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot"));

            /*allCommands.add(Commands.slash("import_config", "Import a configuration from another social network")
                    .addOption(OptionType.STRING, "idtoimport", "The telegram id to import"));

            allCommands.add(Commands.slash("remove_config", "Remove a configuration from another social network")
                    .addOption(OptionType.STRING, "idtoremove", "The telegram id to remove"));
            */
            allCommands.add(Commands.slash("see_id", "See id of your discord"));

            allCommands.add(Commands.slash("quick_start", "Quick start a new bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot")
                    .addOption(OptionType.STRING, "refreshername", "The name of the refresher")
                    .addOptions(
                            new OptionData(OptionType.STRING, "marketplace", "marketPlace bind to the contract")
                                    .addChoice(MarketPlaceEnum.Objkt.name(), MarketPlaceEnum.Objkt.name())
                                    .addChoice(MarketPlaceEnum.fxhash.name(), MarketPlaceEnum.fxhash.name())
                                    .addChoice(MarketPlaceEnum.Teia.name(), MarketPlaceEnum.Teia.name())
                                    .addChoice(MarketPlaceEnum.Rarible.name(), MarketPlaceEnum.Rarible.name())
                    )
                    .addOption(OptionType.STRING, "contract", "contract to add"));

            allCommands.add(Commands.slash("remove_bot", "Remove a bot")
                    .addOption(OptionType.STRING, "botname", "The name of the bot to remove"));
        }

        g.updateCommands().addCommands(allCommands).queue();
    }
}
