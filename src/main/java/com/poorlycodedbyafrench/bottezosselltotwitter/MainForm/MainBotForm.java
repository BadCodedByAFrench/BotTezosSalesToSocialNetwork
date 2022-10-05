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
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotModeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator.CreatorThreadFxhash;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator.CreatorThreadObjkt;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator.CreatorThreadRarible;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.Creator.CreatorThreadTeia;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CreatorThreadMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetwork;
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
     * The thread that will call API for follow stat
     */
    private SalesToSocialNetwork apiHandlerListingAndBidding;
    
    

    /**
     * Tool that will execute query every hours
     */
    private ScheduledThreadPoolExecutor executor;

    private ScheduledFuture<?> scheduledFutureSales;
    private ScheduledFuture<?> scheduledFutureStat;
    private ScheduledFuture<?> scheduledFutureListingAndBidding;

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
        marketplaces.put(MarketPlaceEnum.Objkt, new MarketPlace(MarketPlaceEnum.Objkt, new CreatorThreadObjkt()));
        marketplaces.put(MarketPlaceEnum.Teia, new MarketPlace(MarketPlaceEnum.Teia, new CreatorThreadTeia()));
        marketplaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash, new CreatorThreadFxhash()));
        marketplaces.put(MarketPlaceEnum.Rarible, new MarketPlace(MarketPlaceEnum.Rarible, new CreatorThreadRarible()));

        for (MarketPlaceEnum mp : marketplaces.keySet()) {
            dtbMP.addRow(new Object[]{mp});
        }

        socialNetworks = new ArrayList<SocialNetworkInterface>();

        apiHandlerSales = new SalesToSocialNetwork(model, BotModeEnum.Sale);
        apiHandlerStat = new SalesToSocialNetwork(model, BotModeEnum.Stat);
        apiHandlerListingAndBidding = new SalesToSocialNetwork(model, BotModeEnum.ListingAndBidding);

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
        cb_listingandbidding.setEnabled(stateStart);

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

        cb_telegram.setEnabled(stateStart);
        if (cb_telegram.isSelected() && cb_telegram.isEnabled()) {
            pwd_telegram_token.setEnabled(stateStart);
            txt_telegram_channel.setEnabled(stateStart);
        } else {
            pwd_telegram_token.setEnabled(false);
            txt_telegram_channel.setEnabled(false);
        }
        
        if (stateStart) {

            if (dtb.getRowCount() <= 0) {
                btn_remove_contract.setEnabled(false);
            } else {
                btn_remove_contract.setEnabled(true);
            }

            if (tbl_contracts.getSelectedRowCount() == 1) {
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

                if (!contract.isBlank()) {
                    btn_add_seller.setEnabled(true);
                    btn_add_item.setEnabled(true);
                    tb_royalty_wallet.setEnabled(true);
                    
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

                    if (tbl_seller.isEditing() || tbl_item.isEditing() || tb_royalty_wallet.isFocusOwner()) {
                        tbl_contracts.setEnabled(false);
                    } else {
                        tbl_contracts.setEnabled(true);
                    }
                } else {
                    btn_add_seller.setEnabled(false);
                    btn_remove_seller.setEnabled(false);
                    btn_add_item.setEnabled(false);
                    btn_remove_item.setEnabled(false);
                    tb_royalty_wallet.setEnabled(false);
                }

                if (tbl_seller.isEditing()) {
                    tbl_contracts.setEnabled(false);
                    tbl_item.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                    tb_royalty_wallet.setEnabled(false);
                } else if (tbl_contracts.isEditing()) {
                    tbl_seller.setEnabled(false);
                    tbl_item.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                    tb_royalty_wallet.setEnabled(false);
                } else if (tbl_item.isEditing()) {
                    tbl_contracts.setEnabled(false);
                    tbl_seller.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                    tb_royalty_wallet.setEnabled(false);
                } else if (tb_royalty_wallet.isFocusOwner()) {
                    tbl_contracts.setEnabled(false);
                    tbl_seller.setEnabled(false);
                    tbl_marketplace.setEnabled(false);
                    tbl_item.setEnabled(false);
                } else {
                    tbl_contracts.setEnabled(true);
                    tbl_seller.setEnabled(true);
                    tbl_marketplace.setEnabled(true);
                    tbl_item.setEnabled(true);
                }
            } else {
                tbl_seller.setEnabled(false);
                btn_add_seller.setEnabled(false);
                btn_remove_seller.setEnabled(false);
                tbl_item.setEnabled(false);
                btn_add_item.setEnabled(false);
                btn_remove_item.setEnabled(false);
                tb_royalty_wallet.setEnabled(false);
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
            tb_royalty_wallet.setEnabled(false);
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
        lbl_discord_token1 = new javax.swing.JLabel();
        pwd_discord_token1 = new javax.swing.JPasswordField();
        txt_discord_channel1 = new javax.swing.JTextField();
        lbl_discord_channel1 = new javax.swing.JLabel();
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
        BTN_Configuration = new javax.swing.JButton();
        lbl_discord_token = new javax.swing.JLabel();
        pwd_discord_token = new javax.swing.JPasswordField();
        cb_sales = new javax.swing.JCheckBox();
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
        lbl_royalty_wallet = new javax.swing.JLabel();
        tb_royalty_wallet = new javax.swing.JTextField();
        lbl_telegram_token = new javax.swing.JLabel();
        pwd_telegram_token = new javax.swing.JPasswordField();
        txt_telegram_channel = new javax.swing.JTextField();
        lbl_telegram_channel = new javax.swing.JLabel();
        cb_telegram = new javax.swing.JCheckBox();
        cb_listingandbidding = new javax.swing.JCheckBox();

        btn_add1.setText("Add");

        lbl_discord_token1.setText("Discord token");

        lbl_discord_channel1.setText("Discord channel");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_twitter_public_consumer_key.setText("Public consumer key");
        getContentPane().add(lbl_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(407, 322, -1, -1));
        lbl_twitter_public_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_login");

        getContentPane().add(pwd_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 348, 74, -1));

        lbl_twitter_private_consumer_key.setText("Private consumer key");
        getContentPane().add(lbl_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 354, -1, -1));
        lbl_twitter_private_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_password");

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 385, 90));

        jLabel1.setText("Request status");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 313, -1, -1));

        btn_start.setText("Start");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });
        getContentPane().add(btn_start, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        btn_stop.setText("Stop");
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopActionPerformed(evt);
            }
        });
        getContentPane().add(btn_stop, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        lbl_twitter_access_key.setText("Public Access Key");
        getContentPane().add(lbl_twitter_access_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 388, -1, -1));

        lbl_twitter_private_key.setText("Private Access Key");
        getContentPane().add(lbl_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 418, -1, -1));
        getContentPane().add(pwd_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 412, 74, -1));
        getContentPane().add(pwd_twitter_public_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 382, 74, -1));
        getContentPane().add(pwd_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 316, 74, -1));

        BTN_License.setText("Licenses");
        BTN_License.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LicenseActionPerformed(evt);
            }
        });
        getContentPane().add(BTN_License, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 307, -1, -1));

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 340, 220));

        btn_add_contract.setText("Add");
        btn_add_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_contractActionPerformed(evt);
            }
        });
        getContentPane().add(btn_add_contract, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 67, -1));

        btn_remove_contract.setText("Remove");
        btn_remove_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_contractActionPerformed(evt);
            }
        });
        getContentPane().add(btn_remove_contract, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, -1, -1));

        cb_stat.setText("Stat");
        getContentPane().add(cb_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 341, -1, -1));

        BTN_Configuration.setText("Configuration");
        BTN_Configuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ConfigurationActionPerformed(evt);
            }
        });
        getContentPane().add(BTN_Configuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(284, 307, -1, -1));

        lbl_discord_token.setText("Discord token");
        getContentPane().add(lbl_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 389, -1, -1));
        getContentPane().add(pwd_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 383, 74, -1));

        cb_sales.setText("Sales");
        cb_sales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_salesActionPerformed(evt);
            }
        });
        getContentPane().add(cb_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 341, -1, -1));

        lbl_discord_channel.setText("Discord channel");
        getContentPane().add(lbl_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 419, -1, -1));

        cb_twitter.setText("Twitter");
        cb_twitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_twitterActionPerformed(evt);
            }
        });
        getContentPane().add(cb_twitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 365, -1, -1));

        cb_discord.setText("Discord");
        cb_discord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_discordActionPerformed(evt);
            }
        });
        getContentPane().add(cb_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 365, 80, -1));
        getContentPane().add(txt_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 413, 74, -1));

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

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, 200, 220));

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

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 230, 220));

        btn_remove_seller.setText("Remove");
        btn_remove_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_sellerActionPerformed(evt);
            }
        });
        getContentPane().add(btn_remove_seller, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, -1, -1));

        btn_add_seller.setText("Add");
        btn_add_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_sellerActionPerformed(evt);
            }
        });
        getContentPane().add(btn_add_seller, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 67, -1));

        btn_remove_item.setText("Remove");
        btn_remove_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_itemActionPerformed(evt);
            }
        });
        getContentPane().add(btn_remove_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, -1));

        btn_add_item.setText("Add");
        btn_add_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_itemActionPerformed(evt);
            }
        });
        getContentPane().add(btn_add_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 10, 67, -1));

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

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 40, 160, 220));

        btn_import.setText("Import");
        btn_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importActionPerformed(evt);
            }
        });
        getContentPane().add(btn_import, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 307, -1, -1));

        btn_export.setText("Export");
        btn_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportActionPerformed(evt);
            }
        });
        getContentPane().add(btn_export, new org.netbeans.lib.awtextra.AbsoluteConstraints(723, 307, -1, -1));

        lbl_royalty_wallet.setText("Royalty wallet");
        getContentPane().add(lbl_royalty_wallet, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 272, -1, -1));

        tb_royalty_wallet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tb_royalty_walletFocusLost(evt);
            }
        });
        getContentPane().add(tb_royalty_wallet, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 266, 170, -1));

        lbl_telegram_token.setText("Telegram token");
        getContentPane().add(lbl_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 390, -1, -1));
        getContentPane().add(pwd_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 380, 74, -1));
        getContentPane().add(txt_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 410, 74, -1));

        lbl_telegram_channel.setText("Telegram channel");
        getContentPane().add(lbl_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 420, -1, -1));

        cb_telegram.setText("Telegram");
        cb_telegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_telegramActionPerformed(evt);
            }
        });
        getContentPane().add(cb_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 365, -1, -1));

        cb_listingandbidding.setText("Listing and bidding");
        cb_listingandbidding.setToolTipText("");
        getContentPane().add(cb_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 340, -1, -1));

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

        MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));

        for (int index : tbl_contracts.getSelectedRows()) {
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
        for (int index : tbl_seller.getSelectedRows()) {
            removeInHashMap((String) ((DefaultTableModel) tbl_seller.getModel()).getDataVector().get(index).get(0), 0);
        }

        this.removeRow(tbl_seller);
    }//GEN-LAST:event_btn_remove_sellerActionPerformed

    private void btn_add_sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_sellerActionPerformed
        this.addRow(tbl_seller);
    }//GEN-LAST:event_btn_add_sellerActionPerformed

    private void btn_remove_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_itemActionPerformed
        for (int index : tbl_item.getSelectedRows()) {
            removeInHashMap((String) ((DefaultTableModel) tbl_item.getModel()).getDataVector().get(index).get(0), 1);
        }

        this.removeRow(tbl_item);
    }//GEN-LAST:event_btn_remove_itemActionPerformed

    private void btn_add_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_itemActionPerformed
        this.addRow(tbl_item);
    }//GEN-LAST:event_btn_add_itemActionPerformed

    private void tbl_contractsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_contractsMouseClicked

        if (tbl_contracts.isEnabled()) {
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

            if (tbl_contracts.getSelectedRowCount() == 1) {

                MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

                HashMap<String, List<String>> sellerList = currentmp.getSellerList();
                if (sellerList.containsKey(contract)) {
                    for (String oneSeller : sellerList.get(contract)) {
                        ((DefaultTableModel) tbl_seller.getModel()).addRow(new Object[]{oneSeller});
                    }
                }

                HashMap<String, List<String>> itemList = currentmp.getItemList();

                if (itemList.containsKey(contract)) {
                    for (String item : itemList.get(contract)) {
                        ((DefaultTableModel) tbl_item.getModel()).addRow(new Object[]{item});
                    }
                }

                tb_royalty_wallet.setText(currentmp.getRoyaltywallet().get(contract));
            }

            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_contractsMouseClicked

    private void tbl_contractsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_contractsPropertyChange

        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (!tbl_contracts.isEditing()) {

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

                if (!contract.isBlank() && isUnique == 1) {

                    MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));

                    ArrayList<String> contractsToRemove = new ArrayList<String>();

                    for (String oneContract : currentmp.getContracts()) {
                        if (!currentContracts.contains(oneContract)) {
                            contractsToRemove.add(oneContract);
                        }
                    }

                    for (String contractToRemove : contractsToRemove) {
                        currentmp.removeContract(contractToRemove);
                    }

                    for (String oneCurrentContracts : currentContracts) {
                        if (!currentmp.getContracts().contains(oneCurrentContracts)) {
                            currentmp.addContract(oneCurrentContracts);
                        }
                    }

                } else {
                    dtb.removeRow(tbl_contracts.getSelectedRow());
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_contractsPropertyChange

    private void tbl_sellerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_sellerPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (!tbl_seller.isEditing()) {

                MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

                HashMap<String, List<String>> sellerList = currentmp.getSellerList();

                sellerList.get(contract).clear();

                for (Vector v : ((DefaultTableModel) tbl_seller.getModel()).getDataVector()) {
                    String seller = (String) v.get(0);

                    if (!seller.isBlank()) {
                        currentmp.addFilter(contract, seller.trim(), 0);
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_sellerPropertyChange

    private void tbl_itemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_itemPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (!tbl_item.isEditing()) {

                MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

                HashMap<String, List<String>> itemList = currentmp.getItemList();

                itemList.get(contract).clear();

                for (Vector v : ((DefaultTableModel) tbl_item.getModel()).getDataVector()) {
                    String item = (String) v.get(0);

                    if (!item.isBlank()) {
                        currentmp.addFilter(contract, item.trim(), 1);
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_itemPropertyChange

    private void tbl_marketplaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_marketplaceMouseClicked
        if (tbl_marketplace.isEnabled()) {
            ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);
            tb_royalty_wallet.setText("");

            if (tbl_marketplace.getSelectedRowCount() == 1) {

                MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));

                for (String contract : currentmp.getContracts()) {
                    dtb.addRow(new Object[]{contract});
                }

                if (dtb.getRowCount() == 0) {
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

    private void tb_royalty_walletFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tb_royalty_walletFocusLost
        MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
        String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

        HashMap<String, String> royaltyWallet = currentmp.getRoyaltywallet();

        royaltyWallet.put(contract, tb_royalty_wallet.getText().trim());

        setStateComponent(true);
    }//GEN-LAST:event_tb_royalty_walletFocusLost

    private void cb_telegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_telegramActionPerformed
        setStateComponent(true);
    }//GEN-LAST:event_cb_telegramActionPerformed

    private void cb_salesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_salesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_salesActionPerformed

    private void addRow(JTable tableToAdd) {
        ((DefaultTableModel) tableToAdd.getModel()).addRow(new Object[]{""});
        setStateComponent(true);
    }

    private void removeInHashMap(String filterToRemove, int hashMapToRemove) {
        MarketPlace currentmp = marketplaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));
        if (!filterToRemove.isBlank()) {
            currentmp.removeFilter((String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0), filterToRemove, hashMapToRemove);
        }
    }

    private void removeRow(JTable tableToRemove) {
        DefaultTableModel dtbTable = (DefaultTableModel) tableToRemove.getModel();
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

        for (MarketPlace mp : marketplaces.values()) {
            if (mp.getContracts().size() > 0) {
                atleastoneFill = true;
                for (String contract : mp.getContracts()) {
                    if (contract.isBlank()) {
                        isDataCorrect = false;
                    }
                }
            }
        }

        if (!atleastoneFill) {
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
        
        if (cb_telegram.isSelected()) {

            if (new String(pwd_telegram_token.getPassword()).isBlank()) {
                isDataCorrect = false;
            }

            if (txt_telegram_channel.getText().isBlank()) {
                isDataCorrect = false;
            }
        }
        

        if (!cb_discord.isSelected() && !cb_twitter.isSelected() && !cb_telegram.isSelected() ) {
            isDataCorrect = false;
        }

        if (!cb_stat.isSelected() && !cb_sales.isSelected() && !cb_listingandbidding.isSelected()) {
            isDataCorrect = false;
        }

        return isDataCorrect;
    }

    /**
     * Update the data of Marketplace and SocialNetwork
     */
    private void updateData() throws LoginException, InterruptedException, Exception {

        HashMap<MarketPlaceEnum, MarketPlace> mpsToKeep = new HashMap<MarketPlaceEnum, MarketPlace>();

        for (MarketPlace mp : marketplaces.values()) {
            if (mp.getContracts().size() > 0) {
                mp.resetLastRefresh();
                mpsToKeep.put(mp.getMarketplace(), mp);
            }
        }

        socialNetworks.clear();

        if (cb_discord.isSelected()) {
            DiscordSocialNetwork discordSocialNetwork = new DiscordSocialNetwork();
            discordSocialNetwork.instanceDiscord(new String(pwd_discord_token.getPassword()), txt_discord_channel.getText());
            socialNetworks.add(discordSocialNetwork);
        }

        if (cb_twitter.isSelected()) {
            TwitterSocialNetwork twitterSocialNetwork = new TwitterSocialNetwork();
            twitterSocialNetwork.instanceTwitter(new String(pwd_twitter_public_consumer_key.getPassword()), new String(pwd_twitter_private_consumer_key.getPassword()), new String(pwd_twitter_public_key.getPassword()), new String(pwd_twitter_private_key.getPassword()));
            socialNetworks.add(twitterSocialNetwork);

        }
        
        if (cb_telegram.isSelected()) {
            TelegramSocialNetwork telegramSocialNetwork = new TelegramSocialNetwork();
            telegramSocialNetwork.instanceTelegram(new String(pwd_telegram_token.getPassword()), txt_telegram_channel.getText());
            socialNetworks.add(telegramSocialNetwork);

        }

        apiHandlerSales.setMarketplaces(mpsToKeep);
        apiHandlerSales.setSocialNetworks(socialNetworks);

        apiHandlerStat.setMarketplaces(mpsToKeep);
        apiHandlerStat.setSocialNetworks(socialNetworks);
        
        apiHandlerListingAndBidding.setMarketplaces(mpsToKeep);
        apiHandlerListingAndBidding.setSocialNetworks(socialNetworks);
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
            
            if (cb_listingandbidding.isSelected()) {
                scheduledFutureListingAndBidding = executor.scheduleAtFixedRate(apiHandlerListingAndBidding, 0, BotConfiguration.getConfiguration().getRefreshListingAndBiddingTime(), BotConfiguration.getConfiguration().getRefreshListingAndBidding());
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
        
        if (cb_listingandbidding.isSelected()) {
            scheduledFutureListingAndBidding.cancel(true);
        }
        
    }

    /**
     * Export data into an ini file
     */
    private void export() throws IOException, Exception {

        Gson gson = new Gson();

        Wini ini = new Wini();

        ini.put("MainWindow", "version", 3);
        ini.put("MainWindow", "marketplaces", gson.toJson(marketplaces));

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String saveFile = chooser.getSelectedFile().getAbsolutePath();
            if (!saveFile.toLowerCase().endsWith(".ini")) {
                saveFile += ".ini";
            }
            ini.store(new File(saveFile));
        }
    }

    private void importFile() throws IOException, Exception {

        Gson gson = new Gson();
        java.lang.reflect.Type empHashMapType = new TypeToken<HashMap<MarketPlaceEnum, MarketPlace>>() {
        }.getType();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "INI file", "ini");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
            Wini ini = new Wini(chooser.getSelectedFile());

            int version = ini.get("MainWindow", "version", int.class);
            String json = ini.get("MainWindow", "marketplaces", String.class);

            marketplaces.clear();
            ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

            HashMap<MarketPlaceEnum, MarketPlace> mps = gson.fromJson(json, empHashMapType);

            for (MarketPlaceEnum mp : mps.keySet()) {

                CreatorThreadMarketPlaceInterface cmpi = null;
                if (mp == MarketPlaceEnum.Objkt) {
                    cmpi = new CreatorThreadObjkt();
                } else if (mp == MarketPlaceEnum.Teia) {
                    cmpi = new CreatorThreadTeia();
                } else if (mp == MarketPlaceEnum.fxhash) {
                    cmpi = new CreatorThreadFxhash();
                } else if (mp == MarketPlaceEnum.Rarible) {
                    cmpi = new CreatorThreadRarible();
                }

                if (cmpi != null) {
                    marketplaces.put(mp, new MarketPlace(mp, cmpi));
                    MarketPlace newMp = marketplaces.get(mp);
                    newMp.setContracts(mps.get(mp).getContracts());
                    newMp.setItemList(mps.get(mp).getItemList());
                    newMp.setSellerList(mps.get(mp).getSellerList());

                    if (version >= 2) {
                        newMp.setRoyaltywallet(mps.get(mp).getRoyaltywallet());
                    }

                }
            }

            if (version < 2) {
                marketplaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash, new CreatorThreadFxhash()));
            }
            
            if(version < 3){
                marketplaces.put(MarketPlaceEnum.Rarible, new MarketPlace(MarketPlaceEnum.Rarible, new CreatorThreadRarible()));
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
    private javax.swing.JCheckBox cb_listingandbidding;
    private javax.swing.JCheckBox cb_sales;
    private javax.swing.JCheckBox cb_stat;
    private javax.swing.JCheckBox cb_telegram;
    private javax.swing.JCheckBox cb_twitter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbl_discord_channel;
    private javax.swing.JLabel lbl_discord_channel1;
    private javax.swing.JLabel lbl_discord_token;
    private javax.swing.JLabel lbl_discord_token1;
    private javax.swing.JLabel lbl_royalty_wallet;
    private javax.swing.JLabel lbl_telegram_channel;
    private javax.swing.JLabel lbl_telegram_token;
    private javax.swing.JLabel lbl_twitter_access_key;
    private javax.swing.JLabel lbl_twitter_private_consumer_key;
    private javax.swing.JLabel lbl_twitter_private_key;
    private javax.swing.JLabel lbl_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_discord_token;
    private javax.swing.JPasswordField pwd_discord_token1;
    private javax.swing.JPasswordField pwd_telegram_token;
    private javax.swing.JPasswordField pwd_twitter_private_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_private_key;
    private javax.swing.JPasswordField pwd_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_public_key;
    private javax.swing.JTextField tb_royalty_wallet;
    private javax.swing.JTable tbl_contracts;
    private javax.swing.JTable tbl_item;
    private javax.swing.JTable tbl_marketplace;
    private javax.swing.JTable tbl_seller;
    private javax.swing.JTable tbl_status;
    private javax.swing.JTextField txt_discord_channel;
    private javax.swing.JTextField txt_discord_channel1;
    private javax.swing.JTextField txt_telegram_channel;
    // End of variables declaration//GEN-END:variables
}
