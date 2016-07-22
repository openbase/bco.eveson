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
import java.util.Random;
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
    Random randomGenerator = new Random();
    private int NumSamplesInDir;
    private String sampleFile;

    /**
     * Constructor.
     *
     * @param sampleFile File to be played
     * @throws org.openbase.jul.exception.InstantiationException
     */
    public SampleVoice(String sampleFile) throws org.openbase.jul.exception.InstantiationException {
        this.sampleFile = sampleFile;

        System.err.println("Create SampleVoice for random sample in folder:" + sampleFile);
        //System.out.println("Number of Samples in Directory [" + sampleFile + "] is " + NumSamplesInDir);

        Synthesizer s = Eveson.getSynthesizer();
        LineOut l = Eveson.getLineOut();

        this.samplePlayer = new VariableRateMonoReader();
        s.add(samplePlayer);
        samplePlayer.output.connect(0, l.input, 0);

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
        int randomInt;
        File[] files = new File(sampleFile).listFiles();
        System.out.println("Files: " + sampleFile + ", " + files);
        NumSamplesInDir = files.length;
        String randomSample = null;
        try {
            randomInt = randomGenerator.nextInt(NumSamplesInDir);
            randomSample = files[randomInt].toString();
            System.out.println("sample: " + randomSample);
            sample = SampleLoader.loadFloatSample(new File(randomSample));
        } catch (IOException ex) {
            System.out.println("Could not load: " + randomSample);
        }

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
