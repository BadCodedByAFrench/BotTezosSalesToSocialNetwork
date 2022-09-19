/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Caller.CallRarible;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.LastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import java.util.List;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CreatorThreadMarketPlaceInterface;

/**
 *
 * @author david
 */
public class CreatorThreadRarible implements CreatorThreadMarketPlaceInterface {

    @Override
    public CallMarketPlaceInterface callMarketPlace(BotModeEnum mode, List<String> contracts, LastRefresh lastrefresh) {
        return new CallRarible(mode, contracts, lastrefresh);
    }
    
}
