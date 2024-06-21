package com.jintin.autofactory;


public class MainClass {

    public static void main(String[] args) {
        Animal cat = AnimalFactory.createAnimal(AnimalTags.CAT);
    }
}
