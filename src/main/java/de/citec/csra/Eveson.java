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
import static com.jsyn.engine.SynthesisEngine.DEFAULT_FRAME_RATE;
import com.jsyn.unitgen.LineOut;
import static de.citec.csra.ScopePlayer.Type.ADJUST;
import static de.citec.csra.ScopePlayer.Type.CUSTOM;
import static de.citec.csra.ScopePlayer.Type.PLAY;
import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
import de.citec.csra.remotes.Remotes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.exception.JPServiceException;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.iface.Launchable;

/**
 *
 * @author divine
 */
public class Eveson implements Launchable {

    private static Synthesizer synthesizer;
    private static LineOut lineOut;

    @Override
    public void launch() throws CouldNotPerformException, InterruptedException {
        try {
            // Init audio devices and synthesizer
            AudioDeviceManager audioManager = AudioDeviceFactory.createAudioDeviceManager();
            synthesizer = JSyn.createSynthesizer();
            lineOut = new LineOut();
            synthesizer.add(lineOut);
            int audioDevice = loadAudioDevice(audioManager);
            int outputChannles = audioManager.getMaxInputChannels(audioDevice);

            if (outputChannles <= 0) {
                System.out.println("WARN: Audio channel detection failed. Try to force at least to output channels.");
                outputChannles = 2;
            }
            synthesizer.start();
            synthesizer.start(DEFAULT_FRAME_RATE, -1, 0, audioDevice, outputChannles);
            lineOut.start();

            // Load setting scope - audio mapping
            final String prefix = JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath();
            final Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();
//            List<PlayerConfig> configList = new ArrayList<>();
            
            List<PlayerConfig> configList = new ArrayList<>();
//            HashMap<String,PlayerConfig> configMap = new HashMap<>();
//            configMap.put("/home/kitchen/floor/", new PlayerConfig("/home/kitchen/floor/", "purr.wav", ADJUST));
//            configMap.put("/home/living/ambientlight/", new PlayerConfig("/home/living/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
//            configMap.put("/home/kitchen/ambientlight/", new PlayerConfig("/home/kitchen/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
//            configMap.put("/home/living/temperaturesensor", new PlayerConfig("/home/living/temperaturesensor", "purr.wav", ADJUST));
//            configMap.put("/home/kitchen/powerconsumptionsensor", new PlayerConfig("/home/kitchen/powerconsumptionsensor", "rain.wav", ADJUST));
//            configMap.put("/home/living/powerconsumptionsensor", new PlayerConfig("/home/living/powerconsumptionsensor", "rain.wav", ADJUST));
//            configMap.put("/home/kitchen/soundlocation", new PlayerConfig("/home/kitchen/soundlocation", "woodpecker.wav", ADJUST));
//            configMap.put("/home/living/temperaturesensor", new PlayerConfig("/home/living/temperaturesensor", "/wind.wav", ADJUST));
//            configMap.put("/home/kitchen/powerconsumptionsensor", new PlayerConfig("/home/kitchen/powerconsumptionsensor", "/rain.wav", ADJUST));
//            configMap.put("/home/living/powerconsumptionsensor", new PlayerConfig("/home/living/powerconsumptionsensor", "/rain.wav", ADJUST));
//            configMap.put("/home/kitchen/soundlocation", new PlayerConfig("/home/kitchen/soundlocation", "/woodpecker.wav", PLAY));
//            configMap.put("motionsensor_1", new PlayerConfig("motionsensor_1", "/birds/1.wav", CUSTOM));
//            configMap.put("motionsensor_2", new PlayerConfig("motionsensor_2", "/birds/2.wav", CUSTOM));
//            configMap.put("motionsensor_3", new PlayerConfig("motionsensor_3", "/birds/3.wav", CUSTOM));
//            configMap.put("motionsensor_4", new PlayerConfig("motionsensor_4", "/birds/4.wav", CUSTOM));
//            configMap.put("motionsensor_5", new PlayerConfig("motionsensor_5", "/birds/5.wav", CUSTOM));
//            configMap.put("motionsensor_6", new PlayerConfig("motionsensor_6", "/birds/6.wav", CUSTOM));
//            configMap.put("motionsensor_7", new PlayerConfig("motionsensor_7", "/birds/7.wav", CUSTOM));
//            configMap.put("motionsensor_8", new PlayerConfig("motionsensor_8", "/birds/8.wav", CUSTOM));
//            configMap.put("motionsensor_9", new PlayerConfig("motionsensor_9", "/birds/9.wav", CUSTOM));
//            configMap.put("motionsensor_10", new PlayerConfig("motionsensor_10", "/birds/10.wav", CUSTOM));
//            configMap.put("motionsensor_11", new PlayerConfig("motionsensor_11", "/birds/11.wav", CUSTOM));
            
            // ###############################################################
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
            // ### mapping of birds to motionsensors
            configList.add(new PlayerConfig("motionsensor_1", "/birds/1.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_2", "/birds/2.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_3", "/birds/3.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_4", "/birds/4.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_5", "/birds/5.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_6", "/birds/6.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_7", "/birds/7.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_8", "/birds/8.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_9", "/birds/9.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_10", "/birds/10.wav", CUSTOM));
            configList.add(new PlayerConfig("motionsensor_11", "/birds/11.wav", CUSTOM));
            
            Remotes remotes = new Remotes();
            remotes.init();
            // ###############################################################

            // Init player
//            configMap.values().stream().forEach((config) -> {
//                try {
//                    scopeSampleMap.put(config.getId(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType()));
//                } catch (CouldNotPerformException ex) {
//                    ExceptionPrinter.printHistory(new CouldNotPerformException("error occured... skipping sample " + config.getSampleFile(), ex), System.err);
//                }
//            });
            configList.stream().forEach((config) -> {
                try {
                    scopeSampleMap.put(config.getId(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType()));
                } catch (CouldNotPerformException ex) {
                    ExceptionPrinter.printHistory(new CouldNotPerformException("error occured... skipping sample " + config.getSampleFile(), ex), System.err);
                }
            });

            new EventPlayer(scopeSampleMap).play();
        } catch (JPNotAvailableException ex) {
            throw new CouldNotPerformException("Could not launch eveson!", ex);
        }
    }

    private int loadAudioDevice(final AudioDeviceManager audioManager) throws CouldNotPerformException {
        try {
            System.out.println("load audio device: " + JPService.getProperty(JPAudioOutputDevice.class).getValue() + " ["+JPService.getProperty(JPAudioOutputDevice.class).getAudioOutputDeviceId()+"]");
            int selectedDeviceId = JPService.getProperty(JPAudioOutputDevice.class).getAudioOutputDeviceId();
            System.out.println(audioManager.getMaxInputChannels(selectedDeviceId) + " input and " + audioManager.getMaxOutputChannels(selectedDeviceId) + " output channels found.");
            return selectedDeviceId;
        } catch (JPServiceException ex) {
            throw new CouldNotPerformException("Could not load audio device!", ex);
        }
    }

    public static Synthesizer getSynthesizer() {
        return synthesizer;
    }

    public static LineOut getLineOut() {
        return lineOut;
    }
    
}
