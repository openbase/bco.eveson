package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
import de.citec.csra.ScopePlayer;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import rst.domotic.unit.location.LocationDataType.LocationData;

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
    private double alpha = 0.2;
    private double lastValue = 0;

    Timer time = new Timer();
    private double consumption;
//    private double currentValue;

    public LocationObserver() throws InstantiationException {
        final Map<String, ScopePlayer> scopeSampleMap = EventPlayer.getInstance().getScopeSampleMap();
        ScopePlayer normal = scopeSampleMap.get("POWER_NORMAL");
        ScopePlayer high = scopeSampleMap.get("POWER_HIGH");
        ScopePlayer extreme = scopeSampleMap.get("POWER_EXTREME");
        sp_normal = new ScopePlayer(normal.getSampleFile(), ScopePlayer.Type.BACKGROUND, 1, normal.getRelativeAmplitude(), null);
        sp_high = new ScopePlayer(high.getSampleFile(), ScopePlayer.Type.BACKGROUND, 1, high.getRelativeAmplitude(), null);
        sp_extreme = new ScopePlayer(extreme.getSampleFile(), ScopePlayer.Type.ADJUST, 1, extreme.getRelativeAmplitude(), null);
        time.schedule(new SmoothPowerConsumption(), 0, 100);
    }

    // Update actual consumption value
    @Override
    public void update(Observable<LocationData> source, LocationData data) throws Exception {
//        play(data.getPowerConsumptionState().getConsumption());
        updateConsumption(data.getPowerConsumptionState().getConsumption());
    }

    public void updateConsumption(double consumption) {
        this.consumption = consumption;
//        System.out.println("new consumption value: " + consumption);
    }
    
    
    private double lastSpNormalConsumption = 0;
    private double lastSpHighConsumption = 0;
    private double changeStep = 0.1;
    
    public synchronized void play(double avgConsumption) {
//        System.out.print("new value:" + consumption + ", averaged new value: ");
//        consumption = expAverage(consumption);
//        System.out.println(consumption);
        if (avgConsumption < THRESHOLD_NORMAL) {
            lastSpNormalConsumption = avgConsumption / THRESHOLD_NORMAL;
            sp_normal.play(lastSpNormalConsumption);
            
            lastSpHighConsumption = Math.max(0,lastSpHighConsumption - changeStep);
            sp_high.play(lastSpHighConsumption);
            
        } else if (avgConsumption < THRESHOLD_HIGH) {
            lastSpNormalConsumption = (THRESHOLD_HIGH - avgConsumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
            sp_normal.play(lastSpNormalConsumption);
            lastSpHighConsumption = 1 - lastSpNormalConsumption;
            sp_high.play(lastSpHighConsumption);
            
        } else if (avgConsumption < THRESHOLD_EXTREME) {
//            sp_high.play(1);
            
            lastSpHighConsumption = Math.min(1, lastSpHighConsumption + changeStep);
            sp_normal.play(Math.min(1, lastSpHighConsumption));
            lastSpNormalConsumption = Math.max(0, lastSpNormalConsumption - changeStep);
            sp_normal.play(lastSpNormalConsumption);
            
        } else {
            
            lastSpNormalConsumption = Math.max(0, lastSpNormalConsumption - changeStep);
            sp_normal.play(lastSpNormalConsumption);
            sp_high.play(1);
            sp_extreme.play(1);
        }
            System.out.println("Consumption: " + avgConsumption + " (real value: " + consumption + ", normal: " + lastSpNormalConsumption + ", high: " + lastSpHighConsumption);

    }

    private double expAverage(double value) {
        double newValue = lastValue + alpha * (value - lastValue);
        lastValue = newValue;
        return newValue;
    }

    public static void setThresholds(double normal, double high, double extreme) {
        THRESHOLD_NORMAL = normal;
        THRESHOLD_HIGH = high;
        THRESHOLD_EXTREME = extreme;
    }

    public double getLastValue() {
        return lastValue;
    }

    public class SmoothPowerConsumption extends TimerTask {

        @Override
        public void run() {
            lastValue = expAverage(consumption);
            play(lastValue);
//            System.out.println("consumption: " + consumption + ", value: " + lastValue);

        }
    }
}
