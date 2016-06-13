/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import static isy.eveson.eveson.ScopePlayer.Type.ADJUST;
import static isy.eveson.eveson.ScopePlayer.Type.PLAY;
import isy.eveson.eveson.jp.JPAudioResoureFolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dc.jps.core.JPService;
import rsb.RSBException;

/**
 *
 * @author jplettemeier
 */
public class EventPlayer {

//    private final Map<String, String> scopeSampleMap;
    private final Map<String, ScopePlayer> scopeSampleMap;
    private final List<GenericListener> listenerMap;
    private static Synthesizer synth;
    private static LineOut lineOut;

    public EventPlayer(Map<String, ScopePlayer> scopeSampleMap) {
        this.scopeSampleMap = scopeSampleMap;
        this.listenerMap = new ArrayList<>();
    }

    public void play() throws InterruptedException, RSBException {
        GenericListener listener;
        for (Map.Entry<String, ScopePlayer> config : scopeSampleMap.entrySet()) {
            listener = new GenericListener(config.getKey(), config.getValue());
            listenerMap.add(listener);
        }
    }

    public static void main(String args[]) throws Throwable {
        
        // JPS setup
        JPService.setApplicationName("eveson");
        JPService.registerProperty(JPAudioResoureFolder.class);
        JPService.parseAndExitOnError(args);
        
        System.out.println("audio folder: "+JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath());
        String prefix = JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath();
        
        synth = JSyn.createSynthesizer();
        lineOut = new LineOut();
        synth.add(lineOut);
        synth.start();
        lineOut.start();
        
        Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();

        scopeSampleMap.put("/home/kitchen/floor/", new ScopePlayer(prefix+"purr.wav",ADJUST));
        scopeSampleMap.put("/home/living/motionsensor/", new ScopePlayer(prefix+"bird/swiftoid.wav",PLAY));
        scopeSampleMap.put("/home/kitchen/motionsensor/", new ScopePlayer(prefix+"bird/inspectorj.wav",PLAY));
        scopeSampleMap.put("/home/sports/motionsensor/", new ScopePlayer(prefix+"bird/cajo.wav",PLAY));
        scopeSampleMap.put("/home/living/ambientlight/", new ScopePlayer(prefix+"sound_beim_anzuenden.wav",PLAY));
        scopeSampleMap.put("/home/kitchen/ambientlight/", new ScopePlayer(prefix+"sound_beim_anzuenden.wav",PLAY));
        scopeSampleMap.put("/home/living/temperaturesensor", new ScopePlayer(prefix+"wind.wav",ADJUST));
        scopeSampleMap.put("/home/kitchen/powerconsumptionsensor", new ScopePlayer(prefix+"rain.wav",ADJUST));
        scopeSampleMap.put("/home/living/powerconsumptionsensor", new ScopePlayer(prefix+"rain.wav",ADJUST));
        scopeSampleMap.put("/home/kitchen/soundlocation", new ScopePlayer(prefix+"woodpecker.wav",PLAY));

//        scopeSampleMap.put("/home/<location>/<microphon>/audio/", "Brightness.wav");
//        scopeSampleMap.put("/apartment/calendar/", "Brightness.wav");

        new EventPlayer(scopeSampleMap).play();

        
        
        while (true) {
            Thread.sleep(100);
        }
    }

    public static Synthesizer getSynth() {
        return synth;
    }

    public static LineOut getLineOut() {
        return lineOut;
    }

}