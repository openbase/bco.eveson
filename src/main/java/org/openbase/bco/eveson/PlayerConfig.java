package org.openbase.bco.eveson;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2019 openbase.org
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

import com.google.protobuf.Message;

/**
 *
 * @author jplettemeier
 */
public class PlayerConfig {

    private String id;
    private String sampleFile;
    private ScopePlayer.Type type;
    private String eventFilter; 
    private int maxVoices;
    private float amplitude;

    public PlayerConfig() {
    }

    public PlayerConfig(String id, String sampleFile, ScopePlayer.Type type, int maxVoices, float amplitude, String eventFilter) {
        this.id = id;
        this.sampleFile = sampleFile;
        this.type = type;
        this.maxVoices = maxVoices;
        this.amplitude = amplitude;
        this.eventFilter = eventFilter;
    }

    public String getId() {
        return id;
    }

    public String getSampleFile() {
        return sampleFile;
    }

    public ScopePlayer.Type getType() {
        return type;
    }

    public String getEventFilter() {
        return eventFilter;
    }
    
    public int getMaxVoices() {
        return maxVoices;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

}
