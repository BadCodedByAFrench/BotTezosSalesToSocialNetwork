/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.GeneralStatistic;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class GeneralStatistic {
    
    private String contract;
    
    private String name;
    
    private Double allTimeVolume;
    
    private Double lastDayVolume;
    
    private Double floorPrice;
    
    private Long itemNumber;
    
    private Long countActiveAuctions;
    
    private Long countActiveListing;
    
    private List<String> activeIdEnglishAuction;
    
    private List<String> activeIdDutchAuction;
    
    private List<MarketPlaceEnum> listMps;
    
    private String path;

    public GeneralStatistic(String contract, String name, Double allTimeVolume, Double lastDayVolume, Double floorPrice, Long itemNumber, Long countActiveAuctions, Long countActiveListing, List<String> activeIdEnglishAuction, List<String> activeIdDutchAuction, String path) {
        this.contract = contract;
        this.name = name;
        this.allTimeVolume = allTimeVolume;
        this.lastDayVolume = lastDayVolume;
        this.floorPrice = floorPrice;
        this.itemNumber = itemNumber;
        this.countActiveAuctions = countActiveAuctions;
        this.countActiveListing = countActiveListing;
        this.activeIdEnglishAuction = activeIdEnglishAuction;
        this.activeIdDutchAuction = activeIdDutchAuction;
        this.path = path;
        this.listMps = new ArrayList<>();
    }
    
    public String getContract() {
        return contract;
    }

    public String getName() {
        return name;
    }

    public Double getAllTimeVolume() {
        return allTimeVolume;
    }

    public Double getLastDayVolume() {
        return lastDayVolume;
    }

    public Double getFloorPrice() {
        return floorPrice;
    }

    public Long getItemNumber() {
        return itemNumber;
    }

    public Long getCountActiveAuctions() {
        return countActiveAuctions;
    }

    public Long getCountActiveListing() {
        return countActiveListing;
    }

    public List<String> getActiveIdEnglishAuction() {
        return activeIdEnglishAuction;
    }

    public List<String> getActiveIdDutchAuction() {
        return activeIdDutchAuction;
    }

    public List<MarketPlaceEnum> getListMps() {
        return listMps;
    }

    public String getPath() {
        return path;
    }
    
    public void setAllTimeVolume(Double allTimeVolume) {
        this.allTimeVolume = allTimeVolume;
    }

    public void setLastDayVolume(Double lastDayVolume) {
        this.lastDayVolume = lastDayVolume;
    }

    public void setFloorPrice(Double floorPrice) {
        this.floorPrice = floorPrice;
    }

    public void setItemNumber(Long itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setCountActiveAuctions(Long countActiveAuctions) {
        this.countActiveAuctions = countActiveAuctions;
    }

    public void setCountActiveListing(Long countActiveListing) {
        this.countActiveListing = countActiveListing;
    }
    
    public void replace(GeneralStatistic statReferent) {
        this.contract = statReferent.getContract();
        this.name = statReferent.getName();
        this.allTimeVolume = statReferent.getAllTimeVolume();
        this.lastDayVolume = statReferent.getLastDayVolume();
        this.floorPrice = statReferent.getFloorPrice();
        this.itemNumber = statReferent.getItemNumber();
        this.countActiveAuctions = statReferent.getCountActiveAuctions();
        this.countActiveListing = statReferent.getCountActiveListing();
        this.activeIdEnglishAuction = statReferent.getActiveIdEnglishAuction();
        this.activeIdDutchAuction = statReferent.getActiveIdDutchAuction();
        this.path = statReferent.getPath();
    }
    
}
