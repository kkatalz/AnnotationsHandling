package com.jintin.autofactory;
@ZooElement(AnimalTags.ELEPHANT)
public class Elephant implements Animal {
    @Override
    public String makeSound() {
        return "trumpet";
    }
}