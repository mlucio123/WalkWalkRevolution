package com.example.cse110_project;

import androidx.test.filters.LargeTest;

import com.example.cse110_project.utils.StrideCalculator;

import org.junit.Test;
import org.junit.Assert;

@LargeTest
public class StrideCalculatorTest {
    @Test
    public void testConstructor() {
        StrideCalculator calc = new StrideCalculator(6,0);
        Assert.assertEquals(calc.getFt(), 6);
        Assert.assertEquals(calc.getInch(), 0);
    }

    @Test
    public void testSetters() {
        StrideCalculator calc = new StrideCalculator(6,0);
        calc.setFt(5);
        calc.setInch(7);
        Assert.assertEquals(calc.getFt(), 5);
        Assert.assertEquals(calc.getInch(), 7);
    }

    @Test
    public void testStrideLength() {
        StrideCalculator calc = new StrideCalculator(6,0);
        double strideResult = calc.getStrideLength();
        Assert.assertEquals(strideResult, 2.478, .001);

    }
}
