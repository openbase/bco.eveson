/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

/**
 *
 * @author jplettemeier
 */
public class PlayerConfig {
    private final String scope;
    private final String sampleFile;
    private final ScopePlayer.Type type;

    public PlayerConfig(String scope, String sampleFile, ScopePlayer.Type type) {
        this.scope = scope;
        this.sampleFile = sampleFile;
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public String getSampleFile() {
        return sampleFile;
    }

    public ScopePlayer.Type getType() {
        return type;
    }
    
}
