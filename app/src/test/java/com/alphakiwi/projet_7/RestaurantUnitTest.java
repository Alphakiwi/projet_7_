package com.alphakiwi.projet_7;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alphakiwi.projet_7.fragment.FirstFragment;
import com.alphakiwi.projet_7.model.Restaurant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.robolectric.util.FragmentTestUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;



public class RestaurantUnitTest {





    @org.junit.jupiter.api.Test
    public void getGoodDays() {

        Calendar cal = Calendar.getInstance();


        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;

        if (dayOfWeek == -1){
            dayOfWeek = 6;
        }

        //pour un mercredi (lundi = 0, dimanche = 6)
        assertEquals(dayOfWeek, 2);




    }


    @org.junit.jupiter.api.Test
    public void getRestoWithSuccess() {

        Restaurant resto = new Restaurant("Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",5,3);

        assertEquals(resto.getName(), "Rosso & Bianco");
        assertEquals(resto.getAddress(), "3 rue de lapix");
        assertEquals(resto.getId(), "23455iuytrezrty");
        assertEquals(resto.getDistance(), 5);
        assertEquals(resto.getNote(), 3);

    }

    @org.junit.jupiter.api.Test
    public void setRestoWithSuccess() {

        Restaurant resto = new Restaurant();

        resto.setName("Rosso & Bianco");
        resto.setAddress("3 rue de lapix");
        resto.setId("23455iuytrezrty");
        resto.setDistance(5);
        resto.setNote(3);


        assertEquals(resto.getName(), "Rosso & Bianco");
        assertEquals(resto.getAddress(), "3 rue de lapix");
        assertEquals(resto.getId(), "23455iuytrezrty");
        assertEquals(resto.getDistance(), 5);
        assertEquals(resto.getNote(), 3);

    }

    @org.junit.jupiter.api.Test
    public void getRestaurantSortDistanceWithSuccess() {
        List<Restaurant> restaurants  = Arrays.asList(
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5)
        );

        Collections.sort(restaurants, new Restaurant.RestaurantDistanceComparator());

        List<Restaurant> expectedRestaurants  = Arrays.asList(
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4));

        assertEquals(restaurants.get(0).getId(), expectedRestaurants.get(0).getId());
        assertEquals(restaurants.get(1).getId(), expectedRestaurants.get(1).getId());
        assertEquals(restaurants.get(2).getId(), expectedRestaurants.get(2).getId());
        assertEquals(restaurants.get(3).getId(), expectedRestaurants.get(3).getId());

    }

    @org.junit.jupiter.api.Test
    public void getRestaurantSortMarksWithSuccess() {
        List<Restaurant> restaurants  = Arrays.asList(
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5)
        );

        Collections.sort(restaurants, new Restaurant.RestaurantMarksComparator());

        List<Restaurant> expectedRestaurants  = Arrays.asList(
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2));




        assertEquals(restaurants.get(0).getId(), expectedRestaurants.get(0).getId());
        assertEquals(restaurants.get(1).getId(), expectedRestaurants.get(1).getId());
        assertEquals(restaurants.get(2).getId(), expectedRestaurants.get(2).getId());
        assertEquals(restaurants.get(3).getId(), expectedRestaurants.get(3).getId());


    }

    @org.junit.jupiter.api.Test
    public void getRestaurantSortAZWithSuccess() {
        List<Restaurant> restaurants  = Arrays.asList(
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5)
        );

        Collections.sort(restaurants, new Restaurant.RestaurantAZComparator());

        List<Restaurant> expectedRestaurants  = Arrays.asList(
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3)
        );

        assertEquals(restaurants.get(0).getId(), expectedRestaurants.get(0).getId());
        assertEquals(restaurants.get(1).getId(), expectedRestaurants.get(1).getId());
        assertEquals(restaurants.get(2).getId(), expectedRestaurants.get(2).getId());
        assertEquals(restaurants.get(3).getId(), expectedRestaurants.get(3).getId());


    }

    @org.junit.jupiter.api.Test
    public void getRestaurantSortZAWithSuccess() {
        List<Restaurant> restaurants  = Arrays.asList(
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2),
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5)
        );

        Collections.sort(restaurants, new Restaurant.RestaurantZAComparator());

        List<Restaurant> expectedRestaurants  = Arrays.asList(
                new Restaurant("Z_Rosso & Bianco","3 rue de lapix", "2345rezrty",2,3),
                new Restaurant("D_Rosso & Bianco","3 rue de lapix", "234ihjrty",1,5),
                new Restaurant("B_Rosso & Bianco","3 rue de lapix", "23zrty",5,4),
                new Restaurant("A_Rosso & Bianco","3 rue de lapix", "23455iuytrezrty",3,2)
        );

        assertEquals(restaurants.get(0).getId(), expectedRestaurants.get(0).getId());
        assertEquals(restaurants.get(1).getId(), expectedRestaurants.get(1).getId());
        assertEquals(restaurants.get(2).getId(), expectedRestaurants.get(2).getId());
        assertEquals(restaurants.get(3).getId(), expectedRestaurants.get(3).getId());


    }
}
