/*
 * @author Bruno Salmon
 */

import naga.core.Naga;

public class ObjCApplication {

    public static void main(String[] args) {
        String nagaVersion = new Naga().getVersion();
        System.out.println(nagaVersion);
    }
}