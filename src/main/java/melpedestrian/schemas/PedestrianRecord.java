package melpedestrian.schemas;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class PedestrianRecord implements Serializable{

  private String timestamp;
  private String locationid;
  @SerializedName("direction_1")
  private Integer direction1;
  @SerializedName("direction_2")
  private Integer direction2;
  @SerializedName("total_of_directions")
  private Integer totalOfDirections;
  @SerializedName("location_name")
  private String locationName;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PedestrianRecord pedRecord = (PedestrianRecord) o;

    return Objects.equals(timestamp, pedRecord.timestamp) &&
        Objects.equals(locationid, pedRecord.locationid) &&
        Objects.equals(direction1, pedRecord.direction1) &&
        Objects.equals(direction2, pedRecord.direction2) &&
        Objects.equals(totalOfDirections, pedRecord.totalOfDirections);
  }
  @Override
  public String toString() {
    return "{" +
        "timestamp='" + timestamp + '\'' +
        ", location_id='" + locationid + '\'' +
        ", location_name='" + locationName + '\'' +
        ", direction_1='" + direction1 + '\'' +
        ", direction_2='" + direction2 + '\'' +
        ", total_of_directions='" + totalOfDirections + '\'' +
        '}';
  }
  public PedestrianRecord(String timestamp, String locationid, String locationName){
    this.timestamp = timestamp;
    this.locationid = locationid;
    this.locationName = locationName;
  }
  public PedestrianRecord(String timestamp, String locationid){
    this.timestamp = timestamp;
    this.locationid = locationid;
  }
}
