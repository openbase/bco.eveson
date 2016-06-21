/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceFactory;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.devices.javasound.JavaSoundAudioDevice;
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
            String prefix = JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath();

            synth = JSyn.createSynthesizer();

            AudioDeviceManager audioManager = AudioDeviceFactory.createAudioDeviceManager();

            int numDevices = audioManager.getDeviceCount();
            for (int i = 0; i < numDevices; i++) {
                String deviceName = audioManager.getDeviceName(i);
                int maxInputs = audioManager.getMaxInputChannels(i);
                int maxOutputs = audioManager.getMaxInputChannels(i);
                boolean isDefaultInput = (i == audioManager.getDefaultInputDeviceID());
                boolean isDefaultOutput = (i == audioManager.getDefaultInputDeviceID());
                System.out.println("#" + i + " : " + deviceName);
                System.out.println("  max inputs : " + maxInputs + (isDefaultInput ? "   (default)" : ""));
                System.out.println("  max outputs: " + maxOutputs + (isDefaultOutput ? "   (default)" : ""));
            }

            System.out.println("JavaSoundAudioDevice:" + new JavaSoundAudioDevice().getDefaultOutputDeviceID());

            lineOut = new LineOut();

            synth.add(lineOut);
            synth.start();
            lineOut.start();

            Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();

            // #################### scope - audio mapping ####################
            List<PlayerConfig> configList = new ArrayList<>();
            configList.add(new PlayerConfig("/home/kitchen/floor/", "purr.wav", ADJUST));
            configList.add(new PlayerConfig("/home/living/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
            configList.add(new PlayerConfig("/home/kitchen/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
            configList.add(new PlayerConfig("/home/living/temperaturesensor", "purr.wav", ADJUST));
            configList.add(new PlayerConfig("/home/kitchen/powerconsumptionsensor", "rain.wav", ADJUST));
            configList.add(new PlayerConfig("/home/living/powerconsumptionsensor", "rain.wav", ADJUST));
            configList.add(new PlayerConfig("/home/kitchen/soundlocation", "woodpecker.wav", ADJUST));

            configList.add(new PlayerConfig("/home/living/temperaturesensor", "/wind.wav", ADJUST));
            configList.add(new PlayerConfig("/home/kitchen/powerconsumptionsensor", "/rain.wav", ADJUST));
            configList.add(new PlayerConfig("/home/living/powerconsumptionsensor", "/rain.wav", ADJUST));
            configList.add(new PlayerConfig("/home/kitchen/soundlocation", "/woodpecker.wav", PLAY));
            //mapping of birds to motionsensors
            configList.add(new PlayerConfig("/home/living/motionsensor/couch/", "/birds/1.wav", PLAY));
            configList.add(new PlayerConfig("/home/living/motionsensor/table/", "/birds/2.wav", PLAY));
            configList.add(new PlayerConfig("/home/living/motionsensor/media/", "/birds/3.wav", PLAY));
            configList.add(new PlayerConfig("/home/wardrobe/motionsensor/entrance/", "/birds/4.wav", PLAY));
            configList.add(new PlayerConfig("/home/wardrobe/motionsensor/hallway/", "/birds/5.wav", PLAY));
            configList.add(new PlayerConfig("/home/wardrobe/motionsensor/entrance/", "/birds/6.wav", PLAY));
            configList.add(new PlayerConfig("/home/sports/motionsensor/interaction/", "/birds/7.wav", PLAY));
            configList.add(new PlayerConfig("/home/sports/motionsensor/pathway/", "/birds/8.wav", PLAY));
            configList.add(new PlayerConfig("/home/kitchen/motionsensor/global/", "/birds/9.wav", PLAY));
            configList.add(new PlayerConfig("/home/bath/motionsensor/global/", "/birds/10.wav", PLAY));
            configList.add(new PlayerConfig("/home/bath/motionsensor/entrance/", "/birds/11.wav", PLAY));
            // ###############################################################

            configList.stream().forEach((config) -> {
                try {
                    scopeSampleMap.put(config.getScope(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType()));
                } catch (CouldNotPerformException ex) {
                    ExceptionPrinter.printHistory(new CouldNotPerformException("error occured... skipping sample " + config.getSampleFile(), ex), System.err);
                }
            });

            new EventPlayer(scopeSampleMap).play();

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
            }
            System.exit(0);
        } catch (final Exception ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Eveson runtime error occured!", ex), System.err);
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
