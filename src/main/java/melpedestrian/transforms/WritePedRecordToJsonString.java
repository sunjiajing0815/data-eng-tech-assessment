package melpedestrian.transforms;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import melpedestrian.schemas.PedestrianRecord;
import melpedestrian.schemas.SensorLocationRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

/**
 * A Transform that converts a PCollection containing contains one list of PedestrianRecord objects
 * into a PCollection of one Json string to be written out to a text file
 *
 */
public class WritePedRecordToJsonString extends
    PTransform<PCollection<List<PedestrianRecord>>, PCollection<String>> {
  @Override
  public PCollection<String> expand(PCollection<List<PedestrianRecord>> perRecordList) {

    // Convert lines of text into individual words.
    PCollection<String> pedRecordJsonString = perRecordList.apply(
        ParDo.of(new WritePedRecordToJsonString.PedRecordJsonFn()));

    return pedRecordJsonString;
  }

  static class PedRecordJsonFn extends DoFn<List<PedestrianRecord>, String> {

    @ProcessElement
    public void processElement(@Element List<PedestrianRecord> perRecordList, OutputReceiver<String> receiver) {

      // Split the line into words.
      Gson gson = new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
      String recordJsonString = gson.toJson(perRecordList);
      receiver.output(recordJsonString);
    }
  }

}
