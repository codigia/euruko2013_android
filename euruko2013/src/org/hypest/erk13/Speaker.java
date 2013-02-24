package org.hypest.erk13;

public class Speaker {
    public String id;
    public String name;
    public String title;
    public int avatarId;
    public int avatarBigId;
    public String bio;
    
    public Speaker(String id, String name, String title, int avatarId,
            int avatarBigId, String bio) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.avatarId = avatarId;
        this.avatarBigId = avatarBigId;
        this.bio = bio;
    }
}
