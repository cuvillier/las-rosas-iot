package com.lasrosas.iot.ingestor.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> list(T ... elements) {
        var list = new ArrayList<T>();

        list.addAll(Arrays.asList(elements));

        return list;
    }
}
