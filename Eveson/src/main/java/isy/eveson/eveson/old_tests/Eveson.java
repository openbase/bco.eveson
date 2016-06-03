package isy.eveson.eveson.old_tests;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.instruments.WaveShapingVoice;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.util.VoiceAllocator;
import com.softsynth.jsyn.*;
import com.softsynth.shared.time.TimeStamp;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * This is a quick and dirty example of playing multiple voices via keys.
 */
public class Eveson implements KeyListener {

    private static final int MAX_VOICES = 10;
    private SineOscillator osc;
    private LineOut lineOut;
    private VoiceAllocator allocator;
    private VoiceAllocator allocator2;
    private Synthesizer synth;
    private UnitVoice[] voices;
    private UnitVoice[] voices2;

    public Eveson() {
        init();
    }

    public void init() {
        JFrame window = new JFrame();
        window.setSize(100, 100);
        window.setVisible(true);
        window.addKeyListener(this);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        synth = JSyn.createSynthesizer();

        // Add an output.
        synth.add(lineOut = new LineOut());

        // An array of voices that can be allocated when free
        voices = new UnitVoice[MAX_VOICES];
        for (int i = 0; i < MAX_VOICES; i++) {
            SubtractiveSynthVoice voice = new SubtractiveSynthVoice();
            synth.add(voice);
            voice.getOutput().connect(0, lineOut.input, 0);
            voice.getOutput().connect(0, lineOut.input, 1);
            voices[i] = voice;
        }
        allocator = new VoiceAllocator(voices);

        voices2 = new UnitVoice[MAX_VOICES];
        for (int i = 0; i < MAX_VOICES; i++) {
            WaveShapingVoice voice = new WaveShapingVoice();
            //voice.noteOff();
            synth.add(voice);
            voice.getOutput().connect(0, lineOut.input, 0);
            voice.getOutput().connect(0, lineOut.input, 1);
            voices2[i] = voice;
        }
        allocator2 = new VoiceAllocator(voices2);

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();
        // We only need to start the LineOut. It will pull data from the
        // voices.
        lineOut.start();

        Synth.sleepForTicks(1);
        synth.stop();
    }

    // public static void main(String args[]) {
    //   new Eveson();
    //}
    int keyToNum(char k) {

        int num = Character.toLowerCase(k) - 'a' + 1;
        System.out.println("Key: " + k + ", number in chromatic scale from A: " + num);
        return num;
    }

    double keyToFrequency(char k) {
        int num = keyToNum(k);
        return numToFrequency(num);

    }

    double numToFrequency(int n) {

        final double concertA = 110.0;
        return concertA * Math.pow(2.0, (n * (1.0 / 12.0)));
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        double frequency;
        int num;
        if (Character.isDigit(ke.getKeyChar())) {
            num = Character.getNumericValue(ke.getKeyChar());
            frequency = numToFrequency(num);

            double amplitude = 0.2;
            TimeStamp timeStamp = new TimeStamp(synth.getCurrentTime());
            allocator2.noteOn(num, frequency, amplitude, timeStamp);
        } else if (Character.isAlphabetic(ke.getKeyChar())) {
            num = keyToNum(ke.getKeyChar());
            frequency = keyToFrequency(ke.getKeyChar());
            double amplitude = 0.2;
            TimeStamp timeStamp = new TimeStamp(synth.getCurrentTime());
            allocator.noteOn(num, frequency, amplitude, timeStamp);

        } else {
            return;
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        char k = ke.getKeyChar();
        if (Character.isDigit(k)) {
            allocator2.noteOff(Character.getNumericValue(k), new TimeStamp(synth.getCurrentTime()));
        } else if (Character.isAlphabetic(k)) {
            int num = keyToNum(ke.getKeyChar());
            allocator.noteOff(num, new TimeStamp(synth.getCurrentTime()));
        }
    }
}
