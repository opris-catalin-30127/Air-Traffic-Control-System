package isp.lab10.exercise1;

class TakeoffCommand extends AtcCommand {
    private Integer altitude;

    public TakeoffCommand(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getAltitude() {
        return this.altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }
}
