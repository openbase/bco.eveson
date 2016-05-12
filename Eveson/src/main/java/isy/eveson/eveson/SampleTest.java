package isy.eveson.eveson;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.softsynth.shared.time.TimeStamp;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 * This class is a test! It plays multiple samples at a time.
 *
 * @author mgao
 */
public class SampleTest implements KeyListener, TestEventListener {

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

    public SampleTest() throws IOException, InterruptedException {
        testEventGenerator = new EventGenerator();
        synth = JSyn.createSynthesizer();
        synth.add(lineOut = new LineOut());

        voices = new UnitVoice[MAX_VOICES];
        for (int i = 0; i < MAX_VOICES; i++) {
            SampleVoice voice = new SampleVoice("violin", synth, lineOut);
            voices[i] = voice;
        }
        voiceAllocator = new VoiceAllocator(voices);
        // Test sound output
        voices2 = new UnitVoice[MAX_VOICES];
        for (int i = 0; i < MAX_VOICES; i++) {
            SampleVoice voice = new SampleVoice("piano", synth, lineOut);
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

    }

    public static void main(String args[]) throws IOException, InterruptedException {
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
