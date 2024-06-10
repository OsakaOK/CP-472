#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_LENGTH 1024

// Task 1-2
struct ClimateRecord
{
    char localDate[11];
    double speedMaxGust;
    double totalPrecipitation;
    double minTemperature;
    double maxTemperature;
    double meanTemperature;
};

int parseCSVLine(char *line, struct ClimateRecord *record)
{
    char *token = strtok(line, ",");
    if (token == NULL)
        return 0;

    // Extract only the date part
    strncpy(record->localDate, token, 10);
    record->localDate[10] = '\0';

    token = strtok(NULL, ",");
    if (token == NULL)
        return 0;
    record->speedMaxGust = atof(token);

    token = strtok(NULL, ",");
    if (token == NULL)
        return 0;
    record->totalPrecipitation = atof(token);

    token = strtok(NULL, ",");
    if (token == NULL)
        return 0;
    record->minTemperature = atof(token);

    token = strtok(NULL, ",");
    if (token == NULL)
        return 0;
    record->maxTemperature = atof(token);

    token = strtok(NULL, ",");
    if (token == NULL)
        return 0;
    record->meanTemperature = atof(token);

    return 1;
}

// Task 3
void findMaxPrecipitationMonth(struct ClimateRecord *records, size_t size)
{
    double maxPrecipitation = 0;
    char maxPrecipitationMonth[8];

    int currentYear, currentMonth;
    double currentMonthTotal = 0;

    for (size_t i = 0; i < size; ++i)
    {
        // Extract year and month from the date (assuming date format is YYYY-MM-DD)
        int year, month;
        sscanf(records[i].localDate, "%d-%d", &year, &month);

        // Check if it's a new month
        if (i == 0 || year != currentYear || month != currentMonth)
        {
            if (currentMonthTotal > maxPrecipitation)
            {
                maxPrecipitation = currentMonthTotal;
                sprintf(maxPrecipitationMonth, "%02d-%04d", currentMonth, currentYear);
            }

            // Reset total for the new month
            currentYear = year;
            currentMonth = month;
            currentMonthTotal = records[i].totalPrecipitation;
        }
        else
        {
            // Accumulate total for the current month
            currentMonthTotal += records[i].totalPrecipitation;
        }
    }

    // Check the last month
    if (currentMonthTotal > maxPrecipitation)
    {
        maxPrecipitation = currentMonthTotal;
        sprintf(maxPrecipitationMonth, "%02d-%04d", currentMonth, currentYear);
    }

    printf("Month with the highest TOTAL_PRECIPITATION: %s, Precipitation: %.2f\n", maxPrecipitationMonth, maxPrecipitation);
}

void findMaxGust(struct ClimateRecord *records, size_t size)
{
    double maxGust = 0;
    char maxGustDay[11];

    for (size_t i = 0; i < size; ++i)
    {
        double gust = records[i].speedMaxGust;
        if (gust > maxGust)
        {
            maxGust = gust;
            strcpy(maxGustDay, records[i].localDate);
        }
    }

    printf("Day with the highest SPEED_MAX_GUST: %s, Speed: %.2f\n", maxGustDay, maxGust);
}

void findMaxTemperatureFluctuation(struct ClimateRecord *records, size_t size)
{
    double maxFluctuation = 0;
    char maxFluctuationDay[11]; // Assuming date format is YYYY-MM-DD

    for (size_t i = 0; i < size; ++i)
    {
        double fluctuation = records[i].maxTemperature - records[i].minTemperature;
        if (fluctuation > maxFluctuation)
        {
            maxFluctuation = fluctuation;
            strcpy(maxFluctuationDay, records[i].localDate);
        }
    }

    printf("Day with the largest temperature fluctuation: %s, Fluctuation: %.2f\n", maxFluctuationDay, maxFluctuation);
}

// Task 4
void generateMonthlyAverages(struct ClimateRecord *records, size_t size)
{
    printf("\nMonthly Averages:\n");

    int currentYear = -1;
    int currentMonth = -1;
    int count = 0;
    double totalSpeedMaxGust = 0;
    double totalTotalPrecipitation = 0;
    double totalMinTemperature = 0;
    double totalMaxTemperature = 0;
    double totalMeanTemperature = 0;

    for (size_t i = 0; i < size; ++i)
    {
        int year, month;
        sscanf(records[i].localDate, "%d-%d", &year, &month);

        if (currentYear == -1 || currentMonth == -1 || (currentYear == year && currentMonth == month))
        {
            // Same month or first iteration
            totalSpeedMaxGust += records[i].speedMaxGust;
            totalTotalPrecipitation += records[i].totalPrecipitation;
            totalMinTemperature += records[i].minTemperature;
            totalMaxTemperature += records[i].maxTemperature;
            totalMeanTemperature += records[i].meanTemperature;
            count++;

            // Update current month and year
            currentYear = year;
            currentMonth = month;
        }
        else
        {
            // Print averages for the previous month
            printf("Month-Year: %02d-%04d, Avg SpeedMaxGust: %.2f, Avg TotalPrecipitation: %.2f, Avg MinTemperature: %.2f, Avg MaxTemperature: %.2f, Avg MeanTemperature: %.2f\n",
                   currentMonth, currentYear,
                   totalSpeedMaxGust / count, totalTotalPrecipitation / count,
                   totalMinTemperature / count, totalMaxTemperature / count,
                   totalMeanTemperature / count);

            // Reset totals and count for the new month
            totalSpeedMaxGust = records[i].speedMaxGust;
            totalTotalPrecipitation = records[i].totalPrecipitation;
            totalMinTemperature = records[i].minTemperature;
            totalMaxTemperature = records[i].maxTemperature;
            totalMeanTemperature = records[i].meanTemperature;
            count = 1;

            // Update current month and year
            currentYear = year;
            currentMonth = month;
        }
    }

    // Print averages for the last month
    if (count > 0)
    {
        printf("Month-Year: %02d-%04d, Avg SpeedMaxGust: %.2f, Avg TotalPrecipitation: %.2f, Avg MinTemperature: %.2f, Avg MaxTemperature: %.2f, Avg MeanTemperature: %.2f\n",
               currentMonth, currentYear,
               totalSpeedMaxGust / count, totalTotalPrecipitation / count,
               totalMinTemperature / count, totalMaxTemperature / count,
               totalMeanTemperature / count);
    }
}

void filterRecordsByDate(struct ClimateRecord *records, size_t size)
{
    printf("\nEnter Start Date (YYYY-MM-DD): ");
    char startDate[11];
    scanf("%s", startDate);

    printf("Enter End Date (YYYY-MM-DD): ");
    char endDate[11];
    scanf("%s", endDate);

    printf("\nWeather Records between %s and %s:\n", startDate, endDate);

    for (size_t i = 0; i < size; ++i)
    {
        if (strcmp(records[i].localDate, startDate) >= 0 && strcmp(records[i].localDate, endDate) <= 0)
        {
            printf("Date: %s, SpeedMaxGust: %.2f, TotalPrecipitation: %.2f, MinTemperature: %.2f, MaxTemperature: %.2f, MeanTemperature: %.2f\n",
                   records[i].localDate, records[i].speedMaxGust, records[i].totalPrecipitation,
                   records[i].minTemperature, records[i].maxTemperature, records[i].meanTemperature);
        }
    }
}

// Testing
int main()
{
    FILE *file = fopen("/Users/orz_os/Desktop/CP472/Kham0130_a02/climate-daily.csv", "r");
    if (file == NULL)
    {
        perror("Error opening file");
        return 1;
    }

    // Create a dynamic array to store ClimateRecord structs
    struct ClimateRecord *records = NULL;
    size_t capacity = 0;
    size_t size = 0;

    char line[MAX_LINE_LENGTH];
    while (fgets(line, sizeof(line), file))
    {
        // Remove newline character if present
        size_t len = strlen(line);
        if (len > 0 && line[len - 1] == '\n')
        {
            line[len - 1] = '\0';
        }

        // Allocate memory for a new ClimateRecord
        struct ClimateRecord *newRecord = malloc(sizeof(struct ClimateRecord));
        if (newRecord == NULL)
        {
            perror("Error allocating memory");
            return 1;
        }

        // Parse the CSV line and populate the ClimateRecord
        if (parseCSVLine(line, newRecord))
        {
            // Add the new record to the dynamic array
            if (size == capacity)
            {
                capacity = (capacity == 0) ? 1 : capacity * 2;
                records = realloc(records, capacity * sizeof(struct ClimateRecord));
                if (records == NULL)
                {
                    perror("Error reallocating memory");
                    return 1;
                }
            }

            records[size++] = *newRecord;
        }
        else
        {
            // Skip malformed or missing data
            free(newRecord);
        }
    }

    // Close the file
    fclose(file);

    findMaxPrecipitationMonth(records, size);
    findMaxGust(records, size);
    findMaxTemperatureFluctuation(records, size);

    int choice;
    do
    {
        printf("\nChoose a Report to Generate:\n");
        printf("1. Monthly Averages\n");
        printf("2. Weather Records between a Given Start and End Date\n");
        printf("0. Exit\n");
        printf("Enter your choice (0-2): ");
        scanf("%d", &choice);

        switch (choice)
        {
        case 1:
            generateMonthlyAverages(records, size);
            break;
        case 2:
            filterRecordsByDate(records, size);
            break;
        case 0:
            printf("Exiting...\n");
            break;
        default:
            printf("Invalid choice. Please enter a number between 0 and 2.\n");
        }
    } while (choice != 0);

    // Free allocated memory
    free(records);

    return 0;
}
