package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
import de.citec.csra.ScopePlayer;
import java.util.Map;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import rst.spatial.LocationDataType.LocationData;

/**
 *
 * @author mgao
 */
public class LocationObserver implements Observer<LocationData> {

    private static double THRESHOLD_NORMAL;
    private static double THRESHOLD_HIGH;
    private static double THRESHOLD_EXTREME;
    private ScopePlayer sp_normal;
    private ScopePlayer sp_high;
    private ScopePlayer sp_extreme;
    // parameter and last value for exponential average
    private double alpha = 0.8;
    private double lastValue = 0;

    public LocationObserver() throws InstantiationException {
        final Map<String, ScopePlayer> scopeSampleMap = EventPlayer.getInstance().getScopeSampleMap();
        ScopePlayer normal = scopeSampleMap.get("POWER_NORMAL");
        ScopePlayer high = scopeSampleMap.get("POWER_HIGH");
        ScopePlayer extreme = scopeSampleMap.get("POWER_EXTREME");
        sp_normal = new ScopePlayer(normal.getSampleFile(), ScopePlayer.Type.BACKGROUND, 1, normal.getRelativeAmplitude());
        sp_high = new ScopePlayer(high.getSampleFile(), ScopePlayer.Type.BACKGROUND, 1, high.getRelativeAmplitude());
        sp_extreme = new ScopePlayer(extreme.getSampleFile(), ScopePlayer.Type.ADJUST, 1, extreme.getRelativeAmplitude());
    }

    @Override
    public void update(Observable<LocationData> source, LocationData data) throws Exception {
        play(data.getPowerConsumptionState().getConsumption());

    }

    public void play(double consumption) {
        consumption = expAverage(consumption);
        if (consumption < THRESHOLD_NORMAL) {
            sp_normal.play(consumption / THRESHOLD_NORMAL);
            sp_high.play(0);
        } else if (consumption < THRESHOLD_HIGH) {
            double normalconsumption_amplitude = (THRESHOLD_HIGH - consumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
            sp_normal.play(normalconsumption_amplitude);
            sp_high.play(1 - normalconsumption_amplitude);
        } else if (consumption < THRESHOLD_EXTREME) {
            sp_high.play(1);
            sp_normal.play(0);
        } else {
            sp_high.play(1);
            sp_extreme.play(1);
        }

    }

    private double expAverage(double value) {
        double newValue = lastValue + alpha * (value - lastValue);
        lastValue = newValue;
//        System.out.println("new value:" + value + ", averaged new value: " + newValue);
        return newValue;
    }

    public static void setThresholds(double normal, double high, double extreme) {
        THRESHOLD_NORMAL = normal;
        THRESHOLD_HIGH = high;
        THRESHOLD_EXTREME = extreme;
    }

}
