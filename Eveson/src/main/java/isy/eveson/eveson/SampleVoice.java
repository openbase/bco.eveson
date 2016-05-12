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
import java.io.IOException;
import java.net.URL;

/**
 * Implementation of UnitVoice for Samples, so they can be allocated using a VoiceAllocator.
 * @author mgao
 */
public class SampleVoice implements UnitVoice {

    private static final String SAMPLE_PATH = "/samples/";
    private static final int MAX_VOICES = 30;
    private final VariableRateDataReader samplePlayer;
    private final String instrument;
    private FloatSample sample;

    /**
     * Constructor for setting up the voice and connecting its output.
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
     * Playing a sample until the end.
     * @param d Filename (typically pitch - where 1 = G, 2 = G#..)
     * @param d1 unused
     * @param ts unused
     */
    @Override
    public void noteOn(double d, double d1, TimeStamp ts) {
        URL sampleFile = this.getClass().getResource(SAMPLE_PATH + instrument + "/" + Math.round(d) + ".wav");
        System.out.println(sampleFile.getPath());
        try {
            sample = SampleLoader.loadFloatSample(sampleFile);
            samplePlayer.rate.set(sample.getFrameRate());
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
