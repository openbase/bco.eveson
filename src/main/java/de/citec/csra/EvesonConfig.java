/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import java.util.ArrayList;

/**
 *
 * @author mgao
 */
public class EvesonConfig {

    private final ArrayList<PlayerConfig> playerConfigList;
    public  double PowerConsumptionThresholdNormal; 
    public  double PowerConsumptionThresholdHigh; 
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
