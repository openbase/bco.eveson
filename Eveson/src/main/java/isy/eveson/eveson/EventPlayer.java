/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rsb.RSBException;

/**
 *
 * @author jplettemeier
 */
public class EventPlayer {

    private final Map<String, String> scopeSampleMap;
    private final List<GenericListener> listenerMap;

    public EventPlayer(Map<String, String> scopeSampleMap) {
        this.scopeSampleMap = scopeSampleMap;
        this.listenerMap = new ArrayList<>();
    }

    public void play() throws InterruptedException, RSBException {
        GenericListener listener;
        for (Map.Entry<String, String> config : scopeSampleMap.entrySet()) {
            listener = new GenericListener(config.getKey(), config.getValue());
            listenerMap.add(listener);
        }
    }
    
    public static void main(String args[]) throws Throwable {
        Map<String, String> scopeSampleMap = new HashMap<>();
        scopeSampleMap.put("/home/control/motionsensor", "MotionEvent.wav");
        scopeSampleMap.put("/home/control/tamperswitch", "Tamper.wav");
        scopeSampleMap.put("/home/control/brightnesssensor", "Brightness.wav");
        scopeSampleMap.put("/home/kitchen/floor", "Floor.wav");
//        scopeSampleMap.put("/home/<location>/<microphon>/audio/", "Brightness.wav");
//        scopeSampleMap.put("/apartment/calendar/", "Brightness.wav");
        
        
        new EventPlayer(scopeSampleMap).play();
        
        while(true) {
            Thread.sleep(100);
        }
    }
}