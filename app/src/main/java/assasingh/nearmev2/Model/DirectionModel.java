package assasingh.nearmev2.Model;

/**
 * Created by Assa on 22/03/2017.
 */

public class DirectionModel {

    String polyLine;
    String startAddress;
    String endAddress;
    String duration;
    String distance;

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getPolyLine() {
        return polyLine;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public void setPolyLine(String polyLine) {
        this.polyLine = polyLine;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }
}
