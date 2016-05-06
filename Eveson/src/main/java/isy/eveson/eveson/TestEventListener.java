/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import java.util.EventListener;

/**
 * Dummy listener for dummy event.
 * @author mgao
 */
public interface TestEventListener extends EventListener{
        public void eventReceived( TestEvent event );
}
   
