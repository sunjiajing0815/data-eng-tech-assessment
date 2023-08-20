package melpedestrian.transforms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import melpedestrian.schemas.PedestrianRecord;
import melpedestrian.transforms.WritePedRecordToJsonString.PedRecordJsonFn;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
/** Tests of WritePedRecordToJsonString. */
@RunWith(JUnit4.class)
public class WritePedRecordToJsonStringTest {
  @Rule public TestPipeline p = TestPipeline.create();

  @Test
  public void testPedRecordJsonFn() throws Exception {
    ArrayList<PedestrianRecord> pedRecordList = new ArrayList();
    pedRecordList.add(new PedestrianRecord("2023-08-16T11:00:00+00:00", "2", "Bourke Street Mall (South)"));
    pedRecordList.add(new PedestrianRecord("2023-08-17T12:00:00+00:00", "4", "Town Hall (West)"));
    pedRecordList.add(new PedestrianRecord("2023-08-17T16:00:00+00:00", "6", "Flinders Street Station Underpass"));
    pedRecordList.add(new PedestrianRecord("2023-08-13T13:00:00+00:00", "8", "Webb Bridge"));
    final List<List<PedestrianRecord>> pedRecords = Arrays.asList(pedRecordList);
    PCollection<Long> output =
        p.apply(Create.of(pedRecords))
            .apply(new WritePedRecordToJsonString()).
            apply(Count.globally());
    PAssert.that(output).containsInAnyOrder((Long.valueOf(1)));
    p.run().waitUntilFinish();
  }

}
