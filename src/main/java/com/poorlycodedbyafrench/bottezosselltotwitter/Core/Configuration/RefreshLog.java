/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author david
 */
public class RefreshLog implements Runnable {

    /**
     * Table that contains the log
     */
    private DefaultTableModel model;

    /**
     * Object to get the log to show
     */
    private InterfaceLog source;

    @Override
    public void run() {
        model.setRowCount(0);

        if (source != null) {
            for (List<String> info : source.getRecentOperation()) {
                model.addRow(new Object[]{info.get(0), info.get(1), info.get(2)});
            }
        }
    }

    public RefreshLog(DefaultTableModel model, InterfaceLog source) {
        this.model = model;
        this.source = source;
    }

    public void setSource(InterfaceLog source) {
        this.source = source;
    }
}
