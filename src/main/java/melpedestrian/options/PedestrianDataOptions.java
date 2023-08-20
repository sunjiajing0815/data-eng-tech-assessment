package melpedestrian.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface PedestrianDataOptions extends PipelineOptions{
  /**
   * By default, this example reads from a public dataset containing the text of King Lear. Set
   * this option to choose a different input file or glob.
   */
  @Description("Path of the file to read from")
  @Default.String("source-data/pedestrian/pedestrian-counting-system-monthly-counts-per-hour.json")
  String getPedJsonFile();

  void setPedJsonFile(String value);

  @Description("Path of the file to read from")
  @Default.String("source-data/pedestrian/pedestrian-counting-system-sensor-locations.json")
  String getSensorJsonFile();

  void setSensorJsonFile(String value);

  /** Set this required option to specify where to write the output. */
  @Description("Path of the file to write to")
  @Default.String("output-data/pedestrian-counting-system-monthly-counts-per-hour-w-location")
  String getOutput();

  void setOutput(String value);

}
