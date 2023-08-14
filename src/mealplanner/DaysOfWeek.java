package mealplanner;

public enum DaysOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String nameOfDay;

    DaysOfWeek(String nameOfDay) {
        this.nameOfDay = nameOfDay;
    }

    public String getNameOfDay() {
        return nameOfDay;
    }
}
