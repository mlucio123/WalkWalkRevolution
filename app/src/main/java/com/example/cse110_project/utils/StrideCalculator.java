package com.example.cse110_project.utils;

public class StrideCalculator {

    private int ft;
    private int inch;
    private final int FT_IN_INCH = 12;
    private final double CONSTANT = 0.413;

    public StrideCalculator(int ft, int inch){
        this.ft = ft;
        this.inch = inch;
    }

    public double getStrideLength(){
        // Multiply height in inches by 0.413 then convert your stride length back to feet
        // 63 inches * 0.413 = 26.019 inches / stride = 2.16825 feet / stride
        return (this.ft * FT_IN_INCH + this.inch) * CONSTANT / FT_IN_INCH;
    }

    public void setFt(int newFt) { this.ft = newFt; }
    public void setInch(int newInch) { this.inch = newInch; }

    //FOR TESTING
    public int getFt() { return this.ft; }
    public int getInch() { return this.inch; }

}
