package melpedestrian.schemas;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class SensorLocationRecord implements Serializable {
    @SerializedName("location_id")
    private String locationid;
    @SerializedName("sensor_description")
    private String locationName;
    @SerializedName("sensor_name")
    private String sensorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorLocationRecord pedRecord = (SensorLocationRecord) o;

        return Objects.equals(locationid, pedRecord.locationid) &&
            Objects.equals(sensorName, pedRecord.sensorName);
    }
    @Override
    public String toString() {
        return "SensorLocationRecord{" +
            "location_id='" + locationid + '\'' +
            ", location_name='" + locationName + '\'' +
            ", sensor_name='" + sensorName + '\'' +
            '}';
    }

    public SensorLocationRecord(String locationid, String locationName){
        this.locationid = locationid;
        this.locationName = locationName;
    }
}
