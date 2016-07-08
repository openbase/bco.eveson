package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
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

    double THRESHOLD_NORMAL = 150;
    double THRESHOLD_HIGH = 600; // ?
    double THRESHOLD_EXTREME = 3000;
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
        play(data.getPowerConsumptionState().getCurrent());

    }

    public void play(double consumption) {
        System.out.println("consumption: " + consumption);
        if (consumption < THRESHOLD_NORMAL) {
            sp_normal.play(0);
            sp_high.play(0);
        } else if (consumption < THRESHOLD_HIGH) {
            sp_normal.play(consumption / THRESHOLD_NORMAL);
            sp_high.play(0);
        } else if (consumption > THRESHOLD_EXTREME) {
            sp_high.play(1);
            sp_extreme.play(1);
            System.out.println("ROARING THUNDER!");
        } else {
            double highconsumption_amplitude = (consumption - THRESHOLD_HIGH) / (THRESHOLD_EXTREME - THRESHOLD_HIGH);
            System.out.println("amplitude: " + highconsumption_amplitude);
            sp_high.play(highconsumption_amplitude);
            sp_normal.play(1 - highconsumption_amplitude);
        }
    }

}
