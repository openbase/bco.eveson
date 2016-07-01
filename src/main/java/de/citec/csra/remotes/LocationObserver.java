package de.citec.csra.remotes;

import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import rst.spatial.LocationDataType.LocationData;

/**
 *
 * @author mgao
 */
public class LocationObserver implements Observer<LocationData>{

    @Override
    public void update(Observable<LocationData> source, LocationData data) throws Exception {
        
//        System.out.println("---- Location ----");
//        System.out.println("Power Consumption: " + data.getPowerConsumptionState().getConsumption());
//        System.out.println("Temperature: " + data.getTemperature());
    }
    
}
