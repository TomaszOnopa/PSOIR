package com.example.fuelreportservice.service;

import com.example.fuelreportservice.dto.FuelReportResponse;
import com.example.fuelreportservice.dto.NewFuelReportRequest;
import com.example.fuelreportservice.model.FuelReport;
import com.example.fuelreportservice.repository.FuelReportRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelReportService {
    private final static int REPORTS_PER_PAGE = 5;
    private final FuelReportRepository repository;
    private final WebClient.Builder webClientBuilder;

    public JSONObject getFuelReports(Long carId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum-1, REPORTS_PER_PAGE);
        Page<FuelReport> page = repository.findAllByCarIdOrderByCreationDateDesc(carId, paging);

        List<FuelReportResponse> mileageReports = page
                .getContent()
                .stream()
                .map(this::mapToFuelReportResponse)
                .toList();

        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        jArray.addAll(mileageReports);
        jObject.put("mileageReports", jArray);
        jObject.put("totalItems", page.getTotalElements());
        jObject.put("totalPages", page.getTotalPages());

        return jObject;
    }

    public JSONObject getFuelAvg(Long carId) {
        List<FuelReport> reports = repository.findAllByCarId(carId);

        Double[] sum = {0.0, 0.0, 0.0};
        Integer[] count = {0, 0, 0};
        for (FuelReport report : reports) {
            if (report.getType().equals("Miejski")) {
                sum[0] += report.getMileage();
                count[0] += 1;
            } else if (report.getType().equals("Pozamiejski")) {
                sum[1] += report.getMileage();
                count[1] += 1;
            }
            else {
                sum[2] += report.getMileage();
                count[2] += 1;
            }
        }

        JSONObject jObject = new JSONObject();
        if (count[0] != 0)
            jObject.put("Miejski", sum[0]/count[0]);
        if (count[1] != 0)
            jObject.put("Pozamiejski", sum[1]/count[1]);
        if (count[2] != 0)
            jObject.put("Mieszany", sum[2]/count[2]);

        return jObject;
    }

    public FuelReportResponse addReport(NewFuelReportRequest request) {
        if (request.mileage() == null || request.type() == null) return null;

        boolean isCarExist = Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri("http://car-service/api/car/exist",
                        uriBuilder -> uriBuilder.queryParam("id", request.carId()).build())
                .retrieve()
                .bodyToMono(boolean.class)
                .block());
        if (!isCarExist)
            return null;

        FuelReport report = new FuelReport();

        report.setCarId(request.carId());
        report.setCreationDate(Date.valueOf(LocalDate.now()));
        report.setMileage(request.mileage());
        report.setType(request.type());
        report.setNotes(request.notes());

        FuelReport result = repository.save(report);
        return mapToFuelReportResponse(result);
    }

    private FuelReportResponse mapToFuelReportResponse(FuelReport report) {
        return new FuelReportResponse(
                report.getId(),
                new Date(report.getCreationDate().getTime()),
                report.getCarId(),
                report.getMileage(),
                report.getType(),
                report.getNotes());
    }
}
