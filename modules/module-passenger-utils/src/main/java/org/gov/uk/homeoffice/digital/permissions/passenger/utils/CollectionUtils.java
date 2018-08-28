package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import java.util.*;

import static java.util.Arrays.asList;

public class CollectionUtils {

    public static <T, V> Map<T, V> map(Tuple<T, V>... tuples) {
        Map<T, V> map = new HashMap<>();
        asList(tuples).stream().forEach(tpl -> map.put(tpl.get_1(), tpl.get_2()));
        return map;
    }

    public static <T> List<T> add(List<T> list, T additionalElement){
        List<T> newList = new ArrayList<>();
        if(list != null){
            newList.addAll(list);
        }
        newList.add(additionalElement);
        return newList;
    }


    public static <T> Set<T> add(Set<T> set, T additionalElement){
        Set<T> newSet = new HashSet<>();
        if(set != null){
            newSet.addAll(set);
        }
        newSet.add(additionalElement);
        return newSet;
    }

    public static <T> Set<T> set(T... ts){
        Set<T> set = new HashSet<>();
        asList(ts).forEach(t -> set.add(t));
        return set;

    }

}
