/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openbase.bco.eveson.eventfilter;

import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.devices.sensfloor.FloorModuleStateType;
import rst.devices.sensfloor.FloorModuleStateType.FloorModuleState;

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
