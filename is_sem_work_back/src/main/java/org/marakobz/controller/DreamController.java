package org.marakobz.controller;

import org.marakobz.model.DreamUser;
import org.marakobz.service.DreamService;
import org.marakobz.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    private final DreamService dreamService;
    private final AuthService authService;

    public DreamController(DreamService dreamService, AuthService authService) {
        this.dreamService = dreamService;
        this.authService = authService;
    }

    /*@GetMapping("/admin")
    public ResponseEntity<List<Users>> getAdminUsers() {
        return ResponseEntity.ok(authService.findAllAdminUsers());
    }*/

    @PostMapping("/users/approve")
    public ResponseEntity<Void> approveUser(@RequestBody DreamUser user) {
        try {
            dreamService.approveUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*@GetMapping("/all")
    public ResponseEntity<Page<DreamDto>> getAllDreams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DreamDto> dreamsPage = dreamService.getAllDreams(pageable);
        return ResponseEntity.ok(dreamsPage);
    }*/


    /*@PostMapping
    public ResponseEntity<String> createDream(@RequestBody Dream dream, HttpServletRequest request) {
        try {
            dreamService.createDream(dream, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла непредвиденная ошибка. Пожалуйста, повторите попытку.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Dream> updateDream(@PathVariable("id") Long id, @RequestBody DreamDto updatedDream, HttpServletRequest request) {
        Dream updated = dreamService.updateDream(id, updatedDream, request);
        return ResponseEntity.ok(updated);

    }*/

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        dreamService.deleteDream(id, request);
        return ResponseEntity.noContent().build();

    }*/

    /*@GetMapping("/find")
    public ResponseEntity<Page<DreamDto>> findByFilters(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y,
            @RequestParam(required = false) Long age,
            @RequestParam(required = false) BookCreatureType creatureType,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) Float area,
            @RequestParam(required = false) Long population,
            @RequestParam(required = false) BookCreatureType governor,
            @RequestParam(required = false) Double populationDensity,
            @RequestParam(required = false) Float attackLevel,
            @RequestParam(required = false) Double defenseLevel,
            @RequestParam(required = false) String ringName,
            @RequestParam(required = false) Float ringPower,
            @RequestParam(required = false) Float ringWeight,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ){

        Page<DreamDto> result = dreamService.findByFilters(
                username, name,x, y, age, creatureType, locationName, area, population, governor, populationDensity,
                attackLevel, defenseLevel, ringName, ringPower, ringWeight, page, size, sortField, sortDirection);

        return ResponseEntity.ok(result);
    }*/

}
