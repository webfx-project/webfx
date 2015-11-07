package naga.jre.client;

import naga.core.Naga;

/*
 * @author Bruno Salmon
 */

public class JreApplication {

    public static void main(String[] args) {
        System.out.println(new Naga().getMessage("appName"));
    }
}
