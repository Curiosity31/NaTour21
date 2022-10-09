package com.example.demo.Points;

import java.util.List;

public interface PointServiceDAO {

    void addNewPoint(Point point);

    List<Point> getPointsTracciatoById(Long itinerarioId);
}
