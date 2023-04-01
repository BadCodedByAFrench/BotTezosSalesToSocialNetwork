/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author david
 */
public class GeneralStatisticRefresher implements Runnable{

    public StatisticCallerInterface getCreatorThread(MarketPlaceEnum mpEnum, List<String> contracts){
        
        if (mpEnum == MarketPlaceEnum.Objkt){
            return new ObjktStatistic(contracts);
        }
        return new FxhashStatistic(contracts);
    }
    
    
    
    @Override
    public void run() {
        
        HashMap<MarketPlaceEnum, List<GeneralStatistic>> allNewStat = new HashMap<MarketPlaceEnum, List<GeneralStatistic>>();
        List<MarketPlaceEnum> finishedMarketPlaceToCheck = new ArrayList<MarketPlaceEnum>();
        
        HashMap<MarketPlaceEnum, List<String>> allContractPerMarketPlace = GeneralStatisticManager.getGeneralStatisticManager().allContractPerMarketPlace();
        ExecutorService multithreadMarketPlace = Executors.newFixedThreadPool(allContractPerMarketPlace.size());
        List<Future<HashMap<MarketPlaceEnum, List<GeneralStatistic>>>> allMarketPlaceFutures = new ArrayList<>();

        for (MarketPlaceEnum mpEnum : allContractPerMarketPlace.keySet()) {

            if (allContractPerMarketPlace.get(mpEnum).size() > 0) {
                Future<HashMap<MarketPlaceEnum, List<GeneralStatistic>>> futurNewStat = multithreadMarketPlace.submit(getCreatorThread(mpEnum, allContractPerMarketPlace.get(mpEnum)));
                allMarketPlaceFutures.add(futurNewStat);
            }
        }

        for (Future<HashMap<MarketPlaceEnum, List<GeneralStatistic>>> aFutur : allMarketPlaceFutures) {
            try {
                HashMap<MarketPlaceEnum, List<GeneralStatistic>> newStatsMarketPlace = aFutur.get();

                for (MarketPlaceEnum aMarketPlace : newStatsMarketPlace.keySet()) {
                    allNewStat.putAll(newStatsMarketPlace);
                    finishedMarketPlaceToCheck.add(aMarketPlace);
                }
            } catch (ExecutionException ex) {
                LogManager.getLogManager().writeLog(GeneralStatisticRefresher.class.getName(), ex);
            } catch (Exception ex) {
                LogManager.getLogManager().writeLog(GeneralStatisticRefresher.class.getName(), ex);
            }
        }

        multithreadMarketPlace.shutdown();
        
        for(MarketPlaceEnum mp : finishedMarketPlaceToCheck ){
            GeneralStatisticManager.getGeneralStatisticManager().clear(mp);
            
            for(GeneralStatistic stat : allNewStat.get(mp)){
                GeneralStatisticManager.getGeneralStatisticManager().add(stat, mp);
            }
        }
    }
    
}
