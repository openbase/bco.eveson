package org.openbase.bco.eveson;

import java.util.ArrayList;

/**
 * Configuration for Eveson.
 * @author mgao
 */
public class EvesonConfig {

    /**
     * List of configurations for individual scopes.
     */
    private final ArrayList<PlayerConfig> playerConfigList;
    /**
     * Threshold for normal power consumption.
     */
    public  double PowerConsumptionThresholdNormal; 
    /**
     * Threshold for high power consumption.
     */
    public  double PowerConsumptionThresholdHigh; 
    /**
     * Threshold for very high power consumption.
     */
    public  double PowerConsumptionThresholdExtreme;     
    /**
     * Default number of voices.
     */
    public int defaultVoices; 
    
    public int timeframe; 
    public double alpha;
    public double changestep;
    
    public EvesonConfig() {
        this.playerConfigList = new ArrayList<>();
        
    }

    public ArrayList<PlayerConfig> getPlayerConfigList() {
        return playerConfigList;
    }

    public double getPowerConsumptionThresholdNormal() {
        return PowerConsumptionThresholdNormal;
    }

    public double getPowerConsumptionThresholdHigh() {
        return PowerConsumptionThresholdHigh;
    }

    public double getPowerConsumptionThresholdExtreme() {
        return PowerConsumptionThresholdExtreme;
    }

    public void setPowerConsumptionThresholdNormal(double PowerConsumptionThresholdNormal) {
        this.PowerConsumptionThresholdNormal = PowerConsumptionThresholdNormal;
    }

    public void setPowerConsumptionThresholdHigh(double PowerConsumptionThresholdHigh) {
        this.PowerConsumptionThresholdHigh = PowerConsumptionThresholdHigh;
    }

    public void setPowerConsumptionThresholdExtreme(double PowerConsumptionThresholdExtreme) {
        this.PowerConsumptionThresholdExtreme = PowerConsumptionThresholdExtreme;
    }

    public int getDefaultVoices() {
        return defaultVoices;
    }

    public void setDefaultVoices(int defaultVoices) {
        this.defaultVoices = defaultVoices;
    }

    public int getTimeframe() {
        return timeframe;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getChangestep() {
        return changestep;
    }

}
