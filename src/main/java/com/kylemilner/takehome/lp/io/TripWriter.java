package com.kylemilner.takehome.lp.io;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Component;
import com.kylemilner.takehome.lp.model.TripDTO;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TripWriter {

    public void writeTrips(List<TripDTO> trips) throws Exception {
        log.info("Writing trips.csv");
        try (Writer writer = new FileWriter("trips.csv")) {
            var mappingStrategy = new ColumnPositionMappingStrategyBuilder<TripDTO>().build();
            mappingStrategy.setType(TripDTO.class);
            new StatefulBeanToCsvBuilder<TripDTO>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build().write(trips);
            writer.flush();
        }
    }
}
