/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.old_tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;
import java.io.File;

/**
 * This file plays a minor third using violin samples.
 */
public class PlaySample {

    private Synthesizer synth;
    private VariableRateDataReader samplePlayer;
    private VariableRateDataReader samplePlayer2;
    private LineOut lineOut;

    private static final String SAMPLE_PATH = "src/resources/samples/";

    private void test() {
        File sampleFile, sampleFile2;
        sampleFile = new File(SAMPLE_PATH + "violin" + "/" + "1" + ".wav");
        sampleFile2 = new File(SAMPLE_PATH + "violin" + "/" + "4" + ".wav");
        // sampleFile = new URL("http://www.softsynth.com/samples/NotHereNow22K.wav");

        synth = JSyn.createSynthesizer();

        FloatSample sample, sample2;
        try {
            // We only need to start the LineOut. It will pull data from the
            // sample player.
            synth.add(lineOut = new LineOut());
            lineOut.start();
            // Start synthesizer using default stereo output at 44100 Hz.
            synth.start();
            // Add an output mixer.

            // Load the sample and display its properties.
            SampleLoader.setJavaSoundPreferred(false);
            sample = SampleLoader.loadFloatSample(sampleFile);
            sample2 = SampleLoader.loadFloatSample(sampleFile2);
            System.out.println("Sample has: channels  = " + sample.getChannelsPerFrame());
            System.out.println("            frames    = " + sample.getNumFrames());
            System.out.println("            rate      = " + sample.getFrameRate());
            System.out.println("            loopStart = " + sample.getSustainBegin());
            System.out.println("            loopEnd   = " + sample.getSustainEnd());

            if (sample.getChannelsPerFrame() == 1) {
                synth.add(samplePlayer = new VariableRateMonoReader());
                synth.add(samplePlayer2 = new VariableRateMonoReader());
                samplePlayer.output.connect(0, lineOut.input, 0);
                samplePlayer2.output.connect(0, lineOut.input, 0);
            } else if (sample.getChannelsPerFrame() == 2) {
                synth.add(samplePlayer = new VariableRateStereoReader());
                synth.add(samplePlayer2 = new VariableRateStereoReader());
                samplePlayer.output.connect(0, lineOut.input, 0);
                samplePlayer.output.connect(1, lineOut.input, 1);
                samplePlayer2.output.connect(0, lineOut.input, 0);
                samplePlayer2.output.connect(1, lineOut.input, 1);
            } else {
                throw new RuntimeException("Can only play mono or stereo samples.");
            }

            samplePlayer.rate.set(sample.getFrameRate());
            samplePlayer2.rate.set(sample2.getFrameRate());

            // We can simply queue the entire file.
            // Or if it has a loop we can play the loop for a while.
            System.out.println("queue the sample");
            samplePlayer.dataQueue.queue(sample);
            samplePlayer2.dataQueue.queue(sample2);

            for (int i = 1; i < 10; i++) {
                samplePlayer2.amplitude.set(1 - (double)i/10, new TimeStamp(synth.getCurrentTime() + (double)i/10));
                samplePlayer.amplitude.set(1 - (double)i/10, new TimeStamp(synth.getCurrentTime() +(double)i/10));
                // this is super silly
                samplePlayer.rate.set(sample.getFrameRate() * (1 + (double)i/10), new TimeStamp(synth.getCurrentTime() +(double)i/10));
                samplePlayer2.rate.set(sample2.getFrameRate() * (1 + (double)i/10), new TimeStamp(synth.getCurrentTime() + (double)i/10));
            }

            // Wait until the sample has finished playing.
            do {
                synth.sleepFor(1.0);
            } while (samplePlayer.dataQueue.hasMore());

            synth.sleepFor(0.5);

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        }
        // Stop everything.
        synth.stop();

    }

    public static void main(String[] args) {
        new PlaySample().test();

    }

}
