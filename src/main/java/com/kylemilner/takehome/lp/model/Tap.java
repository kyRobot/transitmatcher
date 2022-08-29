package com.kylemilner.takehome.lp.model;

import java.time.LocalDateTime;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tap {

    @CsvBindByName(column = "ID")
    private int id;

    @CsvBindByName(column = "DateTimeUTC")
    @CsvDate("dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTimeUTC;

    @CsvBindByName(column = "TapType")
    private TapType tapType;

    @CsvBindByName(column = "StopId")
    private String stopId;

    @CsvBindByName(column = "CompanyId")
    private String companyId;

    @CsvBindByName(column = "BusID")
    private String busId;

    @CsvBindByName(column = "PAN")
    private String pan;

}
