
package de.citec.csra;

/**
 *
 * @author jplettemeier
 */
public class PlayerConfig {
    private String id;
    private String sampleFile;
    private ScopePlayer.Type type;
    private int maxVoices;

    public PlayerConfig() {
    }
    public PlayerConfig(String id, String sampleFile, ScopePlayer.Type type,int maxVoices) {
        this.id = id;
        this.sampleFile = sampleFile;
        this.type = type;
        this.maxVoices = maxVoices;
    }

    public String getId() {
        return id;
    }

    public String getSampleFile() {
        return sampleFile;
    }

    public ScopePlayer.Type getType() {
        return type;
    }

    public int getMaxVoices() {
        return maxVoices;
    }

    
}
