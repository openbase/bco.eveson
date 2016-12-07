package de.citec.csra;

import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

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
     * @param sampleFile Directory with samples for this voice.
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
     * @param d Pitch (Unused)
     * @param a Amplitude
     * @param ts TimeStamp (Unused)
     */
    @Override
    public void noteOn(double d, double a, TimeStamp ts) {
        System.out.println("sampleVoice: note On" + new Date(System.currentTimeMillis()));
        if(samplePlayer.dataQueue.hasMore()) {
            System.out.println("SampleVoice: No voice available, aborting");
            return;
        }
        int randomInt;
        File[] files = new File(sampleFile).listFiles();
//        System.out.println("Note On: " + sampleFile);
        NumSamplesInDir = files.length;
        String randomSample = null;

        samplePlayer.dataQueue.clear();
               
        try {
            randomInt = randomGenerator.nextInt(NumSamplesInDir);
            randomSample = files[randomInt].toString();
//            System.out.println("sample: " + randomSample);
            sample = SampleLoader.loadFloatSample(new File(randomSample));
        } catch (IOException ex) {
            System.out.println("Could not load: " + randomSample);
        }

        samplePlayer.rate.set(sample.getFrameRate());
        samplePlayer.dataQueue.queue(sample);
        samplePlayer.amplitude.set(a);

    }

    /**
     * Queue off the current sample.
     *
     * @param ts Timestamp
     */
    @Override
    public void noteOff(TimeStamp ts) {
        samplePlayer.dataQueue.queueOff(sample);
    }

    @Override
    public UnitGenerator getUnitGenerator() {
        return samplePlayer;
    }

    /**
     * Not implemented!
     *
     * @param string
     * @param d
     * @param ts
     */
    @Override
    public void setPort(String string, double d, TimeStamp ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Not implemented!
     *
     * @param i
     */
    @Override
    public void usePreset(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Not implemented!
     *
     * @return
     */
    @Override
    public UnitOutputPort getOutput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
