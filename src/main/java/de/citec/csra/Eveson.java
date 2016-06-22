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
import static de.citec.csra.ScopePlayer.Type.PLAY;
import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
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

            synthesizer.start(DEFAULT_FRAME_RATE, -1, 0, audioDevice, outputChannles);
            lineOut.start();

            // Load setting scope - audio mapping
            final String prefix = JPService.getProperty(JPAudioResoureFolder.class).getValue().getAbsolutePath();
            final Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();
            List<PlayerConfig> configList = new ArrayList<>();

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

            // Init player
            configList.stream().forEach((config) -> {
                try {
                    scopeSampleMap.put(config.getScope(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType()));
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
            System.out.println("load audio device: " + JPService.getProperty(JPAudioOutputDevice.class).getValue());
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
