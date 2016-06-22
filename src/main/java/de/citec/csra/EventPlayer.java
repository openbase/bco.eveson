/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openbase.jul.exception.CouldNotPerformException;

/**
 *
 * @author jplettemeier
 */
public class EventPlayer {

    private final Map<String, ScopePlayer> scopeSampleMap;
    private final List<GenericListener> listenerMap;
   

    public EventPlayer(Map<String, ScopePlayer> scopeSampleMap) {
        this.scopeSampleMap = scopeSampleMap;
        this.listenerMap = new ArrayList<>();
    }

    public void play() throws InterruptedException, CouldNotPerformException {
        try {
            GenericListener listener;
            for (Map.Entry<String, ScopePlayer> config : scopeSampleMap.entrySet()) {
                listener = new GenericListener(config.getKey(), config.getValue());
                listenerMap.add(listener);
            }
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not play!", ex);
        }
    }
}
