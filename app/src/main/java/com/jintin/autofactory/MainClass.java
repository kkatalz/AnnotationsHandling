package com.jintin.autofactory;

public class MainClass {
    public static void main(String[] args) {
        Animal lion = AnimalFactory.createAnimal(AnimalTags.LION);
        System.out.println(lion.makeSound()); // Output: roar

        Animal elephant = AnimalFactory.createAnimal(AnimalTags.ELEPHANT);
        System.out.println(elephant.makeSound()); // Output: trumpet

//        Animal monkey = AnimalFactory.createAnimal("MONKEY");
//        System.out.println(monkey.makeSound()); // Output: error

    }
}