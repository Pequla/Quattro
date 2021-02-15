package com.pequla.quattro;

import com.pequla.quattro.modules.CommandModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;

import javax.security.auth.login.LoginException;

public class Quattro {

    private final JDA jda;

    public Quattro(String token) throws LoginException {
        this.jda = JDABuilder.createDefault(token)
                .setActivity(Activity.competing("Quattro"))
                .addEventListeners(new CommandModule("q!"))
                .build();
    }

    public void generateStatus() throws InterruptedException {
        jda.awaitReady();
        SelfUser bot = jda.getSelfUser();
        System.out.println("Name: " + bot.getName());
        System.out.println("ID: " + bot.getId());
        System.out.println("Servers: " + jda.getGuilds().size());
    }

    public void shutdown() {
        if (jda != null) {
            System.out.println("Disconnecting from Discord API");
            jda.shutdownNow();
        }
    }

    public JDA getJda() {
        return jda;
    }
}
