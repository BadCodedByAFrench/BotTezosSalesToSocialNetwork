/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.LastRefresh;
import java.util.List;

/**
 *
 * @author david
 */
public interface CreatorThreadMarketPlaceInterface {
    
    public CallMarketPlaceInterface callMarketPlace(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh);
}
