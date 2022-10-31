/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass;


import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceProfileStatusEnum;
import java.util.HashMap;

/**
 *
 * @author david
 */
public class MarketPlaceProfileManager {

    private static MarketPlaceProfileManager marketPlaceProfileManager;

    private HashMap<String, MarketPlaceProfile> allMarketPlaceProfile;

    public static MarketPlaceProfileManager getMarketPlaceProfileManager() {

        if (marketPlaceProfileManager == null) {
            marketPlaceProfileManager = new MarketPlaceProfileManager();
        }
        return marketPlaceProfileManager;

    }
    
    private MarketPlaceProfileManager() {
        allMarketPlaceProfile = new HashMap<>();
    }


    public void addMarketPlaceProfile(MarketPlaceProfile marketPlace) {
        allMarketPlaceProfile.put(marketPlace.getName(), marketPlace);
    }

    public void removeMarketPlaceProfile(String name) {
        allMarketPlaceProfile.remove(name);
    }

    public HashMap<String,MarketPlaceProfile> getAllMarketPlaceProfile() {
        return allMarketPlaceProfile;
    }
    
    public void importMarketPlaceProfileManager(HashMap<String, MarketPlaceProfile> allMarketPlaceProfile){
        for(MarketPlaceProfile mps : allMarketPlaceProfile.values()){
            mps.setStatus(MarketPlaceProfileStatusEnum.Ready);
        }
        
        this.allMarketPlaceProfile = allMarketPlaceProfile;
    }
}
