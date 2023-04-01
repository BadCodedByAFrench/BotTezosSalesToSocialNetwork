/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.SocialNetworkClass;

import com.poorlycodedbyafrench.bottezosselltotwitter.Core.Bot.GenericBotManager;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.MarketPlaceEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SaleTypeEnum;
import com.poorlycodedbyafrench.bottezosselltotwitter.Core.MainEnum.SocialNetworkEnum;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 *
 * @author david
 */
public class DiscordGuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild newGuild = event.getGuild();
        DiscordGeneralCommands.updateCommands(newGuild, true);
        GenericBotManager.getGenericBotManager().createGenericBot(newGuild.getId(), SocialNetworkEnum.Discord);
    }

    //I wanted to clea every commands before adding the correct one (in case when a generic bot is used as a specific one or specific as generic) but it's to easy to reach the daily limite (update commands is ok, removing and adding after not)
    public static void clearCommands(Guild g) {
        for (Command aCommand : g.retrieveCommands().complete()) {
            g.deleteCommandById(aCommand.getId());
        }
    }
}
