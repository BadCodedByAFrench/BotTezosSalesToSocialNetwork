/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.util.List;

/**
 * Interface that should be use by all of the SocialNetwork
 * @author david
 */
public interface SocialNetworkInterface {
    
    /**
     * Function called to send informations
     * @param newSales
     * @throws Exception 
     */
    public void send(List<Sale> newSales, int mode) throws Exception;
    
     /**
     * Get the name of the social network
     * @return 
     */
    public SocialNetwork getName();
}
