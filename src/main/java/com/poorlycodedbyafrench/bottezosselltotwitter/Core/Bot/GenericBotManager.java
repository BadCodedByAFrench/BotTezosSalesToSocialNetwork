/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.SearchContractSales;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordGeneralCommands;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordGuildJoinListener;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetworkFullCommands;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetworkFullCommands;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author david
 */
public class GenericBotManager {

    private static GenericBotManager genericBotManager;

    private DiscordSocialNetworkFullCommands genericDiscord;

    private TelegramSocialNetworkFullCommands genericTelegram;

    private HashMap<String, GenericBot> genericsBots;

    private GenericBotManager() {
        genericsBots = new HashMap<>();
    }

    public void removeGenericBot(String id) {
        for (Bot aBot : genericsBots.get(id).getBotsForTheServer().values()) {
            if (aBot.getBotStatus() == BotStatusEnum.Running) {
                aBot.stop();
            }
        }

        genericsBots.remove(id);
    }

    public static void importGenericBotManager(GenericBotManager genericBotManager) throws Exception {
        
        if(genericBotManager.getGenericDiscord() != null){
            genericBotManager.getGenericDiscord().instanceDiscord();
        }
        
        if(genericBotManager.getGenericTelegram() != null){
            genericBotManager.getGenericTelegram().instanceTelegram();
        }
        
        GenericBotManager.genericBotManager = genericBotManager;
    }

    public void setGenericTelegram(TelegramSocialNetworkFullCommands genericTelegram) {
        this.genericTelegram = genericTelegram;
    }

    public void setGenericDiscord(DiscordSocialNetworkFullCommands genericDiscord) {
        
        this.genericDiscord = genericDiscord;
        
        if(this.genericDiscord  != null){
            generateGenericBotDiscord();
        }
    }

    private void generateGenericBotDiscord() {
        clearGenericBotFromSocialNetwork(SocialNetworkEnum.Discord);
        genericDiscord.getJda();
        for (Guild g : genericDiscord.getJda().getGuilds()) {
            
            genericsBots.put(g.getId(), new GenericBot(g.getId(), SocialNetworkEnum.Discord));

            DiscordGeneralCommands.updateCommands(g, true);
        }
    }

    private void clearGenericBotFromSocialNetwork(SocialNetworkEnum socialNetwork) {
        List<String> keys = new ArrayList<>();

        for (String aKey : genericsBots.keySet()) {

            if (genericsBots.get(aKey).getOrigin() == socialNetwork) {
                keys.add(aKey);
            }
        }

        for (String aKeyToRemove : keys) {
            genericsBots.remove(aKeyToRemove);
        }
    }

    public static GenericBotManager getGenericBotManager() {
        if (genericBotManager == null) {
            genericBotManager = new GenericBotManager();
        }
        return genericBotManager;
    }

    public String checkIdExist(String id) {
        if (genericsBots.containsKey(id)) {
            return "";
        }

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {

            if (aBot.getSnProfile() != null) {

                if (aBot.getSnProfile().getTelegram() != null) {

                    if (aBot.getSnProfile().getTelegram().getChannelId() == id) {
                        return "";
                    }
                }
            }
        }

        return "Your server is not repertoried inside the bot please kick/reinvite him or contact the dev";
    }

    public String checkIdExist(String id, Guild aGuild) {
        if (genericsBots.containsKey(id)) {
            return "";
        }

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {

            if (aBot.getSnProfile() != null) {

                if (aBot.getSnProfile().getDiscord() != null) {

                    if (aBot.getSnProfile().getDiscord().getJda() != null) {

                        for (Guild g : aBot.getSnProfile().getDiscord().getJda().getGuilds()) {

                            if (g == aGuild) {
                                return "";
                            }
                        }
                    }
                }
            }
        }

        return "Your server is not repertoried inside the bot please kick/reinvite him or contact the dev";
    }

    public void checkChatExist(String id) {
        if (!genericsBots.containsKey(id)) {
            genericsBots.put(id, new GenericBot(id, SocialNetworkEnum.Telegram));
            genericsBots.get(id).setChannelId(id);
        }
    }

    private String checkBotExist(String name, String id) {
        if (genericsBots.get(id).getBotsForTheServer().containsKey(name)) {
            return "";
        }
        return "This bot doesn't exist ! Create it before !";
    }

    private String botIsRunning(String name, String id) {

        if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Running) {
            return "This bot is running, you need to stop him before to retry the command";
        }
        return "";
    }

    public void createGenericBot(String id, SocialNetworkEnum origin) {
        genericsBots.put(id, new GenericBot(id, origin));
    }

    public String quickStart(String name, String mpProfile, MarketPlaceEnum mp, String contract, Guild guild, TextChannel textChannel) {

        String answer = "";

        this.setChannel(guild, textChannel);
        this.createBot(name, guild.getId());
        answer = this.setMpProfile(name, mpProfile, guild.getId());

        if (answer.equals("Marketplace profile set")) {
            this.addContract(name, mp, contract, guild.getId());
            this.start(name, guild.getId());
        } else {
            this.removeBot(name, guild.getId());
            answer = "The refresherProfile name you gave is not correct, please try again after doing /seeRefresher to know the avaible one";
        }

        return answer;
    }

    public String quickStart(String name, String mpProfile, MarketPlaceEnum mp, String contract, String id) {

        String answer = "";

        this.checkChatExist(id);
        this.createBot(name, id);
        answer = this.setMpProfile(name, mpProfile, id);

        if (answer.equals("Marketplace profile set")) {
            this.addContract(name, mp, contract, id);
            this.start(name, id);
        } else {
            this.removeBot(name, id);
            answer = "The refresherProfile name you gave is not correct, please try again after doing /seeRefresher to know the avaible one";
        }

        return answer;
    }

    public String check(String name, String id) {
        String answer = "";
        answer = checkBotExist(name, id);

        if (answer.equals("")) {

            genericsBots.get(id).getBotsForTheServer().get(name).checkComplete();

            if (!genericsBots.get(id).getBotsForTheServer().get(name).checkMode()) {
                answer = "Please set one mode (sale/stat/listing and bidding) at true";
            }

            if (genericsBots.get(id).getBotsForTheServer().get(name).getMpProfile() == null) {

                if (!answer.isBlank()) {
                    answer += "\n";
                }
                answer += "Please choose a refresher profile";
            }

            if (genericsBots.get(id).getBotsForTheServer().get(name).getAllContracts().size() == 0) {

                if (!answer.isBlank()) {
                    answer += "\n";
                }
                answer += "Please insert at least one contract";
            }

            if (answer.isBlank()) {
                answer = "The bot is runnable !";
            }
        }

        return answer;
    }

    public String start(String name, String id) {
        String answer = "";
        answer = checkBotExist(name, id);

        if (answer.equals("")) {

            if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Running) {
                answer = "Bot is already running !";
            } else {
                DiscordSocialNetwork discord = null;
                TelegramSocialNetwork telegram = null;

                if (genericsBots.get(id).getDiscordHome() != null) {
                    discord = new DiscordSocialNetwork(genericDiscord.getJda(), genericsBots.get(id).getDiscordHome());
                }

                if (genericsBots.get(id).getChannelId() != null) {
                    telegram = new TelegramSocialNetwork(genericTelegram.getTelegramInstance(), genericsBots.get(id).getChannelId());
                }

                SocialNetworkProfile snp = new SocialNetworkProfile();

                snp.setDiscord(discord);
                snp.setTelegram(telegram);

                genericsBots.get(id).getBotsForTheServer().get(name).setSnProfile(snp);
                genericsBots.get(id).getBotsForTheServer().get(name).checkComplete();

                if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Ready) {
                    genericsBots.get(id).getBotsForTheServer().get(name).start();
                    answer = "Bot will start";
                } else if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Uncompleted) {
                    answer = "Bot can't start, he's not completed !";
                }
            }
        }

        return answer;
    }

    public String stop(String name, String id) {
        String answer = "";
        answer = checkBotExist(name, id);

        if (answer.equals("")) {

            if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Running) {
                genericsBots.get(id).getBotsForTheServer().get(name).stop();
                answer = "Bot will stop";
            } else if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Ready) {
                answer = "Bot is already not running !";
            } else if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Uncompleted) {
                answer = "Bot can't stop (and start too), he's not completed !";
            }
        }

        return answer;
    }

    public String status(String name, String id) {
        String answer = "";
        answer = checkBotExist(name, id);

        if (answer.equals("")) {

            answer = "Current bot status : " + genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus();
        }

        return answer;
    }

    public String createBot(String name, String id) {

        String answer = "";
        if (genericsBots.get(id).getDiscordHome() != null || genericsBots.get(id).getChannelId() != null) {
            genericsBots.get(id).addBot(name);
            answer = "Bot created, please complete it now";
        } else {
            answer = "Set up your home channel first !";
        }

        return answer;
    }

    public String removeBot(String name, String id) {

        String answer = "";
        if (genericsBots.containsKey(id)) {

            if (genericsBots.get(id).getBotsForTheServer().get(name).getBotStatus() == BotStatusEnum.Running) {
                genericsBots.get(id).getBotsForTheServer().get(name).stop();
            }

            genericsBots.get(id).removeBot(name);
            answer = "Bot removed";
        } else {
            answer = "This bot do not exist ! ";
        }

        return answer;
    }

    public String addContract(String name, MarketPlaceEnum mp, String contract, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().put(contract, new SearchContractSales(contract));
                answer = "Contract added";
            }

        }
        return answer;
    }

    public String removeContract(String name, MarketPlaceEnum mp, String contract, String id) {
        String answer = "";
        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            if (answer.equals("")) {
                answer = botIsRunning(name, id);
                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().remove(contract);
                answer = "Contract removed";
            }
        }
        return answer;
    }

    public String addSellerFilter(String name, MarketPlaceEnum mp, String contract, String sellerFilter, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().get(contract).addSellerFilter(sellerFilter);
                answer = "Seller filter added";
            }
        }
        return answer;
    }

    public String removeSellerFilter(String name, MarketPlaceEnum mp, String contract, String sellerFilter, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().get(contract).removeSellerFilter(sellerFilter);
                answer = "Seller filter removed";
            }
        }
        return answer;
    }

    public String addItemFilter(String name, MarketPlaceEnum mp, String contract, String itemFilter, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().get(contract).addItemFilter(itemFilter);
                answer = "Item filter added";
            }
        }
        return answer;

    }

    public String removeItemFilter(String name, MarketPlaceEnum mp, String contract, String itemFilter, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().get(contract).removeItemFilter(itemFilter);
                answer = "Item filter removed";
            }
        }
        return answer;
    }

    public String setMpProfile(String name, String mpProfile, String id) {

        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {

                if (MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().containsKey(mpProfile)) {
                    MarketPlaceProfile mpToSet = MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().get(mpProfile);
                    genericsBots.get(id).getBotsForTheServer().get(name).setMpProfile(mpToSet);
                    answer = "Marketplace profile set";
                } else {
                    answer = "This marketplace profile doesn't exist ! Please check the spelling or the existing one with /showMarketPlace";
                }

            }
        }
        return answer;
    }

    public String setRoyaltyWalletContract(String name, MarketPlaceEnum mp, String contract, String royaltyWalletContract, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().get(mp).getAllContractsFromThisMarketPlace().get(contract).setRoyaltyWallet(royaltyWalletContract);
                answer = "Royalty wallet changed";
            }
        }
        return answer;
    }

    public String setSalesMode(String name, boolean activate, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setSalesRunning(activate);
                answer = "Sale mode changed";
            }
        }
        return answer;
    }

    public String setStatMode(String name, boolean activate, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setStatRunning(activate);
                answer = "Stat mode changed";
            }
        }
        return answer;
    }

    public String setListingAndBiddingMode(String name, boolean activate, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setListingAndBiddingRunning(activate);
                answer = "Listing and Bidding mode changed";
            }
        }
        return answer;
    }

    public String setChannel(Guild guild, TextChannel textChannel) {

        for (Bot aBot : genericsBots.get(guild.getId()).getBotsForTheServer().values()) {
            if (aBot.getBotStatus() == BotStatusEnum.Running) {
                return "At least one bot is running, please stop him/them before to do this command";
            }
        }
        genericsBots.get(guild.getId()).setDiscordHome(textChannel);

        return "Home defined";
    }

    public String showIdSalesMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setSecurityIdSales(show);
                answer = "Show the Id Sale changed";
            }
        }
        return answer;
    }

    public String setOrderBySalesMessages(String name, int type, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setOrderBy(type);
                answer = "The OrderBy is changed";
            }
        }
        return answer;
    }

    public String showTypeSalesMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setSaleType(show);
                answer = "Show the sale type changed";
            }
        }
        return answer;
    }

    public String showAddressSalesMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setAdress(show);
                answer = "Show the address changed";
            }
        }
        return answer;
    }

    public String showIPFSSalesMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setIpfs(show);
                answer = "Show the IPFS sale changed";
            }
        }
        return answer;
    }

    public String showRoyaltyWalletSalesMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setRoyaltywalletsale(show);
                answer = "Show the royalty wallet sale changed";
            }
        }
        return answer;
    }

    public String addSalesSentence(String name, String sentence, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).getSentences().add(sentence);
                answer = "Sentence added";
            }
        }
        return answer;
    }

    public String removeSalesSentence(String name, String sentence, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                if (genericsBots.get(id).getBotsForTheServer().get(name).getSentences().contains(sentence)) {
                    genericsBots.get(id).getBotsForTheServer().get(name).getSentences().remove(sentence);
                    answer = "Sentence removed";
                } else {
                    answer = "This sentence do not exist";
                }

            }
        }
        return answer;
    }

    public String addHashtag(String name, String hashtag, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).getHashtags().add(hashtag);
                answer = "Hashtag added";
            }
        }
        return answer;
    }

    public String removeHashtag(String name, String hashtag, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                if (genericsBots.get(id).getBotsForTheServer().get(name).getHashtags().contains(hashtag)) {
                    genericsBots.get(id).getBotsForTheServer().get(name).getHashtags().remove(hashtag);
                    answer = "Hashtag removed";
                } else {
                    answer = "This hashtag do not exist";
                }

            }
        }
        return answer;
    }

    public String labelRoyaltyWallet(String name, String label, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setNameroyaltywallet(label);
                answer = "Name of the label for royalty wallet changed";
            }
        }
        return answer;
    }

    public String showIdStatsMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setSecurityIdStats(show);
                answer = "Show the Id stat changed";
            }
        }
        return answer;
    }

    public String showAveragePriceStatsMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setAvgPriceStat(show);
                answer = "Show the average price changed";
            }
        }
        return answer;
    }

    public String showMinPriceStatsMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setMinPriceStat(show);
                answer = "Show the minimum price changed";
            }
        }
        return answer;
    }

    public String showMaxPriceStatsMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setMaxPriceStat(show);
                answer = "Show the maximum price changed";
            }
        }
        return answer;
    }

    public String showRoyaltyWalletStatsMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setRoyaltywalletstat(show);
                answer = "Show the royalty wallet stat changed";
            }
        }
        return answer;
    }

    public String showIdListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setSecurityIdListingAndBidding(show);
                answer = "Show the Id listing and bidding changed";
            }
        }
        return answer;
    }

    public String showIPFSListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setIpfsListingAndBidding(show);
                answer = "Show the IPFS listing and bidding changed";
            }
        }
        return answer;
    }

    public String showAddressListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setAdressListingAndBidding(show);
                answer = "Show the address listing and bidding changed";
            }
        }
        return answer;
    }

    public String showListingListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setActivateListing(show);
                answer = "Show the listing changed";
            }
        }
        return answer;
    }

    public String showEnglishAuctionListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setActivateEnglishAuction(show);
                answer = "Show the english auction changed";
            }
        }
        return answer;
    }

    public String showDutchAuctionListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setActivateDutchAuction(show);
                answer = "Show the dutch auction changed";
            }
        }
        return answer;
    }

    public String showFloorOfferListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setActivateFloorOffer(show);
                answer = "Show the floor offer changed";
            }
        }
        return answer;
    }

    public String showBiddingListingAndBiddingMessages(String name, boolean show, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                genericsBots.get(id).getBotsForTheServer().get(name).setActivateBidding(show);
                answer = "Show the bidding changed";
            }
        }
        return answer;
    }

    public String addListingAndBiddingSentence(String name, SaleTypeEnum event, String sentence, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                switch (event) {
                    case NewList:
                        genericsBots.get(id).getBotsForTheServer().get(name).getSentencesListing().add(sentence);
                        break;
                    case NewEnglishAuction:
                        genericsBots.get(id).getBotsForTheServer().get(name).getSentencesEnglishAuction().add(sentence);
                        break;
                    case NewDutchAuction:
                        genericsBots.get(id).getBotsForTheServer().get(name).getSentencesDutchAuction().add(sentence);
                        break;
                    case NewFloorOffer:
                        genericsBots.get(id).getBotsForTheServer().get(name).getSentencesFloorOffer().add(sentence);
                        break;
                    case NewBidding:
                        genericsBots.get(id).getBotsForTheServer().get(name).getSentencesBidding().add(sentence);
                        break;
                }

                answer = "Sentence added";
            }
        }
        return answer;
    }

    public String removeListingAndBiddingSentence(String name, SaleTypeEnum event, String sentence, String id) {
        String answer = "";
        answer = checkBotExist(name, id);
        if (answer.equals("")) {
            answer = botIsRunning(name, id);
            if (answer.equals("")) {
                switch (event) {
                    case NewList:
                        if (genericsBots.get(id).getBotsForTheServer().get(name).getSentencesListing().contains(sentence)) {
                            genericsBots.get(id).getBotsForTheServer().get(name).getSentencesListing().remove(sentence);
                        } else {
                            answer = "This sentence do not exist for this event";
                        }

                        break;
                    case NewEnglishAuction:
                        if (genericsBots.get(id).getBotsForTheServer().get(name).getSentencesEnglishAuction().contains(sentence)) {
                            genericsBots.get(id).getBotsForTheServer().get(name).getSentencesEnglishAuction().remove(sentence);
                        } else {
                            answer = "This sentence do not exist for this event";
                        }

                        break;
                    case NewDutchAuction:
                        if (genericsBots.get(id).getBotsForTheServer().get(name).getSentencesDutchAuction().contains(sentence)) {
                            genericsBots.get(id).getBotsForTheServer().get(name).getSentencesDutchAuction().remove(sentence);
                        } else {
                            answer = "This sentence do not exist for this event";
                        }
                        break;
                    case NewFloorOffer:
                        if (genericsBots.get(id).getBotsForTheServer().get(name).getSentencesFloorOffer().contains(sentence)) {
                            genericsBots.get(id).getBotsForTheServer().get(name).getSentencesFloorOffer().remove(sentence);
                        } else {
                            answer = "This sentence do not exist for this event";
                        }
                        break;
                    case NewBidding:
                        if (genericsBots.get(id).getBotsForTheServer().get(name).getSentencesBidding().contains(sentence)) {
                            genericsBots.get(id).getBotsForTheServer().get(name).getSentencesBidding().remove(sentence);
                        } else {
                            answer = "This sentence do not exist for this event";
                        }
                        break;
                }

                if (answer.isBlank()) {
                    answer = "Sentence removed";
                }

            }
        }
        return answer;
    }

    public String seeRefresher() {
        String answer = "";

        for (MarketPlaceProfile mpp : MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().values()) {

            if (!answer.equals("")) {
                answer += "\n";
            }
            answer += mpp.fullDetail();
        }

        if (answer.equals("")) {
            answer = "There's no refresher, please contact your hoster and ask him to add some. Because you can't create a bot";
        }

        return answer;
    }

    public String seeAllBots(String id) {
        String answer = "";

        for (Bot aBot : genericsBots.get(id).getBotsForTheServer().values()) {

            if (!answer.equals("")) {
                answer += "\n";
            }

            answer += aBot.getName();
        }

        if (answer.equals("")) {
            answer += "There's no existing bot";
        }

        return answer;
    }

    public String seeBotContracts(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (MarketPlace aMp : genericsBots.get(id).getBotsForTheServer().get(name).getMarketplaces().values()) {
                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aMp.getMarketplace();

                for (SearchContractSales aContract : aMp.getAllContractsFromThisMarketPlace().values()) {
                    answer += "\n" + aContract.toString();
                }
            }

            if (answer.equals("")) {
                answer += "There's no existing contract";
            }
        }

        return answer;
    }

    public String seeBotSaleSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentences()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }
            if (answer.equals("")) {
                answer += "There's no sale sentence";
            }
        }

        return answer;
    }

    public String seeBotHashtags(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aHashtag : genericsBots.get(id).getBotsForTheServer().get(name).getHashtags()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aHashtag;
            }
            if (answer.equals("")) {
                answer += "There's no hashtag";
            }
        }

        return answer;
    }

    public String seeBotNewListingSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentencesListing()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }
            if (answer.equals("")) {
                answer += "There's no listing sentence";
            }
        }

        return answer;
    }

    public String seeBotNewEnglishAuctionSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentencesEnglishAuction()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }
            if (answer.equals("")) {
                answer += "There's no english auction sentence";
            }
        }

        return answer;
    }

    public String seeBotNewDutchAuctionSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentencesDutchAuction()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }
            if (answer.equals("")) {
                answer += "There's no dutch auction sentence";
            }
        }

        return answer;
    }

    public String seeBotNewFloorOfferSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentencesFloorOffer()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }

            if (answer.equals("")) {
                answer += "There's no floor offer sentence";
            }
        }

        return answer;
    }

    public String seeBotNewBiddingSentences(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {
            for (String aSentence : genericsBots.get(id).getBotsForTheServer().get(name).getSentencesBidding()) {

                if (!answer.equals("")) {
                    answer += "\n";
                }

                answer += aSentence;
            }
            if (answer.equals("")) {
                answer += "There's no bidding sentence";
            }
        }

        return answer;
    }

    public String seeBotGeneralConfig(String name, String id) {
        String answer = "";

        answer = checkBotExist(name, id);

        if (answer.equals("")) {

            Bot bot = genericsBots.get(id).getBotsForTheServer().get(name);

            answer += "Show id stat : " + bot.isSecurityIdSales();
            answer += "\nShow id stat : " + bot.isSecurityIdStats();
            answer += "\nShow id listing & bidding : " + bot.isSecurityIdListingAndBidding();

            answer += "\nShow sale type on sale message : " + bot.isSaleType();
            answer += "\nShow address on sale message : " + bot.isAdress();
            answer += "\nShow IPFS on sale message : " + bot.isIpfs();
            answer += "\nOrder sale by : ";

            if (bot.getOrderBy() == 0) {
                answer += "Timestamp (older to newer)";
            } else {
                answer += "Price (Highest to lowest)";
            }

            answer += "\nShow royalty wallet on sale message : " + bot.isRoyaltywalletsale();

            answer += "\nShow average price on stat message : " + bot.isAvgPriceStat();
            answer += "\nShow minimum price on stat message : " + bot.isMinPriceStat();
            answer += "\nShow maximum price on stat message : " + bot.isMaxPriceStat();
            answer += "\nShow royalty wallet on stat message : " + bot.isRoyaltywalletstat();

            answer += "\nShow address on listing and bidding message : " + bot.isAdressListingAndBidding();
            answer += "\nShow IPFS on listing and bidding message : " + bot.isIpfsListingAndBidding();

            answer += "\nShow new listing on listing and bidding message : " + bot.isActivateListing();
            answer += "\nShow new english auction on listing and bidding message : " + bot.isActivateEnglishAuction();
            answer += "\nShow new dutch on listing and bidding message : " + bot.isActivateDutchAuction();
            answer += "\nShow new floor offer on listing and bidding message : " + bot.isActivateFloorOffer();
            answer += "\nShow new bidding on listing and bidding message : " + bot.isActivateBidding();

            answer += "\nlabel of the royalty wallet in sale and stat message : " + bot.getNameroyaltywallet();

        }
        return answer;
    }

    public String importConfig(String idToImport, SocialNetworkEnum origin, String id, TextChannel newPlace) {
        String answer = "";

        if (origin == SocialNetworkEnum.Telegram) {
            genericsBots.get(idToImport).setChannelId(id);
        } else if (origin == SocialNetworkEnum.Discord) {
            genericsBots.get(idToImport).setDiscordHome(newPlace);
        }

        return answer;
    }

    public String removeConfig(String idToRemove, SocialNetworkEnum origin, String id) {
        String answer = "";

        if (origin == SocialNetworkEnum.Telegram) {
            genericsBots.get(idToRemove).setChannelId("");
        } else if (origin == SocialNetworkEnum.Discord) {
            genericsBots.get(idToRemove).setDiscordHome(null);
        }

        return answer;
    }

    public DiscordSocialNetworkFullCommands getGenericDiscord() {
        return genericDiscord;
    }

    public TelegramSocialNetworkFullCommands getGenericTelegram() {
        return genericTelegram;
    }

    public HashMap<String, GenericBot> getGenericsBots() {
        return genericsBots;
    }

}
