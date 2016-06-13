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
        synth = JSyn.createSynthesizer();
        lineOut = new LineOut();
        synth.add(lineOut);
        synth.start();
        lineOut.start();
        
        Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();
        scopeSampleMap.put("/home/kitchen/floor/", new ScopePlayer("/samples/purr.wav",ADJUST));
        scopeSampleMap.put("/home/living/motionsensor/", new ScopePlayer("/samples/bird/swiftoid.wav",PLAY));
        scopeSampleMap.put("/home/kitchen/motionsensor/", new ScopePlayer("/samples/bird/inspectorj.wav",PLAY));
        scopeSampleMap.put("/home/sports/motionsensor/", new ScopePlayer("/samples/bird/cajo.wav",PLAY));
        scopeSampleMap.put("/home/living/ambientlight/", new ScopePlayer("/samples/sound_beim_anzuenden.wav",PLAY));
        scopeSampleMap.put("/home/kitchen/ambientlight/", new ScopePlayer("/samples/sound_beim_anzuenden.wav",PLAY));
        scopeSampleMap.put("/home/living/temperaturesensor", new ScopePlayer("/samples/wind.wav",ADJUST));
        scopeSampleMap.put("/home/kitchen/powerconsumptionsensor", new ScopePlayer("/samples/rain.wav",ADJUST));
        scopeSampleMap.put("/home/living/powerconsumptionsensor", new ScopePlayer("/samples/rain.wav",ADJUST));
        scopeSampleMap.put("/home/kitchen/soundlocation", new ScopePlayer("/samples/woodpecker.wav",PLAY));
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
