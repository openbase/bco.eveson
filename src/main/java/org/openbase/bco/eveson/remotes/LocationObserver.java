package org.openbase.bco.eveson.remotes;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2021 openbase.org
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

import org.openbase.bco.eveson.EventPlayer;
import org.openbase.bco.eveson.ScopePlayer;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observer;
import org.openbase.jul.pattern.provider.DataProvider;
import org.openbase.type.domotic.unit.location.LocationDataType.LocationData;

/**
 *
 * @author mgao
 */
public class LocationObserver implements Observer<DataProvider<LocationData>, LocationData> {

    private static double THRESHOLD_NORMAL;
    private static double THRESHOLD_HIGH;
    private static double THRESHOLD_EXTREME;
    private ScopePlayer sp_normal;
    private ScopePlayer sp_high;
    private ScopePlayer sp_extreme;
    // parameter and last value for exponential average
    private static double ALPHA = 0.2;
    private double lastValue = 0;
    private static int TIMEFRAME = 100;
    private static double CHANGESTEP = 0.05;

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
        time.schedule(new SmoothPowerConsumption(), 0, TIMEFRAME);
    }

    // Update actual consumption value
    @Override
    public void update(DataProvider<LocationData> source, LocationData data) throws Exception {
//        play(data.getPowerConsumptionState().getConsumption());
        updateConsumption(data.getPowerConsumptionState().getConsumption());
    }

    public void updateConsumption(double consumption) {
        this.consumption = consumption;
//        System.out.println("new consumption value: " + consumption);
    }

    private double lastSpNormalConsumption = 0;
    private double lastSpHighConsumption = 0;

    public synchronized void play(double avgConsumption) {

        double idealNormal = 0;
        double idealHigh = 0;
        if (avgConsumption < THRESHOLD_NORMAL) {
            idealNormal = avgConsumption / THRESHOLD_NORMAL;
            idealHigh = 0;
        } else if (avgConsumption < THRESHOLD_HIGH) {
            idealNormal = (THRESHOLD_HIGH - avgConsumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
            idealHigh = 1 - idealNormal;
        } else { 
            idealNormal = 0;
            idealHigh = 1;
            if (avgConsumption > THRESHOLD_EXTREME) {
                sp_extreme.play(1);
            }
        }

        if (lastSpNormalConsumption > idealNormal + CHANGESTEP) {
            lastSpNormalConsumption = Math.max(0, lastSpNormalConsumption - CHANGESTEP);
        } else if (lastSpNormalConsumption < idealNormal - CHANGESTEP) {
            lastSpNormalConsumption = Math.min(1, lastSpNormalConsumption + CHANGESTEP);
        } else {
            lastSpNormalConsumption = idealNormal;
        }
        sp_normal.play(lastSpNormalConsumption);

        if (lastSpHighConsumption > idealHigh + CHANGESTEP) {
            lastSpHighConsumption = Math.max(0, lastSpHighConsumption - CHANGESTEP);
        } else if (lastSpHighConsumption < idealHigh - CHANGESTEP) {
            lastSpHighConsumption = Math.min(1, lastSpHighConsumption + CHANGESTEP);
        } else {
            lastSpHighConsumption = idealHigh;
        }
        sp_high.play(lastSpHighConsumption);

//        if (avgConsumption < THRESHOLD_NORMAL) {
//            if (lastSpNormalConsumption > avgConsumption / THRESHOLD_NORMAL + changeStep) {
//                lastSpNormalConsumption -= changeStep;
//            } else if (lastSpNormalConsumption < avgConsumption / THRESHOLD_NORMAL - changeStep) {
//                lastSpNormalConsumption += changeStep;
//            } else {
//                lastSpNormalConsumption = avgConsumption / THRESHOLD_NORMAL;
//            }
//
//            sp_normal.play(lastSpNormalConsumption);
//
//            lastSpHighConsumption = Math.max(0, lastSpHighConsumption - changeStep);
//            sp_high.play(lastSpHighConsumption);
//
//        } else if (avgConsumption < THRESHOLD_HIGH) {
//            final double idealNormal = (THRESHOLD_HIGH - avgConsumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
//            final double idealHigh = 1 - idealNormal;
//            if (lastSpNormalConsumption > idealNormal + changeStep) {
//                lastSpNormalConsumption -= changeStep;
//            } else if (lastSpNormalConsumption < idealNormal - changeStep) {
//                lastSpNormalConsumption += changeStep;
//            } else {
//                lastSpNormalConsumption = idealNormal;
//            }
//
////            lastSpNormalConsumption = (THRESHOLD_HIGH - avgConsumption) / (THRESHOLD_HIGH - THRESHOLD_NORMAL);
//            sp_normal.play(lastSpNormalConsumption);
//
//            
//            if (lastSpHighConsumption > idealHigh + changeStep) {
//                lastSpHighConsumption -= changeStep;
//            } else if (lastSpHighConsumption < idealHigh - changeStep) {
//                lastSpHighConsumption += changeStep;
//            } else {
//                lastSpHighConsumption = idealHigh;
//            }
//
////            lastSpHighConsumption = 1 - lastSpNormalConsumption;
//            sp_high.play(lastSpHighConsumption);
//
//        } else if (avgConsumption < THRESHOLD_EXTREME) {
////            sp_high.play(1);
//
//            lastSpHighConsumption = Math.min(1, lastSpHighConsumption + changeStep);
//            sp_normal.play(Math.min(1, lastSpHighConsumption));
//            lastSpNormalConsumption = Math.max(0, lastSpNormalConsumption - changeStep);
//            sp_normal.play(lastSpNormalConsumption);
//
//        } else {
//
//            lastSpNormalConsumption = Math.max(0, lastSpNormalConsumption - changeStep);
//            sp_normal.play(lastSpNormalConsumption);
//            sp_high.play(1);
//            sp_extreme.play(1);
//        }
//        System.out.println("Avg con: " + avgConsumption + " (ideal: " + consumption + "), normal: " + lastSpNormalConsumption + " ("
//                + idealNormal + "), high: " + lastSpHighConsumption + "(" + idealHigh + ")");
//        System.out.println(CHANGESTEP + " " + TIMEFRAME + " " + ALPHA);

    }

    private double expAverage(double value) {
        double newValue = lastValue + ALPHA * (value - lastValue);
        lastValue = newValue;
        return newValue;
    }

    public static void setThresholds(double normal, double high, double extreme, int time, double step, double smoothingFactor) {
        THRESHOLD_NORMAL = normal;
        THRESHOLD_HIGH = high;
        THRESHOLD_EXTREME = extreme;
        ALPHA = smoothingFactor;
        CHANGESTEP = step;
        TIMEFRAME = time;
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
