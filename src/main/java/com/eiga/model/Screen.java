package com.eiga.model;
public class Screen {
    private String screenId, theatreId, name, format, projection, soundSystem, status;
    private int capacity;
    public String getScreenId() { return screenId; } public void setScreenId(String screenId) { this.screenId = screenId; }
    public String getTheatreId() { return theatreId; } public void setTheatreId(String theatreId) { this.theatreId = theatreId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getFormat() { return format; } public void setFormat(String format) { this.format = format; }
    public String getProjection() { return projection; } public void setProjection(String projection) { this.projection = projection; }
    public String getSoundSystem() { return soundSystem; } public void setSoundSystem(String soundSystem) { this.soundSystem = soundSystem; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public int getCapacity() { return capacity; } public void setCapacity(int capacity) { this.capacity = capacity; }
}
