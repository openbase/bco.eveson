/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mgao
 */
public class EventGenerator {
    
    private List listeners = new ArrayList();
    
    public synchronized void fireEvent(char ch) {
        System.out.println("fireEvent");
        System.out.println(ch);
        TestEvent newEvent = new TestEvent(this, ch);
        Iterator l = listeners.iterator();
        while( l.hasNext() ) {
            ((TestEventListener) l.next()).eventReceived(newEvent);
        }
    }
    
    public synchronized void addEventListener(TestEventListener listener) {
        listeners.add(listener);
    }

}
