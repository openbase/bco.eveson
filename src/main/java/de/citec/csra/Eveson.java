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
import de.citec.csra.jp.JPAudioVolume;
import de.citec.csra.remotes.Remotes;
import java.io.File;
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
import org.openbase.jul.processing.JSonObjectFileProcessor;

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
            float amplitude = JPService.getProperty(JPAudioVolume.class).getValue();

            if (amplitude > 1 || amplitude < 0) {
                System.out.println("Invalid amplitude!");
                amplitude = 1.0f;
            }
            System.out.println("Setting maximum amplitude to " + amplitude);
            EventPlayer.setMaxAmplitude(amplitude);

            final Map<String, ScopePlayer> scopeSampleMap = new HashMap<>();

            final JSonObjectFileProcessor fileProcessor =  new JSonObjectFileProcessor(EvesonConfig.class);
            
//            
//            EvesonConfig evesonConfig = new EvesonConfig();
//            ArrayList<PlayerConfig> configList = evesonConfig.getPlayerConfigList();
            
            // ###############################################################
//            configList.add(new PlayerConfig("/home/kitchen/floor/", "purr.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/living/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
//            configList.add(new PlayerConfig("/home/kitchen/ambientlight/", "sound_beim_anzuenden.wav", PLAY));
//            configList.add(new PlayerConfig("/home/living/temperaturesensor", "purr.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/kitchen/powerconsumptionsensor", "rain.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/living/powerconsumptionsensor", "rain.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/kitchen/soundlocation", "woodpecker.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/living/temperaturesensor", "/wind.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/kitchen/powerconsumptionsensor", "/rain.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/living/powerconsumptionsensor", "/rain.wav", ADJUST));
//            configList.add(new PlayerConfig("/home/kitchen/soundlocation", "/woodpecker.wav", PLAY));
//            // ### mapping of birds to motionsensors
//            configList.add(new PlayerConfig("MOTION_SENSOR_10", "/birds/1/1.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_11", "/birds/2/1.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_12", "/birds/3/1.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_4", "/birds/1/2.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_9", "/birds/1/3.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_6", "/birds/1/4.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_14", "/birds/1/5.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_13", "/birds/3/5.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_8", "/birds/2/5.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_7", "/birds/2/7.wav", CUSTOM));
//            configList.add(new PlayerConfig("MOTION_SENSOR_5", "/birds/2/8.wav", CUSTOM));

            
            
            EvesonConfig evesonConfig = (EvesonConfig)(fileProcessor.deserialize(new File("eveson.conf")));
            ArrayList<PlayerConfig> configList = evesonConfig.getPlayerConfigList();
            
            // ###############################################################
            configList.stream().forEach((config) -> {
                try {
                    scopeSampleMap.put(config.getId(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType()));
                } catch (CouldNotPerformException ex) {
                    ExceptionPrinter.printHistory(new CouldNotPerformException("error occured... skipping sample " + config.getSampleFile(), ex), System.err);
                }
            });

            new EventPlayer(scopeSampleMap).play();
            Remotes remotes = new Remotes();
            remotes.init();
        } catch (JPNotAvailableException ex) {
            throw new CouldNotPerformException("Could not launch eveson!", ex);
        }
    }

    private int loadAudioDevice(final AudioDeviceManager audioManager) throws CouldNotPerformException {
        try {
            System.out.println("load audio device: " + JPService.getProperty(JPAudioOutputDevice.class).getValue() + " [" + JPService.getProperty(JPAudioOutputDevice.class).getAudioOutputDeviceId() + "]");
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
