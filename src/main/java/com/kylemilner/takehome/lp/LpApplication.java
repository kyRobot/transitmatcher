package com.kylemilner.takehome.lp;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.kylemilner.takehome.lp.balancing.TapBalancer;
import com.kylemilner.takehome.lp.io.TapReader;
import com.kylemilner.takehome.lp.io.TripWriter;
import com.kylemilner.takehome.lp.matching.TripMatcher;
import com.kylemilner.takehome.lp.model.PricedTrip;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;
import com.kylemilner.takehome.lp.model.Trip;
import com.kylemilner.takehome.lp.model.TripDTO;
import com.kylemilner.takehome.lp.pricing.MoneyUtils;
import com.kylemilner.takehome.lp.pricing.TripPricer;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class LpApplication implements CommandLineRunner {

	@Autowired
	private TapReader tapReader;

	@Autowired
	private TripWriter tripWriter;

	@Autowired
	private TapBalancer tapBalancer;

	@Autowired
	private TripMatcher tripMatcher;

	@Autowired
	private TripPricer tripPricer;

	public static void main(String[] args) {
		log.info("Starting up Takehome :: LP");
		SpringApplication.run(LpApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Matching Taps...");
		List<Tap> taps = tapReader.readTaps();
		TapBalanceResult balanced = tapBalancer.balance(taps);
		List<Trip> trips = tripMatcher.matchTripsFromTaps(balanced);
		List<PricedTrip> pricedTrips = tripPricer.priceTrips(trips);
		List<TripDTO> output = pricedTrips.stream().map(pt -> {
			Trip t = pt.getTrip();
			Tap on = t.getTapOn();
			var dto = new TripDTO();
			dto.setPan(t.getPan());
			dto.setStarted(on.getDateTimeUTC());
			dto.setFromStopId(on.getStopId());
			dto.setBusId(on.getBusId());
			dto.setCompanyId(on.getCompanyId());
			dto.setStatus(t.getType());
			t.getTapOff().ifPresent(off -> {
				dto.setFinished(off.getDateTimeUTC());
				dto.setToStopId(off.getStopId());
				dto.setDurationSeconds(
						ChronoUnit.SECONDS.between(on.getDateTimeUTC(), off.getDateTimeUTC()));
			});
			pt.getPrice().ifPresent(price -> {
				dto.setChargeAmount(MoneyUtils.formatPriceForDollars(price));
			});
			return dto;
		}).collect(Collectors.toList());
		tripWriter.writeTrips(output);

	}

}
