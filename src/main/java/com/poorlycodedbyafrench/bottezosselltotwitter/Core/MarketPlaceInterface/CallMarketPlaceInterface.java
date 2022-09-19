/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.util.concurrent.Callable;

/**
 * Interface that should be use by all of the MarketPlaceEnum
 * @author david
 */
public interface CallMarketPlaceInterface extends Callable {
    
    /**
     * Get the name of the marketplace
     * @return 
     */
    public MarketPlaceEnum getName();
    
}
