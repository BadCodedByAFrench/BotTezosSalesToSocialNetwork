/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.Bot;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.BotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.LogManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.SalesHistoryManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.BotStatusEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Misc.ButtonColumn;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.ini4j.Wini;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author david
 */
public class MultipleBotMenu extends javax.swing.JPanel implements PanelRefreshInterface {

    private MainBotForm mainBotForm;

    private DefaultTableModel model;

    /**
     * Creates new form MultipleBotMenu
     */
    public MultipleBotMenu() {
        initComponents();

        model = (DefaultTableModel) tb_bots.getModel();

        //Configure the button action delete
        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Bot botToRemove = (Bot) table.getValueAt(table.getSelectedRow(), 0);

                if (botToRemove.getBotStatus() != BotStatusEnum.Running) {

                    int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this profile ?", "Warning", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {

                        BotManager.getBotManager().removeBot(botToRemove);
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible to delete, this bot is running", "Impossible", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        ButtonColumn buttonColumnDelete = new ButtonColumn(tb_bots, delete, 5);

        //Configure the button action
        Action edit = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                Bot botToEdit = (Bot) table.getValueAt(table.getSelectedRow(), 0);

                BotManager.getBotManager().setCurrentBot(botToEdit);
                mainBotForm.swapView("oneBotConfiguration");
            }
        };

        ButtonColumn buttonColumnEdit = new ButtonColumn(tb_bots, edit, 4);

        //Configure the button action
        Action stop = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                Bot botToStop = (Bot) table.getValueAt(table.getSelectedRow(), 0);

                if (botToStop.getBotStatus() == BotStatusEnum.Running) {
                    botToStop.stop();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    tb_bots.setValueAt(botToStop.getBotStatus(),modelRow,1);
                }
            }
        };

        ButtonColumn buttonColumnStop = new ButtonColumn(tb_bots, stop, 3);

        //Configure the button action
        Action start = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                Bot botToStart = (Bot) table.getValueAt(table.getSelectedRow(), 0);

                if (botToStart.getBotStatus() == BotStatusEnum.Ready) {
                    botToStart.start();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    tb_bots.setValueAt(botToStart.getBotStatus(),modelRow,1);
                }
            }
        };

        ButtonColumn buttonColumnStart = new ButtonColumn(tb_bots, start, 2);

    }

    public void setMainBotForm(MainBotForm mainBotForm) {
        this.mainBotForm = mainBotForm;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tb_bots = new JTable(){

            @Override

            public boolean isCellEditable(int row, int column){
                if (column == 2){
                    if(((Bot) tb_bots.getValueAt(row,0)).getBotStatus() == BotStatusEnum.Ready){
                        return true;
                    }
                    else{
                        return false;
                    }

                }
                else if(column == 3){
                    if(((Bot) tb_bots.getValueAt(row,0)).getBotStatus() == BotStatusEnum.Running){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return true;
                }
            }
        }
        ;
        txt_bot_name = new javax.swing.JTextField();
        btn_editMarketplace = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btn_import = new javax.swing.JButton();
        btn_export = new javax.swing.JButton();

        setName(""); // NOI18N
        setRequestFocusEnabled(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tb_bots.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Bot name", "Status", "Start", "Stop", "Edit", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tb_bots);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 850, 330));
        add(txt_bot_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 140, -1));

        btn_editMarketplace.setText("Edit marketplace profile");
        btn_editMarketplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editMarketplaceActionPerformed(evt);
            }
        });
        add(btn_editMarketplace, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, -1));

        jButton2.setText("Create bot");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 110, -1));

        jButton3.setText("Edit social network profile");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 60, 190, -1));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel4.setText("Bot manager");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, -1));

        btn_import.setText("Full import");
        btn_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importActionPerformed(evt);
            }
        });
        add(btn_import, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        btn_export.setText("Full export");
        btn_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportActionPerformed(evt);
            }
        });
        add(btn_export, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_editMarketplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editMarketplaceActionPerformed
        this.mainBotForm.swapView("marketPlaceProfileManagerAndCreator");
    }//GEN-LAST:event_btn_editMarketplaceActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.mainBotForm.swapView("socialNetworkProfileManagerAndCreator");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!txt_bot_name.getText().isBlank()) {
            if (!BotManager.getBotManager().getAllBots().containsKey(txt_bot_name.getText())) {
                Bot newBot = new Bot(txt_bot_name.getText());
                BotManager.getBotManager().setCurrentBot(newBot);
                BotManager.getBotManager().addBot(newBot);
                this.mainBotForm.swapView("oneBotConfiguration");
            } else {
                JOptionPane.showMessageDialog(null, "This name already exists, you can't create a new one with this one", "Impossible", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportActionPerformed
        try {

            JPasswordField pf = new JPasswordField();
            int answer = JOptionPane.showConfirmDialog(null, pf, "Enter a Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (answer == JOptionPane.OK_OPTION) {

                String password = new String(pf.getPassword());

                if (!password.equals("")) {
                    Gson gson = new Gson();

                    Wini ini = new Wini();

                    ini.put("FullExport", "version", 1);
                    ini.put("FullExport", "marketplaceprofile", encrypt(gson.toJson(MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile()), password));
                    ini.put("FullExport", "socialNetwork", encrypt(gson.toJson(SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile()), password));
                    ini.put("FullExport", "bots", encrypt(gson.toJson(BotManager.getBotManager().getAllBots()), password));

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
                } else {
                    JOptionPane.showMessageDialog(null, "The password is empty", "Empty field", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception ex) {
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }//GEN-LAST:event_btn_exportActionPerformed

    private void btn_importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importActionPerformed
        try {

            JPasswordField pf = new JPasswordField();
            int answer = JOptionPane.showConfirmDialog(null, pf, "Enter a Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (answer == JOptionPane.OK_OPTION) {

                String password = new String(pf.getPassword());

                if (!password.equals("")) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type empHashMapTypeMp = new TypeToken<HashMap<String, MarketPlaceProfile>>() {
                    }.getType();

                    java.lang.reflect.Type empHashMapTypeSn = new TypeToken<HashMap<String, SocialNetworkProfile>>() {
                    }.getType();

                    java.lang.reflect.Type empHashMapTypeBot = new TypeToken<HashMap<String, Bot>>() {
                    }.getType();

                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "INI file", "ini");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".ini")) {
                        Wini ini = new Wini(chooser.getSelectedFile());

                        if (ini.containsKey("FullExport")) {
                            int version = ini.get("FullExport", "version", int.class);
                            String marketplaceprofiles = ini.get("FullExport", "marketplaceprofile", String.class);
                            String socialnetworkprofiles = ini.get("FullExport", "socialNetwork", String.class);
                            String bot = ini.get("FullExport", "bots", String.class);
                            
                            HashMap<String, MarketPlaceProfile> mps = gson.fromJson(decrypt(marketplaceprofiles,password), empHashMapTypeMp);
                            HashMap<String, SocialNetworkProfile> sns = gson.fromJson(decrypt(socialnetworkprofiles,password), empHashMapTypeSn);
                            HashMap<String, Bot> bots = gson.fromJson(decrypt(bot,password), empHashMapTypeBot);
                            
                            
                            
                            //We set all the profiles to be sure that the imported one with gson are not two different ones
                            for(Bot aBot : bots.values()){
                                if(aBot.getBotStatus() == BotStatusEnum.Running){
                                    aBot.setBotStatus(BotStatusEnum.Ready);
                                }
                                aBot.setHistoryManager(new SalesHistoryManager());
                                aBot.setMpProfile(mps.get(aBot.getMpProfile().getName()));
                                aBot.setSnProfile(sns.get(aBot.getSnProfile().getName()));
                            }
                            
                            MarketPlaceProfileManager.getMarketPlaceProfileManager().importMarketPlaceProfileManager(mps);
                            SocialNetworkProfileManager.getSocialNetworkProfileManager().importSnProfiles(sns);
                            BotManager.getBotManager().importBots(bots);
                            
                            refresh();
                        }

                        
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The password is empty", "Empty field", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Impossible to decrypt, check your password", "Error", JOptionPane.WARNING_MESSAGE);
            LogManager.getLogManager().writeLog(MainBotForm.class.getName(), ex);
        }
    }//GEN-LAST:event_btn_importActionPerformed

    private String encrypt(String data, String password) throws Exception {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        
        
        return encryptor.encrypt(data);

    }
    
    private String decrypt(String data, String password) throws Exception {
        
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(password);
        return decryptor.decrypt(data);

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_editMarketplace;
    private javax.swing.JButton btn_export;
    private javax.swing.JButton btn_import;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_bots;
    private javax.swing.JTextField txt_bot_name;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        model.setRowCount(0);

        for (Bot aBot : BotManager.getBotManager().getAllBots().values()) {
            model.addRow(new Object[]{aBot, aBot.getBotStatus(), "Start", "Stop", "Edit", "Delete"});
        }
    }
}
