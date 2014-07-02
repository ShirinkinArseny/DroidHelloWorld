package com.acidspacecompany.epicwallpaperfight.Network;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.UnitLayer;
import com.acidspacecompany.epicwallpaperfight.World;

public class NetworkDebugger {

    public NetworkDebugger() {

        final Server s=new Server(1234);
        s.setOnInputEvent(new Runnable() {
            @Override
            public void run() {
                String[] msg=s.getLastInputMessage().split(" ");

                switch (msg[0].toLowerCase()) {

                    case "killall" : {
                        UnitLayer.killEverybody();
                        break;
                    }

                    case "resize" : {
                        World.resize(Integer.valueOf(msg[1]), Integer.valueOf(msg[2]));
                        break;
                    }

                    case "config" : {
                        LocalConfigs.setField(msg[1], msg[2]);
                        break;
                    }

                    case "pause" : {
                        World.setPhysicIsWorking(false);
                        break;
                    }

                    case "play" : {
                        World.setPhysicIsWorking(true);
                        break;
                    }
                }
            }
        });

    }

}
