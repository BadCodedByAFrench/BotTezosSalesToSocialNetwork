/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 *
 * @author david
 */
public class DiscordSocialNetworkFullCommands  {

    /**
     * Class that connect to Discord API
     */
    private transient JDA jda;

    private String token;

    public void initiateValue(String token) {
        this.token = token;
    }

    public JDA getJda() {
        return jda;
    }
    
    public String getToken(){
        return token;
    }
    
    public void instanceDiscord() throws LoginException, InterruptedException, Exception {

        jda = JDABuilder.createDefault(token.trim()).build().awaitReady();
        jda.addEventListener(new DiscordGuildJoinListener());
        jda.addEventListener(new DiscordGeneralCommands());
        jda.addEventListener(new DiscordCommandSlashListener());
    }
    
    public void stop(){
        this.jda.shutdown();
    }
}
