package isy.eveson.eveson;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.jsyn.util.VoiceAllocator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public ScopePlayer(String sampleFile, Type type) {
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
            case ADJUST:
                samplePlayer = new VariableRateStereoReader();
                EventPlayer.getSynth().add(samplePlayer);
                 {
                    try {
                        sample = SampleLoader.loadFloatSample(this.getClass().getResource(sampleFile));
                    } catch (IOException ex) {
                        Logger.getLogger(ScopePlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                samplePlayer.output.connect(0, EventPlayer.getLineOut().input, 0);
                samplePlayer.output.connect(1, EventPlayer.getLineOut().input, 1);
                samplePlayer.rate.set(sample.getFrameRate());

        }
    }

    public void play(double amplitude) {
        switch (type) {
            case ADJUST:
                
                System.out.println("scopePlayer: Is something already playing? " + samplePlayer.dataQueue.hasMore());
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
