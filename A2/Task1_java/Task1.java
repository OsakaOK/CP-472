import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Task1 {
    public static void main(String[] args) {
        String csvFile = "/Users/orz_os/Desktop/CP472/Kham0130_a02/climate-daily.csv";
        ArrayList<ClimateRecord> records = readClimateData(csvFile);

        // print records
        // for (ClimateRecord record : records) {
        // System.out.println(record);
        // }

        find_percipitation_month(records);
        find_gust_day(records);
        find_max_fluctuation(records);

        Scanner scanner = new Scanner(System.in);

        // Report Generation Menu
        System.out.println("\nReport Generation Menu:");
        System.out.println("1. Average Monthly Statistics");
        System.out.println("2. Weather Records Between Dates");
        System.out.print("Enter the option (1 or 2): ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                generate_AverageMonthly(records);
                break;
            case 2:
                generate_RecordsBetweenDates(records, scanner);
                break;
            default:
                System.out.println("Invalid option. Exiting...");
        }

        scanner.close();
    }

    // Task 1-2
    private static ArrayList<ClimateRecord> readClimateData(String csvFile) {
        ArrayList<ClimateRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length == 6) {
                        Date localDate = parseDate(data[0].trim());
                        double speedMaxGust = Double.parseDouble(data[1].trim());
                        double totalPrecipitation = Double.parseDouble(data[2].trim());
                        double minTemperature = Double.parseDouble(data[3].trim());
                        double maxTemperature = Double.parseDouble(data[4].trim());
                        double meanTemperature = Double.parseDouble(data[5].trim());

                        ClimateRecord record = new ClimateRecord(localDate, speedMaxGust, totalPrecipitation,
                                minTemperature, maxTemperature, meanTemperature);

                        records.add(record);
                    } else {
                        // System.out.println("Skipping malformed record: " + line);
                    }
                } catch (NumberFormatException | ParseException e) {
                    // System.out.println("Skipping record due to parsing error: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    }

    private static void find_percipitation_month(ArrayList<ClimateRecord> records) {
        Map<String, Double> monthlyTotalPrecipitation = new HashMap<>();

        for (ClimateRecord record : records) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getLocalDate());

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based

            String key = String.format("%02d-%d", month, year);

            double totalPrecipitation = monthlyTotalPrecipitation.getOrDefault(key, 0.0);
            totalPrecipitation += record.getTotalPrecipitation();
            monthlyTotalPrecipitation.put(key, totalPrecipitation);
        }

        String maxMonthYear = "";
        double maxPrecipitation = 0.0;

        for (Map.Entry<String, Double> entry : monthlyTotalPrecipitation.entrySet()) {
            if (entry.getValue() > maxPrecipitation) {
                maxPrecipitation = entry.getValue();
                maxMonthYear = entry.getKey();
            }
        }

        System.out.println("Month with the highest TOTAL_PRECIPITATION: " + maxMonthYear);
        System.out.println("Total Precipitation: " + maxPrecipitation);
    }

    private static void find_gust_day(ArrayList<ClimateRecord> records) {
        Date maxGustDate = null;
        double maxGustSpeed = 0.0;

        for (ClimateRecord record : records) {
            if (record.getSpeedMaxGust() > maxGustSpeed) {
                maxGustSpeed = record.getSpeedMaxGust();
                maxGustDate = record.getLocalDate();
            }
        }

        if (maxGustDate != null) {
            System.out.println("Day with the highest SPEED_MAX_GUST: " + maxGustDate);
            System.out.println("Maximum Gust Speed: " + maxGustSpeed);
        } else {
            System.out.println("No data available.");
        }
    }

    private static void find_max_fluctuation(ArrayList<ClimateRecord> records) {
        Date maxFluctuationDate = null;
        double maxTemperatureFluctuation = 0.0;

        for (ClimateRecord record : records) {
            double temperatureFluctuation = record.getMaxTemperature() - record.getMinTemperature();
            if (temperatureFluctuation > maxTemperatureFluctuation) {
                maxTemperatureFluctuation = temperatureFluctuation;
                maxFluctuationDate = record.getLocalDate();
            }
        }

        if (maxFluctuationDate != null) {
            System.out.println("Day with the largest temperature fluctuation: " + maxFluctuationDate);
            System.out.println("Temperature Fluctuation: " + maxTemperatureFluctuation);
        } else {
            System.out.println("No data available.");
        }
    }

    private static void generate_AverageMonthly(ArrayList<ClimateRecord> records) {
        // TreeMap to store monthly statistics (sorted by month-year key)
        TreeMap<String, ClimateMonthlyStatistics> monthlyStatisticsMap = new TreeMap<>();

        // Calculate monthly statistics
        for (ClimateRecord record : records) {
            String key = getMonthYearKey(record.getLocalDate());

            ClimateMonthlyStatistics monthlyStatistics = monthlyStatisticsMap.computeIfAbsent(key,
                    k -> new ClimateMonthlyStatistics());
            monthlyStatistics.addRecord(record);
        }

        // Display average monthly statistics in chronological order
        System.out.println("\nAverage Monthly Statistics (Chronological Order):");
        for (Map.Entry<String, ClimateMonthlyStatistics> entry : monthlyStatisticsMap.entrySet()) {
            System.out.println("Month-Year: " + entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("-------------------------");
        }
    }

    // Helper method to get the month-year key from a date
    private static String getMonthYearKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based

        return String.format("%02d-%d", month, year);
    }

    private static void generate_RecordsBetweenDates(ArrayList<ClimateRecord> records, Scanner scanner) {
        // Prompt user for start and end dates
        Date startDate = promptForDate(scanner, "Enter the start date (yyyy-MM-dd): ");
        Date endDate = promptForDate(scanner, "Enter the end date (yyyy-MM-dd): ");

        // Check if the date range is valid
        if (startDate != null && endDate != null && startDate.before(endDate)) {
            System.out.println("\nWeather Records Between Dates:");

            // Display records within the specified date range
            for (ClimateRecord record : records) {
                Date currentDate = record.getLocalDate();
                if (currentDate.after(startDate) && currentDate.before(endDate)) {
                    System.out.println(record);
                }
            }
        } else {
            System.out.println("Invalid date range. Start date should be before the end date.");
        }
    }

    // Helper method to prompt user for a date
    private static Date promptForDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String dateString = scanner.next();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use the format yyyy-MM-dd.");
            return null;
        }
    }

}

// Class for Task 1-3
class ClimateRecord {
    private Date localDate;
    private double speedMaxGust;
    private double totalPrecipitation;
    private double minTemperature;
    private double maxTemperature;
    private double meanTemperature;

    public ClimateRecord(Date localDate, double speedMaxGust, double totalPrecipitation,
            double minTemperature, double maxTemperature, double meanTemperature) {
        this.localDate = localDate;
        this.speedMaxGust = speedMaxGust;
        this.totalPrecipitation = totalPrecipitation;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.meanTemperature = meanTemperature;
    }

    // Getter methods
    public Date getLocalDate() {
        return localDate;
    }

    public double getSpeedMaxGust() {
        return speedMaxGust;
    }

    public double getTotalPrecipitation() {
        return totalPrecipitation;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMeanTemperature() {
        return meanTemperature;
    }

    @Override
    public String toString() {
        return "ClimateRecord{" +
                "localDate=" + localDate +
                ", speedMaxGust=" + speedMaxGust +
                ", totalPrecipitation=" + totalPrecipitation +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", meanTemperature=" + meanTemperature +
                '}';
    }
}

// Class for Task 4
class ClimateMonthlyStatistics {
    private int recordCount;
    private double totalSpeedMaxGust;
    private double totalTotalPrecipitation;
    private double totalMinTemperature;
    private double totalMaxTemperature;
    private double totalMeanTemperature;

    public void addRecord(ClimateRecord record) {
        recordCount++;
        totalSpeedMaxGust += record.getSpeedMaxGust();
        totalTotalPrecipitation += record.getTotalPrecipitation();
        totalMinTemperature += record.getMinTemperature();
        totalMaxTemperature += record.getMaxTemperature();
        totalMeanTemperature += record.getMeanTemperature();
    }

    @Override
    public String toString() {
        return String.format("Average SPEED_MAX_GUST: %.2f\n" +
                "Average TOTAL_PRECIPITATION: %.2f\n" +
                "Average MIN_TEMPERATURE: %.2f\n" +
                "Average MAX_TEMPERATURE: %.2f\n" +
                "Average MEAN_TEMPERATURE: %.2f",
                totalSpeedMaxGust / recordCount,
                totalTotalPrecipitation / recordCount,
                totalMinTemperature / recordCount,
                totalMaxTemperature / recordCount,
                totalMeanTemperature / recordCount);
    }
}
