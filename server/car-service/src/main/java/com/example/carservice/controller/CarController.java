package com.example.carservice.controller;

import com.example.carservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("brands")
    public ResponseEntity<?> carMakes() {
        try {
            JSONObject makes = carService.getCarMakes();
            return ResponseEntity.ok(makes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("models")
    public ResponseEntity<?> carModels(@RequestParam String make) {
        try {
            JSONObject models = carService.getCarModels(make);
            if (models.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("generations")
    public ResponseEntity<?> carGenerations(@RequestParam String make, @RequestParam String model) {
        try {
            JSONObject generations = carService.getCarGenerations(make, model);
            if (generations.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(generations);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("versions")
    public ResponseEntity<?> carVersion(@RequestParam String make, @RequestParam String model, @RequestParam String generation) {
        try {
            JSONObject versions = carService.getCarVersions(make, model, generation);
            if (versions.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(versions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("specs")
    public ResponseEntity<?> carSpecs(@RequestParam Long id) {
        try {
            JSONObject specs = carService.getCarSpecs(id);
            if (specs.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(specs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("exist")
    @ResponseStatus(HttpStatus.OK)
    public boolean isCarExist(@RequestParam Long id) {
        return carService.isCarExist(id);
    }
}
