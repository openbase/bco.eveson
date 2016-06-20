/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import static de.citec.csra.ScopePlayer.Type.ADJUST;
import static de.citec.csra.ScopePlayer.Type.PLAY;
import de.citec.csra.jp.JPAudioResoureFolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openbase.jps.core.JPService;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
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

        try {
            System.out.println("audio folder: " + JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath());
            String prefix = JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath();

            synth = JSyn.createSynthesizer();
            lineOut = new LineOut();
            synth.add(lineOut);
            synth.start();
            lineOut.start();

            Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();

            scopeSampleMap.put("/home/kitchen/floor/", new ScopePlayer(prefix + "/purr.wav", ADJUST));
            scopeSampleMap.put("/home/living/ambientlight/", new ScopePlayer(prefix + "/sound_beim_anzuenden.wav", PLAY));
            scopeSampleMap.put("/home/kitchen/ambientlight/", new ScopePlayer(prefix + "/sound_beim_anzuenden.wav", PLAY));
            scopeSampleMap.put("/home/living/temperaturesensor", new ScopePlayer(prefix + "/wind.wav", ADJUST));
            scopeSampleMap.put("/home/kitchen/powerconsumptionsensor", new ScopePlayer(prefix + "/rain.wav", ADJUST));
            scopeSampleMap.put("/home/living/powerconsumptionsensor", new ScopePlayer(prefix + "/rain.wav", ADJUST));
            scopeSampleMap.put("/home/kitchen/soundlocation", new ScopePlayer(prefix + "/woodpecker.wav", PLAY));

            //mapping of birds to motionsensors
            scopeSampleMap.put("/home/living/motionsensor/couch/", new ScopePlayer(prefix + "/birds/1.wav", PLAY));
            scopeSampleMap.put("/home/living/motionsensor/table/", new ScopePlayer(prefix + "/birds/2.wav", PLAY));
            scopeSampleMap.put("/home/living/motionsensor/media/", new ScopePlayer(prefix + "/birds/3.wav", PLAY));
            scopeSampleMap.put("/home/wardrobe/motionsensor/entrance/", new ScopePlayer(prefix + "/birds/4.wav", PLAY));
            scopeSampleMap.put("/home/wardrobe/motionsensor/hallway/", new ScopePlayer(prefix + "/birds/5.wav", PLAY));
            scopeSampleMap.put("/home/wardrobe/motionsensor/entrance/", new ScopePlayer(prefix + "/birds/6.wav", PLAY));
            scopeSampleMap.put("/home/sports/motionsensor/interaction/", new ScopePlayer(prefix + "/birds/7.wav", PLAY));
            scopeSampleMap.put("/home/sports/motionsensor/pathway/", new ScopePlayer(prefix + "/birds/8.wav", PLAY));
            scopeSampleMap.put("/home/kitchen/motionsensor/global/", new ScopePlayer(prefix + "/birds/9.wav", PLAY));
            scopeSampleMap.put("/home/bath/motionsensor/global/", new ScopePlayer(prefix + "/birds/10.wav", PLAY));
            scopeSampleMap.put("/home/bath/motionsensor/entrance/", new ScopePlayer(prefix + "/birds/11.wav", PLAY));

            new EventPlayer(scopeSampleMap).play();

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }
            System.exit(0);
        } catch (final Exception ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Eveson runtime error occured!", ex),System.err);
            System.exit(255);
        }
    }

    public static Synthesizer getSynth() {
        return synth;
    }

    public static LineOut getLineOut() {
        return lineOut;
    }

}
