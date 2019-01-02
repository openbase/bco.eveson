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

import static org.openbase.bco.eveson.ScopePlayer.Type.BACKGROUND;
import static org.openbase.bco.eveson.ScopePlayer.Type.CUSTOM;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openbase.jul.exception.CouldNotPerformException;

/**
 * Handles the mapping from scopes to samples.
 * @author jplettemeier
 */
public class EventPlayer {

    private static EventPlayer instance;
    private final Map<String, ScopePlayer> scopeSampleMap;
    private final List<GenericListener> listenerMap;
    private static float maxAmplitude;

    public EventPlayer(Map<String, ScopePlayer> scopeSampleMap) {
        this.scopeSampleMap = scopeSampleMap;
        this.listenerMap = new ArrayList<>();
        instance = this;
    }

    public void play() throws InterruptedException, CouldNotPerformException {
        try {
            GenericListener listener;
            for (Map.Entry<String, ScopePlayer> config : scopeSampleMap.entrySet()) {
                if (config.getValue().getType().equals(CUSTOM) || config.getValue().getType().equals(BACKGROUND)) {
                    continue;
                }
                listener = new GenericListener(config.getKey(), config.getValue());
                listenerMap.add(listener);
            }
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not play!", ex);
        }
    }

    public Map<String, ScopePlayer> getScopeSampleMap() {
        return scopeSampleMap;
    }

    public static EventPlayer getInstance() {
        return instance;
    }

    public static void setMaxAmplitude(float maxAmplitude) {
        EventPlayer.maxAmplitude = maxAmplitude;
    }

    public static float getMaxAmplitude() {
        return maxAmplitude;
    }
}
