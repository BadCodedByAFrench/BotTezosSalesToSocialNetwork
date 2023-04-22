/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetworkFullCommands;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetworkFullCommands;
import javax.swing.JOptionPane;

/**
 *
 * @author david
 */
public class GenericBotMenu extends javax.swing.JPanel implements PanelRefreshInterface {

    private MainBotForm mainBotForm;
    
    /**
     * Creates new form GenericBotMenu
     */
    public GenericBotMenu() {
        initComponents();
        
        //Put invisible telegram
        //Maybe one day I will try to work on it (I need a better management to know who is an adminstrator from a normal user)
        this.pwd_telegram.setVisible(false);
        this.btn_telegram_connect.setVisible(false);
        this.btn_telegram_disconnect.setVisible(false);
        this.btn_manage_telegram.setVisible(false);
        this.lbl_telegram.setVisible(false);
        this.lbl_telegram_api.setVisible(false);
    }

    private void setState(){
        boolean isDiscordSetUp = GenericBotManager.getGenericBotManager().getGenericDiscord() != null;
        boolean isTelegramSetUp = GenericBotManager.getGenericBotManager().getGenericTelegram()!= null;
        
        this.pwd_discord.setEnabled(!isDiscordSetUp);
        this.btn_discord_connect.setEnabled(!isDiscordSetUp);
        this.btn_discord_disconnect.setEnabled(isDiscordSetUp);
        
        /*this.pwd_telegram.setEnabled(!isTelegramSetUp);
        this.btn_telegram_connect.setEnabled(!isTelegramSetUp);
        this.btn_telegram_disconnect.setEnabled(isTelegramSetUp);*/
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_title = new javax.swing.JLabel();
        lbl_discord = new javax.swing.JLabel();
        lbl_telegram = new javax.swing.JLabel();
        btn_discord_connect = new javax.swing.JButton();
        btn_discord_disconnect = new javax.swing.JButton();
        btn_telegram_disconnect = new javax.swing.JButton();
        btn_telegram_connect = new javax.swing.JButton();
        lbl_discord_api = new javax.swing.JLabel();
        lbl_telegram_api = new javax.swing.JLabel();
        pwd_discord = new javax.swing.JPasswordField();
        pwd_telegram = new javax.swing.JPasswordField();
        btn_menu = new javax.swing.JButton();
        btn_manage_discord = new javax.swing.JButton();
        btn_manage_telegram = new javax.swing.JButton();
        btn_ad = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_title.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        lbl_title.setText("Generic bot menu");
        add(lbl_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 27, -1, -1));

        lbl_discord.setText("Discord");
        add(lbl_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, -1, -1));

        lbl_telegram.setText("Telegram");
        add(lbl_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 120, -1, -1));

        btn_discord_connect.setText("Connect");
        btn_discord_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_discord_connectActionPerformed(evt);
            }
        });
        add(btn_discord_connect, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 90, -1));

        btn_discord_disconnect.setText("Disconnect");
        btn_discord_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_discord_disconnectActionPerformed(evt);
            }
        });
        add(btn_discord_disconnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, -1, -1));

        btn_telegram_disconnect.setText("Disconnect");
        btn_telegram_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_telegram_disconnectActionPerformed(evt);
            }
        });
        add(btn_telegram_disconnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 190, -1, -1));

        btn_telegram_connect.setText("Connect");
        btn_telegram_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_telegram_connectActionPerformed(evt);
            }
        });
        add(btn_telegram_connect, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 150, 90, -1));

        lbl_discord_api.setText("Discord API Keys");
        add(lbl_discord_api, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        lbl_telegram_api.setText("Telegram API Keys");
        add(lbl_telegram_api, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 160, -1, -1));
        add(pwd_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 90, -1));
        add(pwd_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 150, 90, -1));

        btn_menu.setText("Go to main menu");
        btn_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_menuActionPerformed(evt);
            }
        });
        add(btn_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, -1, -1));

        btn_manage_discord.setText("Manage");
        btn_manage_discord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_manage_discordActionPerformed(evt);
            }
        });
        add(btn_manage_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, -1, -1));

        btn_manage_telegram.setText("Manage");
        btn_manage_telegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_manage_telegramActionPerformed(evt);
            }
        });
        add(btn_manage_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 270, -1, -1));

        btn_ad.setText("Ad management");
        btn_ad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adActionPerformed(evt);
            }
        });
        add(btn_ad, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 270, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_discord_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_discord_connectActionPerformed
        DiscordSocialNetworkFullCommands discord = new DiscordSocialNetworkFullCommands();
        discord.initiateValue(new String(pwd_discord.getPassword()));
        try {
            discord.instanceDiscord();
            GenericBotManager.getGenericBotManager().setGenericDiscord(discord);
            JOptionPane.showMessageDialog(null, "Connection successful", "Success", JOptionPane.PLAIN_MESSAGE);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Impossible to connect, check your API Key", "Impossible", JOptionPane.WARNING_MESSAGE);
            LogManager.getLogManager().writeLog(GenericBotMenu.class.getName(), ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossible to connect, check your API Key", "Impossible", JOptionPane.WARNING_MESSAGE);
            LogManager.getLogManager().writeLog(GenericBotMenu.class.getName(), ex);
        }
        
        setState();
    }//GEN-LAST:event_btn_discord_connectActionPerformed

    private void btn_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_menuActionPerformed
        this.mainBotForm.swapView("multipleBotMenu");
    }//GEN-LAST:event_btn_menuActionPerformed

    private void btn_telegram_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_telegram_connectActionPerformed
        try {
            TelegramSocialNetworkFullCommands telegram = new TelegramSocialNetworkFullCommands();
            telegram.initiateValue(new String(pwd_telegram.getPassword()));
            telegram.instanceTelegram();
            GenericBotManager.getGenericBotManager().setGenericTelegram(telegram);
            
           JOptionPane.showMessageDialog(null, "Connection successful", "Success", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossible to connect, check your API Key", "Impossible", JOptionPane.WARNING_MESSAGE);
            LogManager.getLogManager().writeLog(GenericBotMenu.class.getName(), ex);
        }
        
         setState();
    }//GEN-LAST:event_btn_telegram_connectActionPerformed

    private void btn_manage_discordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_manage_discordActionPerformed
        this.mainBotForm.swapView("allDiscordServerGenericBot");
    }//GEN-LAST:event_btn_manage_discordActionPerformed

    private void btn_manage_telegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_manage_telegramActionPerformed
        this.mainBotForm.swapView("allTelegramServerGenericBot");
    }//GEN-LAST:event_btn_manage_telegramActionPerformed

    private void btn_adActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adActionPerformed
        this.mainBotForm.swapView("adCompainMenu");
    }//GEN-LAST:event_btn_adActionPerformed

    private void btn_discord_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_discord_disconnectActionPerformed
        GenericBotManager.getGenericBotManager().getGenericDiscord().stop();
        GenericBotManager.getGenericBotManager().setGenericDiscord(null);
        pwd_discord.setText("");
        JOptionPane.showMessageDialog(null, "Connection stopped", "Stopped", JOptionPane.PLAIN_MESSAGE);
        setState();
    }//GEN-LAST:event_btn_discord_disconnectActionPerformed

    private void btn_telegram_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_telegram_disconnectActionPerformed
       GenericBotManager.getGenericBotManager().getGenericTelegram().stop();
        GenericBotManager.getGenericBotManager().setGenericTelegram(null);
        pwd_telegram.setText("");
        JOptionPane.showMessageDialog(null, "Connection stopped", "Stopped", JOptionPane.PLAIN_MESSAGE);
        setState();
    }//GEN-LAST:event_btn_telegram_disconnectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_ad;
    private javax.swing.JButton btn_discord_connect;
    private javax.swing.JButton btn_discord_disconnect;
    private javax.swing.JButton btn_manage_discord;
    private javax.swing.JButton btn_manage_telegram;
    private javax.swing.JButton btn_menu;
    private javax.swing.JButton btn_telegram_connect;
    private javax.swing.JButton btn_telegram_disconnect;
    private javax.swing.JLabel lbl_discord;
    private javax.swing.JLabel lbl_discord_api;
    private javax.swing.JLabel lbl_telegram;
    private javax.swing.JLabel lbl_telegram_api;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JPasswordField pwd_discord;
    private javax.swing.JPasswordField pwd_telegram;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        setState();
        if(GenericBotManager.getGenericBotManager().getGenericDiscord() != null){
            pwd_discord.setText(GenericBotManager.getGenericBotManager().getGenericDiscord().getToken());
        }
    }

    @Override
    public void setMainBotForm(MainBotForm mainBotForm) {
        this.mainBotForm = mainBotForm;
    }
}