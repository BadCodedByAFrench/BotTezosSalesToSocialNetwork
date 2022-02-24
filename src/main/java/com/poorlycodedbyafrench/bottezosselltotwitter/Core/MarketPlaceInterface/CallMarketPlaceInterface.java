/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Sales.Sale;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Interface that should be use by all of the MarketPlace
 * @author david
 */
public interface CallMarketPlaceInterface {
    
    /**
     * Function to get the sales from a marketplace
     * @return
     * @throws Exception
     */
    public List<Sale> query() throws Exception;
}
