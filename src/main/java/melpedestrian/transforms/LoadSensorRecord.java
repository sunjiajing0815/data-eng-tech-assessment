package melpedestrian.transforms;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import melpedestrian.schemas.SensorLocationRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class LoadSensorRecord extends
    PTransform<PCollection<String>, PCollection<KV<String, SensorLocationRecord>>> {
  @Override
  public PCollection<KV<String, SensorLocationRecord>> expand(PCollection<String> lines) {

    // Convert lines of text into individual words.
    PCollection<KV<String, SensorLocationRecord>> sensorRecords = lines.apply(
        ParDo.of(new LoadSensorRecord.ExtractSensorRecordsFn()));

    return sensorRecords;
  }
  static class ExtractSensorRecordsFn extends DoFn<String, KV<String, SensorLocationRecord>> {

    @ProcessElement
    public void processElement(@Element String element, OutputReceiver<KV<String, SensorLocationRecord>> receiver) {

      // Split the line into words.
      Gson gson = new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
      SensorLocationRecord[] sensorRecArray = gson.fromJson(element, SensorLocationRecord[].class);

      // Output each word encountered into the output PCollection.
      for (SensorLocationRecord r : sensorRecArray) {
        receiver.output(KV.of(r.getLocationid(), r));
      }
    }
  }

}
