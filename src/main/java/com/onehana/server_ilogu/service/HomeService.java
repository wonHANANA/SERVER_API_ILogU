package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.EventDto;
import com.onehana.server_ilogu.dto.ProductDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.HomeResponse;
import com.onehana.server_ilogu.entity.enums.BoardCategory;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.EventRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public HomeResponse getMyHomeInfo(String email) {
        userRepository.findByEmail(email).orElseThrow(() ->
            new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<EventDto> events = eventRepository.findAll().
                stream().map(EventDto::of).toList();

        List<String> keywords = Arrays.asList("#대구", "#강원도", "#캠핑", "#차박", "#축구여행", "#가족여행",
                "#꽃여행", "#호캉스여행", "#도시여행", "#자연");

        List<ProductDto> products = productService.getProductsByCategory(BoardCategory.TRAVEL);
        return HomeResponse.of(BoardCategory.TRAVEL, keywords, events, products);
    }
}
