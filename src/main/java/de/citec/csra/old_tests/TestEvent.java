/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.old_tests;

import java.util.EventObject;

/**
 * Dummy event.
 * @author mgao
 */
public class TestEvent extends EventObject{

    private char ch; 

    public TestEvent(Object source, char k) {
        super(source);
        this.ch = k;
    }

    public char getCh() {
        return ch;
    }

    

    
}
