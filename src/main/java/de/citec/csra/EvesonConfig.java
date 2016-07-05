/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import java.util.ArrayList;

/**
 *
 * @author mgao
 */
public class EvesonConfig {

    private final ArrayList<PlayerConfig> playerConfigList;

    public EvesonConfig() {
        this.playerConfigList = new ArrayList<>();
    }

    public ArrayList<PlayerConfig> getPlayerConfigList() {
        return playerConfigList;
    }

}
