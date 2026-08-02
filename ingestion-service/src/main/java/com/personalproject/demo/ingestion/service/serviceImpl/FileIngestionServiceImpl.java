package com.personalproject.demo.ingestion.service.serviceImpl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.personalproject.demo.ingestion.dto.PaymentCsvRow;
import com.personalproject.demo.ingestion.mapper.PaymentMapper;
import com.personalproject.demo.ingestion.model.PaymentBatch;
import com.personalproject.demo.ingestion.model.PaymentRecord;
import com.personalproject.demo.ingestion.repository.PaymentBatchRepository;
import com.personalproject.demo.ingestion.repository.PaymentRecordRepository;
import com.personalproject.demo.ingestion.service.FileIngestionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileIngestionServiceImpl implements FileIngestionService {
    private PaymentBatchRepository paymentBatchRepository;
    private PaymentRecordRepository paymentRecordRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public PaymentBatch processCsvFile(MultipartFile multipartFile, String username) throws Exception {
        PaymentBatch paymentBatch = new PaymentBatch();
        paymentBatch.setFileName(multipartFile.getOriginalFilename());
        paymentBatch.setCreatedBy(username);

        try(Reader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))){
            HeaderColumnNameMappingStrategy<PaymentCsvRow> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(PaymentCsvRow.class);

            CsvToBean<PaymentCsvRow> csvToBean = new CsvToBeanBuilder<PaymentCsvRow>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<PaymentCsvRow> rows = csvToBean.parse();

            List<PaymentRecord> paymentRecords = paymentMapper.toEntityList(rows, paymentBatch);
            paymentRecordRepository.saveAll(paymentRecords);

            paymentBatch.setTotalRecords(paymentRecords.size());
            paymentBatch.setStatus("COMPLETED");
            return paymentBatchRepository.save(paymentBatch);
        }
    }
}
