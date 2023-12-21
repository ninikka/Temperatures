package fop.w9temp;

import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTemperatures extends Temperatures {
    public StreamTemperatures(String filepath) {
        super(filepath);
    }

    public long size() {
        return stream().count();
    }

    public List<LocalDate> dates() {
        return stream().map(Temperature::getDate).distinct().sorted().collect(Collectors.toList());
    }

    public Set<String> cities() {
        return stream().map(Temperature :: getCity).collect(Collectors.toSet());
    }

    public Set<String> countries() {
        return stream().map(Temperature :: getCountry).collect(Collectors.toSet());
    }

    public Map<String, List<Double>> temperaturesByCountry() {
        Map<String, List<Double>> res = new HashMap<>();

        countries().forEach(x ->res.put(x, new ArrayList<>()));
        stream().forEach(x -> res.get(x.getCountry()).add(x.getTemperature()));

        return res;
    }

    @Override
    public String coldestCountryAbs() {
        return stream().min(Comparator.comparing(Temperature::getTemperature)).get().getCountry();
    }

    @Override
    public String hottestCountryAbs() {
        return stream().max(Comparator.comparing(Temperature::getTemperature)).get().getCountry();
    }

    @Override
    public Map<String, Double> countriesAvgTemperature() {
        Map<String, Double> res = new HashMap<>();

        Set<String> contries = new HashSet<>();
        stream().forEach(x -> contries.add(x.getCountry()));

        contries.stream().forEach(c -> {
            List<Double> temps = new ArrayList<>();

            stream().forEach(x -> {
                if(x.getCountry().equals(c)) temps.add(x.getTemperature());
            });

            double avg = temps.stream().mapToDouble(Double::doubleValue).average().getAsDouble();

            res.put(c, avg);
        });
        return res;
    }

    public Map<String, Double> avgTemperatureDeltaPerYearPerCountry() {
        Map<String, Double> res = new HashMap<>();
        return null;
    }

    public static void main(final String[] args) {
        String filepath = "temperaturesEurope1963Till2013ByCity.csv";
        StreamTemperatures temperatures = new StreamTemperatures(filepath);

        temperatures.printSummary();
        final Map<String, Double> values = temperatures
                .avgTemperatureDeltaPerYearPerCountry();

        print("Averaged yearly temperature delta per country:",
                Arrays.toString(values.entrySet().toArray()));
    }

}
