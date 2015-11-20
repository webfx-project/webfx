package naga.jre.client;

import naga.core.Naga;

/*
 * @author Bruno Salmon
 */

public class JreApplication {

    public static void main(String[] args) {
        String nagaVersion = new Naga().getVersion();
        System.out.println(nagaVersion);
    }
}
