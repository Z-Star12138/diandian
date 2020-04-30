package cn.edu.scujcc.diandian;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//日期格式转换器
public class MyDate {
    //日期转换
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ToJson
    String toJson(Date d) {
        return dateFormat.format(d);
    }

    @FromJson
    Date fromJson(String s) throws ParseException {
        return dateFormat.parse(s);
    }
}
