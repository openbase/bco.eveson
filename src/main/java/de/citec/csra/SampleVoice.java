package de.citec.csra;

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
import java.io.File;
import java.io.IOException;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;

/**
 * A UnitVoice for one sample.
 *
 * @author mgao
 */
public class SampleVoice implements UnitVoice {

    private final VariableRateDataReader samplePlayer;
    private FloatSample sample;

    /**
     * Constructor.
     *
     * @param sampleFile File to be played
     * @throws org.openbase.jul.exception.InstantiationException
     */
    public SampleVoice(String sampleFile) throws org.openbase.jul.exception.InstantiationException {
        try {
            Synthesizer s = Eveson.getSynthesizer();
            LineOut l = Eveson.getLineOut();
            try {
                sample = SampleLoader.loadFloatSample(new File(sampleFile));
            } catch (IOException ex) {
                throw new CouldNotPerformException("Could not load: " + sampleFile, ex);
            }
            if (sample.getChannelsPerFrame() == 2) {
                this.samplePlayer = new VariableRateStereoReader();
                s.add(samplePlayer);
                samplePlayer.output.connect(0, l.input, 0);
                samplePlayer.output.connect(1, l.input, 1);
            } else {
                this.samplePlayer = new VariableRateMonoReader();
                s.add(samplePlayer);
                samplePlayer.output.connect(0, l.input, 0);
            }

        } catch (CouldNotPerformException ex) {
            throw new org.openbase.jul.exception.InstantiationException(this, ex);
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
