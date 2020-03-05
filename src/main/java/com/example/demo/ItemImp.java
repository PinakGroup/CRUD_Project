package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemImp{

    public String itemName;

    public void setItemName(String st) {
        this.itemName = st;
    }

    @JsonIgnore
    public List<Item> getAllItems(){
        return Arrays.asList(new Item());
    }
}
