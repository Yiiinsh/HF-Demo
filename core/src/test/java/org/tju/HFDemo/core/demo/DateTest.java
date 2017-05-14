package org.tju.HFDemo.core.demo;

import org.junit.Test;
import org.tju.HFDemo.core.AbstractTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shaohan.yin on 14/05/2017.
 */
public class DateTest extends AbstractTest {
    @Test
    public void dateTest() throws ParseException {
        String demo = "2017-05-30";
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dft.parse(demo);
        System.out.println(date.compareTo(new Date()));
        System.out.println(date);
        date = new Date();
        System.out.println(date);
    }
}
