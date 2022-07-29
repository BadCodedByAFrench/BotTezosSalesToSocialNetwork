/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.CallFxhash;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.CallTeia;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.CallObjkt;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TwitterSocialNetwork;
import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.security.auth.login.LoginException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.ini4j.Wini;

/**
 * Main form of the application
 *
 * @author david
 */
public class MainBotForm extends javax.swing.JFrame {

    /**
     * List of all Social network where we will share information
     */
    private List<SocialNetworkInterface> socialNetworks;

    /**
     * Table that contains the log
     */
    private DefaultTableModel model;

    /**
     * Table that contains the contracts
     */
    private DefaultTableModel dtb;
    
    /**
     * Table that contains the marketPlace
     */
    private DefaultTableModel dtbMP;

    /**
     * The thread that will call API for follow sales
     */
    private SalesToSocialNetwork apiHandlerSales;

    /**
     * The thread that will call API for follow stat
     */
    private SalesToSocialNetwork apiHandlerStat;

    /**
     * Tool that will execute query every hours
     */
    private ScheduledThreadPoolExecutor executor;

    private ScheduledFuture<?> scheduledFutureSales;
    private ScheduledFuture<?> scheduledFutureStat;
    
        /**
     * List of all tje marketplaces where we will find sales
     */
    private HashMap<MarketPlaceEnum, MarketPlace> marketplaces;

    /**
     * Creates new form MainBotForm Get the table as dataModel Create social and
     * maketplace object Create the APIHandler and Executor
     */
    public MainBotForm() {
        initComponents();

        model = (DefaultTableModel) tbl_status.getModel();
        model.setRowCount(0);

        dtb = (DefaultTableModel) tbl_contracts.getModel();
        
        dtbMP = (DefaultTableModel) tbl_marketplace.getModel();

        
        setStateComponent(true);

        marketplaces = new HashMap<MarketPlaceEnum, MarketPlace>();
        marketplaces.put(MarketPlaceEnum.Objkt, new MarketPlace(MarketPlaceEnum.Objkt, new CallObjkt()));
        marketplaces.put(MarketPlaceEnum.Teia, new MarketPlace(MarketPlaceEnum.Teia, new CallTeia()));
        marketplaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash, new CallFxhash()));
        
        for(MarketPlaceEnum mp : marketplaces.keySet()){
            dtbMP.addRow(new Object[]{mp});
        }
        
        socialNetworks = new ArrayList<SocialNetworkInterface>();

        apiHandlerSales = new SalesToSocialNetwork(model, 0);
        apiHandlerStat = new SalesToSocialNetwork(model, 1);

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);
        
        tbl_contracts.putClientProperty("terminateEditOnFocusLost", true);
        tbl_seller.putClientProperty("terminateEditOnFocusLost", true);
        tbl_item.putClientProperty("terminateEditOnFocusLost", true);
    }

    /**
     * Change the state of the component depending if the bot is running or not
     *
     * @param stateStart
     */
    private void setStateComponent(boolean stateStart) {
        btn_start.setEnabled(stateStart);
        BTN_Configuration.setEnabled(stateStart);
        tbl_contracts.setEnabled(stateStart);
        tbl_seller.setEnabled(stateStart);
        tbl_item.setEnabled(stateStart);
        tbl_marketplace.setEnabled(stateStart);
        btn_import.setEnabled(stateStart);
        btn_export.setEnabled(stateStart);
        cb_stat.setEnabled(stateStart);
        cb_sales.setEnabled(stateStart);
        
        cb_twitter.setEnabled(stateStart);
        if (cb_twitter.isSelected() && cb_twitter.isEnabled()) {
            pwd_twitter_public_consumer_key.setEnabled(stateStart);
            pwd_twitter_private_consumer_key.setEnabled(stateStart);
            pwd_twitter_public_key.setEnabled(stateStart);
            pwd_twitter_private_key.setEnabled(stateStart);
        } else {
            pwd_twitter_public_consumer_key.setEnabled(false);
            pwd_twitter_private_consumer_key.setEnabled(false);
            pwd_twitter_public_key.setEnabled(false);
            pwd_twitter_private_key.setEnabled(false);
        }

        cb_discord.setEnabled(stateStart);
        if (cb_discord.isSelected() && cb_discord.isEnabled()) {
            pwd_discord_token.setEnabled(stateStart);
            txt_discord_channel.setEnabled(stateStart);
        } else {
            pwd_discord_token.setEnabled(false);
            txt_discord_channel.setEnabled(false);
        }

        if (stateStart) {
            
            if (dtb.getRowCount() <= 0) {
                btn_remove_contract.setEnabled(false);
            } else {
                btn_remove_contract.setEnabled(true);
            }
                        
            if(tbl_contracts.getSelectedRowCount() == 1){
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                
                if(!contract.isBlank()){
                    btn_add_seller.setEnabled(true);
                    btn_add_item.setEnabled(true);
                    
                    if (tbl_seller.getRowCount() <= 0) {
                        btn_remove_seller.setEnabled(false);
                    } else {
                        btn_remove_seller.setEnabled(true);
                    }

                    if (tbl_item.getRowCount() <= 0) {
                        btn_remove_item.setEnabled(false);
                    } else {
                        btn_remove_item.setEnabled(true);
                    }
                    
                    if(tbl_seller.isEditing() || tbl_item.isEditing()){
                        tbl_contracts.setEnabled(false);
                    }else{
                        tbl_contracts.setEnabled(true);
                    }
                }
                else{
                    btn_add_seller.setEnabled(false);
                    btn_remove_seller.setEnabled(false);
                    btn_add_item.setEnabled(false);
                    btn_remove_item.setEnabled(false);
                }
                
                if (tbl_seller.isEditing()){
                    tbl_contracts.setEnabled(false);
                    tbl_item.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                }else if (tbl_contracts.isEditing()){
                    tbl_seller.setEnabled(false);
                    tbl_item.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                }else if (tbl_item.isEditing()){
                    tbl_contracts.setEnabled(false);
                    tbl_seller.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                }else{
                    tbl_contracts.setEnabled(true);
                    tbl_seller.setEnabled(true);
                    tbl_marketplace.setEnabled(true);
                    tbl_item.setEnabled(true);
                }
            }
            else{
                tbl_seller.setEnabled(false);
                btn_add_seller.setEnabled(false);
                btn_remove_seller.setEnabled(false);
                tbl_item.setEnabled(false);
                btn_add_item.setEnabled(false);
                btn_remove_item.setEnabled(false);
            }         

            if (tbl_marketplace.getSelectedRowCount() != 1) {
                btn_add_contract.setEnabled(false);
            } else {
                btn_add_contract.setEnabled(true);
            }

        } else {
            btn_remove_contract.setEnabled(false);
            btn_add_contract.setEnabled(false);
            tbl_seller.setEnabled(false);
            btn_add_seller.setEnabled(false);
            btn_remove_seller.setEnabled(false);
            tbl_item.setEnabled(false);
            btn_add_item.setEnabled(false);
            btn_remove_item.setEnabled(false);
        }

        btn_stop.setEnabled(!stateStart);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_add1 = new javax.swing.JButton();
        lbl_twitter_public_consumer_key = new javax.swing.JLabel();
        pwd_twitter_private_consumer_key = new javax.swing.JPasswordField();
        lbl_twitter_private_consumer_key = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_status = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_start = new javax.swing.JButton();
        btn_stop = new javax.swing.JButton();
        lbl_twitter_access_key = new javax.swing.JLabel();
        lbl_twitter_private_key = new javax.swing.JLabel();
        pwd_twitter_private_key = new javax.swing.JPasswordField();
        pwd_twitter_public_key = new javax.swing.JPasswordField();
        pwd_twitter_public_consumer_key = new javax.swing.JPasswordField();
        BTN_License = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_contracts = new javax.swing.JTable();
        btn_add_contract = new javax.swing.JButton();
        btn_remove_contract = new javax.swing.JButton();
        cb_stat = new javax.swing.JCheckBox();
        cb_sales = new javax.swing.JCheckBox();
        BTN_Configuration = new javax.swing.JButton();
        lbl_discord_token = new javax.swing.JLabel();
        pwd_discord_token = new javax.swing.JPasswordField();
        lbl_discord_channel = new javax.swing.JLabel();
        cb_twitter = new javax.swing.JCheckBox();
        cb_discord = new javax.swing.JCheckBox();
        txt_discord_channel = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_item = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_seller = new javax.swing.JTable();
        btn_remove_seller = new javax.swing.JButton();
        btn_add_seller = new javax.swing.JButton();
        btn_remove_item = new javax.swing.JButton();
        btn_add_item = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_marketplace = new javax.swing.JTable();
        btn_import = new javax.swing.JButton();
        btn_export = new javax.swing.JButton();

        btn_add1.setText("Add");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 450));

        lbl_twitter_public_consumer_key.setText("Public consumer key");

        lbl_twitter_private_consumer_key.setText("Private consumer key");

        tbl_status.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "MarketPlace", "Status", "Data"
            }
        ));
        jScrollPane1.setViewportView(tbl_status);

        jLabel1.setText("Request status");

        btn_start.setText("Start");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });

        btn_stop.setText("Stop");
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopActionPerformed(evt);
            }
        });

        lbl_twitter_access_key.setText("Public Access Key");

        lbl_twitter_private_key.setText("Private Access Key");

        BTN_License.setText("Licenses");
        BTN_License.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LicenseActionPerformed(evt);
            }
        });

        tbl_contracts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Contract"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_contracts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_contractsMouseClicked(evt);
            }
        });
        tbl_contracts.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbl_contractsPropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_contracts);

        btn_add_contract.setText("Add");
        btn_add_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_contractActionPerformed(evt);
            }
        });

        btn_remove_contract.setText("Remove");
        btn_remove_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_contractActionPerformed(evt);
            }
        });

        cb_stat.setText("Stat");

        cb_sales.setText("Sales");

        BTN_Configuration.setText("Configuration");
        BTN_Configuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ConfigurationActionPerformed(evt);
            }
        });

        lbl_discord_token.setText("Discord token");

        lbl_discord_channel.setText("Discord channel");

        cb_twitter.setText("Twitter");
        cb_twitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_twitterActionPerformed(evt);
            }
        });

        cb_discord.setText("Discord");
        cb_discord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_discordActionPerformed(evt);
            }
        });

        tbl_item.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item filter"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_item.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbl_itemPropertyChange(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_item);

        tbl_seller.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seller filter"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_seller.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbl_sellerPropertyChange(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_seller);

        btn_remove_seller.setText("Remove");
        btn_remove_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_sellerActionPerformed(evt);
            }
        });

        btn_add_seller.setText("Add");
        btn_add_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_sellerActionPerformed(evt);
            }
        });

        btn_remove_item.setText("Remove");
        btn_remove_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_itemActionPerformed(evt);
            }
        });

        btn_add_item.setText("Add");
        btn_add_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_itemActionPerformed(evt);
            }
        });

        tbl_marketplace.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MarketPlace"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_marketplace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_marketplaceMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_marketplace);

        btn_import.setText("Import");
        btn_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importActionPerformed(evt);
            }
        });

        btn_export.setText("Export");
        btn_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_start)
                                .addGap(41, 41, 41)
                                .addComponent(btn_stop))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_add_contract, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_remove_contract))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_add_seller, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(btn_remove_seller)
                                .addGap(18, 18, 18)
                                .addComponent(btn_add_item, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_remove_item))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BTN_License)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel1)
                                .addGap(61, 61, 61)
                                .addComponent(BTN_Configuration))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl_twitter_public_consumer_key)
                                .addGap(12, 12, 12)
                                .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbl_twitter_private_consumer_key)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_twitter_access_key)
                                            .addComponent(lbl_twitter_private_key))
                                        .addGap(20, 20, 20)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_discord_token)
                                    .addComponent(lbl_discord_channel))
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pwd_discord_token, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_discord_channel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb_stat)
                                    .addComponent(cb_twitter)
                                    .addComponent(btn_import))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb_discord)
                                    .addComponent(cb_sales)
                                    .addComponent(btn_export))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add_item)
                    .addComponent(btn_remove_item)
                    .addComponent(btn_remove_seller)
                    .addComponent(btn_add_seller)
                    .addComponent(btn_remove_contract)
                    .addComponent(btn_add_contract)
                    .addComponent(btn_start)
                    .addComponent(btn_stop))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_export)
                            .addComponent(btn_import))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_stat)
                            .addComponent(cb_sales))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_discord)
                            .addComponent(cb_twitter))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lbl_discord_token)
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pwd_discord_token, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_discord_channel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_discord_channel))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_public_consumer_key))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_private_consumer_key))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_access_key))
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_private_key)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(BTN_License)
                                    .addComponent(BTN_Configuration))
                                .addGap(15, 15, 15)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(15, Short.MAX_VALUE))))
        );

        lbl_twitter_public_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_login");
        lbl_twitter_private_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_password");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Add row in the contracts table Limite to 20 contracts
     *
     * @param evt
     */

    private void btn_add_contractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_contractActionPerformed
        this.addRow(tbl_contracts);
    }//GEN-LAST:event_btn_add_contractActionPerformed

    /**
     * Remove selected row in contracts table Can't remove when there's one line
     *
     * @param evt
     */

    private void btn_remove_contractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_contractActionPerformed
        
        MarketPlace currentmp = marketplaces.get( dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
        
        for(int index : tbl_contracts.getSelectedRows()){
            currentmp.removeContract((String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0));
        }
        
        this.removeRow(tbl_contracts);
        
        ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
        ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);
    }//GEN-LAST:event_btn_remove_contractActionPerformed

    /**
     * We change the state of component and we stop the bot
     *
     * @param evt
     */

    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopActionPerformed
        setStateComponent(true);
        stoploop();
    }//GEN-LAST:event_btn_stopActionPerformed

    /**
     * We check value If it's good, we change the state of component Update data
     * and start the bot
     *
     * @param evt
     */

    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
        if (checkvalue()) {
            try {
                updateData();
                setStateComponent(false);
                startloop();
            } catch (LoginException ex) {
                model.insertRow(0, new Object[]{this.getName().toString(), "Error : unable to connect to the Discord bot", ex.getMessage()});
                LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
            } catch (InterruptedException ex) {
                model.insertRow(0, new Object[]{this.getName().toString(), "Error : unable to connect to the Discord bot", ex.getMessage()});
                LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
            } catch (Exception ex) {
                model.insertRow(0, new Object[]{this.getName().toString(), "Error : unable to connect to the Discord bot", ex.getMessage()});
                LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled", "Empty field", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_startActionPerformed

    /**
     * Show the license window
     *
     * @param evt
     */

    private void BTN_LicenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_LicenseActionPerformed
        JDialog license = new JDialog();
        license.add(new LicenseBot());
        license.pack();
        license.setVisible(true);
    }//GEN-LAST:event_BTN_LicenseActionPerformed

    private void BTN_ConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_ConfigurationActionPerformed
        JDialog license = new JDialog();
        license.add(new ConfigurationMenu());
        license.pack();
        license.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        license.setVisible(true);
    }//GEN-LAST:event_BTN_ConfigurationActionPerformed

    private void cb_discordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_discordActionPerformed
        setStateComponent(true);
    }//GEN-LAST:event_cb_discordActionPerformed

    private void cb_twitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_twitterActionPerformed
        setStateComponent(true);
    }//GEN-LAST:event_cb_twitterActionPerformed

    private void btn_remove_sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_sellerActionPerformed
       for(int index : tbl_seller.getSelectedRows()){
            removeInHashMap((String)((DefaultTableModel) tbl_seller.getModel()).getDataVector().get(index).get(0), 0);
       }
       
       this.removeRow(tbl_seller);
    }//GEN-LAST:event_btn_remove_sellerActionPerformed

    private void btn_add_sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_sellerActionPerformed
        this.addRow(tbl_seller);
    }//GEN-LAST:event_btn_add_sellerActionPerformed

    private void btn_remove_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_itemActionPerformed
        for(int index : tbl_item.getSelectedRows()){
            removeInHashMap((String)((DefaultTableModel) tbl_item.getModel()).getDataVector().get(index).get(0), 1);
        }
        
        this.removeRow(tbl_item);
    }//GEN-LAST:event_btn_remove_itemActionPerformed

    private void btn_add_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_itemActionPerformed
        this.addRow(tbl_item);
    }//GEN-LAST:event_btn_add_itemActionPerformed

    private void tbl_contractsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_contractsMouseClicked
        
        if(tbl_contracts.isEnabled()){
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

            if(tbl_contracts.getSelectedRowCount() == 1){

                MarketPlace currentmp = marketplaces.get( dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                
                HashMap<String, List<String>> sellerList = currentmp.getSellerList();
                if(sellerList.containsKey(contract)){
                    for(String oneSeller : sellerList.get(contract)){
                        ((DefaultTableModel) tbl_seller.getModel()).addRow(new Object[]{oneSeller});
                    }
                }
                
                HashMap<String, List<String>> itemList = currentmp.getItemList();
                
                if(itemList.containsKey(contract)){
                    for(String item : itemList.get(contract)){
                        ((DefaultTableModel) tbl_item.getModel()).addRow(new Object[]{item});
                    }
                }
            }

            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_contractsMouseClicked

    private void tbl_contractsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_contractsPropertyChange

        if ("tableCellEditor".equals(evt.getPropertyName()))
        {
            if (!tbl_contracts.isEditing()){
            
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                
                Vector<Vector> contractsData = dtb.getDataVector();
                
                int isUnique = 0;
                ArrayList<String> currentContracts = new ArrayList<String>();
                
                for (Vector v : contractsData) {

                    if (v.get(0).toString().equals(contract)) {
                        isUnique++;
                    }
                    currentContracts.add(v.get(0).toString().trim());
                }
                
                
                if(!contract.isBlank() && isUnique == 1){
                    
                    MarketPlace currentmp = marketplaces.get( dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                    
                    ArrayList<String> contractsToRemove = new ArrayList<String>();
                    
                    for(String oneContract : currentmp.getContracts()){
                        if(!currentContracts.contains(oneContract)){
                            contractsToRemove.add(oneContract);
                        }
                    }
                    
                    for(String contractToRemove : contractsToRemove){
                        currentmp.removeContract(contractToRemove);
                    }
                    
                    for (String oneCurrentContracts : currentContracts) {
                        if(!currentmp.getContracts().contains(oneCurrentContracts)){
                            currentmp.addContract(oneCurrentContracts);
                        }
                    }
                    
                    
                    
                }else{
                    dtb.removeRow(tbl_contracts.getSelectedRow());
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_contractsPropertyChange

    private void tbl_sellerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_sellerPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName()))
        {
            if (!tbl_seller.isEditing()){
            
                MarketPlace currentmp = marketplaces.get( dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                
                HashMap<String, List<String>> sellerList = currentmp.getSellerList();
                
                sellerList.get(contract).clear();
                
                for(Vector v : ((DefaultTableModel)tbl_seller.getModel()).getDataVector()){
                    String seller = (String) v.get(0);
                
                    if(!seller.isBlank()){
                        currentmp.addFilter(contract, seller.trim(), 0);
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_sellerPropertyChange

    private void tbl_itemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_itemPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName()))
        {
            if (!tbl_item.isEditing()){
                
                MarketPlace currentmp = marketplaces.get( dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                
                HashMap<String, List<String>> itemList = currentmp.getItemList();
                
                itemList.get(contract).clear();
                
                for(Vector v : ((DefaultTableModel)tbl_item.getModel()).getDataVector()){
                    String item = (String) v.get(0);
                
                    if(!item.isBlank()){
                        currentmp.addFilter(contract, item.trim(), 1);
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_itemPropertyChange

    private void tbl_marketplaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_marketplaceMouseClicked
        if(tbl_marketplace.isEnabled()){
            ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

            if(tbl_marketplace.getSelectedRowCount() == 1){

                MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                
                for(String contract : currentmp.getContracts()){
                    dtb.addRow(new Object[]{contract});
                }
                
                if(dtb.getRowCount() == 0){
                    dtb.addRow(new Object[]{""});
                }
            }

            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_marketplaceMouseClicked

    private void btn_importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importActionPerformed
        try {
            this.importFile();
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }//GEN-LAST:event_btn_importActionPerformed

    private void btn_exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportActionPerformed
        try {
            this.export();
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }//GEN-LAST:event_btn_exportActionPerformed

    private void addRow(JTable tableToAdd){
        ((DefaultTableModel) tableToAdd.getModel()).addRow(new Object[]{""});
        setStateComponent(true);
    }
    
    private void removeInHashMap(String filterToRemove, int hashMapToRemove){
        MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
        if(!filterToRemove.isBlank()){
            currentmp.removeFilter((String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0), filterToRemove, hashMapToRemove);
        }
    }
    
    private void removeRow(JTable tableToRemove){
        DefaultTableModel dtbTable = (DefaultTableModel)tableToRemove.getModel();
        if (tableToRemove.getSelectedRows().length != 0) {

            int[] allIndex = tableToRemove.getSelectedRows();
            int[] reversedAllIndex = Arrays.stream(allIndex).boxed()
                    .sorted(Collections.reverseOrder())
                    .mapToInt(Integer::intValue)
                    .toArray();

            for (int i : reversedAllIndex) {
                dtbTable.removeRow(i);
            }
        } else {
            dtbTable.removeRow(dtbTable.getRowCount() - 1);
        }

        setStateComponent(true);
    }
    
    /**
     * Check if the field are fill
     *
     * @return true if all data are correct, else wrong
     */
    private boolean checkvalue() {

        boolean isDataCorrect = true;

        Vector<Vector> contractsData = dtb.getDataVector();

        boolean atleastoneFill = false;
        
        for(MarketPlace mp : marketplaces.values()){
            if(mp.getContracts().size() > 0){
                atleastoneFill = true;
                for(String contract : mp.getContracts()){
                    if(contract.isBlank()){
                        isDataCorrect = false;
                    }
                }
            }
        }   

        if(!atleastoneFill){
            isDataCorrect = false;
        }
        
        if (cb_twitter.isSelected()) {
            if (new String(pwd_twitter_public_consumer_key.getPassword()).isBlank()) {
                isDataCorrect = false;
            }

            if (new String(pwd_twitter_private_consumer_key.getPassword()).isBlank()) {
                isDataCorrect = false;
            }

            if (new String(pwd_twitter_public_key.getPassword()).isBlank()) {
                isDataCorrect = false;
            }

            if (new String(pwd_twitter_private_key.getPassword()).isBlank()) {
                isDataCorrect = false;
            }
        }

        if (cb_discord.isSelected()) {

            if (new String(pwd_discord_token.getPassword()).isBlank()) {
                isDataCorrect = false;
            }

            if (txt_discord_channel.getText().isBlank()) {
                isDataCorrect = false;
            }
        }

        if (!cb_discord.isSelected() && !cb_twitter.isSelected()) {
            isDataCorrect = false;
        }

        if (!cb_stat.isSelected() && !cb_sales.isSelected()) {
            isDataCorrect = false;
        }

        return isDataCorrect;
    }

    /**
     * Update the data of Marketplace and SocialNetwork
     */
    private void updateData() throws LoginException, InterruptedException, Exception {

        HashMap<MarketPlaceEnum, MarketPlace> mpsToKeep = new HashMap<MarketPlaceEnum, MarketPlace>();
        
        for(MarketPlace mp : marketplaces.values()){
            if(mp.getContracts().size()>0){
                mp.resetLastRefresh();
                mpsToKeep.put(mp.getMarketplace(), mp);
            }
        }
        
        socialNetworks.clear();

        if (cb_discord.isSelected()) {
            DiscordSocialNetwork discordSocialNetwork = new DiscordSocialNetwork();
            discordSocialNetwork.instanceDiscord(new String(pwd_discord_token.getPassword()), txt_discord_channel.getText(), model);
            socialNetworks.add(discordSocialNetwork);
        }

        if (cb_twitter.isSelected()) {
            TwitterSocialNetwork twitterSocialNetwork = new TwitterSocialNetwork();
            twitterSocialNetwork.instanceTwitter(new String(pwd_twitter_public_consumer_key.getPassword()), new String(pwd_twitter_private_consumer_key.getPassword()), new String(pwd_twitter_public_key.getPassword()), new String(pwd_twitter_private_key.getPassword()), model);
            socialNetworks.add(twitterSocialNetwork);

        }

        apiHandlerSales.setMarketplaces(mpsToKeep);
        apiHandlerSales.setSocialNetworks(socialNetworks);

        apiHandlerStat.setMarketplaces(mpsToKeep);
        apiHandlerStat.setSocialNetworks(socialNetworks);
    }

    /**
     * Start the bot
     *
     */
    private void startloop() {

        try {
            for (SocialNetworkInterface oneSocialNetwork : socialNetworks) {
                oneSocialNetwork.start();
            }

            if (cb_sales.isSelected()) {
                scheduledFutureSales = executor.scheduleAtFixedRate(apiHandlerSales, 0, BotConfiguration.getConfiguration().getRefreshSalesTime(), BotConfiguration.getConfiguration().getRefreshSales());
            }

            if (cb_stat.isSelected()) {
                scheduledFutureStat = executor.scheduleAtFixedRate(apiHandlerStat, 0, BotConfiguration.getConfiguration().getRefreshSalesStats(), BotConfiguration.getConfiguration().getRefreshStats());
            }
        } catch (Exception ex) {
            model.insertRow(0, new Object[]{"Start", "Error : unable to start", ex.getMessage()});
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }

    /**
     * Stop the bot
     */
    private void stoploop() {

        try {
            for (SocialNetworkInterface oneSocialNetwork : socialNetworks) {
                oneSocialNetwork.stop();
            }
        } catch (Exception ex) {
            model.insertRow(0, new Object[]{"Stop", "Error : unable to stop correctly", ex.getMessage()});
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }

        if (cb_sales.isSelected()) {
            scheduledFutureSales.cancel(true);
        }

        if (cb_stat.isSelected()) {
            scheduledFutureStat.cancel(true);
        }
    }
    
    /**
     * Export data into an ini file
     */
    private void export() throws IOException, Exception{
        
        
        Gson gson = new Gson();
        
        Wini ini = new Wini();
        
        ini.put("MainWindow", "version", 1);
        ini.put("MainWindow", "marketplaces", gson.toJson(marketplaces));
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            
            String saveFile = chooser.getSelectedFile().getAbsolutePath();
            if(!saveFile.toLowerCase().endsWith(".ini"))
            {
                saveFile += ".ini";
            }
            ini.store(new File(saveFile));
        }
    }
    
    private void importFile() throws IOException, Exception {
        
        Gson gson = new Gson();
        java.lang.reflect.Type empHashMapType = new TypeToken<HashMap<MarketPlaceEnum, MarketPlace>>() {}.getType();
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
            Wini ini = new Wini(chooser.getSelectedFile());

            int version = ini.get("MainWindow", "version", int.class);
            String json = ini.get("MainWindow", "marketplaces", String.class);
            
            marketplaces.clear();
            ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);
            
            HashMap <MarketPlaceEnum, MarketPlace> mps = gson.fromJson(json, empHashMapType);
            
            for (MarketPlaceEnum mp : mps.keySet()){
                                
                CallMarketPlaceInterface cmpi = null;
                if(mp == MarketPlaceEnum.Objkt){
                    cmpi = new CallObjkt();
                }
                else if(mp == MarketPlaceEnum.Teia){
                    cmpi = new CallTeia();
                }
                else if(mp == MarketPlaceEnum.fxhash){
                    cmpi = new CallFxhash();
                }
                
                if(cmpi != null){
                    marketplaces.put(mp, new MarketPlace(mp, cmpi));
                    MarketPlace newMp = marketplaces.get(mp);
                    newMp.setContracts(mps.get(mp).getContracts());
                    newMp.setItemList(mps.get(mp).getItemList());
                    newMp.setSellerList(mps.get(mp).getSellerList());
                }
            }
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        } catch (InstantiationException ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        } catch (IllegalAccessException ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainBotForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_Configuration;
    private javax.swing.JButton BTN_License;
    private javax.swing.JButton btn_add1;
    private javax.swing.JButton btn_add_contract;
    private javax.swing.JButton btn_add_item;
    private javax.swing.JButton btn_add_seller;
    private javax.swing.JButton btn_export;
    private javax.swing.JButton btn_import;
    private javax.swing.JButton btn_remove_contract;
    private javax.swing.JButton btn_remove_item;
    private javax.swing.JButton btn_remove_seller;
    private javax.swing.JButton btn_start;
    private javax.swing.JButton btn_stop;
    private javax.swing.JCheckBox cb_discord;
    private javax.swing.JCheckBox cb_sales;
    private javax.swing.JCheckBox cb_stat;
    private javax.swing.JCheckBox cb_twitter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbl_discord_channel;
    private javax.swing.JLabel lbl_discord_token;
    private javax.swing.JLabel lbl_twitter_access_key;
    private javax.swing.JLabel lbl_twitter_private_consumer_key;
    private javax.swing.JLabel lbl_twitter_private_key;
    private javax.swing.JLabel lbl_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_discord_token;
    private javax.swing.JPasswordField pwd_twitter_private_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_private_key;
    private javax.swing.JPasswordField pwd_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_public_key;
    private javax.swing.JTable tbl_contracts;
    private javax.swing.JTable tbl_item;
    private javax.swing.JTable tbl_marketplace;
    private javax.swing.JTable tbl_seller;
    private javax.swing.JTable tbl_status;
    private javax.swing.JTextField txt_discord_channel;
    // End of variables declaration//GEN-END:variables
}
