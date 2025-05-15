package com.findr.FindrAPI.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point createPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude)); // (X, Y) = (Longitude, Latitude)
    }
}