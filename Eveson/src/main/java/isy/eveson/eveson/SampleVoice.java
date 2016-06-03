package isy.eveson.eveson;

import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A UnitVoice for one sample.
 *
 * @author mgao
 */
public class SampleVoice implements UnitVoice {

    private final URL sampleURL;
    private final VariableRateDataReader samplePlayer;
    private FloatSample sample;

    /**
     * Constructor.
     *
     * @param sampleFile File to be played
     */
    public SampleVoice(String sampleFile) {
        Synthesizer s = EventPlayer.getSynth();
        LineOut l = EventPlayer.getLineOut();
        sampleURL = this.getClass().getResource(sampleFile);
        try {
            sample = SampleLoader.loadFloatSample(sampleURL);
        } catch (IOException ex) {
            Logger.getLogger(SampleVoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (sample.getChannelsPerFrame() == 2) {
            this.samplePlayer = new VariableRateStereoReader();
            s.add(samplePlayer);
            samplePlayer.output.connect(0, l.input, 0);
            samplePlayer.output.connect(1, l.input, 1);
        } else{
        this.samplePlayer = new VariableRateMonoReader();
            s.add(samplePlayer);
            samplePlayer.output.connect(0, l.input, 0);
        }
    }

    /**
     * Play a sample.
     *
     * @param d Unused (TODO: use for pitching?)
     * @param d1 Amplitude
     * @param ts TimeStamp
     */
    @Override
    public void noteOn(double d, double d1, TimeStamp ts) {
        samplePlayer.rate.set(sample.getFrameRate());
        samplePlayer.dataQueue.queue(sample);
        samplePlayer.amplitude.set(d1);

    }

    @Override
    public void noteOff(TimeStamp ts) {
        samplePlayer.dataQueue.queueOff(sample);
    }

    // Unused Stuff
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
