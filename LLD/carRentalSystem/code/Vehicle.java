package LLD.carRentalSystem.code;

import java.util.Date;

class Vehicle {
    int id;
    int vehicleNum;
    VehicleType vehicleType;
    String companyName;
    String modelName;
    int kmDriven;
    Date manufacturingDate;
    int avg;
    int cc;
    int dailyRentalCost;
    int hourlyRentalCost;
    int noOfSeat;
    Status status;

    public Vehicle(int id, int vehicleNum, VehicleType vehicleType, String companyName, String modelName,
                   int kmDriven, Date manufacturingDate, int avg, int cc, int dailyRentalCost,
                   int hourlyRentalCost, int noOfSeat, Status status) {
        this.id = id;
        this.vehicleNum = vehicleNum;
        this.vehicleType = vehicleType;
        this.companyName = companyName;
        this.modelName = modelName;
        this.kmDriven = kmDriven;
        this.manufacturingDate = manufacturingDate;
        this.avg = avg;
        this.cc = cc;
        this.dailyRentalCost = dailyRentalCost;
        this.hourlyRentalCost = hourlyRentalCost;
        this.noOfSeat = noOfSeat;
        this.status = status;
    }
}