public class Dog {
    String name;
    String breed;
    int age;
    String color;

    public Dog(String name, String breed, int age, String color) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public String getColor() {
        return color;
    }

    public void bio() {
        System.out.println("Hi my name is " + this.getName() + 
            ". My breed, age, and color are " + this.getBreed() + ", " 
            + this.getAge() + ", " + this.getColor());
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Dog tuffy = new Dog("Tuffy", "Papilon", 5, "White");
        tuffy.bio();

        System.out.println(tuffy.name);
        System.out.println(tuffy.age);

        Dog dogs[] = {
            new Dog("Bruno", "Golden", 3, "cream"),
            new Dog("Timmy", "Pomerania", 4, "orange"),
            new Dog("Rex", "Bulldog", 6, "black"),
        };

        for(Dog dog: dogs) {
            dog.bio();
        }

        for(int i = 0; i < dogs.length; i++) {
            dogs[i].bio();
        }
    }
}
