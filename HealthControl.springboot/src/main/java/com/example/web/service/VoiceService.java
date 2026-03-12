package com.example.web.service;

import org.springframework.web.multipart.MultipartFile;
import com.example.web.dto.VoiceRecognizeResponseDto;

public interface VoiceService {

    VoiceRecognizeResponseDto recognizeAndSave(Integer userId, MultipartFile file);

}
