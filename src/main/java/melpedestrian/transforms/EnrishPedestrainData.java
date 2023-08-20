package melpedestrian.transforms;

import java.util.Map;
import melpedestrian.schemas.PedestrianRecord;
import melpedestrian.schemas.SensorLocationRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;

/**
 * EnrishPedestrainData is a class that contains a static function that applies transformations on
 * [PedestrianRecord] and [sensorLocationView] to enrich PedestrianRecord with location name
 */
public class EnrishPedestrainData {
  public static PCollection<PedestrianRecord> enrichPedestrianRecord(
      PCollection<PedestrianRecord> pedRecords, PCollectionView<Map<String, SensorLocationRecord>> sensorLocationView) {

    return pedRecords.apply(ParDo.of(new DoFn<PedestrianRecord, PedestrianRecord>() {
      // Get city from person and get from city view
      @ProcessElement
      public void processElement(@Element PedestrianRecord pedRecord, OutputReceiver<PedestrianRecord> out,
          ProcessContext context) {
        Map<String, SensorLocationRecord> sensorLocation = context.sideInput(sensorLocationView);
        SensorLocationRecord sensor;
        String locationid = pedRecord.getLocationid();
        if(sensorLocation.containsKey(locationid)) {
          sensor = sensorLocation.get(locationid);
          pedRecord.setLocationName(sensor.getLocationName());
        }
        out.output(pedRecord);
      }

    }).withSideInputs(sensorLocationView));
  }

}
