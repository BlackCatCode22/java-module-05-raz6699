import java.io.*;
import java.util.*;

// Base Animal Class
class Animal {
    private String name;
    private int age;
    private String species;

    public Animal(String name, int age, String species) {
        this.name = name;
        this.age = age;
        this.species = species;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSpecies() { return species; }

    @Override
    public String toString() {
        return name + " (" + age + " years old, " + species + ")";
    }

    public void makeSound() {
        System.out.println("Generic animal sound");
    }
}

class Hyena extends Animal {
    public Hyena(String name, int age) { super(name, age, "Hyena"); }
    @Override public void makeSound() { System.out.println("Sound: Hyena's laugh!"); }
}

class Lion extends Animal {
    public Lion(String name, int age) { super(name, age, "Lion"); }
    @Override public void makeSound() { System.out.println("Sound: Lion's roar!"); }
}

class Tiger extends Animal {
    public Tiger(String name, int age) { super(name, age, "Tiger"); }
    @Override public void makeSound() { System.out.println("Sound: Tiger's growl!"); }
}

class Bear extends Animal {
    public Bear(String name, int age) { super(name, age, "Bear"); }
    @Override public void makeSound() { System.out.println("Sound: Bear's grunt!"); }
}

public class Main {
    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        Map<String, Integer> speciesCount = new HashMap<>();

        // Maps for names
        Map<String, Queue<String>> namesMapQueue = new HashMap<>();
        Map<String, Set<String>> namesMapSet = new HashMap<>();

        // Input files
        File animalsFile = new File("arrivingAnimals.txt");
        File namesFile = new File("animalNames.txt");

        if (!animalsFile.exists() || !namesFile.exists()) {
            System.out.println("Error: One or both input files are missing.");
            return;
        }

        try (BufferedReader nameReader = new BufferedReader(new FileReader(namesFile))) {
            String line;
            String currentSpecies = "";
            while ((line = nameReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.endsWith("Names:")) {
                    currentSpecies = line.replace(" Names:", "").trim().toLowerCase();
                    namesMapQueue.put(currentSpecies, new LinkedList<>());
                    namesMapSet.put(currentSpecies, new HashSet<>());
                } else if (!currentSpecies.isEmpty()) {
                    String[] names = line.split(", ");
                    for (String name : names) {
                        namesMapQueue.get(currentSpecies).add(name);  // Add to FIFO queue
                        namesMapSet.get(currentSpecies).add(name);    // Add to Set
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading names file: " + e.getMessage());
        }

        try (BufferedReader animalReader = new BufferedReader(new FileReader(animalsFile))) {
            String line;
            int lineCount = 1;
            while ((line = animalReader.readLine()) != null) {
                System.out.println(lineCount + ") " + line);

                String regex = "(\\d+) year old.*(\\bhyena\\b|\\blion\\b|\\btiger\\b|\\bbear\\b).*";
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    int age = Integer.parseInt(matcher.group(1));
                    String species = matcher.group(2).toLowerCase();

                    // First attempt to assign a name using FIFO (Queue) with randomization
                    String name = assignNameFromQueue(namesMapQueue, species);

                    if (name == null) {
                        // If FIFO fails, then assign name from Set (if not already used) with randomization
                        name = assignNameFromSet(namesMapSet, species);
                    }

                    if (name == null) {
                        System.out.println("Skipping animal due to missing name for species: " + species);
                        continue;
                    }

                    System.out.println("Assigning Name: " + name);  // Only print the assigned name once

                    System.out.println("Species: " + species);
                    System.out.println("Age: " + age);

                    Animal animal = createAnimal(species, name, age);
                    if (animal != null) {
                        animals.add(animal);
                        speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                        animal.makeSound();
                    }
                    System.out.println();
                }
                lineCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading animals file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("newAnimals.txt"))) {
            writer.write("New Arriving Animals Report\n===========================\n");
            for (String species : speciesCount.keySet()) {
                writer.write(species + "s (Total: " + speciesCount.get(species) + ")\n");
                for (Animal animal : animals) {
                    if (animal.getSpecies().equalsIgnoreCase(species)) {
                        writer.write("  - " + animal.getName() + " (" + animal.getAge() + " years old)\n");
                    }
                }
            }
            writer.flush();
            System.out.println("Report generated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    private static Animal createAnimal(String species, String name, int age) {
        switch (species) {
            case "hyena": return new Hyena(name, age);
            case "lion": return new Lion(name, age);
            case "tiger": return new Tiger(name, age);
            case "bear": return new Bear(name, age);
            default: return null;
        }
    }

    // Assign name using FIFO (Queue) strategy with randomization and ensure no duplicate names
    private static String assignNameFromQueue(Map<String, Queue<String>> namesMapQueue, String species) {
        Queue<String> nameQueue = namesMapQueue.get(species);
        if (nameQueue != null && !nameQueue.isEmpty()) {
            // Convert Queue to List for shuffling
            List<String> nameList = new ArrayList<>(nameQueue);
            Collections.shuffle(nameList); // Shuffle the list for random order

            // Get the first name from the shuffled list and remove it from the queue
            String name = nameList.get(0);
            nameQueue.remove(name); // Remove the name from the original queue
            return name;
        }
        return null;
    }

    // Assign name using Set (preventing reuse) strategy with randomization
    private static String assignNameFromSet(Map<String, Set<String>> namesMapSet, String species) {
        Set<String> nameSet = namesMapSet.get(species);
        if (nameSet != null && !nameSet.isEmpty()) {
            // Convert Set to List for shuffling
            List<String> nameList = new ArrayList<>(nameSet);
            Collections.shuffle(nameList); // Shuffle the list for random order

            // Get the first name from the shuffled list
            String name = nameList.get(0);
            nameSet.remove(name);  // Remove name to prevent reuse
            return name;
        }
        return null;
    }
}
