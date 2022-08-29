package com.kylemilner.takehome.lp.io;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Component;
import com.kylemilner.takehome.lp.model.Tap;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TapReader {

    public List<Tap> readTaps() throws URISyntaxException, IOException {
        log.info("Reading taps.csv");
        try (Reader reader = Files.newBufferedReader(Paths.get("taps.csv"))) {
            return new CsvToBeanBuilder<Tap>(reader).withType(Tap.class).build().parse();
        }
    }
}
