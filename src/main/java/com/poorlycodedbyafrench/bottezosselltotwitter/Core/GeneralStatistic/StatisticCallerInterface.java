/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.util.concurrent.Callable;

/**
 *
 * @author david
 */
public interface StatisticCallerInterface extends Callable {
        
    public MarketPlaceEnum getMp();
}
