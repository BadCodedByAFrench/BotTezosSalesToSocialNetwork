/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.MainForm;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration.PanelRefreshInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.DiscordSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfile;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.SocialNetworkProfileManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TelegramSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass.TwitterSocialNetwork;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkInterface.SocialNetworkInterface;
import com.poorlycodedbyafrench.bottezosselltotwitter.Misc.ButtonColumn;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author david
 */
public class SocialNetworkProfileManagerAndCreator extends javax.swing.JPanel implements PanelRefreshInterface {

    private DefaultTableModel model;
    private MainBotForm mainBotForm;

    private SocialNetworkProfile showedSNProfile;

    private TwitterSocialNetwork showedTwitter;
    private DiscordSocialNetwork showedDiscord;
    private TelegramSocialNetwork showedTelegram;

    /**
     * Creates new form MarketPlaceProfileManagerAndCreator
     */
    public SocialNetworkProfileManagerAndCreator() {
        initComponents();

        //Configure the button action
        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                SocialNetworkProfile snToDelete = (SocialNetworkProfile) table.getValueAt(table.getSelectedRow(), 0);

                String allUseByBots = SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().get(snToDelete.getName()).isUsed();
                if (allUseByBots.equals("")) {

                    int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this profile ?", "Warning", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {

                        SocialNetworkProfileManager.getSocialNetworkProfileManager().removeSocialNetworkProfile(snToDelete);
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                        combo_profile.removeItem(snToDelete);

                        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().size() == 0) {
                            snToDelete = new SocialNetworkProfile();
                        } else {
                            snToDelete = (SocialNetworkProfile) combo_profile.getSelectedItem();
                        }
                        loadValue();
                    }
                    setState();
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible to delete, this profile is currently used by theses bots. Please remove the link between them and the profile first :\n" + allUseByBots, "Impossible", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(tb_allSocialNetworkProfiles, delete, 1);

        model = (DefaultTableModel) tb_allSocialNetworkProfiles.getModel();

        tb_allSocialNetworkProfiles.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row

                if (!event.getValueIsAdjusting() && tb_allSocialNetworkProfiles.getSelectedRow() >= 0) {
                    showedSNProfile = (SocialNetworkProfile) tb_allSocialNetworkProfiles.getValueAt(tb_allSocialNetworkProfiles.getSelectedRow(), 0);
                    combo_profile.setSelectedItem(showedSNProfile);

                    showedTwitter = showedSNProfile.getTwitter();
                    combo_twitter.setSelectedItem(showedTwitter);

                    showedDiscord = showedSNProfile.getDiscord();
                    combo_discord.setSelectedItem(showedDiscord);

                    showedTelegram = showedSNProfile.getTelegram();
                    combo_telegram.setSelectedItem(showedTelegram);
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
        combo_twitter.removeAllItems();
        combo_discord.removeAllItems();
        combo_telegram.removeAllItems();

        for (SocialNetworkProfile snProfile : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().values()) {
            model.addRow(new Object[]{snProfile, "Delete"});
            combo_profile.addItem(snProfile);
        }

        for (TwitterSocialNetwork twitter : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().values()) {
            combo_twitter.addItem(twitter);
        }

        for (DiscordSocialNetwork discord : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllDiscord().values()) {
            combo_discord.addItem(discord);
        }

        for (TelegramSocialNetwork telegram : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTelegram().values()) {
            combo_telegram.addItem(telegram);
        }

        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().size() > 0) {
            showedSNProfile = combo_profile.getItemAt(0);
            combo_profile.setSelectedIndex(0);
            tb_allSocialNetworkProfiles.setRowSelectionInterval(0, 0);
        } else {
            showedSNProfile = new SocialNetworkProfile();
        }

        loadValue();

        setState();
    }

    public void setMainBotForm(MainBotForm mainBotForm) {
        this.mainBotForm = mainBotForm;
    }

    private void deleteSocialNetwork(SocialNetworkInterface sn, JComboBox theCombo) {

        String usedByBots = sn.isUsedByBots();

        if (usedByBots.equals("")) {

            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this profile ?", "Warning", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                String usedBySnProfile = sn.isUsedBySn();

                boolean confirmDelete = true;

                if (!usedBySnProfile.equals("")) {
                    int answerSn = JOptionPane.showConfirmDialog(null, "This profile is used by these Social Network Profile, all the ones with only this social network will be deleted too. Do you confirm ? ", "Warning", JOptionPane.YES_NO_OPTION);

                    if (answerSn == JOptionPane.NO_OPTION) {
                        confirmDelete = false;
                    }
                }

                if (confirmDelete) {

                    List<SocialNetworkProfile> socialNetworkToRemove = new ArrayList<>();
                    for (SocialNetworkProfile aSocialNetworkProfile : SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().values()) {
                        for (SocialNetworkInterface socialNetwork : aSocialNetworkProfile.getAllSocialNetwork()) {
                            if (socialNetwork == sn) {

                                if (sn instanceof DiscordSocialNetwork) {
                                    aSocialNetworkProfile.setDiscord(null);
                                } else if (sn instanceof TwitterSocialNetwork) {
                                    aSocialNetworkProfile.setTwitter(null);
                                } else if (sn instanceof TelegramSocialNetwork) {
                                    aSocialNetworkProfile.setTelegram(null);
                                }

                                if (aSocialNetworkProfile.getAllSocialNetwork().size() == 0) {
                                    socialNetworkToRemove.add(aSocialNetworkProfile);
                                }
                            }
                        }
                    }

                    for (SocialNetworkProfile aSocialNetworkProfile : socialNetworkToRemove) {

                        SocialNetworkProfileManager.getSocialNetworkProfileManager().removeSocialNetworkProfile(aSocialNetworkProfile);
                        int indexToRemove = -1;

                        for (int indexRowTable = 0; indexRowTable < tb_allSocialNetworkProfiles.getRowCount(); indexRowTable++) {

                            if (tb_allSocialNetworkProfiles.getValueAt(indexRowTable, 0) == aSocialNetworkProfile) {
                                indexToRemove = indexRowTable;
                            }
                        }

                        if (indexToRemove != -1) {
                            ((DefaultTableModel) tb_allSocialNetworkProfiles.getModel()).removeRow(indexToRemove);
                        }

                        combo_profile.removeItem(aSocialNetworkProfile);

                        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().size() == 0) {
                            showedSNProfile = new SocialNetworkProfile();
                        } else {
                            showedSNProfile = (SocialNetworkProfile) combo_profile.getSelectedItem();
                        }
                    }

                    if (sn instanceof DiscordSocialNetwork) {
                        SocialNetworkProfileManager.getSocialNetworkProfileManager().removeDiscord((DiscordSocialNetwork) sn);
                    } else if (sn instanceof TwitterSocialNetwork) {
                        SocialNetworkProfileManager.getSocialNetworkProfileManager().removeTwitter((TwitterSocialNetwork) sn);
                    } else if (sn instanceof TelegramSocialNetwork) {
                        SocialNetworkProfileManager.getSocialNetworkProfileManager().removeTelegram((TelegramSocialNetwork) sn);
                    }
                    theCombo.removeItem(sn);
                    sn.turnOff();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Impossible to delete, this profile is currently used by theses bots. Please remove the link between them and the profile first :\n" + usedByBots, "Impossible", JOptionPane.WARNING_MESSAGE);
        }
        loadValue();
        setState();
    }

    private void setState() {

        if (showedSNProfile.getName().isBlank()) {
            txt_name.setEnabled(true);
            btn_create.setEnabled(false);
            btn_cancel.setEnabled(SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().size() > 0);
        } else {
            txt_name.setEnabled(false);
            btn_create.setEnabled(SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().size() < 5);
            btn_cancel.setEnabled(false);
        }

        if (cb_twitter.isSelected()) {
            if (showedTwitter != null) {

                if (showedTwitter.getProfileName().isBlank()) {
                    btn_createTwitter.setEnabled(false);
                    btn_cancelTwitter.setEnabled(true);
                    tb_nameTwitter.setEnabled(true);
                } else {
                    btn_createTwitter.setEnabled(true);
                    btn_cancelTwitter.setEnabled(false);
                    tb_nameTwitter.setEnabled(false);

                }
                pwd_twitter_public_consumer_key.setEnabled(true);
                pwd_twitter_private_consumer_key.setEnabled(true);
                pwd_twitter_public_key.setEnabled(true);
                pwd_twitter_private_key.setEnabled(true);
                btn_saveTwitter.setEnabled(true);
                combo_twitter.setEnabled(true);
            } else {
                btn_createTwitter.setEnabled(true);
                btn_cancelTwitter.setEnabled(false);
                tb_nameTwitter.setEnabled(false);
                btn_saveTwitter.setEnabled(false);
                combo_twitter.setEnabled(true);

                pwd_twitter_public_consumer_key.setEnabled(false);
                pwd_twitter_private_consumer_key.setEnabled(false);
                pwd_twitter_public_key.setEnabled(false);
                pwd_twitter_private_key.setEnabled(false);
            }
        } else {
            btn_createTwitter.setEnabled(false);
            btn_cancelTwitter.setEnabled(false);
            tb_nameTwitter.setEnabled(false);
            btn_saveTwitter.setEnabled(false);
            combo_twitter.setEnabled(false);

            pwd_twitter_public_consumer_key.setEnabled(false);
            pwd_twitter_private_consumer_key.setEnabled(false);
            pwd_twitter_public_key.setEnabled(false);
            pwd_twitter_private_key.setEnabled(false);
        }

        if (cb_discord.isSelected()) {

            if (showedDiscord != null) {

                if (showedDiscord.getProfileName().isBlank()) {
                    btn_createDiscord.setEnabled(false);
                    btn_cancelDiscord.setEnabled(true);
                    tb_nameDiscord.setEnabled(true);
                } else {
                    btn_createDiscord.setEnabled(true);
                    btn_cancelDiscord.setEnabled(false);
                    tb_nameDiscord.setEnabled(false);
                }

                btn_saveDiscord.setEnabled(true);
                combo_discord.setEnabled(true);
                pwd_discord_token.setEnabled(true);
                txt_discord_channel.setEnabled(true);
            } else {
                btn_createDiscord.setEnabled(true);
                btn_cancelDiscord.setEnabled(false);
                tb_nameDiscord.setEnabled(false);
                btn_saveDiscord.setEnabled(false);
                combo_discord.setEnabled(true);
            }
        } else {
            btn_createDiscord.setEnabled(false);
            btn_cancelDiscord.setEnabled(false);
            tb_nameDiscord.setEnabled(false);
            btn_saveDiscord.setEnabled(false);
            combo_discord.setEnabled(false);
            pwd_discord_token.setEnabled(false);
            txt_discord_channel.setEnabled(false);
        }

        if (cb_telegram.isSelected()) {
            if (showedTelegram != null) {

                if (showedTelegram.getProfileName().isBlank()) {
                    btn_createTelegram.setEnabled(false);
                    btn_cancelTelegram.setEnabled(true);
                    tb_nameTelegram.setEnabled(true);
                } else {
                    btn_createTelegram.setEnabled(true);
                    btn_cancelTelegram.setEnabled(false);
                    tb_nameTelegram.setEnabled(false);
                }

                btn_saveTelegram.setEnabled(true);
                combo_telegram.setEnabled(true);
                pwd_telegram_token.setEnabled(true);
                txt_telegram_channel.setEnabled(true);
            } else {
                btn_createTelegram.setEnabled(true);
                btn_cancelTelegram.setEnabled(false);
                tb_nameTelegram.setEnabled(false);
                btn_saveTelegram.setEnabled(false);
                combo_telegram.setEnabled(true);

                pwd_telegram_token.setEnabled(false);
                txt_telegram_channel.setEnabled(false);
            }
        } else {
            btn_createTelegram.setEnabled(false);
            btn_cancelTelegram.setEnabled(false);
            tb_nameTelegram.setEnabled(false);
            btn_saveTelegram.setEnabled(false);
            combo_telegram.setEnabled(false);

            pwd_telegram_token.setEnabled(false);
            txt_telegram_channel.setEnabled(false);
        }
    }

    private void loadValue() {

        this.txt_name.setText(showedSNProfile.getName());
        setSnValue();
    }

    private void loadValueTwitter() {
        cb_twitter.setSelected(false);

        tb_nameTwitter.setText("");
        pwd_twitter_public_consumer_key.setText("");
        pwd_twitter_private_consumer_key.setText("");
        pwd_twitter_public_key.setText("");
        pwd_twitter_private_key.setText("");

        if (showedTwitter != null) {
            cb_twitter.setSelected(true);
            tb_nameTwitter.setText(showedTwitter.getProfileName());
            pwd_twitter_public_consumer_key.setText(showedTwitter.getPublicConsumerKey());
            pwd_twitter_private_consumer_key.setText(showedTwitter.getPrivateConsumerKey());
            pwd_twitter_public_key.setText(showedTwitter.getPublicAccessKey());
            pwd_twitter_private_key.setText(showedTwitter.getPrivateAccessKey());
        }
    }

    private void loadValueDiscord() {
        cb_discord.setSelected(false);

        tb_nameDiscord.setText("");
        pwd_discord_token.setText("");
        txt_discord_channel.setText("");

        if (showedDiscord != null) {
            cb_discord.setSelected(true);
            tb_nameDiscord.setText(showedDiscord.getProfileName());
            pwd_discord_token.setText(showedDiscord.getToken());
            txt_discord_channel.setText(showedDiscord.getChannelName());
        }
    }

    private void loadValueTelegram() {
        cb_telegram.setSelected(false);

        tb_nameTelegram.setText("");
        pwd_telegram_token.setText("");
        txt_telegram_channel.setText("");

        if (showedTelegram != null) {
            cb_telegram.setSelected(true);
            tb_nameTelegram.setText(showedTelegram.getProfileName());
            pwd_telegram_token.setText(showedTelegram.getApiKey());
            txt_telegram_channel.setText(showedTelegram.getChannelId());
        }

    }

    public void setSnValue() {

        if (showedSNProfile != null) {

            showedTwitter = showedSNProfile.getTwitter();
            showedDiscord = showedSNProfile.getDiscord();
            showedTelegram = showedSNProfile.getTelegram();

            loadValueTwitter();
            loadValueDiscord();
            loadValueTelegram();
        }
    }

    private void save() {

        if (checkValue()) {

            try {
                showedSNProfile.setName(this.txt_name.getText());

                if (cb_discord.isSelected()) {
                    showedSNProfile.setDiscord(showedDiscord);
                }

                if (cb_twitter.isSelected()) {
                    showedSNProfile.setTwitter(showedTwitter);
                }

                if (cb_telegram.isSelected()) {
                    showedSNProfile.setTelegram(showedTelegram);
                }

                if (!SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().containsKey(showedSNProfile.getName())) {
                    model.addRow(new Object[]{showedSNProfile, "Delete"});

                    combo_profile.addItem(showedSNProfile);
                    combo_profile.setSelectedItem(showedSNProfile);
                    tb_allSocialNetworkProfiles.setRowSelectionInterval(combo_profile.getSelectedIndex(), combo_profile.getSelectedIndex());
                }

                SocialNetworkProfileManager.getSocialNetworkProfileManager().addSocialNetworkProfile(showedSNProfile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible to check, please verify the value", "Impossible to check", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled or the name is already used or there is already too much profiles for a social network", "Empty field or exist", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveTwitter() {
        if (checkValueTwitter()) {
            try {
                if (showedTwitter == null) {
                    showedTwitter = new TwitterSocialNetwork();
                }

                showedTwitter.initiateValue(new String(pwd_twitter_public_consumer_key.getPassword()), new String(pwd_twitter_private_consumer_key.getPassword()), new String(pwd_twitter_public_key.getPassword()), new String(pwd_twitter_private_key.getPassword()), tb_nameTwitter.getText());
                showedTwitter.instanceTwitter();
                showedTwitter.check();

                if (!SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().containsKey(showedTwitter.getProfileName())) {
                    combo_twitter.addItem(showedTwitter);
                }

                SocialNetworkProfileManager.getSocialNetworkProfileManager().addTwitter(showedTwitter);
                combo_twitter.setSelectedItem(showedTwitter);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible to check, please verify the value", "Impossible to check", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled or the name is already used or there is already too much profiles for a social network", "Empty field or exist", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveDiscord() {
        if (checkValueDiscord()) {
            try {
                if (showedDiscord == null) {
                    showedDiscord = new DiscordSocialNetwork();
                }

                showedDiscord.initiateValue(new String(pwd_discord_token.getPassword()), txt_discord_channel.getText(), tb_nameDiscord.getText());
                showedDiscord.instanceDiscord();
                showedDiscord.check();

                if (!SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllDiscord().containsKey(showedDiscord.getProfileName())) {
                    combo_discord.addItem(showedDiscord);
                }
                SocialNetworkProfileManager.getSocialNetworkProfileManager().addDiscord(showedDiscord);

                combo_discord.setSelectedItem(showedDiscord);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible to check, please verify the value", "Impossible to check", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled or the name is already used or there is already too much profiles for a social network", "Empty field or exist", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveTelegram() {
        if (checkValueTelegram()) {
            try {
                if (showedTelegram == null) {
                    showedTelegram = new TelegramSocialNetwork();
                }

                showedTelegram.initiateValue(new String(pwd_telegram_token.getPassword()), txt_telegram_channel.getText(), tb_nameTelegram.getText());
                showedTelegram.instanceTelegram();
                showedTelegram.check();

                if (!SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTelegram().containsKey(showedTelegram.getProfileName())) {
                    combo_telegram.addItem(showedTelegram);
                }
                combo_telegram.setSelectedItem(showedTelegram);
                SocialNetworkProfileManager.getSocialNetworkProfileManager().addTelegram(showedTelegram);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible to check, please verify the value", "Impossible to check", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All the field are not filled or the name is already used or there is already too much profiles for a social network", "Empty field or exist", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean checkValue() {
        boolean checkValue = true;

        if (this.txt_name.getText().isBlank()) {
            checkValue = false;
        } else {
            if (showedSNProfile.getName().isBlank() && SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllSocialNetworkProfile().containsKey(this.txt_name.getText())) {
                checkValue = false;
            }
        }

        if (!cb_discord.isSelected() && !cb_twitter.isSelected() && !cb_telegram.isSelected()) {
            checkValue = false;
        }

        return checkValue;
    }

    private boolean checkValueTwitter() {

        boolean checkValue = true;

        if (this.tb_nameTwitter.getText().isBlank()) {
            checkValue = false;
        } else {
            if (showedTwitter.getProfileName().isBlank() && SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().containsKey(this.tb_nameTwitter.getText())) {
                checkValue = false;
            }
        }

        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().size() < SocialNetworkProfileManager.getSocialNetworkProfileManager().getMaxNumberOfProfil().get(SocialNetworkEnum.Twitter)) {
            if (new String(pwd_twitter_public_consumer_key.getPassword()).isBlank()) {
                checkValue = false;
            }

            if (new String(pwd_twitter_private_consumer_key.getPassword()).isBlank()) {
                checkValue = false;
            }

            if (new String(pwd_twitter_public_key.getPassword()).isBlank()) {
                checkValue = false;
            }

            if (new String(pwd_twitter_private_key.getPassword()).isBlank()) {
                checkValue = false;
            }
        } else {
            checkValue = false;
        }

        return checkValue;
    }

    private boolean checkValueDiscord() {

        boolean checkValue = true;

        if (this.tb_nameDiscord.getText().isBlank()) {
            checkValue = false;
        } else {
            if (showedDiscord.getProfileName().isBlank() && SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().containsKey(this.tb_nameDiscord.getText())) {
                checkValue = false;
            }
        }

        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllDiscord().size() < SocialNetworkProfileManager.getSocialNetworkProfileManager().getMaxNumberOfProfil().get(SocialNetworkEnum.Discord)) {
            if (new String(pwd_discord_token.getPassword()).isBlank()) {
                checkValue = false;
            }

            if (txt_discord_channel.getText().isBlank()) {
                checkValue = false;
            }
        } else {
            checkValue = false;
        }

        return checkValue;
    }

    private boolean checkValueTelegram() {

        boolean checkValue = true;

        if (this.tb_nameTelegram.getText().isBlank()) {
            checkValue = false;
        } else {
            if (showedTelegram.getProfileName().isBlank() && SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTwitter().containsKey(this.tb_nameTelegram.getText())) {
                checkValue = false;
            }
        }

        if (SocialNetworkProfileManager.getSocialNetworkProfileManager().getAllTelegram().size() < SocialNetworkProfileManager.getSocialNetworkProfileManager().getMaxNumberOfProfil().get(SocialNetworkEnum.Telegram)) {
            if (new String(pwd_telegram_token.getPassword()).isBlank()) {
                checkValue = false;
            }

            if (txt_telegram_channel.getText().isBlank()) {
                checkValue = false;
            }
        } else {
            checkValue = false;
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
        tb_allSocialNetworkProfiles = new javax.swing.JTable();
        btn_create = new javax.swing.JButton();
        txt_name = new javax.swing.JTextField();
        combo_profile = new javax.swing.JComboBox<>();
        btn_save = new javax.swing.JButton();
        btn_return = new javax.swing.JButton();
        lbl_twitter_public_consumer_key = new javax.swing.JLabel();
        pwd_twitter_private_consumer_key = new javax.swing.JPasswordField();
        lbl_twitter_private_consumer_key = new javax.swing.JLabel();
        lbl_twitter_access_key = new javax.swing.JLabel();
        lbl_twitter_private_key = new javax.swing.JLabel();
        pwd_twitter_private_key = new javax.swing.JPasswordField();
        pwd_twitter_public_key = new javax.swing.JPasswordField();
        pwd_twitter_public_consumer_key = new javax.swing.JPasswordField();
        cb_twitter = new javax.swing.JCheckBox();
        lbl_discord_token = new javax.swing.JLabel();
        lbl_discord_channel = new javax.swing.JLabel();
        cb_telegram = new javax.swing.JCheckBox();
        cb_discord = new javax.swing.JCheckBox();
        pwd_discord_token = new javax.swing.JPasswordField();
        pwd_telegram_token = new javax.swing.JPasswordField();
        txt_telegram_channel = new javax.swing.JTextField();
        lbl_telegram_channel = new javax.swing.JLabel();
        lbl_telegram_token = new javax.swing.JLabel();
        txt_discord_channel = new javax.swing.JTextField();
        btn_createTwitter = new javax.swing.JButton();
        btn_saveTwitter = new javax.swing.JButton();
        combo_twitter = new javax.swing.JComboBox<>();
        tb_nameTwitter = new javax.swing.JTextField();
        btn_createDiscord = new javax.swing.JButton();
        tb_nameDiscord = new javax.swing.JTextField();
        btn_saveDiscord = new javax.swing.JButton();
        combo_discord = new javax.swing.JComboBox<>();
        btn_createTelegram = new javax.swing.JButton();
        tb_nameTelegram = new javax.swing.JTextField();
        btn_saveTelegram = new javax.swing.JButton();
        combo_telegram = new javax.swing.JComboBox<>();
        btn_deleteTelegram = new javax.swing.JButton();
        btn_deleteTwitter = new javax.swing.JButton();
        btn_deleteDiscord = new javax.swing.JButton();
        btn_cancelTwitter = new javax.swing.JButton();
        btn_cancelDiscord = new javax.swing.JButton();
        btn_cancelTelegram = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_title.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        lbl_title.setText("SocialNetwork Profile Manager");
        add(lbl_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, -1));

        tb_allSocialNetworkProfiles.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tb_allSocialNetworkProfiles);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 980, 130));

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

        lbl_twitter_public_consumer_key.setText("Public consumer key");
        add(lbl_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 120, 20));
        add(pwd_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 170, -1));

        lbl_twitter_private_consumer_key.setText("Private consumer key");
        add(lbl_twitter_private_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 120, 20));

        lbl_twitter_access_key.setText("Public Access Key");
        add(lbl_twitter_access_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 120, 20));

        lbl_twitter_private_key.setText("Private Access Key");
        add(lbl_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 110, 20));
        add(pwd_twitter_private_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, 170, -1));
        add(pwd_twitter_public_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 170, -1));
        add(pwd_twitter_public_consumer_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 170, -1));

        cb_twitter.setText("Twitter");
        cb_twitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_twitterActionPerformed(evt);
            }
        });
        add(cb_twitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 70, 30));

        lbl_discord_token.setText("Discord token");
        add(lbl_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 90, 20));

        lbl_discord_channel.setText("Discord channel");
        add(lbl_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 100, 20));

        cb_telegram.setText("Telegram");
        cb_telegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_telegramActionPerformed(evt);
            }
        });
        add(cb_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, 90, 30));

        cb_discord.setText("Discord");
        cb_discord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_discordActionPerformed(evt);
            }
        });
        add(cb_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, 80, 30));
        add(pwd_discord_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 200, 170, -1));
        add(pwd_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 200, 170, -1));
        add(txt_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 230, 170, -1));

        lbl_telegram_channel.setText("Telegram channel");
        add(lbl_telegram_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 230, 110, 20));

        lbl_telegram_token.setText("Telegram token");
        add(lbl_telegram_token, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, 110, 20));
        add(txt_discord_channel, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, 170, -1));

        btn_createTwitter.setText("Create");
        btn_createTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createTwitterActionPerformed(evt);
            }
        });
        add(btn_createTwitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        btn_saveTwitter.setText("Save");
        btn_saveTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveTwitterActionPerformed(evt);
            }
        });
        add(btn_saveTwitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, -1, -1));

        combo_twitter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_twitterItemStateChanged(evt);
            }
        });
        combo_twitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_twitterActionPerformed(evt);
            }
        });
        add(combo_twitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 110, -1));
        add(tb_nameTwitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 80, -1));

        btn_createDiscord.setText("Create");
        btn_createDiscord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createDiscordActionPerformed(evt);
            }
        });
        add(btn_createDiscord, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, -1, -1));
        add(tb_nameDiscord, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 80, -1));

        btn_saveDiscord.setText("Save");
        btn_saveDiscord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveDiscordActionPerformed(evt);
            }
        });
        add(btn_saveDiscord, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, -1, -1));

        combo_discord.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_discordItemStateChanged(evt);
            }
        });
        combo_discord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_discordActionPerformed(evt);
            }
        });
        add(combo_discord, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 110, -1));

        btn_createTelegram.setText("Create");
        btn_createTelegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createTelegramActionPerformed(evt);
            }
        });
        add(btn_createTelegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, -1, -1));
        add(tb_nameTelegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 160, 80, -1));

        btn_saveTelegram.setText("Save");
        btn_saveTelegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveTelegramActionPerformed(evt);
            }
        });
        add(btn_saveTelegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 160, -1, -1));

        combo_telegram.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_telegramItemStateChanged(evt);
            }
        });
        add(combo_telegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 160, 120, -1));

        btn_deleteTelegram.setText("Delete this Telegram profile");
        btn_deleteTelegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteTelegramActionPerformed(evt);
            }
        });
        add(btn_deleteTelegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 120, 180, -1));

        btn_deleteTwitter.setText("Delete this Twitter profile");
        btn_deleteTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteTwitterActionPerformed(evt);
            }
        });
        add(btn_deleteTwitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, -1, -1));

        btn_deleteDiscord.setText("Delete this Discord profile");
        btn_deleteDiscord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteDiscordActionPerformed(evt);
            }
        });
        add(btn_deleteDiscord, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, -1, -1));

        btn_cancelTwitter.setText("Cancel");
        btn_cancelTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelTwitterActionPerformed(evt);
            }
        });
        add(btn_cancelTwitter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        btn_cancelDiscord.setText("Cancel");
        btn_cancelDiscord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelDiscordActionPerformed(evt);
            }
        });
        add(btn_cancelDiscord, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, -1, -1));

        btn_cancelTelegram.setText("Cancel");
        btn_cancelTelegram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelTelegramActionPerformed(evt);
            }
        });
        add(btn_cancelTelegram, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 120, -1, -1));

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
            showedSNProfile = (SocialNetworkProfile) combo_profile.getSelectedItem();
            tb_allSocialNetworkProfiles.setRowSelectionInterval(combo_profile.getSelectedIndex(), combo_profile.getSelectedIndex());
            loadValue();
            setState();
        }
    }//GEN-LAST:event_combo_profileItemStateChanged

    private void btn_createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createActionPerformed
        showedSNProfile = new SocialNetworkProfile();
        loadValue();
        setState();
    }//GEN-LAST:event_btn_createActionPerformed

    private void cb_twitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_twitterActionPerformed
        setState();
    }//GEN-LAST:event_cb_twitterActionPerformed

    private void cb_telegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_telegramActionPerformed
        setState();
    }//GEN-LAST:event_cb_telegramActionPerformed

    private void cb_discordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_discordActionPerformed
        setState();
    }//GEN-LAST:event_cb_discordActionPerformed

    private void btn_createTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createTwitterActionPerformed
        showedTwitter = new TwitterSocialNetwork();
        loadValueTwitter();
        setState();
    }//GEN-LAST:event_btn_createTwitterActionPerformed

    private void btn_createDiscordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createDiscordActionPerformed
        showedDiscord = new DiscordSocialNetwork();
        loadValueDiscord();
        setState();
    }//GEN-LAST:event_btn_createDiscordActionPerformed

    private void btn_createTelegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createTelegramActionPerformed
        showedTelegram = new TelegramSocialNetwork();
        loadValueTelegram();
        setState();
    }//GEN-LAST:event_btn_createTelegramActionPerformed

    private void btn_saveTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveTwitterActionPerformed
        saveTwitter();
        setState();
    }//GEN-LAST:event_btn_saveTwitterActionPerformed

    private void btn_saveTelegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveTelegramActionPerformed
        saveTelegram();
        setState();
    }//GEN-LAST:event_btn_saveTelegramActionPerformed

    private void btn_saveDiscordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveDiscordActionPerformed
        saveDiscord();
        setState();
    }//GEN-LAST:event_btn_saveDiscordActionPerformed

    private void combo_twitterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_twitterItemStateChanged
        if (combo_twitter.hasFocus() && combo_twitter.getSelectedIndex() >= 0) {
            showedTwitter = (TwitterSocialNetwork) combo_twitter.getSelectedItem();
            loadValueTwitter();
            setState();
        }
    }//GEN-LAST:event_combo_twitterItemStateChanged

    private void combo_discordItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_discordItemStateChanged
        if (combo_discord.hasFocus() && combo_discord.getSelectedIndex() >= 0) {
            showedDiscord = (DiscordSocialNetwork) combo_discord.getSelectedItem();
            loadValueDiscord();
            setState();
        }
    }//GEN-LAST:event_combo_discordItemStateChanged

    private void combo_telegramItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_telegramItemStateChanged
        if (combo_telegram.hasFocus() && combo_telegram.getSelectedIndex() >= 0) {
            showedTelegram = (TelegramSocialNetwork) combo_telegram.getSelectedItem();
            loadValueTelegram();
            setState();
        }
    }//GEN-LAST:event_combo_telegramItemStateChanged

    private void combo_twitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_twitterActionPerformed

    }//GEN-LAST:event_combo_twitterActionPerformed

    private void combo_discordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_discordActionPerformed

    }//GEN-LAST:event_combo_discordActionPerformed

    private void btn_deleteTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteTwitterActionPerformed
        if (combo_twitter.getSelectedIndex() > -1) {
            deleteSocialNetwork((SocialNetworkInterface) combo_twitter.getSelectedItem(), combo_twitter);
        }

    }//GEN-LAST:event_btn_deleteTwitterActionPerformed

    private void btn_deleteDiscordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteDiscordActionPerformed
        if (combo_discord.getSelectedIndex() > -1) {
            deleteSocialNetwork((SocialNetworkInterface) combo_discord.getSelectedItem(), combo_discord);
        }
    }//GEN-LAST:event_btn_deleteDiscordActionPerformed

    private void btn_deleteTelegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteTelegramActionPerformed
        if (combo_telegram.getSelectedIndex() > -1) {
            deleteSocialNetwork((SocialNetworkInterface) combo_telegram.getSelectedItem(), combo_telegram);
        }
    }//GEN-LAST:event_btn_deleteTelegramActionPerformed

    private void btn_cancelTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelTwitterActionPerformed
        if (combo_twitter.getSelectedIndex() > -1) {
            showedTwitter = (TwitterSocialNetwork) combo_twitter.getSelectedItem();
        } else {
            showedTwitter = null;
        }
        loadValueTwitter();
        setState();
    }//GEN-LAST:event_btn_cancelTwitterActionPerformed

    private void btn_cancelDiscordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelDiscordActionPerformed
        if (combo_discord.getSelectedIndex() > -1) {
            showedDiscord = (DiscordSocialNetwork) combo_discord.getSelectedItem();
        } else {
            showedDiscord = null;
        }
        loadValueDiscord();
        setState();
    }//GEN-LAST:event_btn_cancelDiscordActionPerformed

    private void btn_cancelTelegramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelTelegramActionPerformed
        if (combo_telegram.getSelectedIndex() > -1) {
            showedTelegram = (TelegramSocialNetwork) combo_telegram.getSelectedItem();
        } else {
            showedTelegram = null;
        }
        loadValueTelegram();
        setState();
    }//GEN-LAST:event_btn_cancelTelegramActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if (combo_profile.getSelectedIndex() > -1) {
            showedSNProfile = (SocialNetworkProfile) combo_profile.getSelectedItem();
        }
        loadValue();
        setState();
    }//GEN-LAST:event_btn_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_cancelDiscord;
    private javax.swing.JButton btn_cancelTelegram;
    private javax.swing.JButton btn_cancelTwitter;
    private javax.swing.JButton btn_create;
    private javax.swing.JButton btn_createDiscord;
    private javax.swing.JButton btn_createTelegram;
    private javax.swing.JButton btn_createTwitter;
    private javax.swing.JButton btn_deleteDiscord;
    private javax.swing.JButton btn_deleteTelegram;
    private javax.swing.JButton btn_deleteTwitter;
    private javax.swing.JButton btn_return;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_saveDiscord;
    private javax.swing.JButton btn_saveTelegram;
    private javax.swing.JButton btn_saveTwitter;
    private javax.swing.JCheckBox cb_discord;
    private javax.swing.JCheckBox cb_telegram;
    private javax.swing.JCheckBox cb_twitter;
    private javax.swing.JComboBox<DiscordSocialNetwork> combo_discord;
    private javax.swing.JComboBox<SocialNetworkProfile> combo_profile;
    private javax.swing.JComboBox<TelegramSocialNetwork> combo_telegram;
    private javax.swing.JComboBox<TwitterSocialNetwork> combo_twitter;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_discord_channel;
    private javax.swing.JLabel lbl_discord_token;
    private javax.swing.JLabel lbl_telegram_channel;
    private javax.swing.JLabel lbl_telegram_token;
    private javax.swing.JLabel lbl_title;
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
    private javax.swing.JTable tb_allSocialNetworkProfiles;
    private javax.swing.JTextField tb_nameDiscord;
    private javax.swing.JTextField tb_nameTelegram;
    private javax.swing.JTextField tb_nameTwitter;
    private javax.swing.JTextField txt_discord_channel;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_telegram_channel;
    // End of variables declaration//GEN-END:variables

}
