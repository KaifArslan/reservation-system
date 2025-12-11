package com.kaif.reservation_system.contoller;

import com.kaif.reservation_system.service.RestaurantService;
import com.kaif.reservation_system.model.Restaurant;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService rs){
        this.restaurantService = rs;
    }

    @GetMapping
    List<Restaurant> getAllRestaurants(){
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant rs){
        return restaurantService.createRestaurant(rs);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable int id) {
        return restaurantService.getRestaurantById(id);
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable int id, @RequestBody Restaurant rs){
        return restaurantService.updateRestaurant(id, rs);
    }

    @DeleteMapping("/{id}")
    public String deleteRes(@PathVariable int id){
        restaurantService.deleteRestaurant(id);
        return "Restaurant Deleted successfully";
    }


}
