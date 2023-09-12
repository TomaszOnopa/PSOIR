package com.example.opinionservice.controller;

import com.example.opinionservice.dto.NewOpinionRequest;
import com.example.opinionservice.dto.OpinionResponse;
import com.example.opinionservice.service.OpinionService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opinion")
@RequiredArgsConstructor
public class OpinionController {
    private final OpinionService opinionService;

    @GetMapping("list")
    public ResponseEntity<?> opinions(@RequestParam Long carId, @RequestParam(defaultValue = "1") int page) {
        try {
            JSONObject opinions = opinionService.getOpinions(carId, page);
            return ResponseEntity.ok(opinions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("avg")
    public ResponseEntity<?> opinionsAvg(@RequestParam Long carId) {
        try {
            JSONObject avg = opinionService.getRatingAvg(carId);
            return ResponseEntity.ok(avg);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody NewOpinionRequest request) {
        try {
            OpinionResponse opinion = opinionService.addOpinion(request);
            if (opinion != null)
                return ResponseEntity.ok(opinion);
            else
                return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
