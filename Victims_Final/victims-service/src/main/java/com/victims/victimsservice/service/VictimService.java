package com.victims.victimsservice.service;

import com.victims.victimsservice.dto.Response;
import com.victims.victimsservice.dto.VictimDTO;

import java.util.List;

public interface VictimService {

    Response<?> createVictim(VictimDTO victimDTO);

    Response<VictimDTO> getVictimById(Long id);

    Response<List<VictimDTO>> getAllVictims();

    Response<?> updateVictim(VictimDTO victimDTO);

    Response<?> deleteVictim(String code);

    Response<List<VictimDTO>> getMyVictims();
}