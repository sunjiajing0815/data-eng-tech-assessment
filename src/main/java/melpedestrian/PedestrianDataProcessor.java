package melpedestrian;// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to You under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


import static melpedestrian.transforms.EnrishPedestrainData.enrichPedestrianRecord;

import java.util.List;
import java.util.Map;
import melpedestrian.options.PedestrianDataOptions;
import melpedestrian.schemas.PedestrianRecord;
import melpedestrian.schemas.SensorLocationRecord;
import melpedestrian.transforms.CombinePedRecord;
import melpedestrian.transforms.LoadPedRecord;
import melpedestrian.transforms.LoadSensorRecord;
import melpedestrian.transforms.WritePedRecordToJsonString;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PedestrianDataProcessor{

  private static final Logger LOG = LoggerFactory.getLogger(PedestrianDataProcessor.class);

  public static void main(String[] args) {

    PedestrianDataOptions options = PipelineOptionsFactory.fromArgs(args).create().as(
        PedestrianDataOptions.class);
    Pipeline pipeline = Pipeline.create(options);

    // 1. Load pedestrian-counting-system-monthly-counts-per-hour.json into pedRecords
    PCollection<PedestrianRecord> pedRecords = pipeline.apply("ReadPedestrianRecordJSON", TextIO.read().from(options.getPedJsonFile()))
        .apply(new LoadPedRecord());
    // 2. Load pedestrian-counting-system-sensor-locations.json into sensorRecords as map
    PCollection<KV<String, SensorLocationRecord>> sensorRecords = pipeline.apply("ReadSensorRecordJSON", TextIO.read().from(options.getSensorJsonFile()))
        .apply(new LoadSensorRecord());
    // 3. Load sensorRecords into sensorLocationView as materialized view
    PCollectionView<Map<String, SensorLocationRecord>> sensorLocationView = sensorRecords.apply(View.asMap());
    // 4. Enrich pedRecords with sensorLocationView
    PCollection<PedestrianRecord> output = enrichPedestrianRecord(pedRecords, sensorLocationView);
    // 5. Combine all pedRecords into one list
    PCollection<List<PedestrianRecord>> PedRecordList = output.apply(Combine.globally(new CombinePedRecord()));
    // 6. Dump PedestrianRecord List into Json and write to output file
    PedRecordList.apply("LoadResultToJsonString", new WritePedRecordToJsonString())
        .apply("WriteResult", TextIO.write().withSuffix(".json").to(options.getOutput()));

    pipeline.run().waitUntilFinish();;
  }



  static class LogOutput<T> extends DoFn<T, T> {
    private String prefix;

    LogOutput() {
      this.prefix = "Processing element";
    }

    LogOutput(String prefix) {
      this.prefix = prefix;
    }

    @ProcessElement
    public void processElement(ProcessContext c) throws Exception {
      LOG.info(prefix + ": {}", c.element());
    }
  }
}