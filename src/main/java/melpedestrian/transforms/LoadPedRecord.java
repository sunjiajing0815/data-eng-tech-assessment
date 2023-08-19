package melpedestrian.transforms;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import melpedestrian.schemas.PedestrianRecord;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class LoadPedRecord extends PTransform<PCollection<String>, PCollection<PedestrianRecord>> {
  @Override
  public PCollection<PedestrianRecord> expand(PCollection<String> lines) {

    // Convert lines of text into individual words.
    PCollection<PedestrianRecord> pedRecords = lines.apply(ParDo.of(new ExtractPedRecordsFn()));

    return pedRecords;
  }
  static class ExtractPedRecordsFn extends DoFn<String, PedestrianRecord> {

    @ProcessElement
    public void processElement(@Element String element, OutputReceiver<PedestrianRecord> receiver) {

      // Split the line into words.
      Gson gson = new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
      PedestrianRecord[] pedRecArray = gson.fromJson(element, PedestrianRecord[].class);

      // Output each word encountered into the output PCollection.
      for (PedestrianRecord r : pedRecArray) {
         if(r != null) {
           receiver.output(r);
         }
      }
    }
  }
}
