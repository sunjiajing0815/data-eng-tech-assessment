import static melpedestrian.transforms.EnrishPedestrainData.enrichPedestrianRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import melpedestrian.schemas.PedestrianRecord;
import melpedestrian.schemas.SensorLocationRecord;
import melpedestrian.PedestrianDataProcessor;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests of WritePedRecordToJsonString. */
@RunWith(JUnit4.class)
public class PedestrianDataProcessorTest {
  @Rule
  public TestPipeline p = TestPipeline.create();
  static final List<PedestrianRecord> pedRecordList = Arrays.asList(
    new PedestrianRecord("2023-08-16T11:00:00+00:00", "2"),
    new PedestrianRecord("2023-08-17T12:00:00+00:00", "4"),
    new PedestrianRecord("2023-08-17T16:00:00+00:00", "6"),
    new PedestrianRecord("2023-08-13T13:00:00+00:00", "8"));

  static final List<PedestrianRecord> pedRecordTargetList = Arrays.asList(
      new PedestrianRecord("2023-08-16T11:00:00+00:00", "2", "Bourke Street Mall (South)"),
      new PedestrianRecord("2023-08-17T12:00:00+00:00", "4", "Town Hall (West)"),
      new PedestrianRecord("2023-08-17T16:00:00+00:00", "6", "Flinders Street Station Underpass"),
      new PedestrianRecord("2023-08-13T13:00:00+00:00", "8", "Webb Bridge"));

  static final List<KV<String, SensorLocationRecord>> sensorLocationMap = Arrays.asList(
      KV.of("2", new SensorLocationRecord("2", "Bourke Street Mall (South)")),
      KV.of("4", new SensorLocationRecord("4", "Town Hall (West)")),
      KV.of("6", new SensorLocationRecord("6", "Flinders Street Station Underpass")),
      KV.of("8", new SensorLocationRecord("8", "Webb Bridge"))
  );
  @Test
  public void testEnrichPedestrianRecord() throws Exception {
    PCollection<PedestrianRecord> pedRecords = p.apply("LoadPedRecords",Create.of(pedRecordList));
    PCollection<KV<String, SensorLocationRecord>> sensorMaps = p.apply("LoadSensorRecords",Create.of(sensorLocationMap));
    PCollectionView<Map<String, SensorLocationRecord>> sensorLocationView = sensorMaps.apply(
        View.asMap());
    PCollection<PedestrianRecord> output = enrichPedestrianRecord(pedRecords, sensorLocationView);
    PAssert.that(output).containsInAnyOrder(pedRecordTargetList);
    PCollection<Long> count = output.apply(Count.globally());
    PAssert.that(count).containsInAnyOrder((Long.valueOf(4)));
    p.run().waitUntilFinish();
  }

}
