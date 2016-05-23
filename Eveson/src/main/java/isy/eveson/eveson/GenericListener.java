/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isy.eveson.eveson;

import java.util.logging.Level;
import java.util.logging.Logger;
import rsb.Event;
import rsb.Factory;
import rsb.Handler;
import rsb.InitializeException;
import rsb.Listener;
import rsb.RSBException;

/**
 *
 * @author jplettemeier
 */
public class GenericListener {

    private final String audioSample;
    private final String scope;
    private final Listener listener;

    public GenericListener(final String scope, final String audioSample) throws InitializeException, InterruptedException, RSBException {
        this.scope = scope;
        this.audioSample = audioSample;
        
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    shutdown();
                } catch (RSBException ex) {
                    Logger.getLogger(GenericListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GenericListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        this.listener = Factory.getInstance().createListener(scope, RSBGenericConverterConfig.generateConfig());

        
        
        listener.addHandler(new Handler() {

            @Override
            public void internalNotify(Event event) {
                System.out.println("Play Sample["+audioSample+"] for Scope["+scope+"]");
            }
        }, true);

        listener.activate();
        System.out.println("Listener activated for Scope["+scope+"].");
    }

    public String getScope() {
        return scope;
    }
    
    public void shutdown() throws RSBException, InterruptedException {
        if(listener == null) {
            return;
        }
        
        listener.deactivate();
    }
}
