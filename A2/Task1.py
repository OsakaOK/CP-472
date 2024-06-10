import pandas as pd
from datetime import datetime


# Task 1-2
class ClimateData:
    def __init__(
        self,
        local_date,
        speed_max_gust,
        total_precipitation,
        min_temp,
        max_temp,
        mean_temp,
    ):
        self.local_date = local_date
        self.speed_max_gust = speed_max_gust
        self.total_precipitation = total_precipitation
        self.min_temp = min_temp
        self.max_temp = max_temp
        self.mean_temp = mean_temp

    def __str__(self):
        return (
            f"Date: {self.local_date}, "
            f"Max Gust: {self.speed_max_gust}, "
            f"Precipitation: {self.total_precipitation}, "
            f"Min Temp: {self.min_temp}, "
            f"Max Temp: {self.max_temp}, "
            f"Mean Temp: {self.mean_temp}"
        )


# Convert Data into correct format to prase
def parse_date(date_str):
    try:
        return datetime.strptime(date_str, "%Y-%m-%d %H:%M").date()
    except ValueError:
        return None


# Convert value to float to parse
def parse_numeric(value, default=None):
    try:
        return float(value)
    except ValueError:
        return default


def read_and_process_csv(file_path):
    df = pd.read_csv(file_path)
    climate_data_list = []

    for index, row in df.drop_duplicates().iterrows():
        # If no data in column then skip it
        if pd.notna(row["SPEED_MAX_GUST"]):
            # Parse data and added them to the list
            climate_data = ClimateData(
                local_date=parse_date(row["LOCAL_DATE"]),
                speed_max_gust=parse_numeric(row["SPEED_MAX_GUST"]),
                total_precipitation=parse_numeric(row["TOTAL_PRECIPITATION"]),
                min_temp=parse_numeric(row["MIN_TEMPERATURE"]),
                max_temp=parse_numeric(row["MAX_TEMPERATURE"]),
                mean_temp=parse_numeric(row["MEAN_TEMPERATURE"]),
            )
            climate_data_list.append(climate_data)

            # Print data //testing
            # for data in climate_data_list:
            #     print(data)
            climate_data_df = pd.DataFrame([vars(data) for data in climate_data_list])

    return climate_data_df


# Task 3
def find_max_precipitation_month(climate_data_df):
    # Convert 'local_date' to datetime
    climate_data_df["local_date"] = pd.to_datetime(climate_data_df["local_date"])

    # Group by month and year, sum the total precipitation values
    monthly_precipitation = climate_data_df.groupby(
        [climate_data_df["local_date"].dt.year, climate_data_df["local_date"].dt.month]
    )["total_precipitation"].sum()

    # Find the month-year combination with the highest total precipitation
    max_precipitation_month = monthly_precipitation.idxmax()
    max_precipitation_value = monthly_precipitation.max()
    # Extract year and month from the tuple
    max_precipitation_year, max_precipitation_month = max_precipitation_month

    return max_precipitation_year, max_precipitation_month, max_precipitation_value


def find_max_gust_day(climate_data_df):
    # Find the day with the highest SPEED_MAX_GUST
    max_gust_day = climate_data_df.loc[climate_data_df["speed_max_gust"].idxmax()]

    return max_gust_day


def find_max_fluctuation(climate_data_df):
    # Calculate temperature fluctuation for each day
    climate_data_df["temperature_fluctuation"] = (
        climate_data_df["max_temp"] - climate_data_df["min_temp"]
    )

    # Find the day with the largest temperature fluctuation and round to 1 decimal
    max_fluctuation_day = climate_data_df.loc[
        climate_data_df["temperature_fluctuation"].idxmax()
    ]
    round_max = round(max_fluctuation_day["temperature_fluctuation"], 2)

    return max_fluctuation_day, round_max


# Task 4
def generate_monthly_averages(climate_data_df):
    climate_data_df["local_date"] = pd.to_datetime(climate_data_df["local_date"])
    # Group by year and month and calculate averages
    monthly_averages = climate_data_df.groupby(
        [climate_data_df["local_date"].dt.year, climate_data_df["local_date"].dt.month]
    ).agg(
        {
            "speed_max_gust": "mean",
            "total_precipitation": "mean",
            "min_temp": "mean",
            "max_temp": "mean",
            "mean_temp": "mean",
        }
    )
    # Change format of date to YYYY-MM-DD
    monthly_averages.index = monthly_averages.index.map(
        lambda x: f"{x[0]}-{x[1]:02d}-01"
    )

    return monthly_averages


def generate_records_between_dates(climate_data_df, start_date, end_date):
    start_date = parse_date(start_date)
    end_date = parse_date(end_date)

    if start_date is None or end_date is None:
        print("Invalid date format. Please provide dates in the format 'YYYY-MM-DD'.")
        return None

    # Ensure the dates are within the range of the dataset
    if not (
        climate_data_df["local_date"].min()
        <= start_date
        <= climate_data_df["local_date"].max()
        and climate_data_df["local_date"].min()
        <= end_date
        <= climate_data_df["local_date"].max()
    ):
        print("Provided dates are outside the range of the dataset.")
        return None

    # Filter records
    filtered_records = climate_data_df[
        (climate_data_df["local_date"] >= start_date)
        & (climate_data_df["local_date"] <= end_date)
    ]

    return filtered_records


# Testing
def main():
    file_path = "/Users/orz_os/Desktop/CP472/Kham0130_a02/climate-daily.csv"
    climate_data_df = read_and_process_csv(file_path)
    max_precipitation_year, max_precipitation_month, max_precipitation_value = (
        find_max_precipitation_month(climate_data_df)
    )
    max_gust_day = find_max_gust_day(climate_data_df)
    max_fluctuation_day, round_max = find_max_fluctuation(climate_data_df)

    print(
        f"Month with the highest total precipitation: {max_precipitation_year}-{max_precipitation_month}, "
        f"Total Precipitation: {max_precipitation_value}"
    )
    print(
        f"Day with the highest SPEED_MAX_GUST: {max_gust_day['local_date'].date()}, Speed: {max_gust_day['speed_max_gust']}"
    )
    print(
        f"Day with the largest temperature fluctuation: {max_fluctuation_day['local_date'].date()}, "
        f"Temperature Fluctuation: {round_max}"
    )

    while True:
        print("\nSelect a report to generate:")
        print("1. Monthly Averages")
        print("2. Records Between Dates")
        print("3. Quit")

        choice = input(
            "Enter the number of the report you want to generate (or 'q' to quit): "
        )

        if choice.lower() == "q":
            print("Exiting the program.")
            break
        elif choice == "1":
            monthly_averages = generate_monthly_averages(climate_data_df)
            print("\nMonthly Averages:")
            print(monthly_averages)
        elif choice == "2":
            # When entering input, it needs to be in the exact format as shown in brackets with time default to 0:00
            start_date = input("Enter the start date (YYYY-MM-DD 0:00): ")
            end_date = input("Enter the end date (YYYY-MM-DD 0:00): ")
            records_between_dates = generate_records_between_dates(
                climate_data_df, start_date, end_date
            )
            if records_between_dates is not None:
                print("\nRecords Between Dates:")
                print(records_between_dates)
            else:
                print("Invalid choice. Please enter 1, 2, or 'q'.")


if __name__ == "__main__":
    main()
