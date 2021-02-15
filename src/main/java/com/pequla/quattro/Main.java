package com.pequla.quattro;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                Quattro quattro = new Quattro(args[0]);
                quattro.generateStatus();
                Runtime.getRuntime().addShutdownHook(new Thread(quattro::shutdown));
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please add a bot token as an argument");
        }
    }
}
