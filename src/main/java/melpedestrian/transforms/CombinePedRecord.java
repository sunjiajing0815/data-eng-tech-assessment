package melpedestrian.transforms;

import java.util.ArrayList;
import java.util.List;
import melpedestrian.schemas.PedestrianRecord;
import org.apache.beam.sdk.transforms.Combine.CombineFn;

public class CombinePedRecord extends CombineFn<PedestrianRecord, List<PedestrianRecord>, List<PedestrianRecord>> {

  @Override
  public List<PedestrianRecord> createAccumulator() { return new ArrayList<>(); }

  @Override
  public List<PedestrianRecord> addInput(List<PedestrianRecord> accum, PedestrianRecord input) {
    accum.add(input);
    return accum;
  }

  @Override
  public List<PedestrianRecord> mergeAccumulators(Iterable<List<PedestrianRecord>> accums) {
    List<PedestrianRecord> merged = new ArrayList<>();
    for (List<PedestrianRecord> accum : accums) {
      merged.addAll(accum);
    }
    return merged;
  }

  @Override
  public List<PedestrianRecord> extractOutput(List<PedestrianRecord> accum) {
    return accum;
  }
}
