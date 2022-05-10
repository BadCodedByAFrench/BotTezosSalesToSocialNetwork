/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotLastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.CallObjkt;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TwitterSocialNetwork;
import java.awt.Dialog;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.security.auth.login.LoginException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Main form of the application
 *
 * @author david
 */
public class MainBotForm extends javax.swing.JFrame {

    /**
     * List of all tje marketplaces where we will find sales
     */
    private List<CallMarketPlaceInterface> marketplaces;

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
     * Boolean used because we never stop the thread. And instead of starting
     * the thread when we start the app, we create it at the fist click on
     * "start" button. Then we have the first call
     */
    private boolean firststart;

    /**
     * Creates new form MainBotForm Get the table as dataModel Create social and
     * maketplace object Create the APIHandler and Executor
     */
    public MainBotForm() {
        initComponents();

        model = (DefaultTableModel) tbl_status.getModel();
        model.setRowCount(0);

        dtb = (DefaultTableModel) tbl_contracts.getModel();
        dtb.addRow(new Object[]{""});

        setStateComponent(true);

        marketplaces = new ArrayList<CallMarketPlaceInterface>();
        socialNetworks = new ArrayList<SocialNetworkInterface>();

        marketplaces.add(new CallObjkt());

        apiHandlerSales = new SalesToSocialNetwork(model, 0);
        apiHandlerStat = new SalesToSocialNetwork(model, 1);

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);

        firststart = true;
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
        tbl_contracts.setEnabled(stateStart);

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
            if (dtb.getRowCount() <= 1) {
                btn_remove.setEnabled(false);
            } else {
                btn_remove.setEnabled(true);
            }

            if (dtb.getRowCount() >= 12) {
                btn_add.setEnabled(false);
            } else {
                btn_add.setEnabled(true);
            }

        } else {
            btn_remove.setEnabled(false);
            btn_add.setEnabled(false);
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
        btn_add = new javax.swing.JButton();
        btn_remove = new javax.swing.JButton();
        cb_stat = new javax.swing.JCheckBox();
        cb_sales = new javax.swing.JCheckBox();
        BTN_Configuration = new javax.swing.JButton();
        lbl_discord_token = new javax.swing.JLabel();
        pwd_discord_token = new javax.swing.JPasswordField();
        lbl_discord_channel = new javax.swing.JLabel();
        cb_twitter = new javax.swing.JCheckBox();
        cb_discord = new javax.swing.JCheckBox();
        txt_discord_channel = new javax.swing.JTextField();

        btn_add1.setText("Add");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        if (tbl_status.getColumnModel().getColumnCount() > 0) {
            tbl_status.getColumnModel().getColumn(1).setHeaderValue("Status");
            tbl_status.getColumnModel().getColumn(2).setHeaderValue("Data");
        }

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
        jScrollPane2.setViewportView(tbl_contracts);

        btn_add.setText("Add");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_remove.setText("Remove");
        btn_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(BTN_License)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BTN_Configuration)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_start)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_stop))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(btn_remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(0, 13, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cb_discord)
                                                    .addComponent(cb_twitter))))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lbl_discord_token)
                                                    .addComponent(lbl_discord_channel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(pwd_discord_token, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                                    .addComponent(txt_discord_channel)))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbl_twitter_access_key)
                                                        .addComponent(lbl_twitter_private_key))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbl_twitter_private_consumer_key)
                                                        .addComponent(lbl_twitter_public_consumer_key)
                                                        .addComponent(cb_stat))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cb_sales)
                                                        .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_add)
                                .addGap(7, 7, 7)
                                .addComponent(btn_remove)
                                .addGap(44, 44, 44)
                                .addComponent(cb_twitter))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cb_stat)
                                    .addComponent(cb_sales))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_twitter_public_consumer_key)
                                    .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_private_consumer_key))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_twitter_access_key)
                                    .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_private_key))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_discord_token)
                                    .addComponent(pwd_discord_token, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_discord_channel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_discord_channel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_discord)
                                .addGap(30, 30, 30)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_start)
                            .addComponent(btn_stop)
                            .addComponent(BTN_License)
                            .addComponent(BTN_Configuration))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        dtb.addRow(new Object[]{""});
        setStateComponent(true);
    }//GEN-LAST:event_btn_addActionPerformed

    /**
     * Remove selected row in contracts table Can't remove when there's one line
     *
     * @param evt
     */

    private void btn_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeActionPerformed
        if (tbl_contracts.getSelectedRows().length != 0) {

            int[] allIndex = tbl_contracts.getSelectedRows();
            int[] reversedAllIndex = Arrays.stream(allIndex).boxed()
                    .sorted(Collections.reverseOrder())
                    .mapToInt(Integer::intValue)
                    .toArray();

            for (int i : reversedAllIndex) {
                dtb.removeRow(i);
            }
        } else {
            dtb.removeRow(dtb.getRowCount() - 1);
        }

        setStateComponent(true);
    }//GEN-LAST:event_btn_removeActionPerformed

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

    /**
     * Check if the field are fill
     *
     * @return true if all data are correct, else wrong
     */
    private boolean checkvalue() {

        boolean isDataCorrect = true;

        Vector<Vector> contractsData = dtb.getDataVector();

        for (Vector v : contractsData) {

            if (v.get(0).toString().isBlank()) {
                isDataCorrect = false;
            }
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
    private void updateData() throws LoginException, InterruptedException {

        List<String> contracts = new ArrayList<String>();
        socialNetworks.clear();
        Vector<Vector> contractsData = dtb.getDataVector();
        for (Vector v : contractsData) {
            contracts.add(v.get(0).toString());
        }

        for (CallMarketPlaceInterface oneMarkeplace : marketplaces) {
            if (oneMarkeplace.getClass() == CallObjkt.class) {
                CallObjkt objkt = (CallObjkt) oneMarkeplace;
                objkt.setContracts(contracts);
            }
        }

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

        apiHandlerSales.setMarketplaces(marketplaces);
        apiHandlerSales.setSocialNetworks(socialNetworks);

        apiHandlerStat.setMarketplaces(marketplaces);
        apiHandlerStat.setSocialNetworks(socialNetworks);
        
        BotLastRefresh.getLastRefresh().resetRefresh();

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

        BotLastRefresh.sendToCollector();
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
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_add1;
    private javax.swing.JButton btn_remove;
    private javax.swing.JButton btn_start;
    private javax.swing.JButton btn_stop;
    private javax.swing.JCheckBox cb_discord;
    private javax.swing.JCheckBox cb_sales;
    private javax.swing.JCheckBox cb_stat;
    private javax.swing.JCheckBox cb_twitter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JTable tbl_status;
    private javax.swing.JTextField txt_discord_channel;
    // End of variables declaration//GEN-END:variables
}
