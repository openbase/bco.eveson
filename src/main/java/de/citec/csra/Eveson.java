package de.citec.csra;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceFactory;
import com.jsyn.devices.AudioDeviceManager;
import static com.jsyn.engine.SynthesisEngine.DEFAULT_FRAME_RATE;
import com.jsyn.unitgen.LineOut;
import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
import de.citec.csra.jp.JPAudioVolume;
import de.citec.csra.jp.JPThemeFile;
import de.citec.csra.remotes.LocationObserver;
import de.citec.csra.remotes.Remotes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.exception.JPServiceException;
import org.openbase.jps.preset.JPShowGUI;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.iface.Launchable;
import org.openbase.jul.iface.VoidInitializable;
import org.openbase.jul.processing.JSonObjectFileProcessor;

/**
 *
 * @author divine
 */
public class Eveson implements Launchable<Void>, VoidInitializable {

    private static Synthesizer synthesizer;
    private static LineOut lineOut;
    private EvesonConfig evesonConfig;
    private boolean active = false;

    //TODO: proper activation and deactivation should be implemented by cleaning and shutdown instances. Currently its just a hack.
    @Override
    public void init() throws InitializationException, InterruptedException {

    }

    @Override
    public void activate() throws CouldNotPerformException, InterruptedException {
        active = true;
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

            final JSonObjectFileProcessor fileProcessor = new JSonObjectFileProcessor(EvesonConfig.class);

            evesonConfig = (EvesonConfig) (fileProcessor.deserialize(JPService.getProperty(JPThemeFile.class).getValue()));

            LocationObserver.setThresholds(evesonConfig.PowerConsumptionThresholdNormal,
                    evesonConfig.PowerConsumptionThresholdHigh, evesonConfig.PowerConsumptionThresholdExtreme);

            System.out.println("Thresholds: " + evesonConfig.PowerConsumptionThresholdNormal + ", " + evesonConfig.PowerConsumptionThresholdHigh + ", " + evesonConfig.PowerConsumptionThresholdExtreme);
            if (evesonConfig.getDefaultVoices() < 0) {
                evesonConfig.setDefaultVoices(5);
                System.out.println("Invalid default number for voices: setting to 5...");
            }
            System.out.println("Default voices: " + evesonConfig.getDefaultVoices());
            ArrayList<PlayerConfig> configList = evesonConfig.getPlayerConfigList();

            configList.stream().forEach((config) -> {
                try {
                    int maxVoices = config.getMaxVoices();
                    float relativeAmplitude = config.getAmplitude();
                    if (maxVoices < 1) {
                        maxVoices = evesonConfig.defaultVoices;
                    }
                    if (relativeAmplitude <= 0) {
                        relativeAmplitude = 1.0f;
                    }
//                    System.out.println("Max Voices for " + config.getId() + ": " + maxVoices);
                    scopeSampleMap.put(config.getId(), new ScopePlayer(prefix + "/" + config.getSampleFile(), config.getType(), maxVoices, relativeAmplitude, config.getEventFilter()));
                } catch (CouldNotPerformException ex) {
                    ExceptionPrinter.printHistory(new CouldNotPerformException("error occured... skipping sample " + config.getSampleFile(), ex), System.err);
                }
            });

            new EventPlayer(scopeSampleMap).play();
            Remotes remotes = new Remotes();
            remotes.init();
            if (JPService.getProperty(JPShowGUI.class).getValue()) {
                new PowerTest().setVisible(true);
            }

        } catch (JPNotAvailableException | CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not launch eveson!", ex);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void deactivate() throws CouldNotPerformException, InterruptedException {
        active = false;
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

    public EvesonConfig getEvesonConfig() {
        return evesonConfig;
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("shutdown: Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
