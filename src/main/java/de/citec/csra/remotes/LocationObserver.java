package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
import de.citec.csra.EvesonConfig;
import de.citec.csra.ScopePlayer;
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
    ScopePlayer sp_normal;
    ScopePlayer sp_high;
    ScopePlayer sp_extreme;

    public LocationObserver() throws InstantiationException {
        sp_normal = new ScopePlayer(EventPlayer.getInstance().getScopeSampleMap().get("POWER_NORMAL").getSampleFile(), ScopePlayer.Type.ADJUST);
        sp_high = new ScopePlayer(EventPlayer.getInstance().getScopeSampleMap().get("POWER_HIGH").getSampleFile(), ScopePlayer.Type.ADJUST);
        sp_extreme = new ScopePlayer(EventPlayer.getInstance().getScopeSampleMap().get("POWER_EXTREME").getSampleFile(), ScopePlayer.Type.ADJUST);
    }

    @Override
    public void update(Observable<LocationData> source, LocationData data) throws Exception {
        play(data.getPowerConsumptionState().getConsumption());

    }

    public void play(double consumption) {
        System.out.println("consumption: " + consumption);
        if (consumption < THRESHOLD_NORMAL) {
            sp_normal.play(consumption / THRESHOLD_NORMAL);
            sp_high.play(0);
        } else if (consumption < THRESHOLD_HIGH) {
            double normalconsumption_amplitude = (THRESHOLD_HIGH - consumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
            sp_normal.play(normalconsumption_amplitude);
            sp_high.play(1-normalconsumption_amplitude);
        } else if (consumption < THRESHOLD_EXTREME) {
            sp_high.play(1);
            sp_normal.play(0);
        } else {
            sp_high.play(1);
            sp_extreme.play(1);
            System.out.println("ROARING THUNDER!");
        }
    }

    public static void setThresholds(double normal, double high, double extreme){
        THRESHOLD_NORMAL = normal;
        THRESHOLD_HIGH = high;
        THRESHOLD_EXTREME = extreme;
    }

}
