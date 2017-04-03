/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openbase.bco.eveson.eventfilter;

/**
 *
 * @author mgao
 */
public interface EventFilter<E> {
    
    public void getDataDefaultInstance();
    
    public boolean isCompatible(Object o);
    
    public boolean filter(final Object event);
}
