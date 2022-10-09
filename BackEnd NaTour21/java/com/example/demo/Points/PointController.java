package com.example.demo.Points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/point")
public class PointController {

    @Autowired
    private PointServiceDAO pointServiceDAO;

    @GetMapping("/getPointsTracciatoById/{itinerarioId}")
    public List<Point> getPointsTracciatoById(@PathVariable("itinerarioId") Long itinerarioId) {
        return pointServiceDAO.getPointsTracciatoById(itinerarioId);
    }

    @PostMapping("/registerNewPoint")
    public void registerNewUtente (@RequestBody Point point) {
        pointServiceDAO.addNewPoint(point);
    }


}
