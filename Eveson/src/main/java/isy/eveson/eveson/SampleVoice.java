/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;
import java.io.File;
import java.io.IOException;

/**
 * Implementation of UnitVoice for Samples, so they can be allocated using a VoiceAllocator.
 * @author mgao
 */
public class SampleVoice implements UnitVoice {

    private static final String SAMPLE_PATH = "src/resources/samples/";
    private static final int MAX_VOICES = 30;
    private VariableRateDataReader samplePlayer;
    private String instrument;
    private FloatSample sample;

    /**
     * 
     * @param instrument folder for samples
     * @param s Synthesizer
     * @param l LineOut
     */
    public SampleVoice(String instrument, Synthesizer s, LineOut l) {
        this.instrument = instrument;
        this.samplePlayer = new VariableRateStereoReader();
        s.add(samplePlayer);
        samplePlayer.output.connect(0,l.input,0);
        samplePlayer.output.connect(1,l.input,1);
    }

    /**
     *
     * @param d
     * @param d1
     * @param ts
     */
    @Override
    public void noteOn(double d, double d1, TimeStamp ts) {
        //System.out.println("samplevoice: noteOn");
        File sampleFile;
        sampleFile = new File(SAMPLE_PATH + instrument + "/" + Math.round(d) + ".wav");
        System.out.println(sampleFile.getAbsoluteFile());
        try {
            sample = SampleLoader.loadFloatSample(sampleFile);
            samplePlayer.rate.set(sample.getFrameRate());
            //System.out.println("queueOn the sample");
            samplePlayer.dataQueue.queue(sample);
        } catch (IOException io) {
            // TODO
        }
    }

    @Override
    public void noteOff(TimeStamp ts) {
        samplePlayer.dataQueue.queueOff(sample);
    }

    @Override
    public UnitGenerator getUnitGenerator() {
        return samplePlayer;
    }

    @Override
    public void setPort(String string, double d, TimeStamp ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void usePreset(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UnitOutputPort getOutput() {
        return samplePlayer.output;
    }

    public VariableRateDataReader getSamplePlayer() {
        return samplePlayer;
    }

}
