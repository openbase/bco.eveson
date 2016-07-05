package de.citec.csra.remotes;

import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import rst.spatial.LocationDataType.LocationData;

/**
 *
 * @author mgao
 */
public class LocationObserver implements Observer<LocationData>{
    
    int THRESHOLD_NORMAL = 150;
    int THRESHOLD_HIGH = 600; // ?
    int THRESHOLD_EXTREME = 3000;
    

    @Override
    public void update(Observable<LocationData> source, LocationData data) throws Exception {
        System.out.println("---- Location ----");
        // auf "current" hören
        System.out.println("Power Consumption: " + data.getPowerConsumptionState());
        System.out.println("Temperature: " + data.getTemperature());
    }
    
}
