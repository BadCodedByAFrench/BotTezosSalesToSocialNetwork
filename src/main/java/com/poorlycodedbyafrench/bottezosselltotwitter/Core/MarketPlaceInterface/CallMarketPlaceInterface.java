/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.LastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.HashMap;
import java.util.List;

/**
 * Interface that should be use by all of the MarketPlaceEnum
 * @author david
 */
public interface CallMarketPlaceInterface {
    
    /**
     * Function to get the sales from a marketplace
     * @param mode
     * @param contracts
     * @param lastrefresh
     * @return
     * @throws Exception 
     */
    
    public HashMap<String, Sale> query(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) throws Exception;
    
    /**
     * Get the name of the marketplace
     * @return 
     */
    public MarketPlaceEnum getName();
    
}
