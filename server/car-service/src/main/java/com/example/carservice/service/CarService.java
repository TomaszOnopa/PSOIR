package com.example.carservice.service;

import com.example.carservice.dto.CarGenerationResponse;
import com.example.carservice.dto.CarVersionResponse;
import com.example.carservice.model.Car;
import com.example.carservice.repository.CarRepository;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public JSONObject getCarMakes() {
        List<String> makes = carRepository.findAllMake();

        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        jArray.addAll(makes);
        jObject.put("carMakes", jArray);

        return jObject;
    }

    public JSONObject getCarModels(String make) {
        List<String> models = carRepository.findAllModelByMake(make);
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();

        if (models.size() != 0) {
            jArray.addAll(models);
            jObject.put("carModels", jArray);
        }
        return jObject;
    }

    public JSONObject getCarGenerations(String make, String model) {
        List<CarGenerationResponse> generations = carRepository.findAllByMakeAndModelGroupByGenerationOrderByGeneration(make, model)
                .stream()
                .map(this::mapToCarGenerationResponse)
                .toList();
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();

        if (generations.size() != 0) {
            jArray.addAll(generations);
            jObject.put("carGenerations", jArray);
        }
        return jObject;
    }

    public JSONObject getCarVersions(String make, String model, String generation) {
        List<CarVersionResponse> versions = carRepository.findAllByMakeAndModelAndGenerationOrderByTrim(make, model, generation)
                .stream()
                .map(this::mapToCarVersionResponse)
                .toList();
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();

        if (versions.size() != 0) {
            jArray.addAll(versions);
            jObject.put("carVersions", jArray);
        }
        return jObject;
    }

    public JSONObject getCarSpecs(Long id) {
        JSONObject jObject = new JSONObject();

        Optional<Car> car = carRepository.findCarById(id);
        if (car.isPresent()) {
            jObject.put("car", car);
        }
        return jObject;
    }

    public boolean isCarExist(Long id) {
        return carRepository.existsById(id);
    }

    private CarGenerationResponse mapToCarGenerationResponse(Car car) {
        return new CarGenerationResponse(car.getGeneration(), car.getYearFrom(), car.getYearTo());
    }

    private CarVersionResponse mapToCarVersionResponse(Car car) {
        return new CarVersionResponse(car.getId(), car.getTrim(), car.getSeries());
    }
}
