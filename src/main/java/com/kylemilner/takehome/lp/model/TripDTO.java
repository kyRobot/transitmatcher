package com.kylemilner.takehome.lp.model;

import java.time.LocalDateTime;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    @CsvBindByName(column = "Started")
    @CsvBindByPosition(position = 0)
    @CsvDate("dd-MM-yyyy HH:mm:ss")
    private LocalDateTime started;

    @CsvBindByName(column = "Finished")
    @CsvBindByPosition(position = 1)
    @CsvDate("dd-MM-yyyy HH:mm:ss")
    private LocalDateTime finished;

    @CsvBindByName(column = "DurationSecs")
    @CsvBindByPosition(position = 2)
    private Long durationSeconds;

    @CsvBindByName(column = "FromStopId")
    @CsvBindByPosition(position = 3)
    private String fromStopId;

    @CsvBindByName(column = "ToStopId")
    @CsvBindByPosition(position = 4)
    private String toStopId;

    @CsvBindByName(column = "ChargeAmount")
    @CsvBindByPosition(position = 5)
    private String chargeAmount;

    @CsvBindByName(column = "CompanyId")
    @CsvBindByPosition(position = 6)
    private String companyId;

    @CsvBindByName(column = "BusID")
    @CsvBindByPosition(position = 7)
    private String busId;

    @CsvBindByName(column = "PAN")
    @CsvBindByPosition(position = 8)
    private String pan;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 9)
    private TripType status;
}
