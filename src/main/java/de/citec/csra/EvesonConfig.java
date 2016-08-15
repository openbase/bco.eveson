package de.citec.csra;

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

}
