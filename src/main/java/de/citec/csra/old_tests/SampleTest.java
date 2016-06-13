package de.citec.csra.old_tests;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.softsynth.shared.time.TimeStamp;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.dc.bco.registry.device.lib.DeviceRegistry;
import org.dc.bco.registry.device.remote.DeviceRegistryRemote;
import org.dc.jul.pattern.Observable;
import org.dc.jul.pattern.Observer;
import rsb.AbstractEventHandler;
import rsb.Event;
import rsb.Factory;
import rsb.Listener;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.state.MotionStateType;
import rst.homeautomation.unit.MotionSensorType;
import rst.homeautomation.unit.TemperatureSensorType;
import rst.homeautomation.unit.UnitConfigType;
import rst.homeautomation.unit.UnitTemplateType;

import org.dc.bco.dal.remote.unit.MotionSensorRemote;
import org.dc.bco.dal.remote.unit.LightRemote;
import org.dc.bco.dal.remote.unit.TemperatureSensorRemote;
import rst.homeautomation.unit.LightType;

/**
 * This class is a test! It plays multiple samples at a time.
 *
 * @author mgao
 */
public class SampleTest extends AbstractEventHandler implements KeyListener, TestEventListener {

    private final Synthesizer synth;
    private static final int MAX_VOICES = 30;
    LineOut lineOut;
    VoiceAllocator voiceAllocator;
    VoiceAllocator voiceAllocator2;
    UnitVoice[] voices;
    UnitVoice[] voices2;
    int notenumber = 0;
    private List listeners = new ArrayList();
    EventGenerator testEventGenerator;

    // todo: remove            vvv
    public SampleTest() throws Throwable {
        // RSB test
        //final Factory factory = Factory.getInstance();
        testEventGenerator = new EventGenerator();

        DeviceRegistryRemote deviceRegistryRemote = new DeviceRegistryRemote();
        //final Listener listener = factory.createListener("/home/sports/motionsensor");
        try {
            //listener.activate();

            deviceRegistryRemote.init();
            deviceRegistryRemote.activate();

            List<UnitConfigType.UnitConfig> motionSensors = deviceRegistryRemote.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.MOTION_SENSOR);
            List<MotionSensorRemote> motionSensorRemotes = new ArrayList<>();
            
            MotionSensorRemote remote;
            for (UnitConfigType.UnitConfig motionSensorConfig : motionSensors) {
                remote = new MotionSensorRemote();
                remote.init(motionSensorConfig);
                remote.activate();
                motionSensorRemotes.add(remote);
                remote.addObserver(new Observer<MotionSensorType.MotionSensor>() {

                    @Override
                    public void update(Observable<MotionSensorType.MotionSensor> source, MotionSensorType.MotionSensor data) throws Exception {
                        testEventGenerator.fireEvent('a');
                        
                    }
                });
            }

            //listener.addHandler(this, true);
            
            List<UnitConfigType.UnitConfig> temperatureSensors = deviceRegistryRemote.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.TEMPERATURE_SENSOR);
            List<TemperatureSensorRemote> temperatureSensorRemotes = new ArrayList<>();

            TemperatureSensorRemote tempRemote;
            for (UnitConfigType.UnitConfig temperatureSensorConfig : temperatureSensors) {
                tempRemote = new TemperatureSensorRemote();
                tempRemote.init(temperatureSensorConfig);
                tempRemote.activate();
                temperatureSensorRemotes.add(tempRemote);
                tempRemote.addObserver(new Observer<TemperatureSensorType.TemperatureSensor>() {

                    @Override
                    public void update(Observable<TemperatureSensorType.TemperatureSensor> source, TemperatureSensorType.TemperatureSensor data) throws Exception {
                        testEventGenerator.fireEvent('b');
                    }
                });
            }
            
//            List<UnitConfigType.UnitConfig> lights = deviceRegistryRemote.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.LIGHT);
//            List<LightRemote> lightRemotes = new ArrayList<>();
//
//            LightRemote lightRemote;
//            for (UnitConfigType.UnitConfig lightConfig : lights) {
//                lightRemote = new LightRemote();
//                lightRemote.init(lightConfig);
//                lightRemote.activate();
//                lightRemotes.add(lightRemote);
//                lightRemote.addObserver(new Observer<LightType.Light>() {
//
//                    @Override
//                    public void update(Observable<LightType.Light> source, LightType.Light data) throws Exception {
//                        testEventGenerator.fireEvent('c');
//                    }
//                });
//            }


            synth = JSyn.createSynthesizer();
            synth.add(lineOut = new LineOut());

            voices = new UnitVoice[MAX_VOICES];
            for (int i = 0; i < MAX_VOICES; i++) {
                SampleVoice_old voice = new SampleVoice_old("violin", synth, lineOut);
                voices[i] = voice;
            }
            voiceAllocator = new VoiceAllocator(voices);
            // Test sound output
            voices2 = new UnitVoice[MAX_VOICES];
            for (int i = 0; i < MAX_VOICES; i++) {
                SampleVoice_old voice = new SampleVoice_old("piano", synth, lineOut);
                voices2[i] = voice;
            }
            voiceAllocator2 = new VoiceAllocator(voices2);

            synth.start();
            lineOut.start();
            JFrame window = new JFrame();
            window.setSize(100, 100);
            window.setVisible(true);
            window.addKeyListener(this);
            testEventGenerator.addEventListener(this);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }

        } 
        
        finally {
            //listener.deactivate();
            deviceRegistryRemote.shutdown();
        }

    }

    @Override
    public void handleEvent(Event event) {
        testEventGenerator.fireEvent('a');
    }

    // todo ------------------------------------- vv remove
    public static void main(String args[]) throws Throwable {
        SampleTest s = new SampleTest();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key");
        testEventGenerator.fireEvent(e.getKeyChar());
    }

    @Override
    public synchronized void eventReceived(TestEvent event) {
        System.out.println("event received");

        char k = event.getCh();
        System.out.println(k);
        notenumber++;
        if (Character.isDigit(k)) {
            voiceAllocator.noteOn(notenumber,
                    Character.getNumericValue(k), 0, synth.createTimeStamp());
        } else if (Character.isAlphabetic(k)) {
            int char_num = Character.toLowerCase(k) - 'a' + 1;
            if (char_num > 12) {
                return; // we only have 12 samples
            }
            TimeStamp timeStamp = new TimeStamp(synth.getCurrentTime());
            voiceAllocator2.noteOn(notenumber, char_num, 0, synth.createTimeStamp());
        } else {
            return;
            
        }
        System.out.println(notenumber);
    }

    // the following methods are unused
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
