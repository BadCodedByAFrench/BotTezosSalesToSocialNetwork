/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Ad;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.AdCampaignStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author david
 */
public class AdCampaignManager {

    private HashMap<String, AdCampaign> allAdCompaigns;

    private static AdCampaignManager adCampaignManager;
    
    private AdCampaignManager() {
        allAdCompaigns = new HashMap<>();
    }

    public void addCompaign(AdCampaign anAdCompaign) {

        if (!allAdCompaigns.containsKey(anAdCompaign.getTitle())) {
            allAdCompaigns.put(anAdCompaign.getTitle(), anAdCompaign);
        }
    }
    
    public void removeCompaign(AdCampaign anAdCompaign) {
        if (allAdCompaigns.containsKey(anAdCompaign.getTitle())) {
            allAdCompaigns.remove(anAdCompaign.getTitle());
        }
    }

    public HashMap<String, AdCampaign> getAllAdCompaigns() {
        return allAdCompaigns;
    }

    public static AdCampaignManager getAdCampaignManager() {
        if(adCampaignManager == null){
            adCampaignManager = new AdCampaignManager();
        }
        
        return adCampaignManager;
    }
    
    public AdCampaign getRandomActiveAdCampaign(){
        Random rand = new Random();
        List<AdCampaign> allActiveAdCampaign = new ArrayList<>();
        
        for(AdCampaign anAdCampaign : allAdCompaigns.values()){
            if(anAdCampaign.getStatus() == AdCampaignStatus.Running){
                allActiveAdCampaign.add(anAdCampaign);
            }
        }
        
        if (allActiveAdCampaign.size() > 0) {
            return allActiveAdCampaign.get(rand.nextInt(allActiveAdCampaign.size()));
        }
        
        return null;
    }
    
    public void importAdCampaigns(HashMap<String, AdCampaign> allAdCompaigns){
        this.allAdCompaigns = allAdCompaigns;
    }
    
}
