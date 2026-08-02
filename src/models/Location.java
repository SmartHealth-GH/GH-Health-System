package models;

/**
 * Represents a location in the hospital system
 * e.g., Emergency Unit, Pharmacy, Operating Room
 */
public class Location {
    private int id;
    private String name;
    private String area;
    private String type;
    private double latitude;
    private double longitude;

    // Constructor
    public Location() {}

    public Location(int id, String name, String area, String type,
                    double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}