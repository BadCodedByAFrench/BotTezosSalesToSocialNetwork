/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.HashMap;

/**
 *
 * @author david
 */
public class SocialNetworkProfileManager {
    
    private static SocialNetworkProfileManager socialNetworkProfileManager;
    
    private HashMap<String, SocialNetworkProfile> allSocialNetworkProfile;
    
    private HashMap<String, TwitterSocialNetwork> allTwitter;
    
    private HashMap<String, TelegramSocialNetwork> allTelegram;
    
    private HashMap<String, DiscordSocialNetwork> allDiscord;
    
    private HashMap<SocialNetworkEnum, Integer> maxNumberOfProfil;
    
    
    public static SocialNetworkProfileManager getSocialNetworkProfileManager() {

        if (socialNetworkProfileManager == null) {
            socialNetworkProfileManager = new SocialNetworkProfileManager();
        }
        return socialNetworkProfileManager;

    }
    
    private SocialNetworkProfileManager(){
        
        allSocialNetworkProfile = new HashMap<>();
        allTwitter = new HashMap<>();
        allTelegram = new HashMap<>();
        allDiscord = new HashMap<>();
        
        maxNumberOfProfil = new HashMap<>();
        maxNumberOfProfil.put(SocialNetworkEnum.Twitter,1);
        maxNumberOfProfil.put(SocialNetworkEnum.Discord,40);
        maxNumberOfProfil.put(SocialNetworkEnum.Telegram,30);
        
    }
    
    public void addSocialNetworkProfile(SocialNetworkProfile socialNetworkprofile) {
        allSocialNetworkProfile.put(socialNetworkprofile.getName(), socialNetworkprofile);        
    }

    public void removeSocialNetworkProfile(SocialNetworkProfile socialNetworkprofile) {
        allSocialNetworkProfile.remove(socialNetworkprofile.getName());
    }

    public HashMap<String,SocialNetworkProfile> getAllSocialNetworkProfile() {
        return allSocialNetworkProfile;
    }

    public HashMap<SocialNetworkEnum, Integer> getMaxNumberOfProfil() {
        return maxNumberOfProfil;
    }
    
    public void addTwitter(TwitterSocialNetwork twitter) {
        allTwitter.put(twitter.getProfileName(), twitter);        
    }

    public void removeTwitter(TwitterSocialNetwork twitter) {
        allTwitter.remove(twitter.getProfileName());
    }
    
    public HashMap<String, TwitterSocialNetwork> getAllTwitter() {
        return allTwitter;
    }
    
    public void addTelegram(TelegramSocialNetwork telegram) {
        allTelegram.put(telegram.getProfileName(), telegram);        
    }

    public void removeTelegram(TelegramSocialNetwork telegram) {
        allTelegram.remove(telegram.getProfileName());
    }

    public HashMap<String, TelegramSocialNetwork> getAllTelegram() {
        return allTelegram;
    }
    
    public void addDiscord(DiscordSocialNetwork discord) {
        allDiscord.put(discord.getProfileName(), discord);        
    }

    public void removeDiscord(DiscordSocialNetwork discord) {
        allDiscord.remove(discord.getProfileName());
    }

    public HashMap<String, DiscordSocialNetwork> getAllDiscord() {
        return allDiscord;
    }

    public void importSnProfiles(HashMap<String, SocialNetworkProfile> allSocialNetworkProfile){
        this.allSocialNetworkProfile = allSocialNetworkProfile;
        
        allDiscord.clear();
        allTelegram.clear();
        allTwitter.clear();
        
        for(SocialNetworkProfile snp : this.allSocialNetworkProfile.values()){
            
            for(SocialNetworkInterface sn : snp.getAllSocialNetwork()){
                
                if(sn instanceof DiscordSocialNetwork){
                    DiscordSocialNetwork discord = (DiscordSocialNetwork) sn;
                    allDiscord.put(discord.getProfileName(), discord);
                }
                else if(sn instanceof TelegramSocialNetwork){
                    TelegramSocialNetwork telegram = (TelegramSocialNetwork) sn;
                    allTelegram.put(telegram.getProfileName(), telegram);
                }
                else if(sn instanceof TwitterSocialNetwork){
                    TwitterSocialNetwork twitter = (TwitterSocialNetwork) sn;
                    allTwitter.put(twitter.getProfileName(), twitter);
                }
                
                sn.instance();
            }
        }
    }
    
}
