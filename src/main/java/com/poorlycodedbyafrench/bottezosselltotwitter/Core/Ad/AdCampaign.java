/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Ad;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.AdCampaignStatus;
import java.util.HashMap;

/**
 *
 * @author david
 */
public class AdCampaign {

    private String title;

    private String content;

    private AdCampaignStatus status;

    private boolean permanent;

    private int serverObj;

    private int messageObj;

    private HashMap<String, Integer> campaignProgression;

    public AdCampaign() {
        this.campaignProgression = new HashMap<>();
        title = "";
        content = "";
        status = AdCampaignStatus.Draft;
        permanent = false;
        serverObj = 0;
        messageObj = 0;
    }

    public void updateAdCampaign(String title, String content, boolean permanent, int serverObj, int messageObj, AdCampaignStatus status) {
        this.title = title;
        this.content = content;
        this.permanent = permanent;
        this.serverObj = serverObj;
        this.messageObj = messageObj;
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(AdCampaignStatus status) {
        this.status = status;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void setServerObj(int serverObj) {
        this.serverObj = serverObj;
    }

    public void setMessageObj(int messageObj) {
        this.messageObj = messageObj;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String, Integer> getCampaignProgression() {
        return campaignProgression;
    }

    public String getContent() {
        return content;
    }

    public AdCampaignStatus getStatus() {
        return status;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public int getServerObj() {
        return serverObj;
    }

    public int getMessageObj() {
        return messageObj;
    }

    public void messageSent(String id) {

        if (campaignProgression.containsKey(id)) {
            campaignProgression.replace(id, campaignProgression.get(id) + 1);
        } else {
            campaignProgression.put(id, 1);
        }
        
        checkCampaignComplete();
    }

    private void checkCampaignComplete() {

        if (!permanent) {

            int totalServerRequired = 0;

            for (String id : campaignProgression.keySet()) {
                if (campaignProgression.get(id) >= messageObj) {
                    totalServerRequired++;
                }
            }

            if (totalServerRequired >= serverObj) {
                this.status = AdCampaignStatus.Complete;
            }
        }
    }

    @Override
    public String toString() {
        return title;
    }

    public int getTotalCountMessage() {
        int sumMessage = 0;
        for (Integer servCount : campaignProgression.values()) {
            sumMessage += servCount;
        }
        return sumMessage;
    }
}
