/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Ad.AdCampaign;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Ad.AdCampaignManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.AdCampaignStatus;
import com.poorlycodedbyafrench.bottezosselltotwitter.Misc.ButtonColumn;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author david
 */
public class AdCompainMenu extends javax.swing.JPanel implements PanelRefreshInterface {

    private MainBotForm mainBotForm;
    private DefaultTableModel mdelServers;
    private DefaultTableModel modelAds;
    private AdCampaign showedAd;

    /**
     * Creates new form AdCompainMenu
     */
    public AdCompainMenu() {
        initComponents();

        mdelServers = (DefaultTableModel) tbl_server_count.getModel();
        modelAds = (DefaultTableModel) tbl_ad.getModel();

        combo_status.addItem(AdCampaignStatus.Draft);
        combo_status.addItem(AdCampaignStatus.Running);
        combo_status.addItem(AdCampaignStatus.Complete);

        //Configure the button action
        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                AdCampaign adToDelete = (AdCampaign) table.getValueAt(table.getSelectedRow(), 0);

                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this ad : " + adToDelete + " ?", "Warning", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {

                    AdCampaignManager.getAdCampaignManager().removeCompaign(adToDelete);
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);

                    if (AdCampaignManager.getAdCampaignManager().getAllAdCompaigns().size() == 0) {
                        showedAd = new AdCampaign();
                    } else {
                        showedAd = (AdCampaign) tbl_ad.getValueAt(0, 0);
                        tbl_ad.setRowSelectionInterval(0, 0);
                    }
                    updateValue();
                }
                setState();
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(tbl_ad, delete, 3);

        //Take from https://stackhowto.com/how-to-make-jtextfield-accept-only-numbers/
        this.tb_nb_server.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        this.tb_nb_ad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        tbl_ad.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row

                if (!event.getValueIsAdjusting() && tbl_ad.getSelectedRow() >= 0) {
                    showedAd = (AdCampaign) tbl_ad.getValueAt(tbl_ad.getSelectedRow(), 0);
                    updateValue();
                    setState();
                }
            }
        });

    }

    @Override
    public void refresh() {
        modelAds.setRowCount(0);

        for (AdCampaign ad : AdCampaignManager.getAdCampaignManager().getAllAdCompaigns().values()) {
            modelAds.addRow(new Object[]{ad, ad.getStatus(), ad.getTotalCountMessage(), "Delete"});
        }

        if (AdCampaignManager.getAdCampaignManager().getAllAdCompaigns().size() > 0) {
            showedAd = (AdCampaign) tbl_ad.getValueAt(0, 0);
            tbl_ad.setRowSelectionInterval(0, 0);
        } else {
            showedAd = new AdCampaign();
        }

        updateValue();
        setState();
    }

    @Override
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

        lbl_title = new javax.swing.JLabel();
        lbl_ad_title = new javax.swing.JLabel();
        lbl_ad_text = new javax.swing.JLabel();
        tb_ad_title = new javax.swing.JTextField();
        btn_save = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_ad_content = new javax.swing.JTextArea();
        lbl_all_ad = new javax.swing.JLabel();
        lbl_nb_message = new javax.swing.JLabel();
        tb_nb_ad = new javax.swing.JTextField();
        tb_nb_server = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_server_count = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_ad = new javax.swing.JTable();
        lbl_nb_server = new javax.swing.JLabel();
        lbl_ad_status = new javax.swing.JLabel();
        cb_permanent = new javax.swing.JCheckBox();
        combo_status = new javax.swing.JComboBox<>();
        btn_new = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_title.setFont(new java.awt.Font("sansserif", 1, 22)); // NOI18N
        lbl_title.setText("Ad management");
        add(lbl_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));

        lbl_ad_title.setText("Ad title :");
        add(lbl_ad_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        lbl_ad_text.setText("Ad text :");
        add(lbl_ad_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        tb_ad_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_ad_titleActionPerformed(evt);
            }
        });
        add(tb_ad_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 640, -1));

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, -1, -1));

        txt_ad_content.setColumns(20);
        txt_ad_content.setRows(5);
        jScrollPane1.setViewportView(txt_ad_content);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 640, -1));

        lbl_all_ad.setText("All ad");
        add(lbl_all_ad, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, -1, -1));

        lbl_nb_message.setText("Objective number of messages");
        add(lbl_nb_message, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 170, -1, -1));
        add(tb_nb_ad, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 200, 150, -1));
        add(tb_nb_server, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 150, -1));

        tbl_server_count.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbl_server_count);

        add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 310, 380, 160));

        tbl_ad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title", "Status", "Count", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbl_ad);

        add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 430, 160));

        lbl_nb_server.setText("Objective number of servers");
        add(lbl_nb_server, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        lbl_ad_status.setText("Status");
        add(lbl_ad_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 240, -1, -1));

        cb_permanent.setText("Permanent");
        cb_permanent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_permanentActionPerformed(evt);
            }
        });
        add(cb_permanent, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 80, -1, -1));

        add(combo_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 270, 130, -1));

        btn_new.setText("New");
        btn_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newActionPerformed(evt);
            }
        });
        add(btn_new, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void tb_ad_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_ad_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tb_ad_titleActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        this.mainBotForm.swapView("genericBotMenu");
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newActionPerformed
        showedAd = new AdCampaign();
        updateValue();
        setState();
    }//GEN-LAST:event_btn_newActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed

        if (dataAreCorrect()) {
            boolean doExist = false;
            if (tb_ad_title.isEnabled() && AdCampaignManager.getAdCampaignManager().getAllAdCompaigns().containsKey(tb_ad_title.getText())) {
                doExist = true;
            }

            if (!doExist) {

                showedAd.updateAdCampaign(tb_ad_title.getText(), txt_ad_content.getText(), cb_permanent.isSelected(), Integer.valueOf(tb_nb_server.getText()), Integer.valueOf(tb_nb_ad.getText()), (AdCampaignStatus) combo_status.getSelectedItem());

                if (!AdCampaignManager.getAdCampaignManager().getAllAdCompaigns().containsKey(showedAd.getTitle())) {
                    AdCampaignManager.getAdCampaignManager().addCompaign(showedAd);
                    modelAds.addRow(new Object[]{showedAd, showedAd.getStatus(), showedAd.getTotalCountMessage(), "Delete"});
                    tbl_ad.setRowSelectionInterval(modelAds.getRowCount() - 1, modelAds.getRowCount() - 1);
                } else {
                    tbl_ad.setValueAt(showedAd.getStatus(), tbl_ad.getSelectedRow(), 1);

                }
            } else {
                JOptionPane.showMessageDialog(null, "There's already an ad campaign with this name, choose one another", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            setState();
        } else {
            JOptionPane.showMessageDialog(null, "You need a title and a content to create an ad campaign", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private boolean dataAreCorrect() {
        if (this.tb_ad_title.getText().isBlank() || this.txt_ad_content.getText().isBlank()) {
            return false;
        }
        return true;
    }

    private void cb_permanentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_permanentActionPerformed
        setState();
    }//GEN-LAST:event_cb_permanentActionPerformed

    private void setState() {

        boolean isNew = showedAd.getTitle().isBlank();
        boolean isPermanentSelected = cb_permanent.isSelected();

        tb_ad_title.setEnabled(isNew);
        btn_new.setEnabled(!isNew);

        tb_nb_server.setEnabled(!isPermanentSelected);
        tb_nb_ad.setEnabled(!isPermanentSelected);
    }

    private void updateValue() {
        tb_ad_title.setText(showedAd.getTitle());
        txt_ad_content.setText(showedAd.getContent());
        cb_permanent.setSelected(showedAd.isPermanent());
        tb_nb_server.setText(String.valueOf(showedAd.getServerObj()));
        tb_nb_ad.setText(String.valueOf(showedAd.getMessageObj()));
        combo_status.setSelectedItem(showedAd.getStatus());

        mdelServers.setRowCount(0);

        for (String id : showedAd.getCampaignProgression().keySet()) {
            mdelServers.addRow(new Object[]{id, showedAd.getCampaignProgression().get(id)});
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_new;
    private javax.swing.JButton btn_save;
    private javax.swing.JCheckBox cb_permanent;
    private javax.swing.JComboBox<AdCampaignStatus> combo_status;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbl_ad_status;
    private javax.swing.JLabel lbl_ad_text;
    private javax.swing.JLabel lbl_ad_title;
    private javax.swing.JLabel lbl_all_ad;
    private javax.swing.JLabel lbl_nb_message;
    private javax.swing.JLabel lbl_nb_server;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JTextField tb_ad_title;
    private javax.swing.JTextField tb_nb_ad;
    private javax.swing.JTextField tb_nb_server;
    private javax.swing.JTable tbl_ad;
    private javax.swing.JTable tbl_server_count;
    private javax.swing.JTextArea txt_ad_content;
    // End of variables declaration//GEN-END:variables
}
