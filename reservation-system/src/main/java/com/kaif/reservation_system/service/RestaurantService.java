package com.kaif.reservation_system.service;

import com.kaif.reservation_system.model.Restaurant;
import com.kaif.reservation_system.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restRep;

    public RestaurantService(RestaurantRepository restRep) {
        this.restRep = restRep;
    }

    public Restaurant createRestaurant(Restaurant restaurant){
        return restRep.save(restaurant);
    }
    public List<Restaurant> getAllRestaurants(){
        return restRep.findAll();
    }
    public Restaurant getRestaurantById(int id) {
        return restRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
    public Restaurant updateRestaurant(int id, Restaurant rdNew){
        Restaurant res = restRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant Not Found"));
        res.setName(rdNew.getName());
        res.setAddress(rdNew.getAddress());
        res.setTotalTables(rdNew.getTotalTables());
        res.setClosingTime(rdNew.getClosingTime());
        res.setOpeningTime(rdNew.getOpeningTime());
        res.setMaxPartySize(rdNew.getMaxPartySize());
        return restRep.save(res);
    }

    public void deleteRestaurant(int id){
        restRep.deleteById(id);
    }
}
