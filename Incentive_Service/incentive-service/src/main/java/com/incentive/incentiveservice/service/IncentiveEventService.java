package com.incentive.incentiveservice.service;



import com.incentive.incentiveservice.dto.IncentiveEventDTO;
import com.incentive.incentiveservice.dto.Response;
import com.incentive.incentiveservice.enums.IncentiveType;

import java.util.List;

public interface IncentiveEventService {

    Response<?> createIncentiveEvent(IncentiveEventDTO incentiveEventDTO);

    Response<List<IncentiveEventDTO>> getAllIncentiveEvents();

    Response<Integer> getUserBalance(Long id);

    Response<List<IncentiveEventDTO>> getAllByType(IncentiveType type);

    Response<List<IncentiveEventDTO>> getUserEvents(Long id);
}
