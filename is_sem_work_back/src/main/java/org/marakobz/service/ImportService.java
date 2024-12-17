package org.marakobz.service;

import org.hibernate.NonUniqueResultException;
import org.marakobz.model.*;
import org.marakobz.repository.DreamRepository;
import org.marakobz.repository.ImportHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ImportService {
/*

    private final DreamRepository dreamRepository;
    private final ImportHistoryRepository importHistoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);

    public ImportService(DreamRepository dreamRepository, ImportHistoryRepository importHistoryRepository) {
        this.dreamRepository = dreamRepository;
        this.importHistoryRepository = importHistoryRepository;
    }

    private ImportService self;

    @Autowired
    @Lazy
    public void setImportService(ImportService self) {
        this.self = self;
    }


    @Transactional(rollbackFor = Exception.class)
    public ImportHistory importFromCsv(MultipartFile file, User user) throws Exception {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            throw new IllegalArgumentException("Неверный формат файла. Ожидается CSV.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Загруженный файл пуст.");
        }


        ImportHistory history = new ImportHistory();
        history.setUser(user);
        history.setStartTime(LocalDateTime.now());
        history.setStatus("IN_PROGRESS");
        importHistoryRepository.save(history);

        try {
            List<Dream> creatures = parseCsv(file, user);
            creatures.forEach(creature -> creature.setCreator(user));


            dreamRepository.saveAll(creatures);

            history.setAddedObjects(creatures.size());
            history.setStatus("SUCCESS");

        } catch (NonUniqueResultException e) {
            self.updateImportHistoryOnFailure(history, e, user);
            logger.error("Import failed due to NonUniqueResultException for history ID: {}. Error: {}", history.getId(), e.getMessage());
            throw new IllegalStateException("Multiple records found where unique was expected", e);
        }  catch (Exception e) {
            self.updateImportHistoryOnFailure(history, e, user);
            logger.error("Import failed for history ID: {}. Error: {}", history.getId(), e.getMessage());
            throw e;
        }

        importHistoryRepository.saveAndFlush(history);

        return history;
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImportHistoryOnFailure(ImportHistory history, Exception e, User user) {
        ImportHistory failedHistory = new ImportHistory();

        failedHistory.setUser(user);
        failedHistory.setStatus("FAILED");
        failedHistory.setStartTime(LocalDateTime.now());

        failedHistory.setErrorMessage(e.getMessage());
        importHistoryRepository.saveAndFlush(failedHistory);
    }



    public List<ImportHistoryDto> getAllImportHistory() {
        return importHistoryRepository.findAll().stream().map(history -> {
            ImportHistoryDto dto = new ImportHistoryDto();
            dto.setId(history.getId());
            dto.setCreatorName(history.getCreatorUsername());
            dto.setStatus(history.getStatus());
            dto.setStartTime(String.valueOf(history.getStartTime()));
            dto.setAddedObjects(history.getAddedObjects());
            dto.setFileUrl(history.getFileUrl());
            dto.setFileName(history.getFileName());

            return dto;
        }).collect(Collectors.toList());
    }

    private List<Dream> parseCsv(MultipartFile file, User creator) throws IOException {
        List<Dream> creatures = new ArrayList<>();

        Map<String, MagicCity> cityMap = new HashMap<>();
        Map<String, Ring> ringMap = new HashMap<>();
        Map<String, Dream> creatureMap = new HashMap<>();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 18) {
                    throw new IllegalArgumentException("Неверный формат CSV. Каждая строка должна содержать 18 полей, но найдена строка с " + fields.length + " полями.");
                }

                String name = fields[0].trim();
                if (creatureMap.containsKey(name) || dreamRepository.existsByName(name)) {
                    throw new IllegalArgumentException("BookCreature с именем '" + name + "' уже существует.");
                }

                Dream creature = new Dream();
                creature.setName(name);

                double x = Double.parseDouble(fields[1].trim());
                double y = Double.parseDouble(fields[2].trim());
                Coordinates coord = new Coordinates();
                coord.setX(x);
                coord.setY(y);
                creature.setCoordinates(coord);

                creature.setCreationDate(ZonedDateTime.now());
                creature.setAge(Long.parseLong(fields[3].trim()));

                creature.setCreatureType(BookCreatureType.valueOf(fields[4].trim().toUpperCase()));

                String cityName = fields[5].trim();
                if (cityMap.containsKey(cityName) || magicCityRepository.existsByName(cityName)) {
                    throw new IllegalArgumentException("MagicCity с именем '" + cityName + "' уже существует.");
                }
                MagicCity city = cityMap.get(cityName);
                if (city == null) {
                    if (magicCityRepository.existsByName(cityName)) {
                        city = magicCityRepository.findByName(cityName);
                    } else {
                        city = new MagicCity();
                        city.setName(cityName);
                        city.setArea(Float.parseFloat(fields[6].trim()));
                        city.setPopulation(Long.parseLong(fields[7].trim()));
                        city.setEstablishmentDate(LocalDate.parse(fields[8].trim()));
                        city.setGovernor(BookCreatureType.valueOf(fields[9].trim()));
                        city.setCapital(Boolean.parseBoolean(fields[10].trim()));
                        city.setPopulationDensity(Float.parseFloat(fields[11].trim()));

                        cityMap.put(cityName, city);
                    }
                }
                creature.setCreatureLocation(city);

                creature.setAttackLevel(Float.parseFloat(fields[13].trim()));
                creature.setDefenseLevel(Double.parseDouble(fields[14].trim()));

                String ringName = fields[12].trim();
                if (ringMap.containsKey(ringName) || ringRepository.existsByName(ringName)) {
                    throw new IllegalArgumentException("Ring с именем '" + ringName + "' уже существует.");
                }
                if (!ringName.isEmpty()) {
                    Ring ring = ringMap.get(ringName);
                    if (ring == null) {
                        if (ringRepository.existsByName(ringName)) {
                            ring = ringRepository.findByName(ringName);
                        } else {
                            ring = new Ring();
                            ring.setName(ringName);
                            ring.setPower(Long.parseLong(fields[15].trim()));
                            ring.setWeight(Float.parseFloat(fields[16].trim()));

                            ringMap.put(ringName, ring);
                        }
                    }
                    creature.setRing(ring);
                }

                creature.setApproveUpdates(Boolean.parseBoolean(fields[17].trim()));

                creature.setCreator(creator);

                creatureMap.put(name, creature);

                creatures.add(creature);
            }
        }
        return creatures;
    }

    public ImportHistoryDto convertToDto(ImportHistory history) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ImportHistoryDto dto = new ImportHistoryDto();
        dto.setId(history.getId());
        dto.setStatus(history.getStatus());
        dto.setAddedObjects(history.getAddedObjects());
        dto.setStartTime(history.getStartTime() != null ? history.getStartTime().format(formatter) : null);
        dto.setCreatorName(history.getCreatorUsername());
        dto.setFileName(history.getFileName());
        dto.setFileUrl(history.getFileUrl());
        return dto;
    }


    public List<ImportHistoryDto> getImportHistoryByUser(User user) {
        return importHistoryRepository.findByUser(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    */

}
