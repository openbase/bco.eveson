package org.openbase.bco.eveson;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2017 openbase.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;
import com.jsyn.util.VoiceAllocator;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openbase.jul.exception.CouldNotPerformException;

/**
 * Plays samples that are associated with one scope. Each ScopePlayer has a
 * fixed behavior type and a fixed set of samples from which it will choose
 * randomly every time.
 *
 * @author mgao
 */
public class ScopePlayer {

    private final int MAX_VOICES;
    private String sampleFile;
    private Type type;
    private String eventFilter;

    private VoiceAllocator allocator;

    private VariableRateDataReader samplePlayer;
    private FloatSample sample;
    private int counter = 0; // Voice Allocators need a unique number for each note played

    private int randomInt;
    private Random randomGenerator = new Random();
    private int numSamplesInDir;
    private File[] files;
    private float relativeAmplitude;

    
    /**
     * Constructor.
     *
     * @param sampleFile Folder with all samples which can be randomly chosen by
     * this player.
     * @param type Behavior type of this player.
     * @param maxVoices
     * @param relativeAmplitude
     * @param eventFilter
     * @see Type
     * @throws org.openbase.jul.exception.InstantiationException
     */
    public ScopePlayer(String sampleFile, Type type, int maxVoices, float relativeAmplitude, String eventFilter) throws org.openbase.jul.exception.InstantiationException {
        MAX_VOICES = maxVoices;
        this.relativeAmplitude = relativeAmplitude;
        this.eventFilter = eventFilter;
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
                case ADJUST:
                case BACKGROUND: {
                    try {
                        System.err.println("Adjust/Background: Create SampleVoice for random sample in folder:" + sampleFile);
                        files = new File(sampleFile).listFiles();
                        numSamplesInDir = files.length;
                        randomInt = randomGenerator.nextInt(numSamplesInDir);
                        sampleFile = files[randomInt].toString();
                        System.out.println("sample: " + sampleFile);
                        sample = SampleLoader.loadFloatSample(new File(sampleFile));
                    } catch (IOException ex) {

                        throw new CouldNotPerformException("Could not load: " + sampleFile, ex);
                    }
                }
                samplePlayer = new VariableRateMonoReader();
                samplePlayer.output.connect(0, Eveson.getLineOut().input, 0);
                samplePlayer.output.connect(0, Eveson.getLineOut().input, 1);

                Eveson.getSynthesizer().add(samplePlayer);
                samplePlayer.rate.set(sample.getFrameRate());

            }
        } catch (CouldNotPerformException ex) {
            throw new org.openbase.jul.exception.InstantiationException(this, ex);
        }
    }

    /**
     * Play a sample from this player's folder.
     *
     * @param amplitude Volume from 0 to 1. If an invalid value is given, it
     * will be set to 1.
     */
    public void play(double amplitude) {
        if (amplitude < 0 || amplitude > 1.0) {
            amplitude = 1.0;
        }
        amplitude *= EventPlayer.getMaxAmplitude() * this.relativeAmplitude;
        switch (type) {
            case ADJUST:
                samplePlayer.amplitude.set(amplitude);
                if (!samplePlayer.dataQueue.hasMore()) {
                    randomInt = randomGenerator.nextInt(numSamplesInDir);
                    sampleFile = files[randomInt].toString();
//                    System.out.println("ScopePlayer: PLAY: sample: " + sampleFile);
                    {
                        try {
                            sample = SampleLoader.loadFloatSample(new File(sampleFile));
                        } catch (IOException ex) {
                            Logger.getLogger(ScopePlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    samplePlayer.dataQueue.queue(sample);
                }

                break;
            case PLAY:
                allocator.noteOn(counter, 1, amplitude, Eveson.getSynthesizer().createTimeStamp());
//                System.out.println("ScopePlayer: " + sampleFile);
                counter++;
                break;
            case BACKGROUND:
                samplePlayer.amplitude.set(amplitude);
                samplePlayer.dataQueue.queueLoop(sample, 0, sample.getNumFrames());

        }
    }

    /**
     * Types of events.
     *
     * - Play a sample each time the event takes place <br>
     *
     * - Play a sample only if it's not already playing, and set its amplitude
     * according to event data<br>
     *
     * - Loop a sample forever and set its amplitude according to event data<br>
     *
     * - Custom behavior that depends on event data that don't use the
     * GenericListener.
     */
    public enum Type {

        PLAY, ADJUST, CUSTOM, BACKGROUND;
    }

    public String getSampleFile() {
        return sampleFile;
    }

    public Type getType() {
        return type;
    }

    public int getMAX_VOICES() {
        return MAX_VOICES;
    }

    public float getRelativeAmplitude() {
        return relativeAmplitude;
    }

    public String getEventFilter() {
        return eventFilter;
    }

}
