package org.openbase.bco.eveson.remotes;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2019 openbase.org
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
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observer;
import static org.openbase.type.domotic.state.MotionStateType.MotionState.State.MOTION;

import org.openbase.jul.pattern.provider.DataProvider;
import org.openbase.type.domotic.unit.dal.MotionDetectorDataType.MotionDetectorData;

/**
 *
 * @author mgao
 */
public class MotionSensorObserver implements Observer<DataProvider<MotionDetectorData>, MotionDetectorData> {

    private ScopePlayer sp;
    private String sampleFile;
    private final String id;

    public MotionSensorObserver(String id) throws InstantiationException {
        final Map<String, ScopePlayer> scopeSampleMap = EventPlayer.getInstance().getScopeSampleMap();
        this.id = id;
        if (scopeSampleMap.containsKey(id)) {
            final ScopePlayer player = scopeSampleMap.get(id);
            sampleFile = player.getSampleFile();

            sp = new ScopePlayer(sampleFile, ScopePlayer.Type.PLAY, player.getMAX_VOICES(), player.getRelativeAmplitude(), null);
        }
    }

    @Override
    public void update(DataProvider<MotionDetectorData> source, MotionDetectorData data) throws Exception {

//        System.out.println("MotionSensorObserver: " + data.getMotionState().getValue() + " in " + id);
        if (data.getMotionState().getValue().equals(MOTION)) {
//            System.out.println("Motion Sensor:" + sampleFile);
            sp.play(1);
        }
    }

}
