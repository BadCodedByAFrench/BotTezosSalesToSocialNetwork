/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.ApiRunnable.SalesToSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotConfiguration;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.BotLastRefresh;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.CallObjkt;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceInterface.CallMarketPlaceInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TwitterSocialNetwork;
import java.awt.Dialog;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Main form of the application
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
    private ScheduledThreadPoolExecutor  executor;
    
    private ScheduledFuture<?> scheduledFutureSales;
    private ScheduledFuture<?> scheduledFutureStat;
    
    /**
     * Boolean used because we never stop the thread. And instead of starting the thread when we start the app, we create it at the fist click on "start" button. Then we have the first call
     */
    private boolean firststart;
    /**
     * Creates new form MainBotForm
     * Get the table as dataModel
     * Create social and maketplace object
     * Create the APIHandler and Executor
     */
    public MainBotForm() {
        initComponents();
        
        model = (DefaultTableModel) tbl_status.getModel();
        model.setRowCount(0);
        
        dtb = (DefaultTableModel)tbl_contracts.getModel();
        dtb.addRow(new Object[]{""});
        
        setStateComponent(true);
                
        
        marketplaces = new ArrayList<CallMarketPlaceInterface>();
        socialNetworks = new ArrayList<SocialNetworkInterface>();
        
        marketplaces.add(new CallObjkt());
        
        socialNetworks.add(new TwitterSocialNetwork());
              
        apiHandlerSales = new SalesToSocialNetwork(model,0);
        apiHandlerStat = new SalesToSocialNetwork(model,1);
        
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);
        
        firststart = true;
    }

    /**
     * Change the state of the component depending if the bot is running or not
     * @param stateStart 
     */
    private void setStateComponent(boolean stateStart){
        btn_start.setEnabled(stateStart);
        BTN_Configuration.setEnabled(stateStart);
        tbl_contracts.setEnabled(stateStart);
        pwd_twitter_public_consumer_key.setEnabled(stateStart);
        pwd_twitter_private_consumer_key.setEnabled(stateStart);
        pwd_twitter_public_key.setEnabled(stateStart);
        pwd_twitter_private_key.setEnabled(stateStart);
        tbl_contracts.setEnabled(stateStart);
        
        if(stateStart){
            if(dtb.getRowCount() <= 1 ){
                btn_remove.setEnabled(false);
            }else{
                btn_remove.setEnabled(true);
            }
            
            if(dtb.getRowCount() >= 12){
                btn_add.setEnabled(false);
            }
            else{
                btn_add.setEnabled(true);
            }
            
        }else{
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
        cb_twitter_stat = new javax.swing.JCheckBox();
        cb_twitter_sales = new javax.swing.JCheckBox();
        BTN_Configuration = new javax.swing.JButton();

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

        cb_twitter_stat.setText("Stat");

        cb_twitter_sales.setText("Sales");

        BTN_Configuration.setText("Configuration");
        BTN_Configuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ConfigurationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_twitter_private_consumer_key)
                                            .addComponent(lbl_twitter_public_consumer_key))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_twitter_access_key)
                                            .addComponent(lbl_twitter_private_key)
                                            .addComponent(cb_twitter_stat))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_twitter_sales)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BTN_License)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btn_start)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btn_stop))
                                    .addComponent(BTN_Configuration))
                                .addGap(13, 13, 13)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_twitter_public_consumer_key)
                            .addComponent(pwd_twitter_public_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_add))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_twitter_private_consumer_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_twitter_private_consumer_key)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(btn_remove)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_twitter_access_key)
                            .addComponent(pwd_twitter_public_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pwd_twitter_private_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_twitter_private_key))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_twitter_stat)
                            .addComponent(cb_twitter_sales))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BTN_License)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BTN_Configuration)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_start)
                            .addComponent(btn_stop))
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lbl_twitter_public_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_login");
        lbl_twitter_private_consumer_key.getAccessibleContext().setAccessibleName("lbl_twitter_password");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    

     /**
     * Add row in the contracts table
     * Limite to 20 contracts
     * @param evt 
     */
    
    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        dtb.addRow(new Object[]{""});
        setStateComponent(true);
    }//GEN-LAST:event_btn_addActionPerformed

     /**
     * Remove selected row in contracts table
     * Can't remove when there's one line
     * @param evt 
     */
    
    private void btn_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeActionPerformed
        if(tbl_contracts.getSelectedRows().length != 0){
            
            int[] allIndex = tbl_contracts.getSelectedRows();
            int[] reversedAllIndex = Arrays.stream(allIndex).boxed()
                    .sorted(Collections.reverseOrder())
                    .mapToInt(Integer::intValue)
                    .toArray();

            for (int i : reversedAllIndex ){
                 dtb.removeRow(i);
            }
        }
        else{
            dtb.removeRow(dtb.getRowCount()-1);
        }
        
        setStateComponent(true);
    }//GEN-LAST:event_btn_removeActionPerformed

     /**
     * We change the state of component
     * and we stop the bot
     * @param evt 
     */
    
    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopActionPerformed
        setStateComponent(true);
        stoploop();
    }//GEN-LAST:event_btn_stopActionPerformed

     /**
     * We check value
     * If it's good, we change the state of component
     * Update data
     * and start the bot
     * @param evt 
     */
    
    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
        if (checkvalue()) {
            setStateComponent(false);
            updateData();
            startloop(); 
        }
        else{
            JOptionPane.showMessageDialog(null, "All the field are not filled", "Empty field", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_startActionPerformed

     /**
     * Show the license window
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

    
    /**
     * Check if the field are fill
     * @return true if all data are correct, else wrong
     */
    private boolean checkvalue(){
        
        boolean isDataCorrect = true;
        
        Vector<Vector> contractsData = dtb.getDataVector();
        
        for(Vector v : contractsData){
            
            if (v.get(0).toString().isBlank()){
                isDataCorrect = false;
            }
        }
        
        if (new String(pwd_twitter_public_consumer_key.getPassword()).isBlank()){
            isDataCorrect = false;
        }
        
        if (new String(pwd_twitter_private_consumer_key.getPassword()).isBlank()){
            isDataCorrect = false;
        }
        
        if (new String(pwd_twitter_public_key.getPassword()).isBlank()){
            isDataCorrect = false;
        }
        
        if ( new String(pwd_twitter_private_key.getPassword()).isBlank()){
            isDataCorrect = false;
        }
        
        if(!cb_twitter_stat.isSelected() && !cb_twitter_sales.isSelected() ){
            isDataCorrect = false;
        }
        
        return isDataCorrect;
    }
    
    /**
     * Update the data of Marketplace and SocialNetwork
     */
    private void updateData(){
        
        List<String> contracts = new ArrayList<String>(); 
        Vector<Vector> contractsData = dtb.getDataVector();
        for(Vector v : contractsData){
            contracts.add(v.get(0).toString());
        }
        
        for(CallMarketPlaceInterface oneMarkeplace : marketplaces){
                if (oneMarkeplace.getClass() == CallObjkt.class){
                    CallObjkt objkt = (CallObjkt) oneMarkeplace;
                    objkt.setContracts(contracts);
                }
        }
        
        for(SocialNetworkInterface oneSocialNetwork : socialNetworks){
            
            if (oneSocialNetwork.getClass() == TwitterSocialNetwork.class){
                TwitterSocialNetwork twitterSocialNetwork = (TwitterSocialNetwork) oneSocialNetwork;
                twitterSocialNetwork.instanceTwitter(new String(pwd_twitter_public_consumer_key.getPassword()), new String(pwd_twitter_private_consumer_key.getPassword()), new String(pwd_twitter_public_key.getPassword()), new String(pwd_twitter_private_key.getPassword()), cb_twitter_stat.isSelected(), cb_twitter_sales.isSelected(), model);
            }
        }
        
        
        apiHandlerSales.setMarketplaces(marketplaces);
        apiHandlerSales.setSocialNetworks(socialNetworks);
        
        apiHandlerStat.setMarketplaces(marketplaces);
        apiHandlerStat.setSocialNetworks(socialNetworks);
        
    }
    
    /**
     * Start the bot
     * 
     */
    private void startloop(){

        if(cb_twitter_sales.isSelected()){
            scheduledFutureSales = executor.scheduleAtFixedRate(apiHandlerSales, 0, BotConfiguration.getConfiguration().getRefreshSalesTime(), BotConfiguration.getConfiguration().getRefreshSales());
        }
        
        if(cb_twitter_stat.isSelected()){
            scheduledFutureStat = executor.scheduleAtFixedRate(apiHandlerStat, 0, BotConfiguration.getConfiguration().getRefreshSalesStats(), BotConfiguration.getConfiguration().getRefreshStats());
        }
    }
    
    /**
     * Stop the bot
     */
    private void stoploop(){
        
        if(cb_twitter_sales.isSelected()){
            scheduledFutureSales.cancel(true);
        }
        
        if(cb_twitter_stat.isSelected()){
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
            java.util.logging.Logger.getLogger(MainBotForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainBotForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainBotForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainBotForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JCheckBox cb_twitter_sales;
    private javax.swing.JCheckBox cb_twitter_stat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_twitter_access_key;
    private javax.swing.JLabel lbl_twitter_private_consumer_key;
    private javax.swing.JLabel lbl_twitter_private_key;
    private javax.swing.JLabel lbl_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_private_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_private_key;
    private javax.swing.JPasswordField pwd_twitter_public_consumer_key;
    private javax.swing.JPasswordField pwd_twitter_public_key;
    private javax.swing.JTable tbl_contracts;
    private javax.swing.JTable tbl_status;
    // End of variables declaration//GEN-END:variables
}
