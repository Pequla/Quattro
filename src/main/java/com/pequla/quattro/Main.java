package com.pequla.quattro;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                JDA jda = JDABuilder.createDefault(args[0])
                        .setActivity(Activity.competing("Quattro"))
                        .addEventListeners(new CommandModule())
                        .build();
                try {
                    jda.awaitReady();
                    System.out.println("Guilds: " + jda.getGuilds().size());
                    SelfUser user = jda.getSelfUser();
                    System.out.println("ID: "+user.getId());
                    System.out.println("Name: "+user.getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (LoginException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please add a bot token as an argument");
        }
    }
}
