package de.citec.csra;

import static de.citec.csra.ScopePlayer.Type.CUSTOM;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openbase.jul.exception.CouldNotPerformException;

/**
 *
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
                if (config.getValue().getType().equals(CUSTOM)) {
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
