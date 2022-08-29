# LP Takehome

Takehome challenge for a simplified / toy transit network trip matching exercise

## How to Run

- checkout the repo to `~/wherever/you/like/lp`
- Replace the file `taps.csv` in the root directory with the desired input. Header values must match.
- To see test results execute `./gradlew build` from the repository root directory
- execute `./gradlew bootRun` from the repository root directory
- output will be written to `trips.csv`

## Project Assumptions

### Matching

- A Completed trip has two taps {ON,OFF} where the on stop is different to the off stop
- A Cancelled trip is any {ON,OFF} pairing at the same stop
- An Incomplete trip is one with an ON but no OFF
- An OFF without an ON is ignored
- Trip duration has no impact on matching i.e a long delay between an ON & OFF at the same stop is still cancelled

### Pricing

- Cancelled Trips have Zero Cost
- A trip with any unknown stop is unpriced

## Data

- input data is read from `taps.csv` relative to the project root, an example taps.csv is provided in the read location
- output data is written to `trips.csv` relative to the project root, an example trips.csv is provided in the write location
- output gripe: the header row isn't generated / is missing
