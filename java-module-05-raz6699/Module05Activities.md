# Module05 Activities: Design Document

## **1. Introduction**
The Animal Name Assignment System is designed to assign names to animals arriving at a zoo. The program processes input from two files:
- `arrivingAnimals.txt`: Contains information about animals arriving at the zoo (age, species, color, etc.).
- `animalNames.txt`: Contains a list of names available for each species of animal.

The system uses different data structures to assign names to animals, handle data, and generate a report.

---

## **2. Components**

### **2.1. Animal Class (Base Class)**

#### **Attributes**:
- `name`: The name of the animal.
- `age`: The age of the animal.
- `species`: The species of the animal (e.g., hyena, lion, tiger, bear).

#### **Methods**:
- **Constructor**: Initializes the name, age, and species of the animal.
- **getName()**: Returns the name of the animal.
- **getAge()**: Returns the age of the animal.
- **getSpecies()**: Returns the species of the animal.
- **makeSound()**: Outputs a generic animal sound by default.

### **2.2. Hyena, Lion, Tiger, Bear (Derived Classes)**

Each of these classes is a subclass of the `Animal` class, and they represent specific species. They override the `makeSound()` method to provide specific sounds for each species.

#### **Attributes**:
- Inherits from `Animal`.

#### **Methods**:
- **Constructor**: Initializes the name, age, and species (specific to the class).
- **makeSound()**: Overrides the base `makeSound()` method with the sound specific to the species (e.g., hyena's laugh, lion's roar, etc.).

### **2.3. Main Class**

#### **Attributes**:
- `List<Animal> animals`: A list to store created animal objects.
- `Map<String, Integer> speciesCount`: A map to keep track of how many animals of each species are added.
- `Map<String, Queue<String>> namesMapQueue`: A map to store names for each species in a FIFO queue.
- `Map<String, Set<String>> namesMapSet`: A map to store names for each species in a set (for random access).

#### **Methods**:
- **Main Method**:
    - Reads and processes input files (`arrivingAnimals.txt` and `animalNames.txt`).
    - Creates animals based on the species and assigns names to them using a FIFO queue or set.
    - Prints information about each animal and their assigned names.
    - Generates a report and writes it to `newAnimals.txt`.

- **createAnimal()**: This method creates an instance of a specific animal subclass (e.g., `Hyena`, `Lion`, etc.) based on the species provided.

- **assignNameFromQueue()**: This method assigns a name using the FIFO (First-In-First-Out) queue strategy.

- **assignNameFromSet()**: This method assigns a name using a `Set` to ensure uniqueness and prevent reusing names.

---

## **3. Data Structures**

### **3.1. List<Animal> animals**
- **Purpose**: Stores animal objects that are created during the program's execution. This allows easy tracking and interaction with animals.
- **Usage**: Every time an animal is created and assigned a name, it is added to this list.

### **3.2. Map<String, Integer> speciesCount**
- **Purpose**: Keeps track of how many animals of each species are added to the zoo.
- **Usage**: Every time an animal is added to the `animals` list, the count for its species is updated in this map.

### **3.3. Map<String, Queue<String>> namesMapQueue**
- **Purpose**: Stores a FIFO queue of names for each species. This structure ensures names are assigned in the order they appear in the input file.
- **Usage**: Names are assigned in a first-come, first-serve manner, and once a name is assigned, it is removed from the queue.

### **3.4. Map<String, Set<String>> namesMapSet**
- **Purpose**: Stores a set of names for each species. The set ensures that names are unique, and names cannot be reused.
- **Usage**: Names are assigned randomly from the set, and once a name is assigned, it is removed from the set to prevent reuse.

---

## **4. Data Flow**

1. **Reading Input Files**: The `Main` class reads the `animalNames.txt` and `arrivingAnimals.txt` files.
    - The `animalNames.txt` file is parsed to populate the `namesMapQueue` and `namesMapSet` for each species.
    - The `arrivingAnimals.txt` file is processed line by line, extracting information about the animal's age and species.

2. **Assigning Names**:
    - For each arriving animal, the system first tries to assign a name using the `Queue` (FIFO) strategy. If the queue is empty, the system attempts to assign a name from the `Set`.
    - If both the queue and set are exhausted, the animal is skipped, and the process moves on to the next animal.

3. **Creating Animal Objects**: After successfully assigning a name, an animal object is created using the appropriate subclass (`Hyena`, `Lion`, `Tiger`, `Bear`). The animal object is then added to the `animals` list.

4. **Generating Report**:
    - After processing all animals, a report is generated showing the number of animals for each species.
    - The report includes the names and ages of each animal, organized by species, and is written to a file called `newAnimals.txt`.

---

## **5. Interaction Between Components**

- The `Main` class is responsible for orchestrating the interaction between components:
    - It reads from files and manages the flow of data into the respective maps (`namesMapQueue`, `namesMapSet`, `speciesCount`).
    - It creates animal objects based on the species and assigns them names.
    - It generates the final report based on the data processed.

- The `Animal` class provides the base functionality (such as attributes and methods) for the specific animal types (hyena, lion, tiger, bear).

- Each specific animal subclass (like `Hyena`, `Lion`, etc.) provides the implementation for specific behaviors (e.g., sound).

---

## **6. Conclusion**
This design document outlines the structure and components of the Animal Name Assignment System. The system processes animal data, assigns names in an efficient manner using queues and sets, and generates a comprehensive report of the animals at the zoo.

