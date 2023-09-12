package com.example.opinionservice.service;

import com.example.opinionservice.dto.NewOpinionRequest;
import com.example.opinionservice.dto.OpinionResponse;
import com.example.opinionservice.model.Opinion;
import com.example.opinionservice.repository.OpinionRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpinionService {
    private final static int OPINIONS_PER_PAGE = 10;
    private final OpinionRepository opinionRepository;
    private final WebClient.Builder webClientBuilder;

    public JSONObject getOpinions(Long carId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum-1, OPINIONS_PER_PAGE);
        Page<Opinion> page = opinionRepository.findAllByCarIdOrderByCreationDateDesc(carId, paging);

        List<OpinionResponse> opinions = page
                .getContent()
                .stream()
                .map(this::mapToOpinionResponse)
                .toList();

        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        jArray.addAll(opinions);
        jObject.put("opinions", jArray);
        jObject.put("totalItems", page.getTotalElements());
        jObject.put("totalPages", page.getTotalPages());

        return jObject;
    }

    public JSONObject getRatingAvg(Long carId) {
        JSONObject jObject = new JSONObject();

        Double sum = 0.0;
        List<Opinion> opinions = opinionRepository.findAllByCarId(carId);
        for (Opinion opinion : opinions)
            sum += opinion.getRating();
        if (!opinions.isEmpty())
            jObject.put("rating", sum/opinions.size());

        return jObject;
    }

    public OpinionResponse addOpinion(NewOpinionRequest request) {
        if (request.rating() == null) return null;

        //Check if car exists
        boolean isCarExist = Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri("http://car-service/api/car/exist",
                        uriBuilder -> uriBuilder.queryParam("id", request.carId()).build())
                .retrieve()
                .bodyToMono(boolean.class)
                .block());
        if (!isCarExist)
            return null;

        Opinion opinion = new Opinion();

        opinion.setCarId(request.carId());
        opinion.setCreationDate(Date.valueOf(LocalDate.now()));
        opinion.setRating(request.rating());
        opinion.setComment(request.comment());

        Opinion result = opinionRepository.save(opinion);
        return mapToOpinionResponse(result);
    }

    private OpinionResponse mapToOpinionResponse(Opinion opinion) {
        return new OpinionResponse(
                opinion.getId(),
                opinion.getCarId(),
                new Date(opinion.getCreationDate().getTime()),
                opinion.getRating(),
                opinion.getComment()
        );
    }
}
