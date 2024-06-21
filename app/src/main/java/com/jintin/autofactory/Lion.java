package com.jintin.autofactory;

@ZooElement(AnimalTags.LION)
public class Lion implements Animal {
    @Override
    public String makeSound() {
        return "roar";
    }
}
