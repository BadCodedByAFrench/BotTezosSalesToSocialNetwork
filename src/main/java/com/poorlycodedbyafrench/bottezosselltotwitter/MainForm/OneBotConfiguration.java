/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.RefreshLog;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlace;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.SearchContractSales;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TwitterSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author david
 */
public class OneBotConfiguration extends javax.swing.JPanel implements PanelRefreshInterface {

    /**
     * List of all the marketplaces where we will find sales
     */
    private HashMap<MarketPlaceEnum, MarketPlace> showedMarketPlaces;

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

    private SocialNetworkProfile showedSnProfile;

    private MainBotForm mainBotForm;

    private Bot bot;

    private RefreshLog logRefresher;
    
    private ScheduledFuture<?> scheduledLogRefresh;
    
        /**
     * Tool that will execute query every X minutes/hours/days
     */
    private ScheduledThreadPoolExecutor executor;

    /**
     * Creates new form OneBotConfiguration
     */
    public OneBotConfiguration() {
        initComponents();

        model = (DefaultTableModel) tbl_status.getModel();
        model.setRowCount(0);

        logRefresher = new RefreshLog(model,null);

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        
        scheduledLogRefresh = executor.scheduleAtFixedRate(logRefresher, 0, 1, TimeUnit.MINUTES);
        
        dtb = (DefaultTableModel) tbl_contracts.getModel();

        dtbMP = (DefaultTableModel) tbl_marketplace.getModel();

        showedMarketPlaces = new HashMap<MarketPlaceEnum, MarketPlace>();
        showedMarketPlaces.put(MarketPlaceEnum.Objkt, new MarketPlace(MarketPlaceEnum.Objkt));
        showedMarketPlaces.put(MarketPlaceEnum.Teia, new MarketPlace(MarketPlaceEnum.Teia));
        showedMarketPlaces.put(MarketPlaceEnum.fxhash, new MarketPlace(MarketPlaceEnum.fxhash));
        showedMarketPlaces.put(MarketPlaceEnum.Rarible, new MarketPlace(MarketPlaceEnum.Rarible));

        for (MarketPlaceEnum mp : showedMarketPlaces.keySet()) {
            dtbMP.addRow(new Object[]{mp});
        }

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

        stateStart = bot.getBotStatus() != BotStatusEnum.Running;

        btn_start.setEnabled(bot.getBotStatus() == BotStatusEnum.Ready);
        btn_stop.setEnabled(bot.getBotStatus() == BotStatusEnum.Running);
        btn_save.setEnabled(bot.getBotStatus() != BotStatusEnum.Running);

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
    }

    private void addRow(JTable tableToAdd) {
        ((DefaultTableModel) tableToAdd.getModel()).addRow(new Object[]{""});
        setStateComponent(true);
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

        for (MarketPlace mp : showedMarketPlaces.values()) {
            if (mp.getAllContractsString().size() > 0) {
                atleastoneFill = true;
                for (String contract : mp.getAllContractsString()) {
                    if (contract.isBlank()) {
                        isDataCorrect = false;
                    }
                }
            }
        }

        if (!atleastoneFill) {
            isDataCorrect = false;
        }

        if (combo_profile.getSelectedIndex() < 0) {
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

        this.bot.setMarketplaces(showedMarketPlaces);

        showedSnProfile = (SocialNetworkProfile) combo_profile.getSelectedItem();

        bot.setSnProfile(showedSnProfile);

        bot.setStatRunning(cb_stat.isSelected());
        bot.setSalesRunning(cb_sales.isSelected());
        bot.setListingAndBiddingRunning(cb_listingandbidding.isSelected());
    }

    /**
     * Export data into an ini file
     */
    private void exportFile() throws IOException, Exception {

        bot.exportFile();
    }

    private void importFile() throws IOException, Exception {

        bot.importFile();
        showedMarketPlaces = SerializationUtils.clone(bot.getMarketplaces());
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        cb_listingandbidding = new javax.swing.JCheckBox();
        btn_menu = new javax.swing.JButton();
        combo_profile = new javax.swing.JComboBox<>();
        btn_save = new javax.swing.JButton();

        setName("oneBotConfiguration"); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_twitter_public_consumer_key.setText("Public consumer key");
        add(lbl_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 320, -1, -1));

        pwd_twitter_private_consumer_key.setEnabled(false);
        add(pwd_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 350, 74, -1));

        lbl_twitter_private_consumer_key.setText("Private consumer key");
        add(lbl_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, -1, -1));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 340, 385, 100));

        jLabel1.setText("Request status");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 303, -1, -1));

        btn_start.setText("Start");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });
        add(btn_start, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, -1, -1));

        btn_stop.setText("Stop");
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopActionPerformed(evt);
            }
        });
        add(btn_stop, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, -1, -1));

        lbl_twitter_access_key.setText("Public Access Key");
        add(lbl_twitter_access_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, -1, -1));

        lbl_twitter_private_key.setText("Private Access Key");
        add(lbl_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, -1, -1));

        pwd_twitter_private_key.setEnabled(false);
        add(pwd_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 410, 74, -1));

        pwd_twitter_public_key.setEnabled(false);
        add(pwd_twitter_public_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 380, 74, -1));

        pwd_twitter_public_consumer_key.setEnabled(false);
        add(pwd_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, 74, -1));

        BTN_License.setText("Licenses");
        BTN_License.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_LicenseActionPerformed(evt);
            }
        });
        add(BTN_License, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 297, -1, -1));

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

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 30, 320, 220));

        btn_add_contract.setText("Add");
        btn_add_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_contractActionPerformed(evt);
            }
        });
        add(btn_add_contract, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 67, -1));

        btn_remove_contract.setText("Remove");
        btn_remove_contract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_contractActionPerformed(evt);
            }
        });
        add(btn_remove_contract, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 0, -1, -1));

        cb_stat.setText("Stat");
        add(cb_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(612, 331, -1, -1));

        BTN_Configuration.setText("Configuration");
        BTN_Configuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ConfigurationActionPerformed(evt);
            }
        });
        add(BTN_Configuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 297, -1, -1));

        lbl_discord_token.setText("Discord token");
        add(lbl_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 390, -1, -1));

        pwd_discord_token.setEnabled(false);
        add(pwd_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 380, 74, -1));

        cb_sales.setText("Sales");
        cb_sales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_salesActionPerformed(evt);
            }
        });
        add(cb_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(716, 331, -1, -1));

        lbl_discord_channel.setText("Discord channel");
        add(lbl_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 420, -1, -1));

        txt_discord_channel.setEnabled(false);
        add(txt_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 410, 74, -1));

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

        add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(763, 30, 200, 220));

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

        add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(523, 30, 230, 220));

        btn_remove_seller.setText("Remove");
        btn_remove_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_sellerActionPerformed(evt);
            }
        });
        add(btn_remove_seller, new org.netbeans.lib.awtextra.AbsoluteConstraints(673, 0, -1, -1));

        btn_add_seller.setText("Add");
        btn_add_seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_sellerActionPerformed(evt);
            }
        });
        add(btn_add_seller, new org.netbeans.lib.awtextra.AbsoluteConstraints(523, 0, 67, -1));

        btn_remove_item.setText("Remove");
        btn_remove_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remove_itemActionPerformed(evt);
            }
        });
        add(btn_remove_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(883, 0, -1, -1));

        btn_add_item.setText("Add");
        btn_add_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_itemActionPerformed(evt);
            }
        });
        add(btn_add_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(763, 0, 67, -1));

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

        add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 190, 220));

        btn_import.setText("Import");
        btn_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importActionPerformed(evt);
            }
        });
        add(btn_import, new org.netbeans.lib.awtextra.AbsoluteConstraints(612, 297, -1, -1));

        btn_export.setText("Export");
        btn_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportActionPerformed(evt);
            }
        });
        add(btn_export, new org.netbeans.lib.awtextra.AbsoluteConstraints(713, 297, -1, -1));

        lbl_royalty_wallet.setText("Royalty wallet");
        add(lbl_royalty_wallet, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 262, -1, -1));

        tb_royalty_wallet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tb_royalty_walletFocusLost(evt);
            }
        });
        add(tb_royalty_wallet, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 256, 170, -1));

        lbl_telegram_token.setText("Telegram token");
        add(lbl_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 390, -1, -1));

        pwd_telegram_token.setEnabled(false);
        add(pwd_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 380, 74, -1));

        txt_telegram_channel.setEnabled(false);
        add(txt_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 410, 74, -1));

        lbl_telegram_channel.setText("Telegram channel");
        add(lbl_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 420, -1, -1));

        cb_listingandbidding.setText("Listing and bidding");
        cb_listingandbidding.setToolTipText("");
        add(cb_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 330, -1, -1));

        btn_menu.setText("Go to main menu");
        btn_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_menuActionPerformed(evt);
            }
        });
        add(btn_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 297, -1, -1));

        combo_profile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_profileItemStateChanged(evt);
            }
        });
        add(combo_profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 350, 360, -1));

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 60, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
        if (this.bot.getBotStatus() == BotStatusEnum.Ready) {
            bot.start();
            setStateComponent(false);
        }
    }//GEN-LAST:event_btn_startActionPerformed

    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopActionPerformed
        if (this.bot.getBotStatus() == BotStatusEnum.Running) {
            bot.stop();
            setStateComponent(true);
        }
    }//GEN-LAST:event_btn_stopActionPerformed

    private void BTN_LicenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_LicenseActionPerformed

        this.mainBotForm.swapView("licenseBot");

    }//GEN-LAST:event_BTN_LicenseActionPerformed

    private void tbl_contractsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_contractsMouseClicked

        if (tbl_contracts.isEnabled()) {
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

            if (tbl_contracts.getSelectedRowCount() == 1) {

                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

                if (!contract.isBlank()) {
                    for (String oneSeller : showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).getSellerFilter()) {
                        ((DefaultTableModel) tbl_seller.getModel()).addRow(new Object[]{oneSeller});
                    }

                    for (String item : showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).getItemFilter()) {
                        ((DefaultTableModel) tbl_item.getModel()).addRow(new Object[]{item});
                    }
                    tb_royalty_wallet.setText(showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).getRoyaltyWallet());
                }
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

                    ArrayList<String> contractsToRemove = new ArrayList<String>();

                    for (String oneContract : showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsString()) {
                        if (!currentContracts.contains(oneContract)) {
                            contractsToRemove.add(oneContract);
                        }
                    }

                    for (String contractToRemove : contractsToRemove) {
                        showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().remove(contractToRemove);
                    }

                    for (String oneCurrentContracts : currentContracts) {
                        if (!showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().containsKey(oneCurrentContracts)) {
                            showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().put(oneCurrentContracts, new SearchContractSales(oneCurrentContracts));
                        }
                    }
                } else {
                    dtb.removeRow(tbl_contracts.getSelectedRow());
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_contractsPropertyChange

    private void btn_add_contractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_contractActionPerformed
        this.addRow(tbl_contracts);
    }//GEN-LAST:event_btn_add_contractActionPerformed

    private void btn_remove_contractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_contractActionPerformed

        for (int index : tbl_contracts.getSelectedRows()) {
            showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().remove(dtb.getDataVector().get(index).get(0));
        }

        this.removeRow(tbl_contracts);

        ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
        ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);
    }//GEN-LAST:event_btn_remove_contractActionPerformed

    private void BTN_ConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_ConfigurationActionPerformed
        this.mainBotForm.swapView("configurationMenu");
    }//GEN-LAST:event_BTN_ConfigurationActionPerformed

    private void cb_salesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_salesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_salesActionPerformed

    private void tbl_itemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_itemPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (!tbl_item.isEditing()) {

                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).getItemFilter().clear();

                for (Vector v : ((DefaultTableModel) tbl_item.getModel()).getDataVector()) {
                    String item = (String) v.get(0);

                    if (!item.isBlank()) {
                        showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).addItemFilter(item.trim());
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_itemPropertyChange

    private void tbl_sellerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbl_sellerPropertyChange
        if ("tableCellEditor".equals(evt.getPropertyName())) {
            if (!tbl_seller.isEditing()) {

                String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
                showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).getSellerFilter().clear();

                for (Vector v : ((DefaultTableModel) tbl_seller.getModel()).getDataVector()) {
                    String seller = (String) v.get(0);

                    if (!seller.isBlank()) {
                        showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).addSellerFilter(seller.trim());
                    }
                }
            }
            setStateComponent(true);
        }
    }//GEN-LAST:event_tbl_sellerPropertyChange

    private void btn_remove_sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_sellerActionPerformed
        for (int index : tbl_seller.getSelectedRows()) {
            String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
            String seller = (String) ((DefaultTableModel) tbl_seller.getModel()).getDataVector().get(index).get(0);

            showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).removeSellerFilter(seller);
        }

        this.removeRow(tbl_seller);
    }//GEN-LAST:event_btn_remove_sellerActionPerformed

    private void btn_add_sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_sellerActionPerformed
        this.addRow(tbl_seller);
    }//GEN-LAST:event_btn_add_sellerActionPerformed

    private void btn_remove_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remove_itemActionPerformed
        for (int index : tbl_item.getSelectedRows()) {
            String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);
            String item = (String) ((DefaultTableModel) tbl_item.getModel()).getDataVector().get(index).get(0);

            showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).removeItemFilter(item);
        }

        this.removeRow(tbl_item);
    }//GEN-LAST:event_btn_remove_itemActionPerformed

    private void btn_add_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_itemActionPerformed
        this.addRow(tbl_item);
    }//GEN-LAST:event_btn_add_itemActionPerformed

    private void tbl_marketplaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_marketplaceMouseClicked
        if (tbl_marketplace.isEnabled()) {
            ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
            ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);
            tb_royalty_wallet.setText("");

            if (tbl_marketplace.getSelectedRowCount() == 1) {

                MarketPlace currentmp = showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0));

                for (String contract : currentmp.getAllContractsString()) {
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
            this.exportFile();
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }//GEN-LAST:event_btn_exportActionPerformed

    private void tb_royalty_walletFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tb_royalty_walletFocusLost
        String contract = (String) dtb.getDataVector().get(tbl_contracts.getSelectedRow()).get(0);

        if (!contract.isBlank()) {
            showedMarketPlaces.get(dtbMP.getDataVector().get(tbl_marketplace.getSelectedRow()).get(0)).getAllContractsFromThisMarketPlace().get(contract).setRoyaltyWallet(tb_royalty_wallet.getText().trim());
        }
        setStateComponent(true);
    }//GEN-LAST:event_tb_royalty_walletFocusLost

    private void btn_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_menuActionPerformed
        this.mainBotForm.swapView("multipleBotMenu");
    }//GEN-LAST:event_btn_menuActionPerformed

    private void combo_profileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_profileItemStateChanged
        if (combo_profile.hasFocus()) {
            showedSnProfile = (SocialNetworkProfile) combo_profile.getSelectedItem();
            setSnValue();
        }
    }//GEN-LAST:event_combo_profileItemStateChanged

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        if (checkvalue()) {
            try {
                updateData();
                bot.checkComplete();
                setStateComponent(false);

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
    }//GEN-LAST:event_btn_saveActionPerformed

    public void setMainBotForm(MainBotForm mainBotForm) {
        this.mainBotForm = mainBotForm;
    }

    @Override
    public void refresh() {
        combo_profile.removeAllItems();
        for (SocialNetworkProfile snProfile : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().values()) {
            combo_profile.addItem(snProfile);
        }

        bot = BotManager.getBotManager().getCurrentBot();
        showedMarketPlaces = SerializationUtils.clone(bot.getMarketplaces());
        showedSnProfile = bot.getSnProfile();
        logRefresher.setSource(bot);
        setValue();
        setStateComponent(true);
    }

    public void setValue() {
        ((DefaultTableModel) tbl_contracts.getModel()).setRowCount(0);
        ((DefaultTableModel) tbl_item.getModel()).setRowCount(0);

        ((DefaultTableModel) tbl_seller.getModel()).setRowCount(0);
        tb_royalty_wallet.setText("");

        cb_stat.setSelected(bot.isStatRunning());
        cb_sales.setSelected(bot.isSalesRunning());
        cb_listingandbidding.setSelected(bot.isListingAndBiddingRunning());
        
        combo_profile.setSelectedItem(showedSnProfile);
        setSnValue();
    }

    public void setSnValue() {

        pwd_twitter_public_consumer_key.setText("");
        pwd_twitter_private_consumer_key.setText("");
        pwd_twitter_public_key.setText("");
        pwd_twitter_private_key.setText("");
        pwd_discord_token.setText("");
        txt_discord_channel.setText("");
        pwd_telegram_token.setText("");
        txt_telegram_channel.setText("");

        if (showedSnProfile != null) {

            
            for (SocialNetworkInterface sn : showedSnProfile.getAllSocialNetwork()) {

                if (sn instanceof TwitterSocialNetwork) {
                    TwitterSocialNetwork twitter = (TwitterSocialNetwork) sn;
                    pwd_twitter_public_consumer_key.setText(twitter.getPublicConsumerKey());
                    pwd_twitter_private_consumer_key.setText(twitter.getPrivateConsumerKey());
                    pwd_twitter_public_key.setText(twitter.getPublicAccessKey());
                    pwd_twitter_private_key.setText(twitter.getPrivateAccessKey());
                }

                if (sn instanceof DiscordSocialNetwork) {
                    DiscordSocialNetwork discord = (DiscordSocialNetwork) sn;
                    pwd_discord_token.setText(discord.getToken());
                    txt_discord_channel.setText(discord.getChannelName());
                }

                if (sn instanceof TelegramSocialNetwork) {
                    TelegramSocialNetwork telegram = (TelegramSocialNetwork) sn;
                    pwd_telegram_token.setText(telegram.getApiKey());
                    txt_telegram_channel.setText(telegram.getChannelId());
                }
            }
        } else {
            combo_profile.setSelectedIndex(-1);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_Configuration;
    private javax.swing.JButton BTN_License;
    private javax.swing.JButton btn_add_contract;
    private javax.swing.JButton btn_add_item;
    private javax.swing.JButton btn_add_seller;
    private javax.swing.JButton btn_export;
    private javax.swing.JButton btn_import;
    private javax.swing.JButton btn_menu;
    private javax.swing.JButton btn_remove_contract;
    private javax.swing.JButton btn_remove_item;
    private javax.swing.JButton btn_remove_seller;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_start;
    private javax.swing.JButton btn_stop;
    private javax.swing.JCheckBox cb_listingandbidding;
    private javax.swing.JCheckBox cb_sales;
    private javax.swing.JCheckBox cb_stat;
    private javax.swing.JComboBox<SocialNetworkProfile> combo_profile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbl_discord_channel;
    private javax.swing.JLabel lbl_discord_token;
    private javax.swing.JLabel lbl_royalty_wallet;
    private javax.swing.JLabel lbl_telegram_channel;
    private javax.swing.JLabel lbl_telegram_token;
    private javax.swing.JLabel lbl_twitter_access_key;
    private javax.swing.JLabel lbl_twitter_private_consumer_key;
    private javax.swing.JLabel lbl_twitter_private_key;
    private javax.swing.JLabel lbl_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_discord_token;
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
    private javax.swing.JTextField txt_telegram_channel;
    // End of variables declaration//GEN-END:variables
}
