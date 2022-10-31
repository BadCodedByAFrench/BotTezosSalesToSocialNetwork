/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MarketPlaceClass.MarketPlaceProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Misc.ButtonColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author david
 */
public class MarketPlaceProfileManagerAndCreator extends javax.swing.JPanel implements PanelRefreshInterface {

    private DefaultTableModel model;
    private JButton deleteBotButton = new JButton();
    private MainBotForm mainBotForm;

    private MarketPlaceProfile showedMpProfile;

    /**
     * Creates new form MarketPlaceProfileManagerAndCreator
     */
    public MarketPlaceProfileManagerAndCreator() {
        initComponents();

        deleteBotButton.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "Do you want to delete this profile?");
            }
        }
        );

        //Configure the button action
        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                MarketPlaceProfile mpToDelete = (MarketPlaceProfile) table.getValueAt(table.getSelectedRow(), 0);

                String usedByBots = MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().get(mpToDelete.getName()).isUsed();
                if (usedByBots.equals("")) {

                    int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this profile ?", "Warning", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {

                        MarketPlaceProfileManager.getMarketPlaceProfileManager().removeMarketPlaceProfile(mpToDelete.getName());
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                        combo_profile.removeItem(mpToDelete);

                        if (MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().size() == 0) {
                            showedMpProfile = new MarketPlaceProfile();
                        } else {
                            showedMpProfile = (MarketPlaceProfile) combo_profile.getSelectedItem();
                        }
                        loadValue();
                    }
                    setState();
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible to delete, this profile is currently used by theses bots. Please remove the link between them and the profile first :\n" + usedByBots, "Impossible", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(tb_allMarketPlaceProfiles, delete, 1);

        model = (DefaultTableModel) tb_allMarketPlaceProfiles.getModel();

        this.combo_refresh_sales.addItem(TimeUnit.MINUTES.toString());
        this.combo_refresh_sales.addItem(TimeUnit.HOURS.toString());
        this.combo_refresh_sales.addItem(TimeUnit.DAYS.toString());

        this.combo_refresh_stat.addItem(TimeUnit.HOURS.toString());
        this.combo_refresh_stat.addItem(TimeUnit.DAYS.toString());

        this.combo_refresh_listingandbidding.addItem(TimeUnit.MINUTES.toString());
        this.combo_refresh_listingandbidding.addItem(TimeUnit.HOURS.toString());
        this.combo_refresh_listingandbidding.addItem(TimeUnit.DAYS.toString());

        //Take from https://stackhowto.com/how-to-make-jtextfield-accept-only-numbers/
        this.txt_refresh_sales.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        this.txt_refresh_stats.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        this.txt_refresh_listingandbidding.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        tb_allMarketPlaceProfiles.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row

                if (!event.getValueIsAdjusting() && tb_allMarketPlaceProfiles.getSelectedRow() >= 0) {
                    showedMpProfile = (MarketPlaceProfile) tb_allMarketPlaceProfiles.getValueAt(tb_allMarketPlaceProfiles.getSelectedRow(), 0);
                    combo_profile.setSelectedItem(showedMpProfile);
                    loadValue();
                    setState();
                }
            }
        });
    }

    @Override
    public void refresh() {
        model.setRowCount(0);

        combo_profile.removeAllItems();

        for (MarketPlaceProfile mpProfile : MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().values()) {
            model.addRow(new Object[]{mpProfile, "Delete"});
            combo_profile.addItem(mpProfile);
        }

        if (MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().size() > 0) {
            showedMpProfile = combo_profile.getItemAt(0);
            combo_profile.setSelectedIndex(0);
            tb_allMarketPlaceProfiles.setRowSelectionInterval(0, 0);
        } else {
            showedMpProfile = new MarketPlaceProfile();
        }

        loadValue();

        setState();
    }

    public void setMainBotForm(MainBotForm mainBotForm) {
        this.mainBotForm = mainBotForm;
    }

    private void setState() {

        if (showedMpProfile.getName().isBlank()) {
            txt_name.setEnabled(true);
            btn_create.setEnabled(false);
            btn_cancel.setEnabled(MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().size() > 0);
        } else {
            txt_name.setEnabled(false);
            btn_create.setEnabled(MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().size() < 5);
            btn_cancel.setEnabled(false);
        }
    }

    private void loadValue() {
        this.txt_refresh_sales.setText(String.valueOf(showedMpProfile.getRefreshSalesTime()));
        this.txt_refresh_stats.setText(String.valueOf(showedMpProfile.getRefreshSalesStats()));
        this.txt_refresh_listingandbidding.setText(String.valueOf(showedMpProfile.getRefreshListingAndBiddingTime()));

        this.combo_refresh_sales.setSelectedItem(showedMpProfile.getRefreshSales().toString());
        this.combo_refresh_stat.setSelectedItem(showedMpProfile.getRefreshStats().toString());
        this.combo_refresh_listingandbidding.setSelectedItem(showedMpProfile.getRefreshListingAndBidding().toString());

        this.txt_name.setText(showedMpProfile.getName());
    }

    private void save() {

        if (checkValue()) {
            showedMpProfile.setRefreshSalesTime(Integer.valueOf(this.txt_refresh_sales.getText()));
            showedMpProfile.setRefreshSalesStats(Integer.valueOf(this.txt_refresh_stats.getText()));
            showedMpProfile.setRefreshListingAndBiddingTime(Integer.valueOf(this.txt_refresh_listingandbidding.getText()));

            showedMpProfile.setRefreshSales(TimeUnit.valueOf(this.combo_refresh_sales.getSelectedItem().toString().toUpperCase()));
            showedMpProfile.setRefreshStats(TimeUnit.valueOf(this.combo_refresh_stat.getSelectedItem().toString().toUpperCase()));
            showedMpProfile.setRefreshListingAndBidding(TimeUnit.valueOf(this.combo_refresh_listingandbidding.getSelectedItem().toString().toUpperCase()));

            showedMpProfile.setName(this.txt_name.getText());

            if (!MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().containsKey(showedMpProfile.getName())) {
                MarketPlaceProfileManager.getMarketPlaceProfileManager().addMarketPlaceProfile(showedMpProfile);
                model.addRow(new Object[]{showedMpProfile, "Delete"});

                combo_profile.addItem(showedMpProfile);
                combo_profile.setSelectedItem(showedMpProfile);
                tb_allMarketPlaceProfiles.setRowSelectionInterval(combo_profile.getSelectedIndex(), combo_profile.getSelectedIndex());
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled or the name is already used", "Empty field or exist", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean checkValue() {
        boolean checkValue = true;

        if (!this.txt_refresh_sales.getText().isBlank() && !this.txt_refresh_stats.getText().isBlank() && !this.txt_refresh_listingandbidding.getText().isBlank()) {
            if (Integer.valueOf(this.txt_refresh_sales.getText()) == 0 || Integer.valueOf(this.txt_refresh_stats.getText()) == 0 || Integer.valueOf(this.txt_refresh_listingandbidding.getText()) == 0) {
                checkValue = false;
            }
        } else {
            checkValue = false;
        }

        if (this.txt_name.getText().isBlank()) {
            checkValue = false;
        } else {
            if (showedMpProfile.getName().isBlank() && MarketPlaceProfileManager.getMarketPlaceProfileManager().getAllMarketPlaceProfile().containsKey(this.txt_name.getText())) {
                checkValue = false;
            }
        }

        return checkValue;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_allMarketPlaceProfiles = new javax.swing.JTable();
        lbl_refresh_stat = new javax.swing.JLabel();
        txt_refresh_stats = new javax.swing.JTextField();
        lbl_stat = new javax.swing.JLabel();
        combo_refresh_stat = new javax.swing.JComboBox<>();
        lbl_sales1 = new javax.swing.JLabel();
        txt_refresh_sales = new javax.swing.JTextField();
        lbl_refresh_sales = new javax.swing.JLabel();
        combo_refresh_sales = new javax.swing.JComboBox<>();
        lbl_listingandbidding = new javax.swing.JLabel();
        lbl_refresh_listingandbidding = new javax.swing.JLabel();
        txt_refresh_listingandbidding = new javax.swing.JTextField();
        combo_refresh_listingandbidding = new javax.swing.JComboBox<>();
        btn_create = new javax.swing.JButton();
        txt_name = new javax.swing.JTextField();
        combo_profile = new javax.swing.JComboBox<>();
        btn_save = new javax.swing.JButton();
        btn_return = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_title.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        lbl_title.setText("MarketPlace Profile Manager");
        add(lbl_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, -1));

        tb_allMarketPlaceProfiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Delete"
            }
        ));
        jScrollPane1.setViewportView(tb_allMarketPlaceProfiles);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 980, 110));

        lbl_refresh_stat.setText("Refresh every");
        add(lbl_refresh_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 200, -1, -1));
        add(txt_refresh_stats, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 70, -1));

        lbl_stat.setText("Stat");
        add(lbl_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, -1, -1));

        add(combo_refresh_stat, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 110, -1));

        lbl_sales1.setText("Sales");
        add(lbl_sales1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));
        add(txt_refresh_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 70, -1));

        lbl_refresh_sales.setText("Refresh every");
        add(lbl_refresh_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        add(combo_refresh_sales, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 110, -1));

        lbl_listingandbidding.setText("Listing and binding");
        add(lbl_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 160, -1, -1));

        lbl_refresh_listingandbidding.setText("Refresh every");
        add(lbl_refresh_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 200, -1, -1));
        add(txt_refresh_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 190, 70, -1));

        add(combo_refresh_listingandbidding, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 190, 110, -1));

        btn_create.setText("Create");
        btn_create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createActionPerformed(evt);
            }
        });
        add(btn_create, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 60, -1, -1));
        add(txt_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 290, -1));

        combo_profile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_profileItemStateChanged(evt);
            }
        });
        add(combo_profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 110, -1));

        btn_save.setText("Check & Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, -1, -1));

        btn_return.setText("Return");
        btn_return.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_returnActionPerformed(evt);
            }
        });
        add(btn_return, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, -1, -1));

        btn_cancel.setText("Cancel");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });
        add(btn_cancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 60, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_returnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_returnActionPerformed
        this.mainBotForm.swapView("multipleBotMenu");
    }//GEN-LAST:event_btn_returnActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        save();
        setState();
    }//GEN-LAST:event_btn_saveActionPerformed

    private void combo_profileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_profileItemStateChanged
        if (combo_profile.hasFocus() && combo_profile.getSelectedIndex() >= 0) {
            showedMpProfile = (MarketPlaceProfile) combo_profile.getSelectedItem();
            tb_allMarketPlaceProfiles.setRowSelectionInterval(combo_profile.getSelectedIndex(), combo_profile.getSelectedIndex());
            loadValue();
            setState();
        }
    }//GEN-LAST:event_combo_profileItemStateChanged

    private void btn_createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createActionPerformed
        showedMpProfile = new MarketPlaceProfile();
        loadValue();
        setState();
    }//GEN-LAST:event_btn_createActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if (combo_profile.getSelectedIndex() > -1) {
            showedMpProfile = (MarketPlaceProfile) combo_profile.getSelectedItem();
        }
        loadValue();
        setState();
    }//GEN-LAST:event_btn_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_create;
    private javax.swing.JButton btn_return;
    private javax.swing.JButton btn_save;
    private javax.swing.JComboBox<MarketPlaceProfile> combo_profile;
    private javax.swing.JComboBox<String> combo_refresh_listingandbidding;
    private javax.swing.JComboBox<String> combo_refresh_sales;
    private javax.swing.JComboBox<String> combo_refresh_stat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_listingandbidding;
    private javax.swing.JLabel lbl_refresh_listingandbidding;
    private javax.swing.JLabel lbl_refresh_sales;
    private javax.swing.JLabel lbl_refresh_stat;
    private javax.swing.JLabel lbl_sales1;
    private javax.swing.JLabel lbl_stat;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JTable tb_allMarketPlaceProfiles;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_refresh_listingandbidding;
    private javax.swing.JTextField txt_refresh_sales;
    private javax.swing.JTextField txt_refresh_stats;
    // End of variables declaration//GEN-END:variables

}
