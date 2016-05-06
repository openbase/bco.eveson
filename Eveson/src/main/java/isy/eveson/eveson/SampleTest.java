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
import javax.swing.JFrame;

/**
 * This class is a test! It plays multiple samples at a time.
 *
 * @author mgao
 */
public class SampleTest implements KeyListener {

    private final Synthesizer synth;
    private static final String SAMPLE_PATH = "src/resources/samples/";
    private static final int MAX_VOICES = 50;
    LineOut lineOut;
    VoiceAllocator voiceAllocator;
    VoiceAllocator voiceAllocator2;
    UnitVoice[] voices;
    UnitVoice[] voices2;
    int notenumber = 0;

    public SampleTest() throws IOException, InterruptedException {
        synth = JSyn.createSynthesizer();
        synth.add(lineOut = new LineOut());
        synth.start();
        lineOut.start();

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

//        File sampleFile;
//        sampleFile = new File(SAMPLE_PATH + "violin" + "/" + "01" + ".wav");
//        FloatSample s = SampleLoader.loadFloatSample(sampleFile);
//        SampleVoice voice2 = new SampleVoice("violin");
//        synth.add(voice2.getSamplePlayer());
//        voice2.getOutput().connect(0,lineOut.input,0);
//        voice2.getOutput().connect(1,lineOut.input,1);
//        voice2.getSamplePlayer().dataQueue.queue(s);
        JFrame window = new JFrame();
        window.setSize(100, 100);
        window.setVisible(true);
        window.addKeyListener(this);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String args[]) throws IOException, InterruptedException {
        SampleTest s = new SampleTest();
        s.loop(s.voiceAllocator, 1, 1, 4);
        s.loop(s.voiceAllocator, 4, 2, 5);
        s.loop(s.voiceAllocator, 8, 5, 7);
        s.loop(s.voiceAllocator2, 1, 1, 1);

    }

    private void loop(VoiceAllocator voiceAllocator, int pitch,
            double mintime, double maxtime) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char k = e.getKeyChar();
            notenumber++;
        if (Character.isDigit(k)) {
            //System.out.println("key pressed: " + e.getKeyChar());
//            voices[0].noteOn(1, 0, synth.createTimeStamp());
            System.out.println(notenumber);
            voiceAllocator.noteOn(notenumber,
                    Character.getNumericValue(k), 0, synth.createTimeStamp());
        } else if (Character.isAlphabetic(e.getKeyChar())) {
            int char_num = Character.toLowerCase(k) - 'a' + 1;
            if(char_num > 12) return; // we only have 12 samples
            TimeStamp timeStamp = new TimeStamp(synth.getCurrentTime());
            voiceAllocator2.noteOn(notenumber, char_num, 0, synth.createTimeStamp());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
