package de.citec.csra;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.jsyn.util.VoiceAllocator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openbase.jul.exception.CouldNotPerformException;

/**
 * Handles events for one scope.
 *
 * @author mgao
 */
public class ScopePlayer {

    private final int MAX_VOICES = 5;
    private String sampleFile;
    private Type type;

    private VoiceAllocator allocator;

    private VariableRateDataReader samplePlayer;
    private FloatSample sample;
    private int counter = 0; // Voice Allocators need a unique number for each note played

    public ScopePlayer(String sampleFile, Type type) throws org.openbase.jul.exception.InstantiationException {
        try {
            System.out.println("Load: " + sampleFile);
            this.sampleFile = sampleFile;
            this.type = type;
            switch (type) {
                case PLAY:
                    UnitVoice[] voices = new UnitVoice[MAX_VOICES];
                    for (int i = 0; i < MAX_VOICES; i++) {
                        SampleVoice voice = new SampleVoice(sampleFile);
                        voices[i] = voice;
                    }
                    allocator = new VoiceAllocator(voices);
                    break;
                case ADJUST: {
                    try {
                        sample = SampleLoader.loadFloatSample(new File(sampleFile));
                    } catch (IOException ex) {
                        Logger.getLogger(ScopePlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (sample.getChannelsPerFrame() == 2) {
                    samplePlayer = new VariableRateStereoReader();
                    samplePlayer.output.connect(0, EventPlayer.getLineOut().input, 0);
                    samplePlayer.output.connect(1, EventPlayer.getLineOut().input, 1);
                } else {

                    samplePlayer = new VariableRateMonoReader();
                    samplePlayer.output.connect(0, EventPlayer.getLineOut().input, 0);
                }

                EventPlayer.getSynth().add(samplePlayer);
                samplePlayer.rate.set(sample.getFrameRate());

            }
        } catch (CouldNotPerformException ex) {
            throw new org.openbase.jul.exception.InstantiationException(this, ex);
        }
    }

    public void play(double amplitude) {
        switch (type) {
            case ADJUST:

                samplePlayer.amplitude.set(amplitude);

                if (!samplePlayer.dataQueue.hasMore()) {
                    samplePlayer.dataQueue.queue(sample);
                }

                break;
            case PLAY:
                allocator.noteOn(counter, 1, amplitude, EventPlayer.getSynth().createTimeStamp());
                counter++;
        }
    }

    /**
     * Types of events.
     *
     * 1. Play a sample each time the event takes place 2. Adjust a sample's
     * amplitude according to event data (or play if no sample is playing)
     */
    public enum Type {

        PLAY, ADJUST;
    }

    public String getSampleFile() {
        return sampleFile;
    }

    public Type getType() {
        return type;
    }

}
