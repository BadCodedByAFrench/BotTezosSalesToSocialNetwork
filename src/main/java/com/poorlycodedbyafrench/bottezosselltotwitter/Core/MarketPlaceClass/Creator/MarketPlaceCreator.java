/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CreatorThreadMarketPlaceInterface;

/**
 *
 * @author david
 */
public class MarketPlaceCreator {
    
    public static CreatorThreadMarketPlaceInterface getCreatorThread(MarketPlaceEnum mpEnum){
        
        if (mpEnum == MarketPlaceEnum.Objkt){
            return new CreatorThreadObjkt();
        }
        else if (mpEnum == MarketPlaceEnum.Teia){
            return new CreatorThreadTeia();
        }
        else if (mpEnum == MarketPlaceEnum.fxhash){
            return new CreatorThreadFxhash();
        }
        return new CreatorThreadRarible();
    }
}
