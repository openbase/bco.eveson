/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openbase.bco.eveson.eventfilter;

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

import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import org.openbase.type.device.sensfloor.FloorModuleStateType;
import org.openbase.type.device.sensfloor.FloorModuleStateType.FloorModuleState;

/**
 *
 * @author mgao
 */
public class FloorModuleStateFilter implements EventFilter<FloorModuleState> {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<FloorModuleStateType.FloorModuleState>(FloorModuleStateType.FloorModuleState.getDefaultInstance()));
    }

    @Override
    public boolean isCompatible(Object o) {
        return (o != null && o instanceof FloorModuleState);
    }

    @Override
    public boolean filter(Object data) {
        if (data == null) {
            return true;
        }

        if (!(data instanceof FloorModuleState)) {
            return true;
        }
//        System.out.println("ming filterd: "+!((FloorModuleState) data).getSegmentList().stream().anyMatch((seg) -> (seg.getActivationSegment())));
//        System.out.println("marian filterd: "+((FloorModuleState) data).getSegmentList().stream().allMatch((seg) -> (seg.getActivationSegment())));
        return !((FloorModuleState) data).getSegmentList().stream().anyMatch((seg) -> (seg.getActivationSegment()));
    }

    @Override
    public void getDataDefaultInstance() {
        FloorModuleState.getDefaultInstance();
    }

}
