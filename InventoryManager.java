import java.io.*;
import java.util.*;

public class InventoryManager {

    static Scanner scanner = new Scanner(System.in);
    static List<Item> items = new ArrayList<>();
    static String fileName = "inventory.txt"; // Text file to store items

    public static void main(String[] args) {
        loadItemsFromFile(); // Load items from the text file

        int option;
        do {
            // Menu
            System.out.println("Inventory Manager");
            System.out.println("1. Add New Item");
            System.out.println("2. Get All Items");
            System.out.println("3. Get Item By ID");
            System.out.println("4. Update Item");
            System.out.println("5. Delete Item");
            System.out.println("0. Exit");
            System.out.print("Enter your option: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    addNewItem();
                    break;
                case 2:
                    getAllItems();
                    break;
                case 3:
                    getItemById();
                    break;
                case 4:
                    updateItem();
                    break;
                case 5:
                    deleteItem();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    saveItemsToFile(); // Save items to the text file before exit
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    // Add new item with unique ID
    private static void addNewItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();

        // Generate unique ID by checking the highest existing ID
        int newId = items.isEmpty() ? 1 : items.get(items.size() - 1).getId() + 1;

        Item newItem = new Item(newId, name, price);
        items.add(newItem);
        System.out.println("Item added: " + newItem);
    }

    // Get all items
    private static void getAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items available.");
        } else {
            System.out.println("All Items:");
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    // Get item by ID
    private static void getItemById() {
        System.out.print("Enter item ID: ");
        int id = scanner.nextInt();

        Optional<Item> item = items.stream()
                                   .filter(i -> i.getId() == id)
                                   .findFirst();

        if (item.isPresent()) {
            System.out.println("Item found: " + item.get());
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    // Update item by ID
    private static void updateItem() {
        System.out.print("Enter item ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Optional<Item> itemToUpdate = items.stream()
                                           .filter(i -> i.getId() == id)
                                           .findFirst();

        if (itemToUpdate.isPresent()) {
            Item item = itemToUpdate.get();
            System.out.println("Current item: " + item);

            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                item.setName(newName);
            }

            System.out.print("Enter new price (leave blank to keep current): ");
            String newPriceInput = scanner.nextLine();
            if (!newPriceInput.isEmpty()) {
                double newPrice = Double.parseDouble(newPriceInput);
                item.setPrice(newPrice);
            }

            System.out.println("Item updated: " + item);
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    // Delete item by ID
    private static void deleteItem() {
        System.out.print("Enter item ID to delete: ");
        int id = scanner.nextInt();

        Optional<Item> itemToDelete = items.stream()
                                           .filter(i -> i.getId() == id)
                                           .findFirst();

        if (itemToDelete.isPresent()) {
            items.remove(itemToDelete.get());
            System.out.println("Item deleted successfully.");
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    // Load items from the text file
    private static void loadItemsFromFile() {
        File file = new File(fileName);

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        items.add(new Item(id, name, price));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }
        }
    }

    // Save items to the text file
    private static void saveItemsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Item item : items) {
                writer.write(item.getId() + "," + item.getName() + "," + item.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
